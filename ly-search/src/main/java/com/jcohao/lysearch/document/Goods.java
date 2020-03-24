package com.jcohao.lysearch.document;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id; // spuId
    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String all;     // 所有需要被搜索的信息，包含标题，分类，甚至品牌
    @Field(type = FieldType.keyword, index = false)
    private String subTitle; // 副标题
    private Long brandId;   // 品牌 id
    private Long cid1;  // 1 级分类
    private Long cid2;  // 2 级分类
    private Long cid3;  // 3 级分类
    private Date createTime;    // 创建时间
    private List<Long> price;   // 价格，所有 sku 的价格集合，方便根据价格进行筛选过滤
    @Field(type = FieldType.keyword, index = false)
    private String skus;    // sku 信息的 json 格式
    private Map<String, Object> specs;      // 可搜索的规格参数，key 为参数名，value 为参数值
}
