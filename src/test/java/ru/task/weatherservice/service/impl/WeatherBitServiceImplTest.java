package ru.task.weatherservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.task.weatherservice.config.properties.WeatherBitProperties;
import ru.task.weatherservice.model.Coordinate;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherBitServiceImplTest {

    public static final Coordinate COORDINATE = new Coordinate(45.0351532, 38.9772396);
    public static final WeatherBitProperties PROPERTIES = new WeatherBitProperties(
            "https://api.weatherbit.io", "/v2.0", "/current",
            "X-RapidAPI-Key", "your_api_key", "ru_RU", 24, 168, "true");
    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WeatherBitServiceImpl weatherBitServiceImpl;

    @BeforeEach
    void setUp() {
        weatherBitServiceImpl = new WeatherBitServiceImpl(PROPERTIES, httpClient, objectMapper);
    }

    private static Stream<Arguments> statusCodeAndExpectedMessage() {
        return Stream.of(
                Arguments.of(500, "Internal server error. Something went wrong on our end."),
                Arguments.of(400, "The request was invalid or cannot be served. Check your request."),
                Arguments.of(401, "Access is denied. Please check your API key."),
                Arguments.of(403, "Access is denied. Please check your API key."),
                Arguments.of(429, "Too many requests. Please wait and try again later."),
                Arguments.of(-111, "Unknown response status: -111 Unable to process the request."),
                Arguments.of(509, "Unexpected response status: 509 BANDWIDTH_LIMIT_EXCEEDED Please contact support.")
        );
    }

    @ParameterizedTest
    @MethodSource("statusCodeAndExpectedMessage")
    void shouldReturnExpectedMessageOnRequestOfCurrentDayForecastBasedOnStatusCodeInsteadOfThrowingAnException(
            int statusCode, String expectedMessage) throws Exception {
        HttpResponse<String> fakeResponse = mock(HttpResponse.class);
        when(fakeResponse.statusCode()).thenReturn(statusCode);
        when(httpClient.sendAsync(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(CompletableFuture.completedFuture(fakeResponse));

        CompletableFuture<String> result = weatherBitServiceImpl.getCurrentDayForecastUsingExternalService(COORDINATE);

        assertTrue(result.get(1, TimeUnit.SECONDS).contains(expectedMessage));
    }

    @ParameterizedTest
    @MethodSource("statusCodeAndExpectedMessage")
    void shouldReturnExpectedMessageOnRequestOfWeeklyForecastBasedOnStatusCodeInsteadOfThrowingAnException(
            int statusCode, String expectedMessage
    ) throws Exception {
        HttpResponse<String> fakeResponse = mock(HttpResponse.class);
        when(fakeResponse.statusCode()).thenReturn(statusCode);
        when(httpClient.sendAsync(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(CompletableFuture.completedFuture(fakeResponse));

        CompletableFuture<String> result = weatherBitServiceImpl.getWeeklyForecastUsingExternalService(COORDINATE);

        assertTrue(result.get(1, TimeUnit.SECONDS).contains(expectedMessage));
    }
}