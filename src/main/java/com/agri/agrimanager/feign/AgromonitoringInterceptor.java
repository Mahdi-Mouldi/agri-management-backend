package com.agri.agrimanager.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Component
public class AgromonitoringInterceptor implements RequestInterceptor {
    @Value("${agromonitoring.api.key}")
    private String apiKey;

    @Override
    public void apply(RequestTemplate requestTemplate){
        requestTemplate.query("appid", apiKey);
    }
}