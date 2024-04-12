package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.task.weatherservice.config.properties.NominatimOpenStreetMapProperties;
import ru.task.weatherservice.exception.ExternalGeoServiceException;
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
@Qualifier("nominatim")
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
        URI uri = UriComponentsBuilder.fromUriString(properties.baseUrl())
                .queryParam("format", "json")
                .queryParam("q", city)
                .build().toUri();

        return getResponseOfCoordinates(uri);
    }

    private CompletableFuture<Optional<Coordinate>> getResponseOfCoordinates(URI uri) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .handle((response, e) -> {
                    if (e != null) {
                        LOGGER.error("Error occurred sending request.", e);
                        return Optional.empty();
                    }
                    List<Coordinate> coordinates = null;
                    try {
                        coordinates = objectMapper.readValue(response.body(), new TypeReference<>() {
                        });
                        return coordinates.stream().findFirst();
                    } catch (JsonProcessingException ex) {
                        throw new ExternalGeoServiceException("Error occurred while parsing response", ex);
                    }
                });
    }
}
