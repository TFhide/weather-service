package ru.task.weatherservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.service.ExternalGeoService;
import ru.task.weatherservice.service.ExternalWeatherService;
import ru.task.weatherservice.service.WeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final List<ExternalWeatherService> externalWeatherServices;
    private final ExternalGeoService externalGeoService;
    private final Environment env; // Добавляем Environment

    @Autowired
    public WeatherServiceImpl(List<ExternalWeatherService> externalWeatherServices,
                              ExternalGeoService externalGeoService,
                              Environment env) { // Изменяем конструктор для внедрения Environment
        this.externalWeatherServices = externalWeatherServices;
        this.externalGeoService = externalGeoService;
        this.env = env;
    }

    @Override
    public List<String> getCurrentDayForecast(String city) {
        return getForecasts(city, service -> service.getCurrentDayForecastUsingExternalService(getCoordinate(city)))
                .stream()
                .collect(Collectors.toList());
    }

    private List<String> getForecasts(String city, Function<ExternalWeatherService, CompletableFuture<String>> forecastFunction) {
        String provider = env.getProperty("weather.provider", "all");
        Stream<ExternalWeatherService> servicesStream = externalWeatherServices.stream();

        if (!provider.equals("all")) {
            servicesStream = servicesStream.filter(s -> s.getClass().getSimpleName().toLowerCase().contains(provider.toLowerCase()));
        }

         List<CompletableFuture<String>> futures = servicesStream
                .map(service -> forecastFunction.apply(service))
                .toList();

        return CompletableFuture<List<String>> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .toList());
    }

    @Override
    public List<String> getWeeklyForecast(String city) {
        return new ArrayList<>(getForecasts(city, service -> service.getWeeklyForecastUsingExternalService(getCoordinate(city))));
    }

    private Coordinate getCoordinate(String city) {
        return externalGeoService.searchCoordinates(city)
                .join()
                .orElseThrow(() -> new IllegalArgumentException("Empty coordinates."));
    }
}
