package sample.weatherapp.models;

import java.util.List;


public record DailyForecastRoot(
    String cityName,
    List<DailyForecast> forecasts
) {}
