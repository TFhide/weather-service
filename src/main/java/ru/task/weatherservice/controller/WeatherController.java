package ru.task.weatherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.task.weatherservice.service.WeatherService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public CompletableFuture<ResponseEntity<List<String>>> getCurrentWeather(
            @RequestParam(defaultValue = "Krasnodar") String city) {
        return weatherService.getCurrentDayForecast(city)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/weekly")
    public CompletableFuture<ResponseEntity<List<String>>> getWeeklyForecast(
            @RequestParam(defaultValue = "Krasnodar") String city) {
        return weatherService.getWeeklyForecast(city)
                .thenApply(ResponseEntity::ok);
    }
}
