package com.agri.agrimanager.controller;

import com.agri.agrimanager.dto.WeatherDTO;
import com.agri.agrimanager.entity.WeatherData;
import com.agri.agrimanager.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin
public class WeatherController {
    private final WeatherService weatherService;
    
    @GetMapping("/parcelle/{parcelleId}")
    public WeatherDTO getWeatherByParcelle(@PathVariable Long parcelleId) throws JsonProcessingException {
        return weatherService.getWeatherForecast(parcelleId);
    }
    @GetMapping("/history/{parcelleId}")
    public List<WeatherDTO> getWeatherHistoryByParcelle(@PathVariable Long parcelleId){
        return weatherService.getWeatherHistoryByParcelle(parcelleId);
    }
    @DeleteMapping("/{id}")
    public void deleteWeather(@PathVariable Long id){
        weatherService.deleteWeather(id);
    }

}
