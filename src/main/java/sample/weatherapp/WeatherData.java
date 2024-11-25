package sample.weatherapp;

public class WeatherData {
  private String cityName;
  private double temperature;
  private int humidity;
  private String description;

  public WeatherData(String cityName, double temperature, int humidity, String description) {
    this.cityName = cityName;
    this.temperature = temperature;
    this.humidity = humidity;
    this.description = description;
  }

  public String getCityName() {
    return cityName;
  }

  public double getTemperature() {
    return temperature;
  }

  public int getHumidity() {
    return humidity;
  }

  public String getDescription() {
    return description;
  }
}