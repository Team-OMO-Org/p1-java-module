package sample.weatherapp;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WeatherDailyForecastsDataWrapper {
  private final List<DailyForecastWrapper> dailyForecastWrappers;

  public WeatherDailyForecastsDataWrapper(WeatherDailyForecastData dailyForecastsData, Locale locale) {
    this.dailyForecastWrappers = dailyForecastsData.forecasts().stream()
        .map(forecast -> new DailyForecastWrapper(forecast, locale))
        .collect(Collectors.toList());
  }

  public List<DailyForecastWrapper> getDailyForecastWrappers() {
    return dailyForecastWrappers;
  }
}
