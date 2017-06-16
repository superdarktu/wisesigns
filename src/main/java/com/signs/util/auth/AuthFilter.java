package com.signs.util.auth;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.signs.model.commons.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private TokenManager tokenManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext sc = filterConfig.getServletContext();
        AnnotationConfigEmbeddedWebApplicationContext cxt = (AnnotationConfigEmbeddedWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
        tokenManager = (TokenManager) cxt.getBean("tokenManager");
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        log.info("接收到的uri是: {}", uri);
        log.info("接收到的参数是: {}", JSON.toJSONString(httpRequest.getParameterMap()));

        if (uri.indexOf("/api/user") == 0
                || uri.indexOf("/api/test") == 0
                || uri.indexOf("/api/waterPrice") == 0
                || uri.indexOf("/api/area/division") == 0
                || uri.indexOf("/api/manager") == 0
                || uri.indexOf("/api/community") == 0
                || uri.indexOf("/api") == 0
                ) {
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader("Authorization");
        if ((token != null) && (token.length() == 32)) {
            if (tokenManager.checkToken(token)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();

        Result result = new Result();
        result.setError("token无效");
        httpResponse.getWriter().write(mapper.writeValueAsString(result));
    }

    @Override
    public void destroy() {
    }
}