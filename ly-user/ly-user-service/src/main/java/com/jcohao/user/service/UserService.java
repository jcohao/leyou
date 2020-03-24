package com.jcohao.user.service;


import com.jcohao.common.utils.CodeUtils;
import com.jcohao.common.utils.NumberUtils;
import com.jcohao.user.model.User;
import com.jcohao.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    /**
     * 查找数据库中有没有指定用户的信息
     * @param data 数据
     * @param type 数据类型，1 为用户名，2 为手机号
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return userMapper.selectCount(record) == 0;
    }

    public Boolean sendVerifyCode(String phone) {
        // 生成验证码
        String code = NumberUtils.generateCode(6);
        try {
            // 发送短信
            Map<String, String> msg = new HashMap<>();
            msg.put("phone", phone);
            msg.put("code", code);
            log.info("手机号: {}, 验证码: {}", phone, code);
            // amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            log.error("发送短信失败，phone：{}，code：{}", phone, code, e);
            return false;
        }
    }

    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();

        // 从 Redis 中取出验证码
        String codeCache = redisTemplate.opsForValue().get(key);

        // 检查验证码是否正确
        if (code == null || !codeCache.equals(code)) {
            return false;
        }

        user.setId(null);
        user.setCreated(new Date());

        // 生成盐值
        String salt = CodeUtils.md5Hex(user.getPassword());
        user.setSalt(salt);
        user.setPassword(CodeUtils.generate(user.getPassword()));

        // 写入数据库
        boolean check = userMapper.insertSelective(user) == 1;

        if (check) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("删除缓存验证码失败，code：{}", code, e);
            }
        }

        return check;
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null) {
            return null;
        }

        if (CodeUtils.verify(CodeUtils.generate(password), user.getPassword())) {
            return null;
        }

        return user;
    }
}
