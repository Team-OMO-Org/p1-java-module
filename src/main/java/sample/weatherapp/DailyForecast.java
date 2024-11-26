package sample.weatherapp;

public record Forecast(
    String dateTime,
    double temperature,
    int humidity,
    String description
) {}
