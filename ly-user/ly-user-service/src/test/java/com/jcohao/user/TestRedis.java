package com.jcohao.user;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    @Autowired
    private StringRedisTemplate template;

    @Test
    public void testRedis() {
        template.opsForValue().set("name", "jcohao");
        String val = template.opsForValue().get("name");
        System.out.println("name: " + val);
    }

}
