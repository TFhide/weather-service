package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.task.weatherservice.exception.WeatherServiceException;
import ru.task.weatherservice.service.ExternalWeatherService;
import ru.task.weatherservice.service.WeatherService;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final ExternalWeatherService externalWeatherService;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherServiceImpl(ExternalWeatherService externalWeatherService, ObjectMapper objectMapper) {
        this.externalWeatherService = externalWeatherService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getCurrentDayForecast(String city) {
        try {
            return objectMapper.writeValueAsString(externalWeatherService.getCurrentDayForecastUsingExternalService(city));
        } catch (JsonProcessingException e) {
            throw new WeatherServiceException("Error occurred while processing weather data");
        } catch (Exception e) {
            throw new WeatherServiceException("Failed to get weather from external service.", e);
        }
    }

    @Override
    public List<String> getWeeklyForecast(String city) {
        try {
            String json = objectMapper.writeValueAsString(externalWeatherService.getCurrentDayForecastUsingExternalService(city));
            return List.of(json, json, json);
        } catch (JsonProcessingException e) {
            throw new WeatherServiceException("Error occurred while processing weather data");
        } catch (Exception e) {
            throw new WeatherServiceException("Failed to get weather from external service.", e);
        }
    }

}
