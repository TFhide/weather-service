package ru.task.weatherservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherBitResponseDTO(
        @JsonProperty("city_name") String cityName,
        @JsonProperty("country_code") String countryCode,
        List<Data> data,
        @JsonProperty("lat") String latitude,
        @JsonProperty("lon") String longitude,
        @JsonProperty("state_code") String stateCode,
        String timezone
) {
    public record Weather(
            int code,
            String description,
            String icon
    ) {
    }

    public record Data(
            @JsonProperty("app_temp") double appTemp,
            int clouds,
            @JsonProperty("clouds_hi") int cloudsHi,
            @JsonProperty("clouds_low") int cloudsLow,
            @JsonProperty("clouds_mid") int cloudsMid,
            String datetime,
            double dewpt,
            double dhi,
            double dni,
            double ghi,
            double ozone,
            String pod,
            int pop,
            double precip,
            int pres,
            int rh,
            double slp,
            int snow,
            @JsonProperty("snow_depth") int snowDepth,
            @JsonProperty("solar_rad") double solarRad,
            double temp,
            @JsonProperty("timestamp_local") String timestampLocal,
            @JsonProperty("timestamp_utc") String timestampUtc,
            long ts,
            double uv,
            double vis,
            Weather weather,
            @JsonProperty("wind_cdir") String windCdir,
            @JsonProperty("wind_cdir_full") String windCdirFull,
            @JsonProperty("wind_dir") int windDir,
            @JsonProperty("wind_gust_spd") double windGustSpd,
            @JsonProperty("wind_spd") double windSpd
    ) {
    }
}