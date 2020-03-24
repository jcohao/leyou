package com.jcohao.item.api;


import com.jcohao.item.model.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 品牌服务接口
 */
@RequestMapping("brand")
public interface BrandApi {
    /**
     * 根据品牌 id 的集合查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list")
    List<Brand> queryBrandsByIds(@RequestParam("ids") List<Long> ids);
}
