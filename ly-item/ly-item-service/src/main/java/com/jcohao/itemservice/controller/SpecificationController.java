package com.jcohao.itemservice.controller;

import com.jcohao.item.model.SpecGroup;
import com.jcohao.item.model.SpecParam;
import com.jcohao.itemservice.service.SpecGroupService;
import com.jcohao.itemservice.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;

    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> getSpecGroupsByCid(@PathVariable("cid") Long cid) {
        List<SpecGroup> specGroups = specGroupService.getSpecGroupsByCid(cid);

        if (specGroups == null || specGroups.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(specGroups);
    }


//    @GetMapping("/params")
//    public ResponseEntity<List<SpecParam>> getSpecParamsByGid(@RequestParam("gid") Long gid) {
//        List<SpecParam> specParams = specParamService.getSpecParamsByGid(gid);
//
//        if (specParams == null || specParams.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return ResponseEntity.ok(specParams);
//    }

    /**
     * 根据条件获取规格参数，为上一个方法的扩展
     * @param gid 分组 id
     * @param cid 类别 id
     * @param searching 搜索字段
     * @param generic 是否为 SKU 通用属性，用于通用属性查询
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic
    ) {
        List<SpecParam> specParams = specParamService.querySpecParam(gid, cid, searching, generic);
        if (specParams == null || specParams.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(specParams);
    }

    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid) {
        return getSpecGroupsByCid(cid);
    }
}
