package ru.task.weatherservice.model;

public record ApiResponse(boolean success, String serviceName, String message, String data) {

    public static ApiResponse ok(String serviceName, String data) {
        return new ApiResponse(true, serviceName, null, data);
    }

    public static ApiResponse error(String serviceName, String message) {
        return new ApiResponse(false, serviceName, message, null);
    }
}

