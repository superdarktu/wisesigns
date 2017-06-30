package com.signs.util.auth;

import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * token参数处理器
 */
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(AccessToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (token == null || token.isEmpty()) return new AccessToken();
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        ServletContext sc = servletRequest.getServletContext();
        AnnotationConfigEmbeddedWebApplicationContext cxt = (AnnotationConfigEmbeddedWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
        TokenManager tokenManager = (TokenManager) cxt.getBean("tokenManager");
        return tokenManager.getToken(token);
    }
}