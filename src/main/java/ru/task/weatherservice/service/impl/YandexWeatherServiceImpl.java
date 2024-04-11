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
public class YandexWeatherServiceImpl  {

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
//
//    @Override
//    public CompletableFuture<String> getCurrentDayForecastUsingExternalService(Coordinate coordinate) {
//        return CompletableFuture.supplyAsync(() -> {
//            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
//                    .replacePath(properties.apiVersion())
//                    .path(properties.endpoint())
//                    .queryParam("lat", coordinate.latitude())
//                    .queryParam("lon", coordinate.longitude())
//                    .queryParam("limit", properties.limit())
//                    .queryParam("lang", properties.lang())
//                    .queryParam("hours", properties.hours())
//                    .queryParam("extra", properties.extra())
//                    .build().toUri();
//
//            return getYandexWeatherResponseDTO(uri);
//        }).exceptionally(e -> {
//            LOGGER.error("Error occurred while getting current day forecast", e);
//            return "";
//        });
//    }
//
//    @Override
//    public CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate) {
//        return CompletableFuture.supplyAsync(() -> {
//        URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
//                .replacePath(properties.apiVersion())
//                .path(properties.endpoint())
//                .queryParam("lat", coordinate.latitude())
//                .queryParam("lon", coordinate.longitude())
//                .queryParam("lang", properties.lang())
//                .queryParam("limit", properties.limit() + 6)
//                .queryParam("hours", properties.hours())
//                .queryParam("extra", properties.extra())
//                .build().toUri();
//            return getYandexWeatherResponseDTO(uri);
//        }).exceptionally(e -> {
//            LOGGER.error("Error occurred while getting weekly forecast", e);
//            return "";
//        });
//    }
//
//    private String getYandexWeatherResponseDTO(URI uri) {
//        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri)
//                .header(properties.apiKeyHeader(), properties.apiKey()).build();
//        LOGGER.info("Sending request to {}", uri);
//
//        HttpResponse<String> response;
//        try {
//            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            YandexWeatherResponseDTO dto = objectMapper.readValue(response.body(), YandexWeatherResponseDTO.class);
//            return objectMapper.writeValueAsString(dto);
//        } catch (JsonProcessingException e) {
//            LOGGER.error("Error occurred while processing response", e);
//            throw new ExternalWeatherServiceException("Error occurred while processing response");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new ExternalWeatherServiceException("Thread was interrupted during HTTP request", e);
//        } catch (Exception e) {
//            LOGGER.error("Error occurred while sending request", e);
//            throw new ExternalWeatherServiceException("Error occurred due request process");
//        }
//    }
}
