package com.jcohao.itemservice.service;


import com.jcohao.item.model.SpecGroup;
import com.jcohao.itemservice.mapper.SpecGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;


    /**
     * 根据商品分类 id 查询其对应的规格属性分组
     * @param cid
     * @return
     */
    public List<SpecGroup> getSpecGroupsByCid(Long cid) {
        // 还可以根据 record 去 select，不用自己去写 sql
//        SpecGroup specGroup = new SpecGroup();
//        specGroup.setCid(cid);
//        specGroupMapper.select(specGroup);
        return specGroupMapper.selectSpecGroupsByCid(cid);
    }
}
