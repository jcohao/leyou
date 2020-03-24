package com.jcohao.lysearch.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.jcohao.common.pojo.PageResult;
import com.jcohao.common.pojo.SearchRequest;
import com.jcohao.common.utils.JsonUtils;
import com.jcohao.item.model.*;
import com.jcohao.lysearch.client.BrandClient;
import com.jcohao.lysearch.client.CategoryClient;
import com.jcohao.lysearch.client.GoodsClient;
import com.jcohao.lysearch.client.SpecificationClient;
import com.jcohao.lysearch.document.Goods;
import com.jcohao.lysearch.pojo.SearchResult;
import com.jcohao.lysearch.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 将数据库中的 Spu 信息查询出来，转化为 Goods 然后转存到 ES 中即可导入索引
 */
@Service
@Slf4j
public class IndexService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private ElasticsearchTemplate esTemplate;


    /**
     * 创建存入 es 中的对象
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu) {
        Long id = spu.getId();
        // 查找对应 spu 的 sku 数据
        List<Sku> skus = goodsClient.querySkuBySpuId(id);
        SpuDetail detail = goodsClient.querySpuDetailById(id);
        // 商品分类名称
        List<String> names = categoryClient.queryNamesByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())
        );

        // 查询规格参数
        List<SpecParam> params = specificationClient
                .querySpecParam(null, spu.getCid3(), null, true);

        // TODO 查询品牌名称

        // 处理 sku
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();
        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price", sku.getPrice());
            skuList.add(map);
        }

        // 处理规格参数
        Map<Long, String> genericMap = JsonUtils.parseMap(detail.getGenericSpec(), Long.class, String.class);
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(detail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
            });

        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            if (param.getGeneric()) {
                // 通用参数
                String value = genericMap.get(param.getId());
                if (param.getNumeric()) {
                    // 数值类型，需要存储为一个分段
                    value = chooseSegment(value, param);
                }
                specs.put(param.getName(), value);
            } else {
                // 特有参数
                specs.put(param.getName(), specialMap.get(param.getId()));
            }
        }

        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(id);
        goods.setSubTitle(spu.getSubTitle());
        // 搜索条件，拼接：标题、分类、品牌
        goods.setAll(spu.getTitle() + " " + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);

        return goods;
    }


    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其他";

        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }

            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }

                break;
            }
        }

        return result;
    }


    /**
     * 搜索商品的分页信息
     * @param request
     * @return
     */
    public PageResult<Goods> search(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            // 如果用户没有输入搜索条件，则可提供默认的，或者返回 null
            return null;
        }

        Integer page = request.getPage() - 1;   // page 从 0 开始
        Integer size = request.getSize();

        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 查询
        // 对结果进行筛选，只匹配这几个字段中的内容
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[] {"id", "skus", "subTitle"}, null));

        // 基本查询
        // QueryBuilder query = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
        // 布尔查询
        QueryBuilder query = buildBasicQueryWithFilter(request);
        queryBuilder.withQuery(query);

        // 分页
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 聚合
        String categoryAggName = "category";
        String brandAggName = "brand";

        // 对商品分类进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 对品牌进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        // 排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序字段不为空，则进行排序
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }

        // 返回结果
        // Page<Goods> result = repository.search(queryBuilder.build());
        // 查询，返回包含聚合信息的结果
        AggregatedPage<Goods> result = (AggregatedPage<Goods>) repository.search(queryBuilder.build());

        // 获取聚合结果
        List<Brand> brands = getBrandAggResult(result.getAggregation(brandAggName));
        List<Category> categories = getCategoryAggResult(result.getAggregation(categoryAggName));

        // 判断商品分类数量，看是否需要对规格参数进行聚合
        List<Map<String, Object>> specs = null;
        // 当分类只有一个时才进行规格参数的过滤
        if (categories != null && categories.size() == 1) {
            specs = getSpecs(categories.get(0).getId(), query);
        }


        // 解析结果
        Long total = result.getTotalElements();
        long totalPage = (total.intValue() + size - 1) / size;
        // return new PageResult<>(total, totalPage, result.getContent());
        return new SearchResult(total, totalPage, result.getContent(), categories, brands, specs);
    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRequest request) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 基本条件查询
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // 整理过滤条件
        Map<String, String> filter = request.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            // 字符串类型，进行 term 查询
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // 添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }


    // 解析品牌聚合结果
    private List<Brand> getBrandAggResult(Aggregation aggregation) {

        try {
            LongTerms brandAgg = (LongTerms) aggregation;
            List<Long> brandIds = new ArrayList<>();

            for (LongTerms.Bucket bucket : brandAgg.getBuckets()) {
                brandIds.add(bucket.getKeyAsNumber().longValue());
            }

            // 根据品牌的 id 集合查询品牌
            return brandClient.queryBrandsByIds(brandIds);
        } catch (Exception e) {
            log.error("品牌聚合出现异常", e);
            return null;
        }
    }

    // 解析商品分类聚合结果
    private List<Category> getCategoryAggResult(Aggregation aggregation) {
        try {
            List<Category> categories = new ArrayList<>();
            LongTerms categoryAgg = (LongTerms) aggregation;
            List<Long> cids = new ArrayList<>();
            for (LongTerms.Bucket bucket : categoryAgg.getBuckets()) {
                cids.add(bucket.getKeyAsNumber().longValue());
            }

            // 根据 id 查询分类名称
            List<String> names = categoryClient.queryNamesByIds(cids);

            for (int i = 0; i < names.size(); i++) {
                Category category = new Category();
                category.setId(cids.get(i));
                category.setName(names.get(i));
                categories.add(category);
            }

            return categories;
        } catch (Exception e) {
            log.error("分类聚合出现异常", e);
            return null;
        }
    }

    // 聚合规格参数
    private List<Map<String, Object>> getSpecs(Long cid, QueryBuilder query) {

            // 根据分类查询规格
            List<SpecParam> params = specificationClient.querySpecParam(null, cid, true, null);

            // 创建集合，保存规格过滤条件
            List<Map<String, Object>> specs = new ArrayList<>();

            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);


            // 聚合规格参数
            params.forEach(p -> {
                String key = p.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));
            });

            // 查询，获取结果
            AggregatedPage<Goods> result = esTemplate.queryForPage(queryBuilder.build(), Goods.class);
            // 解析结果
            Aggregations aggs = result.getAggregations();

            // 解析聚合结果
            for (SpecParam param : params) {
                Map<String, Object> spec = new HashMap<>();
                String key = param.getName();
                StringTerms terms = null;
                try {
                    // TODO StringTerms 获取失败
                    terms = aggs.get(key);
                } catch (Exception e) {
                    log.error("获取[" + key +"]数据失败");
                    continue;
                }
                if (terms != null) {
                    spec.put("k", key);
                    spec.put("options", terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList()));
                    specs.add(spec);
                }
            }

            return specs;
    }

    public void deleteGoods(Long id) {
        repository.deleteById(id);
    }

}
