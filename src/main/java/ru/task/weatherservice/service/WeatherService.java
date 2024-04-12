package ru.task.weatherservice.service;


import java.util.List;

public interface WeatherService {
    List<String> getCurrentDayForecast(String city);

    List<String> getWeeklyForecast(String city);

    void setExternalWeatherServices(List<ExternalWeatherService> mockedServices);
}
