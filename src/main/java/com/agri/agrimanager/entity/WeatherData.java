    package com.agri.agrimanager.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;

    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public class WeatherData {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "parcelle_id")
        private Parcelle parcelle;

        private LocalDate date;

        // Température
        private Double temperatureMax; // en °C
        private Double temperatureMin; // en °C
        private Double temperatureMean;

        // Précipitations
        private Double precipitation; // en mm
        private Double rainSum;

        //Vent
        private double windSpeedMax; // en km/h

        // Humidité


        //Sol
        private Double soilTemperature; // en °C
        private Double soilMoisture; // en %
        //Ensoleillement
        private Double sunshineDuration; // en heures
        private Double uvIndex; // indice UV
    }
