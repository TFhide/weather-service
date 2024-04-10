package ru.task.weatherservice.model.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(String message, HttpStatus status) {
}
