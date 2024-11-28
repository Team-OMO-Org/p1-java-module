package sample.weatherapp;

public record Forecast(
    long dt, // Timestamp of the forecast data
    double temp, // Temperature in degrees Celsius
    double feelsLike, // Perceived temperature in degrees Celsius
    double tempMin, // Minimum temperature in degrees Celsius
    double tempMax, // Maximum temperature in degrees Celsius
    int pressure, // Atmospheric pressure in hPa
    int seaLevel, // Atmospheric pressure at sea level in hPa
    int grndLevel, // Atmospheric pressure at ground level in hPa
    int humidity, // Humidity percentage
    double tempKf, // Internal parameter for temperature correction
    String weatherMain, // Main weather condition (e.g., Rain, Snow)
    String weatherDescription, // Detailed weather description
    String weatherIcon, // Weather icon ID
    int cloudsAll, // Cloudiness percentage
    double windSpeed, // Wind speed in meters/second
    int windDeg, // Wind direction in degrees
    double windGust, // Wind gust speed in meters/second
    int visibility, // Visibility in meters
    double pop, // Probability of precipitation
    String pod, // Part of the day (e.g., d for day, n for night)
    String dtTxt // Date and time of the forecast in text format
) {}
