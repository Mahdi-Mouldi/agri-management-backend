package com.agri.agrimanager.repository;

import com.agri.agrimanager.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByParcelleId(Long parcelleId);
}
