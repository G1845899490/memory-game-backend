package com.activateme.memorygame.config;

// 导入 Spring 的配置和 Web 相关类
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration 注解：标记此类为 Spring 配置类，启动时自动加载
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 实现 addCorsMappings 方法，配置全局 CORS 规则
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加 CORS 映射，应用于所有 API 路径 (/**)
        registry.addMapping("/**")
                // 允许的源（前端域名）
                // 生产环境中替换为具体域名，如 "http://your-domain.com"
                // 多个域名可以用数组：{"http://domain1.com", "http://domain2.com"}
                .allowedOrigins("http://localhost:8081", "https://localhost:8081", "http://localhost:80", "https://localhost:80")
                // 允许的 HTTP 方法
                // 包括常见的 REST 方法：GET（查询）、POST（创建）、PUT（更新）、DELETE（删除）
                // 允许的 HTTP 方法，包括 OPTIONS（预检请求必须支持，浏览器会在发送非简单请求（Non-simple Request）前自动发起预检请求）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                // "*" 表示允许所有头，如 Content-Type、Authorization
                .allowedHeaders("*")
                // 是否允许发送 cookie 或其他凭证
                // 设置为 true 时，allowedOrigins 不能为 "*"，必须明确指定域名
                .allowCredentials(true)
                // 预检请求（OPTIONS）的缓存时间（秒）
                // 浏览器会缓存预检结果，减少重复请求，提高性能
                .maxAge(3600);
    }
}
