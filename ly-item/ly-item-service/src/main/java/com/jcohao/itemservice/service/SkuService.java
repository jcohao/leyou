package com.jcohao.itemservice.service;

import com.jcohao.item.model.Sku;
import com.jcohao.itemservice.mapper.SkuMapper;
import com.jcohao.itemservice.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    public List<Sku> querySkuBySpuId(Long id) {
        Sku record = new Sku();
        record.setSpuId(id);
        List<Sku> skus = skuMapper.select(record);
        // 为了回显方便，同时查出 sku 的库存
        for (Sku sku : skus) {
            sku.setStock(stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        }
        return skus;
    }
}
