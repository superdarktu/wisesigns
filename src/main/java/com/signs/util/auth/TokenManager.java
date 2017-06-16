package com.signs.util.auth;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenManager {

    @Resource
    private StringRedisTemplate redis;

    private static long timeout = 30;


    public void setTimeout(long timeout) {
        TokenManager.timeout = timeout;
    }

    /**
     * 生成token
     */
    public AccessToken createToken(String userId, String type) {
        deleteTokenByUserId(userId);
        //使用uuid作为源token
        String token = UUID.randomUUID().toString().replace("-", "");
        AccessToken model = new AccessToken();
        model.setToken(token);
        model.setUserId(userId);
        //存储到redis并设置过期时间
        redis.boundValueOps(token).set(JSON.toJSONString(model), timeout, TimeUnit.DAYS);
        redis.boundValueOps(userId + "").set(token, timeout, TimeUnit.DAYS);
        return model;
    }

    /**
     * 检查token是否有效
     */
    Boolean checkToken(String token) {
        if (token == null || token.isEmpty()) return false;
        String str = redis.boundValueOps(token).get();
        if (str == null) return false;
        AccessToken model = JSON.parseObject(str, AccessToken.class);
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redis.boundValueOps(token).expire(timeout, TimeUnit.DAYS);
        redis.boundValueOps(model.getUserId() + "").expire(timeout, TimeUnit.DAYS);
        return true;
    }

    /**
     * 获取token
     */
    public AccessToken getToken(String token) {
        AccessToken result = new AccessToken();
        if (token == null || token.isEmpty()) return result;
        String str = redis.boundValueOps(token).get();
        if (str == null) return result;
        return JSON.parseObject(str, AccessToken.class);
    }

    /**
     * 清除token
     */
    public void deleteToken(String token) {
        if (token == null || token.isEmpty()) return;
        String str = redis.boundValueOps(token).get();
        if (str == null) return;
        AccessToken model = JSON.parseObject(str, AccessToken.class);
        redis.delete(token);
        redis.delete(model.getUserId() + "");
    }

    /**
     * 根据用户id清除token
     */
    private void deleteTokenByUserId(String userId) {
        if (userId == null) return;
        String token = redis.boundValueOps(userId).get();
        if (token == null) return;
        deleteToken(token);
    }
}