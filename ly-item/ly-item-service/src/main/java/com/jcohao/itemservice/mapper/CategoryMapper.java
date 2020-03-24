package com.jcohao.itemservice.mapper;

import com.jcohao.item.model.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

// 继承一个通用 mapper 的接口，类似于 jpa
public interface CategoryMapper extends Mapper<Category> {
    // 根据品牌 id 查询它的商品类别集合
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);

    // 根据品牌 id 查询其名称
    @Select("SELECT name FROM tb_category WHERE id = #{cid}")
    String queryNameById(Long cid);

}
