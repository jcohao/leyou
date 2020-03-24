package com.jcohao.itemservice.controller;

import com.jcohao.common.pojo.PageResult;
import com.jcohao.item.model.Spu;
import com.jcohao.item.model.SpuBo;
import com.jcohao.item.model.SpuDetail;
import com.jcohao.itemservice.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spu")
public class SpuController {


    @Autowired
    private SpuService spuService;


    /**
     * 分页过滤查询
     * @param key 过滤的关键词
     * @param saleable 是否有效
     * @param page 当前页数
     * @param rows 每一页的项数
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<SpuBo>> queryByPage(
        @RequestParam(value = "key", required = false) String key,
        @RequestParam(value = "saleable") Boolean saleable,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "rows", defaultValue = "5") Integer rows) {
        PageResult<SpuBo> pageResult = spuService.queryByPage(key, saleable, page, rows);

        if (pageResult == null || pageResult.getTotal() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        SpuDetail detail = spuService.querySpuDetailById(id);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(detail);
    }

    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        Spu spu = spuService.querySpuById(id);
        if (spu == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(spu);
    }

}
