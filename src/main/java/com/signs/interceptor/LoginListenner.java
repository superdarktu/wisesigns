package com.signs.interceptor;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginListenner  implements HttpSessionAttributeListener {

    private Map<String, HttpSession> map = new HashMap<String, HttpSession>();

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {

        String name = httpSessionBindingEvent.getName();

        if (name.equals("id")) {
            String value = httpSessionBindingEvent.getValue().toString();
            if (!StringUtil.isEmpty(value)) {
                HttpSession session = map.get(value);
                session.removeAttribute(value);
                session.invalidate();
            }
            map.put(value, httpSessionBindingEvent.getSession());
        }

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }
}
