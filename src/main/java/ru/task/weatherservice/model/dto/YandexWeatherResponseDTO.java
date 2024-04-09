package ru.task.weatherservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record YandexWeatherResponseDTO(
        long now,
        String nowDt,
        Info info,
        GeoObject geoObject,
        Yesterday yesterday,
        Fact fact,
        List<Forecast> forecasts
) {

    public record Tzinfo(
            String name,
            String abbr,
            boolean dst,
            int offset
    ) {}

    public record Info(
            boolean n,
            int geoId,
            String url,
            double lat,
            double lon,
            Tzinfo tzinfo,
            @JsonProperty("def_pressure_mm") int defPressureMm,
            @JsonProperty("def_pressure_pa") int defPressurePa,
            String slug,
            int zoom,
            boolean nr,
            boolean ns,
            boolean nsr,
            boolean p,
            boolean f,
            @JsonProperty("_h")boolean h
    ) {}

    public record District(int id, String name) {}
    public record Locality(int id, String name) {}
    public record Province(int id, String name) {}
    public record Country(int id, String name) {}

    public record GeoObject(
            District district,
            Locality locality,
            Province province,
            Country country
    ) {}

    public record Yesterday(int temp) {}

    public record AccumPrec(
            @JsonProperty("1") double oneDay,
            @JsonProperty("3") double threeDays,
            @JsonProperty("7") double sevenDays
    ) {}

    public record Fact(
            @JsonProperty("obs_time") long obsTime,
            long uptime,
            int temp,
            @JsonProperty("feels_like") int feelsLike,
            String icon,
            String condition,
            int cloudness,
            @JsonProperty("prec_type") int precType,
            @JsonProperty("prec_prob") int precProb,
            @JsonProperty("prec_strength") double precStrength,
            @JsonProperty("is_thunder") boolean isThunder,
            @JsonProperty("wind_speed") double windSpeed,
            @JsonProperty("wind_dir") String windDir,
            @JsonProperty("pressure_mm") int pressureMm,
            @JsonProperty("pressure_pa") int pressurePa,
            int humidity,
            String daytime,
            boolean polar,
            String season,
            String source,
            @JsonProperty("accum_prec") AccumPrec accumPrec,
            @JsonProperty("soil_moisture") double soilMoisture,
            @JsonProperty("soil_temp") int soilTemp,
            @JsonProperty("uv_index") int uvIndex,
            @JsonProperty("wind_gust") double windGust
    ) {}

    public record Part(
            @JsonProperty("_source") String source,
            @JsonProperty("temp_min") int tempMin,
            @JsonProperty("temp_avg") int tempAvg,
            @JsonProperty("temp_max") int tempMax,
            @JsonProperty("wind_speed") double windSpeed,
            @JsonProperty("wind_gust") double windGust,
            @JsonProperty("wind_dir") String windDir,
            @JsonProperty("pressure_mm") int pressureMm,
            @JsonProperty("pressure_pa") int pressurePa,
            int humidity,
            @JsonProperty("soil_temp") int soilTemp,
            @JsonProperty("soil_moisture") double soilMoisture,
            @JsonProperty("prec_mm") double precMm,
            @JsonProperty("prec_prob") int precProb,
            @JsonProperty("prec_period") int precPeriod,
            int cloudness,
            @JsonProperty("prec_type") int precType,
            @JsonProperty("prec_strength") double precStrength,
            String icon,
            String condition,
            @JsonProperty("uv_index") int uvIndex,
            @JsonProperty("feels_like") int feelsLike,
            String daytime,
            boolean polar,
            @JsonProperty("fresh_snow_mm") int freshSnowMm
    ){}

    public record PartShort(
            @JsonProperty("_source") String source,
            int temp,
            @JsonProperty("wind_speed") double windSpeed,
            @JsonProperty("wind_gust") double windGust,
            @JsonProperty("wind_dir") String windDir,
            @JsonProperty("pressure_mm") int pressureMm,
            @JsonProperty("pressure_pa") int pressurePa,
            int humidity,
            @JsonProperty("soil_temp") int soilTemp,
            @JsonProperty("soil_moisture") double soilMoisture,
            @JsonProperty("prec_mm") double precMm,
            @JsonProperty("prec_prob") int precProb,
            @JsonProperty("prec_period") int precPeriod,
            int cloudness,
            @JsonProperty("prec_type") int precType,
            @JsonProperty("prec_strength") double precStrength,
            String icon,
            String condition,
            @JsonProperty("uv_index") int uvIndex,
            @JsonProperty("feels_like") int feelsLike,
            String daytime,
            boolean polar,
            @JsonProperty("fresh_snow_mm") int freshSnowMm
    ){}

    public record Parts(
            Part day,
            @JsonProperty("day_short") PartShort dayShort,
            Part evening,
            Part morning,
            Part night,
            @JsonProperty("night_short") PartShort nightShort
    ) {}

    public record Hour(
            String hour,
            @JsonProperty("hour_ts") long hourTs,
            int temp,
            @JsonProperty("feels_like") int feelsLike,
            String icon,
            String condition,
            int cloudness,
            @JsonProperty("prec_type") int precType,
            @JsonProperty("prec_strength") double precStrength,
            @JsonProperty("is_thunder")boolean isThunder,
            @JsonProperty("wind_dir") String windDir,
            @JsonProperty("wind_speed") double windSpeed,
            @JsonProperty("wind_gust") double wind_Gust,
            @JsonProperty("pressure_mm") int pressureMm,
            @JsonProperty("pressure_pa") int pressurePa,
            int humidity,
            @JsonProperty("uv_index") int uvIndex,
            @JsonProperty("soil_temp") int soilTemp,
            @JsonProperty("soil_moisture") double soilMoisture,
            @JsonProperty("prec_mm") double precMm,
            @JsonProperty("prec_period") int precPeriod,
            @JsonProperty("prec_prob") int precProb
    ) {}

    public record Biomet(
            int index,
            String condition
    ) {}

    public record Forecast(
            String date,
            @JsonProperty("date_ts") long dateTs,
            int week,
            String sunrise,
            String sunset,
            @JsonProperty("rise_begin") String riseBegin,
            @JsonProperty("set_end") String setEnd,
            @JsonProperty("moon_code") int moonCode,
            @JsonProperty("moon_text") String moonText,
            Parts parts,
            List<Hour> hours,
            Biomet biomet
    ) {}
}
