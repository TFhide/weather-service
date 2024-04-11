package ru.task.weatherservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "external.openmap.street")
public record NominatimOpenStreetMapProperties(
        String baseUrl,
        String endpoint
) {
}
