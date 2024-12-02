package sample.weatherapp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class WeatherData {
  private LocalDateTime dateTime;
  private String city;
  private String country;
  private String iconId;
  private double temperature;
  private double feelsLikeTemperature;
  private int pressure;
  private int humidity;
  private String description;
  private WindData wind;

  public WeatherData(
      long dt,
      int timezoneOffset,
      String city,
      String country,
      double temperature,
      String iconId,
      double feelsLikeTemperature,
      int pressure,
      int humidity,
      String description,
      double windSpeed,
      int windDeg) {

    this.dateTime = createDateTime(dt, timezoneOffset);
    this.city = city;
    this.country = country;
    this.temperature = temperature;
    this.iconId = iconId;
    this.feelsLikeTemperature = feelsLikeTemperature;
    this.pressure = pressure;
    this.humidity = humidity;
    this.description = description;
    this.wind = new WindData(windSpeed, windDeg);
  }

  private LocalDateTime createDateTime(long dt, int timezoneOffset) {
    ZoneOffset offset = ZoneOffset.ofTotalSeconds(timezoneOffset); //  time zone offset from UTC
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), offset);
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public double getTemperature() {
    return temperature;
  }

  public double getFeelsLikeTemperature() {
    return feelsLikeTemperature;
  }

  public int getPressure() {
    return pressure;
  }

  public int getHumidity() {
    return humidity;
  }

  public String getDescription() {
    return description;
  }

  public WindData getWind() {
    return wind;
  }

  public String getIconId() {
    return iconId;
  }

  public String formattedWeatherInfo() {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, hh:mma");
    String descriptionFormatted =
        description.substring(0, 1).toUpperCase() + description.substring(1);

    return String.format(
        "%s\n%s, %s\n%s %.2f°C\n%s. Feels like %.2f°C\n%s\t\t%shPa\nHumidity: %s%%\n",
        dateTime.format(dateFormatter),
        city,
        country,
        iconId,
        temperature,
        descriptionFormatted,
        feelsLikeTemperature,
        wind,
        pressure,
        humidity);
  }
}
