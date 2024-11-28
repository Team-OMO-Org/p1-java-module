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

    long dt = node.get("dt").asLong();
    JsonNode main = node.get("main");
    double temp = main.get("temp").asDouble() - 273.15;
    double feelsLike = main.get("feels_like").asDouble() - 273.15;
    double tempMin = main.get("temp_min").asDouble() - 273.15;
    double tempMax = main.get("temp_max").asDouble() - 273.15;
    int pressure = main.get("pressure").asInt();
    int seaLevel = main.get("sea_level").asInt();
    int grndLevel = main.get("grnd_level").asInt();
    int humidity = main.get("humidity").asInt();
    double tempKf = main.get("temp_kf").asDouble();

    JsonNode weather = node.get("weather").get(0);
    String weatherMain = weather.get("main").asText();
    String weatherDescription = weather.get("description").asText();
    String weatherIcon = weather.get("icon").asText();

    int cloudsAll = node.get("clouds").get("all").asInt();
    double windSpeed = node.get("wind").get("speed").asDouble();
    int windDeg = node.get("wind").get("deg").asInt();
    double windGust = node.get("wind").get("gust").asDouble();
    int visibility = node.get("visibility").asInt();
    double pop = node.get("pop").asDouble();
    String pod = node.get("sys").get("pod").asText();
    String dtTxt = node.get("dt_txt").asText();

    Forecast forecast = new Forecast(
        dt, temp, feelsLike, tempMin, tempMax, pressure, seaLevel, grndLevel, humidity, tempKf,
        weatherMain, weatherDescription, weatherIcon, cloudsAll, windSpeed, windDeg, windGust,
        visibility, pop, pod, dtTxt
    );
    return forecast;
  }
}
