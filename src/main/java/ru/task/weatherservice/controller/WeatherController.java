package ru.task.weatherservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.task.weatherservice.exception.WeatherServiceException;
import ru.task.weatherservice.model.dto.ErrorResponseDTO;
import ru.task.weatherservice.service.WeatherService;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ResponseEntity<List<String>> getCurrentWeather(@RequestParam(defaultValue = "Krasnodar") String city) {
        return ResponseEntity.ok().body(weatherService.getCurrentDayForecast(city));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<String>> getWeeklyForecast(@RequestParam(defaultValue = "Krasnodar") String city) {
        return ResponseEntity.ok().body(weatherService.getWeeklyForecast(city));
    }

    @ExceptionHandler(WeatherServiceException.class)
    ResponseEntity<ErrorResponseDTO> weatherServiceExceptionHandler(WeatherServiceException exception) {
        return ResponseEntity.internalServerError().body(new ErrorResponseDTO(exception.getMessage(),
                INTERNAL_SERVER_ERROR));
    }
}
