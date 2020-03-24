package com.jcohao.itemservice.mapper;

import com.jcohao.item.model.SpecGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecGroupMapper extends Mapper<SpecGroup> {

    @Select("SELECT * FROM tb_spec_group WHERE cid = #{cid}")
    List<SpecGroup> selectSpecGroupsByCid(@Param("cid") Long cid);
}