package sample.weatherapp;

import org.junit.jupiter.api.Test;
import sample.weatherapp.models.WeatherSummary;

class WeatherSummaryTest {

  @Test
  void formattedWeatherInfo() {
    WeatherSummary weatherData = new WeatherSummary(1732192844L, 3600, "Berlin", "DE", 276.43, "iconId",
        271.75, 996, 79, "broken clouds", 6.26, 236);
    System.out.println(weatherData.formattedWeatherInfo());
  }
}
