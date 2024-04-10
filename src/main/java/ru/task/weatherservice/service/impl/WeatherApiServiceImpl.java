package ru.task.weatherservice.service.impl;

import ru.task.weatherservice.model.dto.WeatherApiResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

public class WeatherApiServiceImpl implements ExternalWeatherService<WeatherApiResponseDTO> {
    @Override
    public WeatherApiResponseDTO getCurrentDayForecastUsingExternalService(String city) {
        return null;
    }

    @Override
    public WeatherApiResponseDTO getWeeklyForecastUsingExternalService(String city) {
        return null;
    }
}
