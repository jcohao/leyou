package com.jcohao.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import tk.mybatis.spring.annotation.MapperScan;

// 整个 item 工程里面只有 service 是需要启动的
@SpringBootApplication
@EnableDiscoveryClient
// 要添加扫包范围 spring 才会为该接口实现类注册 bean
@MapperScan("com.jcohao.itemservice.mapper")
public class ItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }
}
