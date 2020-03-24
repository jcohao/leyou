package com.jcohao.lysearch.listener;


import com.jcohao.item.model.Spu;
import com.jcohao.lysearch.service.IndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {
    @Autowired
    private IndexService indexService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.create.index.queue", durable = "true"),
        exchange = @Exchange(value = "ly.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
        key = {"item.insert", "item.update"}))
    public void listenCreate(Long id) throws Exception {
        if (id == null)
            return;
        Spu spu = new Spu();
        spu.setId(id);
        // 创建或更新索引
        indexService.buildGoods(spu);
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "ly.delete.index.queue", durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))
    public void listenDelete(Long id) {
        if (id == null)
            return;

        // 删除索引
        indexService.deleteGoods(id);
    }
}
