package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.task.weatherservice.config.properties.NominatimOpenStreetMapProperties;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.service.ExternalGeoService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class NominatimOpenStreetMapServiceImpl implements ExternalGeoService {

    private final NominatimOpenStreetMapProperties properties;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(NominatimOpenStreetMapServiceImpl.class);

    @Autowired
    public NominatimOpenStreetMapServiceImpl(NominatimOpenStreetMapProperties properties, HttpClient httpClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CompletableFuture<Optional<Coordinate>> searchCoordinates(String city) {
        LOGGER.info("Searching coordinates for city: {}", city);
        return CompletableFuture.supplyAsync((() -> {
        URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                .queryParam("format", "json")
                .queryParam("q", city)
                .build().toUri();
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Coordinate> coordinates = objectMapper.readValue(response.body(), new TypeReference<>() {
            });
            Optional<Coordinate> firstCoordinate = coordinates.stream().findFirst();
            LOGGER.info("Found first coordinate {}", firstCoordinate);
            return firstCoordinate;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.error("Error occurred while searching coordinates", e);
            return Optional.empty();
        }
    }
    ));
    }
}
