package sample.weatherapp.models;

import java.util.List;


public record WeatherDailyForecastData(
    String cityName,
    List<DailyForecast> forecasts
) {}
