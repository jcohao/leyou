package com.jcohao.itemservice.service;


import com.jcohao.item.model.SpecParam;
import com.jcohao.itemservice.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据规格组 id 查找该组下的规格参数
     * @param gid
     * @return
     */
//    public List<SpecParam> getSpecParamsByGid(Long gid) {
//        SpecParam specParam = new SpecParam();
//        specParam.setGroupId(gid);
//        return specParamMapper.select(specParam);
//    }

    /**
     * 根据条件查找规格参数，为上一个方法的扩展
     * @param gid
     * @param cid
     * @param searching
     * @param generic
     * @return
     */
    public List<SpecParam> querySpecParam(
            Long gid, Long cid, Boolean searching, Boolean generic
    ) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }
}
