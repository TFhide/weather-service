package ru.task.weatherservice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinate(
        @JsonProperty("latitude") @JsonAlias("lat") double latitude,
        @JsonProperty("longitude") @JsonAlias("lon") double longitude
) {
}
