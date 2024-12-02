package sample.weatherapp;

import org.junit.jupiter.api.Test;
import sample.weatherapp.models.WeatherData;

class WeatherDataTest {

  @Test
  void formattedWeatherInfo() {

    WeatherData weatherData =
        new WeatherData(
            1732192844L, 3600, "Berlin", "DE", 276.43, 271.75, 996, 79, "broken clouds", 6.26, 236);
    System.out.println(weatherData.formattedWeatherInfo());
  }
}
