package ru.task.weatherservice.exception;

public class ExternalGeoServiceException extends RuntimeException {

        public ExternalGeoServiceException(String message) {
            super(message);
        }

        public ExternalGeoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
}
