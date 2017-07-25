package com.signs.interceptor;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Map;

@WebListener
public class LoginListenner  implements HttpSessionListener {

    private Map<String, HttpSession> map = new HashMap<String,
            HttpSession>();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        String name = httpSessionEvent.getSession().getAttributeNames().toString();
        System.out.println(httpSessionEvent.getSession().getAttributeNames());

        /*if (name.equals("id")) {
            String value = httpSessionBindingEvent.getValue
                    ().toString();
            if (!StringUtil.isEmpty(value)) {
                HttpSession session = map.get(value);
                session.removeAttribute(value);
                session.invalidate();
            }
            map.put(value, httpSessionBindingEvent.getSession());
        }*/
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent
                                         httpSessionEvent) {

    }
}
