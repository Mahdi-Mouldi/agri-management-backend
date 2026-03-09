package com.agri.agrimanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autorise angular aa acceder a tous les api
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("*") //tous les methods http get post ..
                .allowedHeaders("*") //accepte tous les headers envoyés par Angular.
                .allowCredentials(true);
    }

    

}

