package com.signs;

import com.signs.config.RedisObjectSerializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Controller
@EnableWebMvc
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400 * 30)
@MapperScan(basePackages = "com.signs.mapper")
public class Application extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("50MB");
        factory.setMaxRequestSize("50MB");
        return factory.createMultipartConfig();
    }

   /* @Bean
    public HttpSessionListener httpSessionListener() {
        System.out.println("开始");
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent httpSessionEvent) {
                System.out.println("生产");
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

            }
        };
    }*/
}
