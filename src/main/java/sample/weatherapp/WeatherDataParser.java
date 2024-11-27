package sample.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataParser {

  public static WeatherData parseWeatherData(String jsonResponse) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.readTree(jsonResponse);
    String cityName = json.get("name").asText();
    JsonNode main = json.get("main");
    double temp = main.get("temp").asDouble() - 273.15; // Convert from Kelvin to Celsius
    int humidity = main.get("humidity").asInt();
    String weatherDescription = json.get("weather").get(0).get("description").asText();

    return new WeatherData(cityName, temp, humidity, weatherDescription);
  }


  public static WeatherDailyForecastData parseWeatherForecastData(String jsonResponse) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(jsonResponse);

    String cityName = rootNode.path("city").path("name").asText();
    List<DailyForecast> forecasts = new ArrayList<>();

    JsonNode listNode = rootNode.path("list");
    for (JsonNode node : listNode) {
      String dateTime = node.path("dt_txt").asText();
      double temperature = node.path("main").path("temp").asDouble()- 273.15; // Convert from Kelvin to Celsius
      int humidity = node.path("main").path("humidity").asInt();
      String description = node.path("weather").get(0).path("description").asText();
      String weatherIcon = node.path("weather").get(0).path("icon").asText();

      DailyForecast forecast = new DailyForecast(dateTime, temperature, humidity, description, weatherIcon);
      forecasts.add(forecast);
    }

    return new WeatherDailyForecastData(cityName, forecasts);
  }
}
