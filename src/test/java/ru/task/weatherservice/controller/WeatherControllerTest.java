package ru.task.weatherservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.task.weatherservice.config.properties.NominatimOpenStreetMapProperties;
import ru.task.weatherservice.config.properties.WeatherApiProperties;
import ru.task.weatherservice.config.properties.WeatherBitProperties;
import ru.task.weatherservice.config.properties.YandexWeatherProperties;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.service.ExternalGeoService;
import ru.task.weatherservice.service.ExternalWeatherService;
import ru.task.weatherservice.service.impl.WeatherApiServiceImpl;
import ru.task.weatherservice.service.impl.WeatherBitServiceImpl;
import ru.task.weatherservice.service.impl.YandexWeatherServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(value = WeatherController.class)
@ComponentScan({"ru.task.weatherservice.service.impl", "ru.task.weatherservice.config"})
@EnableConfigurationProperties({WeatherBitProperties.class, YandexWeatherProperties.class,
        WeatherApiProperties.class, NominatimOpenStreetMapProperties.class})
class WeatherControllerTest {
    private static final String WEATHER_CURRENT_ENDPOINT = "/api/weather/current";
    private static final String WEATHER_WEEKLY_ENDPOINT = "/api/weather/weekly";
    private static final String CITY = "Krasnodar";
    private static final Coordinate COORDINATE = new Coordinate(45.0351532, 38.9772396);
    private static final String EXPECTED_FORECAST =
            "[{\"city_name\":\"Krasnodar\",\"data\":[{\"app_temp\":25,\"weather\":{\"description\":\"Sunny\"}}]}]";
    private final List<ExternalWeatherService> mockedServices = new ArrayList<>();

    @Value("${weather.provider}")
    private String provider;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExternalGeoService mockGeoService;
    @MockBean
    private WeatherBitServiceImpl mockWeatherBitService;
    @MockBean
    private YandexWeatherServiceImpl mockYandexWeatherService;
    @MockBean
    private WeatherApiServiceImpl mockWeatherApiService;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        when(mockGeoService.searchCoordinates(CITY))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(COORDINATE)));

        switch (provider) {
            case "weatherbit":
                CompletableFuture<String> futureForecastWeatherBit = CompletableFuture.completedFuture(EXPECTED_FORECAST);
                if (testInfo.getTestMethod().get().getName().startsWith("day"))
                    when(mockWeatherBitService.getCurrentDayForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastWeatherBit);
                else
                    when(mockWeatherBitService.getWeeklyForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastWeatherBit);
                mockedServices.add(mockWeatherBitService);
                break;
            case "yandex":
                CompletableFuture<String> futureForecastYandex = CompletableFuture.completedFuture(EXPECTED_FORECAST);
                if (testInfo.getTestMethod().get().getName().startsWith("day"))
                    when(mockYandexWeatherService.getCurrentDayForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastYandex);
                else
                    when(mockYandexWeatherService.getWeeklyForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastYandex);
                mockedServices.add(mockYandexWeatherService);
                break;
            case "weatherapi":
                CompletableFuture<String> futureForecastWeatherApi = CompletableFuture.completedFuture(EXPECTED_FORECAST);
                if (testInfo.getTestMethod().get().getName().startsWith("day"))
                    when(mockWeatherApiService.getCurrentDayForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastWeatherApi);
                else
                    when(mockWeatherApiService.getWeeklyForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastWeatherApi);
                mockedServices.add(mockWeatherApiService);
                break;
            case "all":
                CompletableFuture<String> futureForecastAll = CompletableFuture.completedFuture(EXPECTED_FORECAST);
                if (testInfo.getTestMethod().get().getName().startsWith("day")) {
                    when(mockWeatherBitService.getCurrentDayForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastAll);
                    when(mockYandexWeatherService.getCurrentDayForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastAll);
                    when(mockWeatherApiService.getCurrentDayForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastAll);
                } else {
                    when(mockWeatherBitService.getWeeklyForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastAll);
                    when(mockYandexWeatherService.getWeeklyForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastAll);
                    when(mockWeatherApiService.getWeeklyForecastUsingExternalService(COORDINATE))
                            .thenReturn(futureForecastAll);
                }
                mockedServices.add(mockWeatherBitService);
                mockedServices.add(mockYandexWeatherService);
                mockedServices.add(mockWeatherApiService);
                break;
        }
    }

    @Test
    void dayShouldReturnCurrentDayForecast() throws Exception {
        MvcResult result = mockMvc.perform(get(WEATHER_CURRENT_ENDPOINT)
                        .param("city", CITY))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk());

        switch (provider) {
            case "weatherbit", "yandex", "weatherapi":
                verify(mockedServices.get(0)).getCurrentDayForecastUsingExternalService(COORDINATE);
                break;
            case "all":
                mockedServices.forEach(service ->
                        verify(service).getCurrentDayForecastUsingExternalService(COORDINATE));
                break;
        }
    }

    @Test
    void weeklyShouldReturnWeeklyForecast() throws Exception {
        MvcResult result = mockMvc.perform(get(WEATHER_WEEKLY_ENDPOINT)
                        .param("city", CITY))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk());

        switch (provider) {
            case "weatherbit", "yandex", "weatherapi":
                verify(mockedServices.get(0)).getWeeklyForecastUsingExternalService(COORDINATE);
                break;
            case "all":
                mockedServices.forEach(service ->
                        verify(service).getWeeklyForecastUsingExternalService(COORDINATE));
                break;
        }
    }
}