package com.poscodx.jblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.poscodx.jblog.config.app.DBConfig;
import com.poscodx.jblog.config.app.MyBatisConfig;

@Configuration
@Import({DBConfig.class, MyBatisConfig.class})
public class AppConfig {

}