package com.jcohao.authservice.properties;


import com.jcohao.authCommon.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "ly.jwt")
@Slf4j
@Data
public class JwtProperties {

    private String secret;  // 密钥

    private String pubKeyPath;  // 公钥

    private String priKeyPath;  // 私钥

    private int expire;     // token 过期时间

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private String cookieName;

    private int cookieMaxAge;

    @PostConstruct
    public void init() {
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥私钥失败", e);
            throw new RuntimeException();
        }
    }
}
