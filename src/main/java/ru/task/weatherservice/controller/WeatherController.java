package ru.task.weatherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.task.weatherservice.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentWeather(@RequestParam(defaultValue = "Krasnodar") String city) {
        return ResponseEntity.ok().body(weatherService.getCurrentDayForecast(city));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<String>> getWeeklyForecast(@RequestParam(defaultValue = "Krasnodar") String city) {
        return ResponseEntity.ok().body(weatherService.getWeeklyForecast(city));
    }
}
