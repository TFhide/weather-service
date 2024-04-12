package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.task.weatherservice.config.properties.YandexWeatherProperties;
import ru.task.weatherservice.exception.ExternalWeatherServiceException;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.model.dto.YandexWeatherResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


@Service
@Qualifier("yandex")
public class YandexWeatherServiceImpl implements ExternalWeatherService {

    private final YandexWeatherProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexWeatherServiceImpl.class);

    @Autowired
    public YandexWeatherServiceImpl(YandexWeatherProperties properties, HttpClient httpClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CompletableFuture<String> getCurrentDayForecastUsingExternalService(Coordinate coordinate) {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam("lat", coordinate.latitude())
                    .queryParam("lon", coordinate.longitude())
                    .queryParam("limit", properties.limit())
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours())
                    .queryParam("extra", properties.extra())
                    .build().toUri();

            return getYandexWeatherResponseDTO(uri);
    }

    @Override
    public CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate) {
        URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                .replacePath(properties.apiVersion())
                .path(properties.endpoint())
                .queryParam("lat", coordinate.latitude())
                .queryParam("lon", coordinate.longitude())
                .queryParam("lang", properties.lang())
                .queryParam("limit", properties.limit() + 6)
                .queryParam("hours", properties.hours())
                .queryParam("extra", properties.extra())
                .build().toUri();

            return getYandexWeatherResponseDTO(uri);
    }

    private CompletableFuture<String> getYandexWeatherResponseDTO(URI uri) {
        return httpClient.sendAsync(HttpRequest.newBuilder().uri(uri).GET()
                .header(properties.apiKeyHeader(), properties.apiKey()).build(), HttpResponse.BodyHandlers.ofString())
                .handle((response, e) -> {
                    try {
                        YandexWeatherResponseDTO dto =
                                objectMapper.readValue(response.body(), YandexWeatherResponseDTO.class);
                        return dto.toString();
                    } catch (JsonProcessingException ex) {
                        LOGGER.error("Error processing the response", ex);
                        throw new ExternalWeatherServiceException("Error processing the response", ex);
                    }
                });
    }
}
