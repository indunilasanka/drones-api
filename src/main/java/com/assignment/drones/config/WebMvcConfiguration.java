package com.assignment.drones.config;

import com.assignment.drones.handler.CorrelationIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Custom Web MVC Configure
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final CorrelationIdInterceptor interceptor;

    @Autowired
    public WebMvcConfiguration(CorrelationIdInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
