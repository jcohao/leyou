package com.jcohao.goodspage.service;

import com.jcohao.goodspage.client.BrandClient;
import com.jcohao.goodspage.client.CategoryClient;
import com.jcohao.goodspage.client.GoodsClient;
import com.jcohao.goodspage.client.SpecificationClient;
import com.jcohao.item.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GoodsService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadModel(Long id) {
        try {
            // 模型参数
            Map<String, Object> modelMap = new HashMap<>();

            // 查询 spu
            Spu spu = goodsClient.querySpuById(id);

            // 查询 spuDetail
            SpuDetail detail = goodsClient.querySpuDetailById(id);

            // 查询 sku
            List<Sku> skus = goodsClient.querySkuBySpuId(id);

            // 填充数据
            modelMap.put("spu", spu);
            modelMap.put("spuDetail", detail);
            modelMap.put("skus", skus);

            // 商品分类
            List<Category> categories = getCategories(spu);
            if (categories != null) {
                modelMap.put("categories", categories);
            }

            // 品牌数据
            List<Brand> brands = brandClient.queryBrandsByIds(Arrays.asList(spu.getBrandId()));
            modelMap.put("brand", brands.get(0));

            // 查询规格组及组内参数
            List<SpecGroup> groups = specificationClient.querySpecsByCid(spu.getCid3());
            modelMap.put("groups", groups);

            // 查询商品分类下的特有规格参数
            List<SpecParam> params = specificationClient.querySpecParam(null, spu.getCid3(), null, false);
            // 组装成 id:name 形式的键值对
            Map<Long, String> paramMap = new HashMap<>();
            for (SpecParam param : params) {
                paramMap.put(param.getId(), param.getName());
            }

            modelMap.put("paramMap", paramMap);
            return modelMap;
        } catch (Exception e) {
            log.error("加载商品数据出错，spuId：{}", id, e);
            return null;
        }
    }

    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(),
                    spu.getCid2(), spu.getCid3()));

            Category category1 = new Category();
            category1.setName(names.get(0));
            category1.setId(spu.getCid1());

            Category category2 = new Category();
            category2.setName(names.get(1));
            category2.setId(spu.getCid2());

            Category category3 = new Category();
            category3.setName(names.get(2));
            category3.setId(spu.getCid3());

            return Arrays.asList(category1, category2, category3);
        } catch (Exception e) {
            log.error("查询商品分类出错，spuId：{}", spu.getId(), e);
            return null;
        }
    }

}
