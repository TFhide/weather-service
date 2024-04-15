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
    private static final Logger LOGGER =
            LoggerFactory.getLogger(NominatimOpenStreetMapServiceImpl.class);

    @Autowired
    public NominatimOpenStreetMapServiceImpl(NominatimOpenStreetMapProperties properties,
                                             HttpClient httpClient, ObjectMapper objectMapper) {
        this.properties = properties;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public CompletableFuture<Optional<Coordinate>> searchCoordinates(String city) {
            return getResponseOfCoordinates(getUri(city));
    }

    private CompletableFuture<Optional<Coordinate>> getResponseOfCoordinates(URI uri) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        List<Coordinate> coordinates = objectMapper.readValue(response.body(),
                                new TypeReference<>() {});
                        LOGGER.info("Successfully found coordinates: {}", coordinates.stream().findFirst());
                        return coordinates.stream().findFirst();
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error while parsing response", e);
                        return Optional.<Coordinate>empty();
                    }
                }).exceptionally(ex -> {
                    LOGGER.error("Error occurred sending request.", ex);
                    return Optional.empty();
                });
    }

    private URI getUri(String city) {
        return UriComponentsBuilder.fromUriString(properties.baseUrl())
                .queryParam("format", "json")
                .queryParam("q", city)
                .build().toUri();
    }
}
