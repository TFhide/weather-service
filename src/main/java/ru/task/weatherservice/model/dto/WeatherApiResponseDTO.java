package ru.task.weatherservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherApiResponseDTO(
        Location location,
        Current current,
        Forecast forecast
) {
    public record Location(
            String name,
            String region,
            String country,
            double lat,
            double lon,
            @JsonProperty("tz_id") String tzId,
            @JsonProperty("localtime_epoch")long localtimeEpoch,
            String localtime
    ) {}

    public record Condition(
            String text,
            String icon,
            int code
    ) {}

    public record Current(
            @JsonProperty("last_updated_epoch") long lastUpdatedEpoch,
            @JsonProperty("last_updated") String lastUpdated,
            @JsonProperty("temp_c") double tempC,
            @JsonProperty("temp_f") double tempF,
            @JsonProperty("is_day") int isDay,
            Condition condition,
            @JsonProperty("wind_mph") double windMph,
            @JsonProperty("wind_kph") double windKph,
            @JsonProperty("wind_degree") int windDegree,
            @JsonProperty("wind_dir") String windDir,
            @JsonProperty("pressure_mb") double pressureMb,
            @JsonProperty("pressure_in") double pressureIn,
            @JsonProperty("precip_mm") double precipMm,
            @JsonProperty("precip_in") double precipIn,
            int humidity,
            int cloud,
            @JsonProperty("feelslike_c") double feelslikeC,
            @JsonProperty("feelslike_f") double feelslikeF,
            @JsonProperty("vis_km") double visKm,
            @JsonProperty("vis_miles") double visMiles,
            double uv,
            @JsonProperty("gust_mph") double gustMph,
            @JsonProperty("gust_kph") double gustKph
    ) {}

    public record Day(
            @JsonProperty("maxtemp_c") double maxtempC,
            @JsonProperty("maxtemp_f") double maxtempF,
            @JsonProperty("mintemp_c") double mintempC,
            @JsonProperty("mintemp_f") double mintempF,
            @JsonProperty("avgtemp_c") double avgtempC,
            @JsonProperty("avgtemp_f") double avgtempF,
            @JsonProperty("maxwind_mph") double maxwindMph,
            @JsonProperty("maxwind_kph") double maxwindKph,
            @JsonProperty("totalprecip_mm") double totalprecipMm,
            @JsonProperty("totalprecip_in") double totalprecipIn,
            @JsonProperty("totalsnow_cm") double totalsnowCm,
            @JsonProperty("avgvis_km") double avgvisKm,
            @JsonProperty("avgvis_miles") double avgvisMiles,
            int avghumidity,
            @JsonProperty("daily_will_it_rain") int dailyWillItRain,
            @JsonProperty("daily_chance_of_rain") int dailyChanceOfRain,
            @JsonProperty("daily_will_it_snow") int dailyWillItSnow,
            @JsonProperty("daily_chance_of_snow") int dailyChanceOfSnow,
            Condition condition,
            double uv
    ) {}

    public record Astro(
            String sunrise,
            String sunset,
            String moonrise,
            String moonset,
            @JsonProperty("moon_phase") String moonPhase,
            @JsonProperty("moon_illumination") int moonIllumination,
            @JsonProperty("is_moon_up") int isMoonUp,
            @JsonProperty("is_sun_up") int isSunUp
    ) {}

    public record Hour(
            @JsonProperty("time_epoch") long timeEpoch,
            String time,
            @JsonProperty("temp_c") double tempC,
            @JsonProperty("temp_f") double tempF,
            @JsonProperty("is_day") int isDay,
            Condition condition,
            @JsonProperty("wind_mph") double windMph,
            @JsonProperty("wind_kph") double windKph,
            @JsonProperty("wind_degree") int windDegree,
            @JsonProperty("wind_dir") String windDir,
            @JsonProperty("pressure_mb") double pressureMb,
            @JsonProperty("pressure_in") double pressureIn,
            @JsonProperty("precip_mm") double precipMm,
            @JsonProperty("precip_in") double precipIn,
            @JsonProperty("snow_cm") double snowCm,
            int humidity,
            int cloud,
            @JsonProperty("feelslike_c") double feelslikeC,
            @JsonProperty("feelslike_f") double feelslikeF,
            @JsonProperty("windchill_c") double windchillC,
            @JsonProperty("windchill_f") double windchillF,
            @JsonProperty("heatindex_c") double heatindexC,
            @JsonProperty("heatindex_f") double heatindexF,
            @JsonProperty("dewpoint_c") double dewpointC,
            @JsonProperty("dewpoint_f") double dewpointF,
            @JsonProperty("will_it_rain") int willItRain,
            @JsonProperty("chance_of_rain") int chanceOfRain,
            @JsonProperty("will_it_snow") int willItSnow,
            @JsonProperty("chance_of_snow") int chanceOfSnow,
            @JsonProperty("vis_km") double visKm,
            @JsonProperty("vis_miles") double visMiles,
            @JsonProperty("gust_mph") double gustMph,
            @JsonProperty("gust_kph") double gustKph,
            double uv
    ) {}

    public record ForecastDay(
            String date,
            @JsonProperty("date_epoch") long dateEpoch,
            Day day,
            Astro astro,
            List<Hour> hour
    ) {}

    public record Forecast(
            List<ForecastDay> forecastday
    ) {}
}
