package ru.task.weatherservice.config.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "external.weather.api")
public record WeatherApiProperties(
        @NotNull
        String baseUrl,
        @NotNull
        String apiVersion,
        @NotNull
        String endpoint,
        @NotNull
        String apiKey
) {
}