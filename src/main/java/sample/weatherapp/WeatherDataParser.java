package sample.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
