package ru.task.weatherservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.task.weatherservice.config.properties.NominatimOpenStreetMapProperties;
import ru.task.weatherservice.config.properties.WeatherApiProperties;
import ru.task.weatherservice.config.properties.WeatherBitProperties;
import ru.task.weatherservice.config.properties.YandexWeatherProperties;
import ru.task.weatherservice.model.Coordinate;
import ru.task.weatherservice.service.ExternalGeoService;
import ru.task.weatherservice.service.ExternalWeatherService;
import ru.task.weatherservice.service.WeatherService;
import ru.task.weatherservice.service.impl.NominatimOpenStreetMapServiceImpl;
import ru.task.weatherservice.service.impl.WeatherApiServiceImpl;
import ru.task.weatherservice.service.impl.WeatherBitServiceImpl;
import ru.task.weatherservice.service.impl.YandexWeatherServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(value = WeatherController.class)
@ComponentScan({"ru.task.weatherservice.service.impl", "ru.task.weatherservice.config"})
@EnableConfigurationProperties({WeatherBitProperties.class, WeatherApiProperties.class, YandexWeatherProperties.class, NominatimOpenStreetMapProperties.class})
class WeatherControllerTest {
    private static final String WEATHER_CURRENT_ENDPOINT = "/api/weather/current";
    private static final String WEATHER_WEEKLY_ENDPOINT = "/api/weather/weekly";
    private static final String CITY = "Krasnodar";
    private static final Coordinate COORDINATE = new Coordinate(45.0351532, 38.9772396);
    private static final String EXPECTED_FORECAST =
            "{\"city_name\":\"Krasnodar\",\"data\":[{\"app_temp\":25,\"weather\":{\"description\":\"Sunny\"}}]}";

    @Value("${weather.provider}")
    private String provider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExternalGeoService externalGeoService;
    @MockBean
    private WeatherService weatherService;


//    @BeforeEach
//    void setUp() {
//        List<ExternalWeatherService> mockedServices = new ArrayList<>();
//
//        // Создаем моки для каждого сервиса, который хотим добавить в список
//        ExternalWeatherService mockWeatherBitService = mock(WeatherBitServiceImpl.class);
//        ExternalWeatherService mockYandexWeatherService = mock(YandexWeatherServiceImpl.class);
//        ExternalWeatherService mockWeatherApiService = mock(WeatherApiServiceImpl.class);
//
//        CompletableFuture<String> futureForecast = CompletableFuture.completedFuture(EXPECTED_FORECAST);
//
//        when(externalGeoService.searchCoordinates(CITY))
//                .thenReturn(CompletableFuture.completedFuture(Optional.of(COORDINATE)));
//
//        when(mockWeatherBitService.getCurrentDayForecastUsingExternalService(COORDINATE))
//                .thenReturn(futureForecast);
//        when(mockWeatherBitService.getCurrentDayForecastUsingExternalService(COORDINATE))
//                .thenReturn(futureForecast);
//        when(mockWeatherBitService.getCurrentDayForecastUsingExternalService(COORDINATE))
//                .thenReturn(futureForecast);
//
//        mockedServices.add(mockWeatherBitService);
//        mockedServices.add(mockYandexWeatherService);
//        mockedServices.add(mockWeatherApiService);
//
//        weatherService.setExternalWeatherServices(mockedServices);
//    }

    @Test
    void dayShouldReturnCurrentDayForecast() throws Exception {
//        Stream<ExternalWeatherService> servicesStream = externalWeatherServices.stream();
//        servicesStream = filterServicesByProvider(provider, servicesStream);

//        when(externalGeoService.searchCoordinates(CITY))
//                .thenReturn(CompletableFuture.completedFuture(Optional.of(COORDINATE)));
//        servicesStream.forEach(service -> {
//            when(service.getCurrentDayForecastUsingExternalService(COORDINATE))
//                    .thenReturn(CompletableFuture.completedFuture(EXPECTED_FORECAST));
//        });
        List<ExternalWeatherService> mockedServices = new ArrayList<>();

//         Создаем моки для каждого сервиса, который хотим добавить в список
        ExternalGeoService mockGeoService = mock(NominatimOpenStreetMapServiceImpl.class);
        ExternalWeatherService mockWeatherBitService = mock(WeatherBitServiceImpl.class);
        ExternalWeatherService mockYandexWeatherService = mock(YandexWeatherServiceImpl.class);
        ExternalWeatherService mockWeatherApiService = mock(WeatherApiServiceImpl.class);

        CompletableFuture<String> futureForecast = CompletableFuture.completedFuture(EXPECTED_FORECAST);

        when(externalGeoService.searchCoordinates(CITY))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(COORDINATE)));
        when(weatherService.getCurrentDayForecast(CITY))
                .thenReturn(List.of(EXPECTED_FORECAST));
        when(mockWeatherBitService.getCurrentDayForecastUsingExternalService(COORDINATE))
                .thenReturn(futureForecast);
        when(mockYandexWeatherService.getCurrentDayForecastUsingExternalService(COORDINATE))
                .thenReturn(futureForecast);
        when(mockWeatherApiService.getCurrentDayForecastUsingExternalService(COORDINATE))
                .thenReturn(futureForecast);

        mockedServices.add(mockWeatherBitService);
        mockedServices.add(mockYandexWeatherService);
        mockedServices.add(mockWeatherApiService);

        weatherService.setExternalWeatherServices(mockedServices);

        mockedServices = filterServicesByProvider(provider, mockedServices.stream());

        when(mockGeoService.searchCoordinates(CITY))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(COORDINATE)));

        mockMvc.perform(get(WEATHER_CURRENT_ENDPOINT)
                        .param("city", CITY))
                .andExpect(status().isOk());
//                .andExpect(content().string(EXPECTED_FORECAST));

        verify(mockGeoService).searchCoordinates(CITY);
//        mockedServices
//                .forEach(service -> verify(service, atLeastOnce()).getCurrentDayForecastUsingExternalService(COORDINATE));
//        servicesStream
//                .forEach(service -> verify(service, atLeastOnce()).getCurrentDayForecastUsingExternalService(COORDINATE));
    }

    private List<ExternalWeatherService> filterServicesByProvider(String provider, Stream<ExternalWeatherService> servicesStream) {
        return provider.equals("all") ? servicesStream.toList() :
                servicesStream.filter(s -> s.getClass().getSimpleName().toLowerCase().contains(provider.toLowerCase())).toList();
    }
}


//    @BeforeEach
//    public void setup(TestInfo testInfo) {
//        String methodName = testInfo.getTestMethod().get().getName();
//        Stream<ExternalWeatherService> servicesStream = externalWeatherServices.stream();
//        servicesStream = filterServicesByProvider(provider, servicesStream);
//
//        CompletableFuture<Optional<Coordinate>> coordinateFuture = CompletableFuture.completedFuture(Optional.of(COORDINATE));
//        CompletableFuture<String> forecastFuture = CompletableFuture.completedFuture(EXPECTED_FORECAST);
//
//        when(externalGeoService.searchCoordinates(CITY)).thenReturn(coordinateFuture);

//        if (methodName.startsWith("day")) {
//            servicesStream.forEach(service -> {
//                when(service.getCurrentDayForecastUsingExternalService(COORDINATE))
//                        .thenReturn(forecastFuture);
//            });
//        } else {
//            servicesStream.forEach(service -> {
//                when(service.getWeeklyForecastUsingExternalService(COORDINATE))
//                        .thenReturn(forecastFuture);
//            });
//        }
//    }