package com.kcjs.cloud.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class CorsConfig {

    /**
     * 处理跨域
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许跨域的域名，*表示所有
        config.addAllowedOrigin("*");

        // 允许携带 cookie，前端要设置 withCredentials=true，Origin 不能是 *
        config.setAllowCredentials(true);

        // 允许的方法
        config.addAllowedMethod("*");

        // 允许的头信息
        config.addAllowedHeader("*");

        // 跨域请求存活时间
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        // 匹配所有路径
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
