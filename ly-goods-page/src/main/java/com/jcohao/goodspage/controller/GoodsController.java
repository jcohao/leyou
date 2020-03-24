package com.jcohao.goodspage.controller;


import com.jcohao.goodspage.service.FileService;
import com.jcohao.goodspage.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private FileService fileService;


    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long id) {
        Map<String, Object> modelMap = goodsService.loadModel(id);
        model.addAllAttributes(modelMap);
        // 判断是否需要生成新的页面
        if (!fileService.exists(id)) {
            fileService.syncCreateHtml(id);
        }
        return "item";
    }
}
