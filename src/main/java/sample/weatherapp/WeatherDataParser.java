package sample.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataParser {
  /*
  public static WeatherData parseWeatherData(String jsonResponse) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.readTree(jsonResponse);
    String cityName = json.get("name").asText();
    JsonNode main = json.get("main");
    double temp = main.get("temp").asDouble() - 273.15; // Convert from Kelvin to Celsius
    int humidity = main.get("humidity").asInt();
    String weatherDescription = json.get("weather").get(0).get("description").asText();

    return new WeatherData(cityName, temp, humidity, weatherDescription);
  }*/

  public static WeatherData parseWeatherData(String jsonResponse) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode json = mapper.readTree(jsonResponse);
    long dt = json.get("dt").asLong();
    int timezone = json.get("timezone").asInt();
    String cityName = json.get("name").asText();
    JsonNode sys = json.get("sys");
    String country = sys.get("country").asText();
    JsonNode main = json.get("main");
    String iconId = json.get("weather").get(0).get("icon").asText();
    double temp = main.get("temp").asDouble() - 273.15; // Convert from Kelvin to Celsius
    double feelsLikeTemp = main.get("feels_like").asDouble() - 273.15;
    int pressure = main.get("pressure").asInt();
    int humidity = main.get("humidity").asInt();
    String weatherDescription = json.get("weather").get(0).get("description").asText();
    JsonNode wind = json.get("wind");
    double windSpeed = wind.get("speed").asDouble();
    int windDeg = wind.get("deg").asInt();

    return new WeatherData(
        dt,
        timezone,
        cityName,
        country,
        temp,
        iconId,
        feelsLikeTemp,
        pressure,
        humidity,
        weatherDescription,
        windSpeed,
        windDeg);
  }

  public static List<Forecast> parseForecastList(String jsonResponse) throws Exception {

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.readTree(jsonResponse);
    JsonNode listNode = rootNode.path("list");

    List<Forecast> forecasts = new ArrayList<>();
    for (JsonNode node : listNode) {
      Forecast forecast = getForecast(node);
      forecasts.add(forecast);
    }

    return forecasts;
  }

  private static Forecast getForecast(JsonNode node) {

      long dt = node.has("dt") ? node.get("dt").asLong() : 0L;
      JsonNode main = node.has("main") ? node.get("main") : null;
      double temp = main != null && main.has("temp") ? main.get("temp").asDouble() - 273.15 : 0.0;
      double feelsLike = main != null && main.has("feels_like") ? main.get("feels_like").asDouble() - 273.15 : 0.0;
      double tempMin = main != null && main.has("temp_min") ? main.get("temp_min").asDouble() - 273.15 : 0.0;
      double tempMax = main != null && main.has("temp_max") ? main.get("temp_max").asDouble() - 273.15 : 0.0;
      int pressure = main != null && main.has("pressure") ? main.get("pressure").asInt() : 0;
      int seaLevel = main != null && main.has("sea_level") ? main.get("sea_level").asInt() : 0;
      int grndLevel = main != null && main.has("grnd_level") ? main.get("grnd_level").asInt() : 0;
      int humidity = main != null && main.has("humidity") ? main.get("humidity").asInt() : 0;
      double tempKf = main != null && main.has("temp_kf") ? main.get("temp_kf").asDouble() : 0.0;

      JsonNode weather = node.has("weather") && node.get("weather").size() > 0 ? node.get("weather").get(0) : null;
      String weatherMain = weather != null && weather.has("main") ? weather.get("main").asText() : "";
      String weatherDescription = weather != null && weather.has("description") ? weather.get("description").asText() : "";
      String weatherIcon = weather != null && weather.has("icon") ? weather.get("icon").asText() : "";

      int cloudsAll = node.has("clouds") && node.get("clouds").has("all") ? node.get("clouds").get("all").asInt() : 0;
      double windSpeed = node.has("wind") && node.get("wind").has("speed") ? node.get("wind").get("speed").asDouble() : 0.0;
      int windDeg = node.has("wind") && node.get("wind").has("deg") ? node.get("wind").get("deg").asInt() : 0;
      double windGust = node.has("wind") && node.get("wind").has("gust") ? node.get("wind").get("gust").asDouble() : 0.0;
      int visibility = node.has("visibility") ? node.get("visibility").asInt() : 0;
      double pop = node.has("pop") ? node.get("pop").asDouble() : 0.0;
      String pod = node.has("sys") && node.get("sys").has("pod") ? node.get("sys").get("pod").asText() : "";
      String dtTxt = node.has("dt_txt") ? node.get("dt_txt").asText() : "";

      return new Forecast(
          dt, temp, feelsLike, tempMin, tempMax, pressure, seaLevel, grndLevel, humidity, tempKf,
          weatherMain, weatherDescription, weatherIcon, cloudsAll, windSpeed, windDeg, windGust,
          visibility, pop, pod, dtTxt
      );
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
