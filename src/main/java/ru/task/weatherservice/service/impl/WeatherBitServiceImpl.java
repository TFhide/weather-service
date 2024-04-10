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
import ru.task.weatherservice.model.dto.WeatherBitResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@ConditionalOnProperty(name = "weather.provider", havingValue = "weatherbit")
public class WeatherBitServiceImpl implements ExternalWeatherService<WeatherBitResponseDTO> {

    private final WeatherBitProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(YandexWeatherServiceImpl.class);

    @Autowired
    public WeatherBitServiceImpl(WeatherBitProperties properties, HttpClient httpClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public WeatherBitResponseDTO getCurrentDayForecastUsingExternalService(String city) {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam(properties.apiKeyHeader(), properties.apiKey())
                    .queryParam("city", city)
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours())
                    .queryParam("units", properties.units())
                    .build().toUri();

            return getYandexWeatherResponseDTO(uri);
    }

    @Override
    public WeatherBitResponseDTO getWeeklyForecastUsingExternalService(String city) {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam(properties.apiKeyHeader(), properties.apiKey())
                    .queryParam("city", city)
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours() + 244)
                    .queryParam("units", properties.units())
                    .build().toUri();

            return getYandexWeatherResponseDTO(uri);
        }

    private WeatherBitResponseDTO getYandexWeatherResponseDTO(URI uri) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        LOGGER.info("Sending request to {}", uri.toString());
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            WeatherBitResponseDTO yandexWeatherResponseDTO =
                    objectMapper.readValue(response.body(), WeatherBitResponseDTO.class);
            return yandexWeatherResponseDTO;
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error occurred while processing response from {}", uri, e);
            throw new ExternalWeatherServiceException("Error occurred while processing weather data");
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending request to {}", uri, e);
            throw new ExternalWeatherServiceException("Error occurred due request process");
        }
    }
}
