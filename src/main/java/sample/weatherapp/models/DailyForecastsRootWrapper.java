package sample.weatherapp.models;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DailyForecastsRootWrapper {
  private final List<DailyForecastWrapper> dailyForecastWrappers;

  public DailyForecastsRootWrapper(DailyForecastRoot dailyForecastsData, Locale locale) {
    this.dailyForecastWrappers = dailyForecastsData.forecasts().stream()
        .map(forecast -> new DailyForecastWrapper(forecast, locale))
        .collect(Collectors.toList());
  }

  public List<DailyForecastWrapper> getDailyForecastWrappers() {
    return dailyForecastWrappers;
  }
}
