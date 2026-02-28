package com.agri.agrimanager.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Open-Meteo",
        url = "${open-meteo.base.url}")
public interface WeatherClient {
    @GetMapping("/forecast")
    String getWeatherForecast(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("daily") String daily ,
            @RequestParam("timezone") String timezone,
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate
    );
}
