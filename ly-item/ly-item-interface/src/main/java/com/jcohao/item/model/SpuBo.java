package com.jcohao.item.model;


import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

// 商品列表中返回的结果还要带上商品分类还有品牌
// 这些在 SPU 表中都是没有的，所以要扩展 SPU 表
// 新增商品或修改商品时还返回了商品详情以及 sku 集合
// 也得加上
@Data
public class SpuBo extends Spu {
    @Transient
    private String cname;
    @Transient
    private String bname;
    @Transient
    private SpuDetail spuDetail;
    @Transient
    private List<Sku> skus;
}
