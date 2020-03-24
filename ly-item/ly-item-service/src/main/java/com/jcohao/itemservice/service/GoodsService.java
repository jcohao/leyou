package com.jcohao.itemservice.service;

import com.jcohao.item.model.*;
import com.jcohao.itemservice.mapper.SkuMapper;
import com.jcohao.itemservice.mapper.SpuDetailMapper;
import com.jcohao.itemservice.mapper.SpuMapper;
import com.jcohao.itemservice.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Transactional
    public void save(SpuBo spu) {
        // 保存 spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);
        // 保存 spu 详情
        spu.getSpuDetail().setSpuId(spu.getId());
        spuDetailMapper.insert(spu.getSpuDetail());

        // 保存 sku 和库存信息
        saveSkuAndStock(spu.getSkus(), spu.getId());

        // 发送新增消息
        sendMessage(spu.getId(), "insert");
    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            if (!sku.getEnable()) {
                // 无效信息无需保存
                continue;
            }

            // 保存 sku
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insert(sku);

            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);

            // 发送更新消息
            sendMessage(spuId, "update");
        }
    }


    public Spu querySpuById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 发送消息到 MQ 中去
     */
    private void sendMessage(Long id, String type) {
        // 用 try catch 包裹，不让消息的发送影响到正常的业务逻辑
        try {
            amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            log.error("{} 商品消息发送异常，商品 id:{}", type, id, e);
        }
    }
}
