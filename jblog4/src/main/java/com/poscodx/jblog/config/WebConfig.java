package com.poscodx.jblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.poscodx.jblog.config.app.SecurityConfig;
import com.poscodx.jblog.config.web.FileUploadConfig;
import com.poscodx.jblog.config.web.LocaleConfig;
import com.poscodx.jblog.config.web.MvcConfig;
import com.poscodx.jblog.interceptor.LocaleInterceptor;

@Configuration
@EnableAspectJAutoProxy
@Import({MvcConfig.class, LocaleConfig.class, SecurityConfig.class, FileUploadConfig.class})
@ComponentScan({"com.poscodx.jblog.controller", "com.poscodx.jblog.exception"})
public class WebConfig implements WebMvcConfigurer {
	/* @Bean
    public HandlerInterceptor localeInterceptor() {
        return new LocaleInterceptor();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(localeInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/assets/**");
    } */
}
