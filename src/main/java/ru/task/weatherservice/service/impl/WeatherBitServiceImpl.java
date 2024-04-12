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
import ru.task.weatherservice.exception.ExternalWeatherServiceException;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.model.dto.WeatherBitResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("weatherbit")
public class WeatherBitServiceImpl implements ExternalWeatherService {

    private final WeatherBitProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherBitServiceImpl.class);

    @Autowired
    public WeatherBitServiceImpl(WeatherBitProperties properties, HttpClient httpClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CompletableFuture<String> getCurrentDayForecastUsingExternalService(Coordinate coordinate) {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam(properties.apiKeyHeader(), properties.apiKey())
                    .queryParam("lat", coordinate.latitude())
                    .queryParam("lon", coordinate.longitude())
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours())
                    .queryParam("units", properties.units())
                    .build().toUri();

            return getWeatherBitResponseDTO(uri);
        }


    @Override
    public CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate) {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam(properties.apiKeyHeader(), properties.apiKey())
                    .queryParam("lat", coordinate.latitude())
                    .queryParam("lon", coordinate.longitude())
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours() + 144)
                    .queryParam("units", properties.units())
                    .build().toUri();

            return getWeatherBitResponseDTO(uri);
    }

    private CompletableFuture<String> getWeatherBitResponseDTO(URI uri) {
        return httpClient.sendAsync(HttpRequest.newBuilder().uri(uri).GET().build(), HttpResponse.BodyHandlers.ofString())
                .handle((response, e) -> {
                    if (e != null) {
                        LOGGER.error("Error occurred sending request.", e);
                        return "Error occurred sending request.";
                    }
                    try {
                        WeatherBitResponseDTO dto = objectMapper.readValue(response.body(), WeatherBitResponseDTO.class);
                        return dto.toString();
                    } catch (JsonProcessingException ex) {
                        LOGGER.error("Error processing the response", ex);
                        throw new ExternalWeatherServiceException("Error processing the response", ex);
                    }
                });
    }
}