package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.task.weatherservice.config.properties.WeatherBitProperties;
import ru.task.weatherservice.model.ApiResponse;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.model.dto.WeatherBitResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;
import utils.HttpStatusHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("weatherbit")
public class WeatherBitServiceImpl implements ExternalWeatherService {

    public static final String SERVICE_NAME = "WeatherBit";
    private final WeatherBitProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherBitServiceImpl.class);

    @Autowired
    public WeatherBitServiceImpl(WeatherBitProperties properties, HttpClient httpClient,
                                 ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CompletableFuture<String> getCurrentDayForecastUsingExternalService(Coordinate coordinate) {
            return getWeatherBitResponseDTO(getUri(coordinate, properties.hours()));
        }

    @Override
    public CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate) {
            return getWeatherBitResponseDTO(getUri(coordinate, properties.week()));
    }

    private CompletableFuture<String> getWeatherBitResponseDTO(URI uri) {
        return httpClient.sendAsync(HttpRequest.newBuilder().uri(uri).GET()
                        .build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        ApiResponse apiResponse = HttpStatusHandler.handleResponseStatus(SERVICE_NAME, response);
                        if(!apiResponse.success()) {
                            LOGGER.error("Error response: {}", apiResponse.message());
                            return apiResponse.message();
                        }
                        String data =
                                objectMapper.readValue(response.body(), WeatherBitResponseDTO.class).toString();
                        return ApiResponse.ok(SERVICE_NAME, data).toString();
                    } catch (JsonProcessingException ex) {
                        LOGGER.error("Error processing the response.", ex);
                        return ApiResponse.error(SERVICE_NAME,"Error processing the response.").message();
                    }
                }).exceptionally(ex -> {
                    LOGGER.error("Error occurred sending request.", ex);
                    return ApiResponse.error(SERVICE_NAME,"Error occurred sending request.").message();
                });
    }

    private URI getUri(Coordinate coordinate, int hoursOrWeek) {
        return UriComponentsBuilder.fromUriString(properties.baseUrl())
                .replacePath(properties.apiVersion())
                .path(properties.endpoint())
                .queryParam(properties.apiKeyHeader(), properties.apiKey())
                .queryParam("lat", coordinate.latitude())
                .queryParam("lon", coordinate.longitude())
                .queryParam("lang", properties.lang())
                .queryParam("hours", hoursOrWeek)
                .queryParam("units", properties.units())
                .build().toUri();
    }
}