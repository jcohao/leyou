package com.jcohao.upload.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // 添加 CORS 配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 允许的域
        config.addAllowedOrigin("http://manage.leyou.com");
        // 是否发送 cookie 信息
        config.setAllowCredentials(false);
        // 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("POST");

        // 允许的头信息
        config.addAllowedHeader("*");

        // 添加映射路径，这里拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configSource);
    }
}
