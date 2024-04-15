# Weather Forecast API

The "weather-service" project provides a REST API for obtaining weather forecasts for the current day and the week ahead, with the option to select a city. The service accesses external weather service APIs to retrieve data.

## Usage
The project provides the following endpoints:
- `GET /api/weather/current?city={city_name}` - Retrieves the weather forecast for the current day.
- `GET /api/weather/weekly?city={city_name}` - Retrieves the weather forecast for the week. Where `{city_name}` is an optional parameter, the name of the city for which the forecast is to be obtained. If no city is specified, a default city is used.

To simplify the process of sending requests to the API, the project includes a `WeatherForecastRequests.http` file, which contains examples of requests to the API.

## Configuration
In the `application.yaml` file, you can configure parameters such as:
- The weather forecast provider (Yandex, WeatherBit, WeatherApi, or all (simultaneous requests to multiple services))
- API keys for external weather services