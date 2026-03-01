package com.agri.agrimanager.service;

import com.agri.agrimanager.entity.Parcelle;
import com.agri.agrimanager.entity.WeatherData;
import com.agri.agrimanager.feign.WeatherClient;
import com.agri.agrimanager.repository.ParcelleRepository;
import com.agri.agrimanager.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;
    private final WeatherRepository weatherRepository;
    private final ParcelleRepository parcelleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public double[] extractLatitudeLongitude(String geoJson){
        try{
            JsonNode geometry = objectMapper.readTree(geoJson);
            JsonNode coordinates = geometry.get("coordinates").get(0);

            double totalLat=0;
            double totalLong=0;
            int count = coordinates.size();

            for(JsonNode point : coordinates){
                totalLong += point.get(0).asDouble();
                totalLat += point.get(1).asDouble();
            }
            return new double[]{totalLat/count, totalLong/count};

        }catch (Exception e) {
            throw new RuntimeException("Erreur extraction centroïde GeoJSON", e);
        }
    }
    public WeatherData getWeatherForecast(Long parcelleId) throws JsonProcessingException {
        Parcelle parcelle = parcelleRepository.findById(parcelleId).orElse(null);
        double[] centroid = extractLatitudeLongitude(parcelle.getGeometryJson());
        double latitude = centroid[0];
        double longitude = centroid[1];

        String response = weatherClient.getWeatherForecast(
                latitude,
                longitude,
                "temperature_2m_max,temperature_2m_min,temperature_2m_mean," +
                        "precipitation_sum,rain_sum," +
                        "wind_speed_10m_max,wind_speed_10m_mean," + // ← ajouter !
                        "sunshine_duration,uv_index_max",
                "Africa/Tunis",
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString()
        );

        JsonNode json = objectMapper.readTree(response);
        JsonNode daily = json.path("daily");
        WeatherData weatherData = WeatherData.builder()
                .parcelle(parcelle)
                .date(LocalDate.now())
                .temperatureMax(daily.path("temperature_2m_max").get(0).asDouble())
                .temperatureMin(daily.path("temperature_2m_min").get(0).asDouble())
                .temperatureMean(daily.path("temperature_2m_mean").get(0).asDouble())
                .precipitation(daily.path("precipitation_sum").get(0).asDouble())
                .rainSum(daily.path("rain_sum").get(0).asDouble())
                .windSpeedMax(daily.path("wind_speed_10m_max").get(0).asDouble())
                .windSpeedMean(daily.path("wind_speed_10m_mean").get(0).asDouble())
                .sunshineDuration(daily.path("sunshine_duration").get(0).asDouble())
                .uvIndex(daily.path("uv_index_max").get(0).asDouble())
                // ← soilTemperature et soilMoisture supprimés !
                .build();
        return weatherRepository.save(weatherData);
    }
    public List<WeatherData> getWeatherHistoryByParcelle(Long parcelleId){
        return weatherRepository.findByParcelleId(parcelleId);
    }
    public void deleteWeather(Long id){
        weatherRepository.deleteById(id);
    }
}
