package com.jcohao.authservice.service;

import com.jcohao.authCommon.entity.UserInfo;
import com.jcohao.authCommon.utils.JwtUtils;
import com.jcohao.authservice.client.UserClient;
import com.jcohao.authservice.properties.JwtProperties;
import com.jcohao.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private UserClient userClient;

    public String authentication(String username, String password) {
        try {
            // 查询用户
            ResponseEntity<User> resp = userClient.queryUser(username, password);
            if (!resp.hasBody()) {
                log.error("用户信息不存在，{}", username);
                return null;
            }
            // 获取登录用户
            User user = resp.getBody();
            // 生成 token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    prop.getPrivateKey(), prop.getExpire());
            return token;
        } catch (Exception e) {
            log.error("生成 token 失败", e);
            return null;
        }
    }
}
