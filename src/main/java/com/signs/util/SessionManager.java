package com.signs.util;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class SessionManager {

    private Map<String, HttpSession> map = new HashMap<String,HttpSession>();

    public void addSession(String phone,HttpSession httpSession){

        HttpSession session = map.get(phone);
        if(session != null && !session.getId().equals(httpSession.getId())){
            session.invalidate();
            map.remove(phone);
            throw new NullPointerException();
        }
        map.put(phone,httpSession);
    }
}
