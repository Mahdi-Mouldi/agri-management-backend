package com.agri.agrimanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {
    private Long id;
    private Long parcelleId;    // ← juste l'ID, pas toute l'entité Parcelle
    private LocalDate date;

    // Température
    private Double temperatureMax;
    private Double temperatureMin;
    private Double temperatureMean;

    // Précipitations
    private Double precipitation;
    private Double rainSum;

    // Vent
    private Double windSpeedMax;
    private Double windSpeedMean;

    // Humidité
//    private Double humidityMax;
//    private Double humidityMin;
//
//    // Sol
//    private Double soilTemperature;
//    private Double soilMoisture;

    // Ensoleillement
    private Double sunshineDuration;
    private Double uvIndex;

}
