package sample.weatherapp;

public record Forecast(
    long dt,
    double temp,
    double feelsLike,
    double tempMin,
    double tempMax,
    int pressure,
    int seaLevel,
    int grndLevel,
    int humidity,
    double tempKf,
    String weatherMain,
    String weatherDescription,
    String weatherIcon,
    int cloudsAll,
    double windSpeed,
    int windDeg,
    double windGust,
    int visibility,
    double pop,
    String pod,
    String dtTxt
) {}
