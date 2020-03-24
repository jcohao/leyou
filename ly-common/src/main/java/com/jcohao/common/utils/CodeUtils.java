package com.jcohao.common.utils;



import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.util.Random;

/**
 * 使用加密算法 MD5 生成密码盐值，并进行加密和验证
 */
public class CodeUtils {
    /**
     * 生成一个 32 位的盐值
     * @param src
     * @return
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generate(String password) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sb.length();
        // 不够 16 位则添 0
        if (len < 16) {
            for (int i = 0; i < 16-len; i++) {
                sb.append(0);
            }
        }

        String salt = sb.toString();

        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i+=3) {
            // 取 MD5 后密文的第一位
            cs[i] = password.charAt(i/3*2);
            // 取盐值第一位
            cs[i+1] = salt.charAt(i/3);
            // 取 MD5 后密文的第二位
            cs[i+2] = password.charAt(i/3*2+1);
        }

        return new String(cs);
    }


    public static boolean verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];

        for (int i = 0; i < 48; i+=3) {
            cs1[i/3*2] = md5.charAt(i);
            cs1[i/3*2+1] = md5.charAt(i+2);
            cs2[i/3] = md5.charAt(i+1);
        }

        String salt = new String(cs2);

        return md5Hex(password + salt).equals(new String(cs1));
    }

    public static void main(String[] args) {
        System.out.println(generate("hao123456").length());
        System.out.println(verify("hao123456", generate("hao123456")));
    }
}
