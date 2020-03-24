package com.jcohao.item.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tb_spec_param")
@Data
public class SpecParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 商品类目 Id，其实通过规格组 id 能够查询得到类目 Id
    // 但是涉及连表查询，效率不高，为提高效率，这里违反范例，设置类目 Id 方便查询
    private Long cid;

    // 规格组 id
    private Long groupId;

    // 规格参数名称
    private String name;

    // 是否为数字类型
    @Column(name = "`numeric`")
    private Boolean numeric;

    // 数字类型的单位
    private String unit;

    // 是否为 SKU 通用属性，用于通用属性查询
    private Boolean generic;

    // 该参数是否用于过滤
    private Boolean searching;

    // 范围连接符
    private String segments;
}
