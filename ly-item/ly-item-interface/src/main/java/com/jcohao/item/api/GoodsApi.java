package com.jcohao.item.api;


import com.jcohao.common.pojo.PageResult;
import com.jcohao.item.model.Sku;
import com.jcohao.item.model.Spu;
import com.jcohao.item.model.SpuBo;
import com.jcohao.item.model.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品服务接口
 */
public interface GoodsApi {

    /**
     * 分页查询
     */
    @GetMapping("/spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    );


    /**
     * 根据 spu 商品 id 查询 sku
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);


    /**
     * 根据 spu 的 id 查询 sku
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @GetMapping("/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);
}
