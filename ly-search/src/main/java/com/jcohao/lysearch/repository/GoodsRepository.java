package com.jcohao.lysearch.repository;

import com.jcohao.lysearch.document.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
