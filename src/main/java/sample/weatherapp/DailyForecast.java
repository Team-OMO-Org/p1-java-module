package sample.weatherapp;

public class DailyForecast {
  private final String dateTime;
  private final double temperature;
  private final int humidity;
  private final String description;

  public DailyForecast(String dateTime, double temperature, int humidity, String description) {
    this.dateTime = dateTime;
    this.temperature = temperature;
    this.humidity = humidity;
    this.description = description;
  }

  public String getDateTime() {
    return dateTime;
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