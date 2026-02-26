package com.agri.agrimanager.feign;

import feign.Logger;
import feign.Retryer;

import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.codec.Encoder; // ← ajoute ça

@Configuration
public class FeignConfig {
    @Bean
    public AgromonitoringInterceptor agromonitoringInterceptor() {
        return new AgromonitoringInterceptor();
    }
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000,3000,3);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    @Bean
    public Encoder feignEncoder() {
        return new JacksonEncoder(); // ← force Jackson pour sérialiser correctement
    }
}
