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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final List<ExternalWeatherService> externalWeatherServices;
    private final ExternalGeoService externalGeoService;
    @Value("${weather.provider}")
    private String provider;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Autowired
    public WeatherServiceImpl(List<ExternalWeatherService> externalWeatherServices,
                              ExternalGeoService externalGeoService) {
        this.externalWeatherServices = externalWeatherServices;
        this.externalGeoService = externalGeoService;
    }

    @Override
    public CompletableFuture<List<String>> getCurrentDayForecast(String city) {
        CompletableFuture<Optional<Coordinate>> coordinateFuture =
                externalGeoService.searchCoordinates(city);
        return coordinateFuture.thenCompose(coordinateOpt -> {
            if (coordinateOpt.isPresent()) {
                Coordinate coordinate = coordinateOpt.get();
                return getForecasts(service -> service.getCurrentDayForecastUsingExternalService(coordinate));
            } else {
                return CompletableFuture
                        .completedFuture(Collections.singletonList("Coordinates not found for city: " + city));
            }
        });
    }

    @Override
    public CompletableFuture<List<String>> getWeeklyForecast(String city) {
        CompletableFuture<Optional<Coordinate>> coordinateFuture = externalGeoService.searchCoordinates(city);
        return coordinateFuture.thenCompose(coordinateOpt -> {
            if (coordinateOpt.isPresent()) {
                Coordinate coordinate = coordinateOpt.get();
                return getForecasts(service -> service.getWeeklyForecastUsingExternalService(coordinate));
            } else {
                return CompletableFuture
                        .completedFuture(Collections.singletonList("Coordinates not found for city: " + city));
            }
        });
    }

    private Stream<ExternalWeatherService> filterServicesByProvider(String provider,
                                                                    Stream<ExternalWeatherService> servicesStream) {
        return provider.equals("all") ? servicesStream :
                servicesStream.filter(s -> s.getClass().getSimpleName().toLowerCase()
                        .contains(provider.toLowerCase()));
    }

    private CompletableFuture<List<String>> getForecasts(Function<ExternalWeatherService,
            CompletableFuture<String>> forecastFunction) {
        List<CompletableFuture<String>> handledFutures = filterServicesByProvider(provider,
                externalWeatherServices.stream())
                .map(forecastFunction)
                .map(future -> future.exceptionally(e -> {
                    LOGGER.error("Error occurred sending request.", e);
                    return "Error occurred sending request.";
                })).toList();

        return CompletableFuture.allOf(handledFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    List<String> forecasts = handledFutures.stream()
                            .map(CompletableFuture::join)
                            .toList();
                    LOGGER.info("Successfully gathered all forecasts {}", forecasts); // Перемещено сюда
                    return forecasts;
                });
    }
}
