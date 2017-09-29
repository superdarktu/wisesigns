package com.signs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan
public class MyWebAppConfigurer
        extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/upload/**").addResourceLocations("classpath:/static/upload/");
        super.addResourceHandlers(registry);
    }

    public static void main(String args[]){

        /*for(int i=0;i<100;i++){
            System.out.println((i)/((i)*0.02+10));
        }*/
        System.out.println((110)/((110)*0.02+10));

    }

}