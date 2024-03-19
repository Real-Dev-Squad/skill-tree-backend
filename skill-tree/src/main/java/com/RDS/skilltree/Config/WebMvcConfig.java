package com.RDS.skilltree.Config;

import com.RDS.skilltree.utils.UUIDValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UUIDValidationInterceptor uuidValidationInterceptor;

    @Autowired
    public WebMvcConfig(UUIDValidationInterceptor uuidValidationInterceptor) {
        this.uuidValidationInterceptor = uuidValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(uuidValidationInterceptor).addPathPatterns("/v1/endorsements");
    }
}
