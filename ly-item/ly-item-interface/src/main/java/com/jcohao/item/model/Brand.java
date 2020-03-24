package com.jcohao.item.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_brand")
@Data
public class Brand implements Serializable {

    // 品牌 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 品牌名字
    private String name;

    // 图片链接
    private String image;

    // 首字母，用于排序
    private Character letter;
}
