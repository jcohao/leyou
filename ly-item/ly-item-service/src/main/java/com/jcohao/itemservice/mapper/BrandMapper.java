package com.jcohao.itemservice.mapper;

import com.jcohao.item.model.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long> {
    // 因为没有在数据库表中设置外键，所以需要额外逻辑来维护表关系，原因是：
    // 外键会严重影响数据库读写的效率
    // 数据删除时需要级联删除，很麻烦


    // 更新商品表和商品类目表的中间表
    @Insert("INSERT INTO tb_category_brand(category_id, brand_id) VALUES(#{cid}, #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("DELETE FROM tb_category_brand WHERE brand_id=#{bid}")
    int deleteByBrandId(@Param("bid") Long bid);

    // 根据类别 id 查找对应品牌
    @Select("SELECT * FROM tb_brand WHERE id IN (SELECT brand_id FROM tb_category_brand WHERE category_id = ${cid})")
    List<Brand> queryBrandsByCid(@Param("cid") Long cid);
}
