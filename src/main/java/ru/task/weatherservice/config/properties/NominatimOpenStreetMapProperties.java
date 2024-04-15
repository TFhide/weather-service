package ru.task.weatherservice.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "external.openmap.street")
public record NominatimOpenStreetMapProperties(
        @NotBlank
        String baseUrl,
        @NotBlank
        String endpoint
) {
}
