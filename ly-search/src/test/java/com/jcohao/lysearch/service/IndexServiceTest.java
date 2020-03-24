package com.jcohao.lysearch.service;


import com.jcohao.common.pojo.PageResult;
import com.jcohao.item.model.SpuBo;
import com.jcohao.lysearch.client.GoodsClient;
import com.jcohao.lysearch.document.Goods;
import com.jcohao.lysearch.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTest {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size;

        do {
            // 查询 spu
            PageResult<SpuBo> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<SpuBo> spus = result.getItems();

            // spu 转化为 goods
            List<Goods> goods = spus.stream().map(spu -> indexService.buildGoods(spu)).collect(Collectors.toList());

            // 把 goods 放入索引库
            goodsRepository.saveAll(goods);

            size = spus.size();
            page++;
        } while (size == 100);
    }

}
