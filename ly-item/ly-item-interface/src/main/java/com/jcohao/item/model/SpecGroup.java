package com.jcohao.item.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * 规格参数分组表
 */
@Entity
@Table(name = "tb_spec_group")
@Data
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 商品分类 id
    private Long cid;

    // 组名
    private String name;

    @Transient
    private List<SpecParam> params;     // 该组下所有规格参数的集合
}
