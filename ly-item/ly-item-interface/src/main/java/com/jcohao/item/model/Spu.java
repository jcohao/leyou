package com.jcohao.item.model;



import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "tb_spu")
@Data
public class Spu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 标题
    private String title;

    // 子标题
    private String subTitle;

    // 1 级类目 id
    private Long cid1;

    // 2 级类目 id
    private Long cid2;

    // 3 级类目 id
    private Long cid3;

    // 商品所属品牌 id
    private Long brandId;

    // 是否上架，true 上架，false 下架
    private Boolean saleable;

    // 是否有效,true 有效，false 无效
    private Boolean valid;

    // 添加时间
    private Date createTime;

    // 最后修改时间
    private Date lastUpdateTime;

}
