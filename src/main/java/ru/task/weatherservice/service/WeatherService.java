package ru.task.weatherservice.service;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WeatherService {
    CompletableFuture<List<String>> getCurrentDayForecast(String city);

    CompletableFuture<List<String>> getWeeklyForecast(String city);
}
