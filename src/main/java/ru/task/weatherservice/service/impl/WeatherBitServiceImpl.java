package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
//@ConditionalOnProperty(name = "weather.provider", havingValue = "weatherbit")
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
        return CompletableFuture.supplyAsync(() -> {
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

            return getYandexWeatherResponseDTO(uri);
        }).exceptionally(e -> {
            LOGGER.error("Error occurred while getting current day forecast", e);
            return "";
        });
    }

    @Override
    public CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate) {
        return CompletableFuture.supplyAsync(() -> {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam(properties.apiKeyHeader(), properties.apiKey())
                    .queryParam("lat", coordinate.latitude())
                    .queryParam("lon", coordinate.longitude())
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours() + 244)
                    .queryParam("units", properties.units())
                    .build().toUri();

            return getYandexWeatherResponseDTO(uri);
        }).exceptionally(e -> {
            LOGGER.error("Error occurred while getting weekly forecast", e);
            return "";
        });
        }

    private String getYandexWeatherResponseDTO(URI uri) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        LOGGER.info("Sending request to {}", uri);
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            WeatherBitResponseDTO dto = objectMapper.readValue(response.body(), WeatherBitResponseDTO.class);
            return objectMapper.writeValueAsString(dto);
        }
        catch (JsonProcessingException e) {
            throw new ExternalWeatherServiceException("Error occurred while processing response", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ExternalWeatherServiceException("Thread was interrupted during HTTP request", e);
        }
        catch (Exception e) {
            LOGGER.error("Error occurred while sending request to {}", uri, e);
            throw new ExternalWeatherServiceException("Error occurred due request process", e);
        }
    }
}
