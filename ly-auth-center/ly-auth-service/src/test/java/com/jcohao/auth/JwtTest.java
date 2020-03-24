package com.jcohao.auth;

import com.jcohao.authCommon.entity.UserInfo;
import com.jcohao.authCommon.utils.JwtUtils;
import com.jcohao.authCommon.utils.RsaUtils;
import com.jcohao.authservice.LyAuthServiceApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyAuthServiceApplication.class)
public class JwtTest {

    private static final String pubKeyPath = "D:\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        String token = JwtUtils.generateToken(new UserInfo(1L, "jcohao"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJqY29oYW8iLCJleHAiOjE1ODQ4NDE2NDF9.WnvIdZ6ZcV8ZFcCjVh_-3bnJrc2O08JrJuVzMyn79D4T8VZ2wFTKsSho4XxxcM8KpJFYaqKniOqtRn9pKO8CJxUvSlVM1V42KJFPZ-lOpEaw_AiTQvLy3d8AhMmCBKl3rwvhAxRy4D4sxKLstsSctrxDE6THDVDEEw6kPpEk0bQ";

        UserInfo user = JwtUtils.getInfoFormToken(token, publicKey);
        System.out.println("id = " + user.getId());
        System.out.println("username = " + user.getUsername());
    }
}
