package ru.task.weatherservice.service.impl;

import ru.task.weatherservice.model.dto.WeatherBitResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

public class WeatherBitServiceImpl implements ExternalWeatherService<WeatherBitResponseDTO> {
    @Override
    public WeatherBitResponseDTO getCurrentDayForecastUsingExternalService(String city) {
        return null;
    }

    @Override
    public WeatherBitResponseDTO getWeeklyForecastUsingExternalService(String city) {
        return null;
    }
}
