package com.audrey.homepage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 *
 * 功能：
 * 1. 配置静态资源访问路径
 * 2. 让上传的文件可以通过URL访问
 *
 * @author Claude
 * @date 2026-01-20
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源映射
     *
     * 作用：
     * - 当访问 /uploads/** 时，从 uploads/ 目录读取文件
     * - 例如：访问 /uploads/avatars/1_abc.jpg
     *        实际读取 项目根目录/uploads/avatars/1_abc.jpg
     *
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的访问路径
        registry.addResourceHandler("/uploads/**")  // URL模式
                .addResourceLocations("file:uploads/");  // 实际文件位置

        // 解释：
        // - /uploads/** : 匹配所有以 /uploads/ 开头的URL
        //   例如：/uploads/avatars/1.jpg, /uploads/projects/2.png
        //
        // - file:uploads/ : 从文件系统的 uploads/ 目录读取
        //   file: 前缀表示这是文件系统路径，不是classpath
        //   uploads/ 是相对于项目根目录的路径
    }
}
