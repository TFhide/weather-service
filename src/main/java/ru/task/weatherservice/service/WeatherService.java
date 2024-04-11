package ru.task.weatherservice.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {
    List<String> getCurrentDayForecast(String city);

    List<String> getWeeklyForecast(String city);
}
