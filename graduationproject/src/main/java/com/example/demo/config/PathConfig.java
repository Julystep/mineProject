package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathConfig implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/homework/**").addResourceLocations("file:E:/homework/");  //注意这里面的斜杠需要加全

        registry.addResourceHandler("/img/**").addResourceLocations("file:E:/img/");
    }

}
