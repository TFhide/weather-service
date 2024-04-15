package ru.task.weatherservice.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "external.weather.bit")
public record WeatherBitProperties(
        @NotBlank
        String baseUrl,
        @NotBlank
        String apiVersion,
        @NotBlank
        String endpoint,
        @NotBlank
        String apiKeyHeader,
        @NotNull
        String apiKey,
        String lang,
        @NotNull
        @Positive
        Integer hours,
        @NotNull
        @Positive
        Integer week,
        String units
) {
}
