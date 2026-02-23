package com.agri.agrimanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgriManagerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriManagerBackendApplication.class, args);
    }

}
