package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.task.weatherservice.config.properties.WeatherApiProperties;
import ru.task.weatherservice.exception.ExternalWeatherServiceException;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.model.dto.WeatherApiResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("weatherapi")
public class WeatherApiServiceImpl  {

    private final WeatherApiProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiServiceImpl.class);

    @Autowired
    public WeatherApiServiceImpl(WeatherApiProperties properties, HttpClient httpClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }
//
//    @Override
//    public CompletableFuture<String> getCurrentDayForecastUsingExternalService(Coordinate coordinate) {
//        return CompletableFuture.supplyAsync(() -> {
//                URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
//                        .replacePath(properties.apiVersion())
//                        .path(properties.endpoint())
//                        .queryParam("q", coordinate.latitude() + "," + coordinate.longitude())
//                        .queryParam("lang", properties.lang())
//                        .queryParam("days", properties.days())
//                        .build().toUri();
//
//                return getWeatherApiResponseDTO(uri);
//        }).exceptionally(e -> {
//            LOGGER.error("Error occurred while getting current day forecast", e);
//            return "";
//        });
//    }
//
//    @Override
//    public CompletableFuture<String> getWeeklyForecastUsingExternalService(Coordinate coordinate) {
//        return CompletableFuture.supplyAsync(() -> {
//            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
//                    .replacePath(properties.apiVersion())
//                    .path(properties.endpoint())
//                    .queryParam("q", coordinate.latitude() + "," + coordinate.longitude())
//                    .queryParam("days", properties.days() + 6)
//                    .queryParam("lang", properties.lang())
//                    .build().toUri();
//            return getWeatherApiResponseDTO(uri);
//        }).exceptionally(e -> {
//            LOGGER.error("Error occurred while getting weekly forecast", e);
//            return "";
//        });
//    }
//
//    private String getWeatherApiResponseDTO(URI uri) {
//        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri)
//                .header(properties.apiKeyHeader(), properties.apiKey()).build();
//        LOGGER.info("Sending request to {}", uri);
//
//        HttpResponse<String> response;
//        try {
//            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            WeatherApiResponseDTO dto = objectMapper.readValue(response.body(), WeatherApiResponseDTO.class);
//            return objectMapper.writeValueAsString(dto);
//        } catch (JsonProcessingException e) {
//            LOGGER.error("Error occurred while processing response from {}", uri, e);
//            throw new ExternalWeatherServiceException("Error occurred while processing response");
//        } catch (Exception e) {
//            LOGGER.error("Error occurred while sending request to {}", uri, e);
//            throw new ExternalWeatherServiceException("Error occurred due request process");
//        }
//    }
}
