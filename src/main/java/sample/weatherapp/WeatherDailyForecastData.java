package sample.weatherapp;

import java.util.List;

public record WeatherDailyForecastData(
    String cityName,
    List<DailyForecast> forecasts
) {}
