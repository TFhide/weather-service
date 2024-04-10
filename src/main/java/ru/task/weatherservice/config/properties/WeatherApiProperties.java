package ru.task.weatherservice.config.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
        String apiKeyHeader,
        @NotNull
        String apiKey,
        String lang,
        @NotNull
        @Positive
        int days
) {
}
