package sample.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
