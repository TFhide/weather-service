package ru.task.weatherservice.service;

import ru.task.weatherservice.model.Coordinate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ExternalGeoService {

    CompletableFuture<Optional<Coordinate>> searchCoordinates(String city);

}
