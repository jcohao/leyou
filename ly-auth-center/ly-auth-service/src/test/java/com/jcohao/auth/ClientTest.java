package com.jcohao.auth;


import com.jcohao.authservice.LyAuthServiceApplication;
import com.jcohao.authservice.client.UserClient;
import com.jcohao.authservice.service.AuthService;
import com.jcohao.user.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyAuthServiceApplication.class)
public class ClientTest {

    @Autowired
    private UserClient client;

    @Autowired
    private AuthService service;

    @Test
    public void queryTest() {
        ResponseEntity<User> userResponseEntity = client.queryUser("ning", "hao123456");
        User user = userResponseEntity.getBody();
        System.out.println(user);

    }

    @Test
    public void serviceTest() {
        String token = service.authentication("ning", "hao123456");
        System.out.println(token);
    }
}
