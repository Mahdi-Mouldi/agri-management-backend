package com.agri.agrimanager.mapper;

import com.agri.agrimanager.dto.WeatherDTO;
import com.agri.agrimanager.entity.WeatherData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherMapper {
    public WeatherDTO toDTO(WeatherData weatherData) {
        if(weatherData == null) {
            return null;
        }
          return WeatherDTO.builder()
                  .id(weatherData.getId())
                  .parcelleId(weatherData.getParcelle().getId())
                  .date(weatherData.getDate())
                  .temperatureMax(weatherData.getTemperatureMax())
                  .temperatureMin(weatherData.getTemperatureMin())
                  .temperatureMean(weatherData.getTemperatureMean())
                    .precipitation(weatherData.getPrecipitation())
                    .rainSum(weatherData.getRainSum())
                    .windSpeedMax(weatherData.getWindSpeedMax())
                    .windSpeedMean(weatherData.getWindSpeedMean())
                  .humidityMax(weatherData.getHumidityMax())
                    .humidityMin(weatherData.getHumidityMin())
                    .soilTemperature(weatherData.getSoilTemperature())
                    .soilMoisture(weatherData.getSoilMoisture())
                    .sunshineDuration(weatherData.getSunshineDuration())
                    .uvIndex(weatherData.getUvIndex())
                    .build();
    }
    public List<WeatherDTO> toDTOList(List<WeatherData> list) {
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
