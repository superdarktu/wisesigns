package com.signs.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class AppWebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    // 多个拦截器组成一个拦截器链
    // addPathPatterns 用于添加拦截规则
    // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/api/user/login").excludePathPatterns("/api/error/error")
                .excludePathPatterns("/app/**");
                /*.excludePathPatterns("/app/user/otherLogin").excludePathPatterns("/app/user/msg").excludePathPatterns("/app/user/login").excludePathPatterns("/app/user/reg")
                .excludePathPatterns("/app/alipay/return").excludePathPatterns("/app/alipay/back").excludePathPatterns("/api/interface").excludePathPatterns("/app/picture/image");*/
        super.addInterceptors(registry);
    }
}
