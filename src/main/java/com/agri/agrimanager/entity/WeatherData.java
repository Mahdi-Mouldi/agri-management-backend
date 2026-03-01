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

        @Column(name = "date", nullable = false)
        private LocalDate date;

        @ManyToOne
        @JoinColumn(name = "parcelle_id", nullable = false)
        private Parcelle parcelle;

        @Column(name = "temperature_max")
        private Double temperatureMax;

        @Column(name = "temperature_min")
        private Double temperatureMin;

        @Column(name = "temperature_mean")
        private Double temperatureMean;

        @Column(name = "precipitation")
        private Double precipitation;

        @Column(name = "rain_sum")
        private Double rainSum;

        @Column(name = "wind_speed_max")
        private Double windSpeedMax;

        @Column(name = "wind_speed_mean") // ← colonne qui causait le problème !
        private Double windSpeedMean;

        @Column(name = "sunshine_duration")
        private Double sunshineDuration;

        @Column(name = "uv_index")
        private Double uvIndex;
    }
