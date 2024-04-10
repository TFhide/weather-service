package ru.task.weatherservice.service.impl;

import ru.task.weatherservice.model.dto.YandexWeatherResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

public class YandexWeatherServiceImpl implements ExternalWeatherService<YandexWeatherResponseDTO> {

    @Override
    public YandexWeatherResponseDTO getCurrentDayForecastUsingExternalService(String city) {
        return null;
    }

    @Override
    public YandexWeatherResponseDTO getWeeklyForecastUsingExternalService(String city) {
        return null;
    }
}
