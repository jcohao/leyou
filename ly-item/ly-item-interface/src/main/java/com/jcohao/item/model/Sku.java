package com.jcohao.item.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_sku")
@Data
public class Sku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // sku 的 id

    private Long spuId;     // 其对应的 spu 的 id

    private String title;   // 标题

    private String images;  // 图片链接

    private Long price;   // 价格

    private String indexes; // 具体规格参数的对应索引

    private String ownSpec; // 具体规格参数的值

    private Boolean enable; // 是否有效

    private Date createTime;    // 创建时间

    private Date lastUpdateTime;    // 最后修改时间

    @Transient
    private Integer stock;  // 库存
}
