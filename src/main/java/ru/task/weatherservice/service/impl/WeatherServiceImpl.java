package ru.task.weatherservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.service.ExternalGeoService;
import ru.task.weatherservice.service.ExternalWeatherService;
import ru.task.weatherservice.service.WeatherService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final List<ExternalWeatherService> externalWeatherServices;
    private final ExternalGeoService externalGeoService;
    @Value("${weather.provider}")
    private String provider;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherBitServiceImpl.class);

    @Autowired
    public WeatherServiceImpl(List<ExternalWeatherService> externalWeatherServices, ExternalGeoService externalGeoService) {
        this.externalWeatherServices = externalWeatherServices;
        this.externalGeoService = externalGeoService;
    }

    @Override
    public List<String> getCurrentDayForecast(String city) {
        return getForecasts(service -> service.getCurrentDayForecastUsingExternalService(getCoordinate(city)));
    }

    @Override
    public List<String> getWeeklyForecast(String city) {
        return getForecasts(service -> service.getWeeklyForecastUsingExternalService(getCoordinate(city)));
    }

    private Coordinate getCoordinate(String city) {
        return externalGeoService.searchCoordinates(city)
                .join()
                .orElseThrow(() -> new IllegalArgumentException("Empty coordinates."));
    }

    private Stream<ExternalWeatherService> filterServicesByProvider(String provider, Stream<ExternalWeatherService> servicesStream) {
        return provider.equals("all") ? servicesStream :
                servicesStream.filter(s -> s.getClass().getSimpleName().toLowerCase().contains(provider.toLowerCase()));
    }

    private List<String> getForecasts(Function<ExternalWeatherService, CompletableFuture<String>> forecastFunction) {
        Stream<ExternalWeatherService> servicesStream = externalWeatherServices.stream();
        servicesStream = filterServicesByProvider(provider, servicesStream);

        List<CompletableFuture<String>> futureList =
                servicesStream.map(forecastFunction)
                        .toList();

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));

        return allDone.thenApply(v -> futureList.stream()
                        .map(future -> future.exceptionally(e -> {
                            LOGGER.error("Error during forecast fetch", e);
                            return "Error";
                        }).join())
                        .toList())
                .join();

//        CompletableFuture<Void> allResults =
//                CompletableFuture.allOf(futureList.toArray(CompletableFuture[]::new));
//
//        return allResults.thenApply(unused -> futureList.stream()
//                        .map(CompletableFuture::join)
//                        .toList())
//                .join();
    }
}
