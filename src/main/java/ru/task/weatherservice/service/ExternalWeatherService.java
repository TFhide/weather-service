package ru.task.weatherservice.service;

import ru.task.weatherservice.model.Coordinate;

import java.util.concurrent.CompletableFuture;

public interface ExternalWeatherService {

    CompletableFuture<String> getCurrentDayForecastUsingExternalService(Coordinate coordinate);

    CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate);

}
