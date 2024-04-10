package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.task.weatherservice.config.properties.YandexWeatherProperties;
import ru.task.weatherservice.exception.ExternalWeatherServiceException;
import ru.task.weatherservice.model.dto.YandexWeatherResponseDTO;
import ru.task.weatherservice.service.ExternalWeatherService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@ConditionalOnMissingBean(value = ExternalWeatherService.class, ignored = YandexWeatherServiceImpl.class)
public class YandexWeatherServiceImpl implements ExternalWeatherService<YandexWeatherResponseDTO> {

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
    public YandexWeatherResponseDTO getCurrentDayForecastUsingExternalService(String city) {
            URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                    .replacePath(properties.apiVersion())
                    .path(properties.endpoint())
                    .queryParam("city", city)
                    .queryParam("limit", properties.limit())
                    .queryParam("lang", properties.lang())
                    .queryParam("hours", properties.hours())
                    .queryParam("extra", properties.extra())
                    .build().toUri();

            return getYandexWeatherResponseDTO(uri);
    }

    @Override
    public YandexWeatherResponseDTO getWeeklyForecastUsingExternalService(String city) {
        URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                .replacePath(properties.apiVersion())
                .path(properties.endpoint())
                .queryParam("city", city)
                .queryParam("lang", properties.lang())
                .queryParam("limit", properties.limit() + 6)
                .queryParam("hours", properties.hours())
                .queryParam("extra", properties.extra())
                .build().toUri();
            return getYandexWeatherResponseDTO(uri);
    }

    private YandexWeatherResponseDTO getYandexWeatherResponseDTO(URI uri) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri)
                .header(properties.apiKeyHeader(), properties.apiKey()).build();
        LOGGER.info("Sending request to {}", uri.toString());

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            YandexWeatherResponseDTO yandexWeatherResponseDTO =
                    objectMapper.readValue(response.body(), YandexWeatherResponseDTO.class);
            return yandexWeatherResponseDTO;
        } catch (JsonProcessingException e) {
            LOGGER.error("Error occurred while processing response from {}", uri, e);
            throw new ExternalWeatherServiceException("Error occurred while processing response");
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending request to {}", uri, e);
            throw new ExternalWeatherServiceException("Error occurred due request process");
        }
    }
}
