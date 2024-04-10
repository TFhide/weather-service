package ru.task.weatherservice.service.impl;

import org.springframework.stereotype.Service;
import ru.task.weatherservice.service.WeatherService;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Override
    public String getCurrentDayForecast(String city) {
        return null;
    }

    @Override
    public List<String> getWeeklyForecast(String city) {
        return null;
    }

}
