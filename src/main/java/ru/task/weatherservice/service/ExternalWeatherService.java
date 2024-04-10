package ru.task.weatherservice.service;


public interface ExternalWeatherService<T> {
    T getCurrentDayForecastUsingExternalService(String city);

    T getWeeklyForecastUsingExternalService(String city);
}
