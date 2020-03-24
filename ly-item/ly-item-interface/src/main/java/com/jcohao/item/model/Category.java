package com.jcohao.item.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_category")
@Data
public class Category implements Serializable {

    // 类别 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 品牌名
    private String name;

    // 品牌类别是由层次关系的，保存其上层类别 id
    private Long parentId;

    // 是否为别人的上层类别
    private Boolean isParent;

    // 排序等级
    private Integer sort;
}
