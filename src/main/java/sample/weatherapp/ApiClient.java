package sample.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ApiClient {

  private static final String API_KEY = System.getenv("API_KEY");
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

  //  public String getWeatherData(String city) throws Exception {
  //    String urlString = BASE_URL + city + "&appid=" + API_KEY;
  //    URL url = new URI(urlString).toURL();
  //    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
  //    conn.setRequestMethod("GET");
  //
  //    StringBuilder content = new StringBuilder();
  //    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
  //      String inputLine;
  //      while ((inputLine = in.readLine()) != null) {
  //        content.append(inputLine);
  //      }
  //    }
  //    conn.disconnect();
  //    return content.toString();
  //  }

  private String getResponse(String endpoint) throws Exception {

    URL url = new URI(BASE_URL + endpoint + "&appid=" + API_KEY).toURL();
    System.out.println("COMPLETE_URL: " + url);

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    StringBuilder content = new StringBuilder();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
    }
    conn.disconnect();
    return content.toString();
  }

  public String getCurrentWeatherByCityName(String city) throws Exception {
    return getResponse("weather?q=" + city);
  }



  //  The OpenWeatherMap API provides a variety of endpoints to query different types of weather
  // data. Here are some of the main endpoints you can use:
  //  Current Weather Data:
  //  By city ID: weather?id={city ID}&appid={API key} 2925177

  public String getCurrentWeatherByCityId(String cityId) throws Exception {
    return getResponse("weather?id=" + cityId);
  }

  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

  public String getCurrentWeatherByCoordinates(double lat, double lon) throws Exception {
    return getResponse("weather?lat=" + lat + "&lon=" + lon);
  }

  //  By ZIP code: http://api.openweathermap.org/data/2.5/weather?zip={zip code},{country
  // code}&appid={API key}
  //      5 Day / 3 Hour Forecast:
  //  By city name: http://api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}

  public String getWeatherForecastByCityName(String city) throws Exception {
    return getResponse("forecast?q=" + city);
  }

  //  Historical data:
  // http://api.openweathermap.org/data/2.5/air_pollution/history?lat={lat}&lon={lon}&start={start}&end={end}&appid={API key}

  public String getHistoricalPollutionData(double lat, double lon, long start, long end) throws Exception {
    return getResponse(
        "air_pollution/history?lat=" + lat + "&lon=" + lon + "&start=" + start + "&end=" + end);
  }

  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/timemachine?lat={lat}&lon={lon}&dt={time}&appid={API
  // key}
  public String getTimemachineData(double lat, double lon, long time) throws Exception {
    return getResponse("timemachine?lat=" + lat + "&lon=" + lon + "&dt=" + time);
  }

  // 3 hourly forecast for 4 days from now including today
  //  By city ID: http://api.openweathermap.org/data/2.5/forecast?id={city ID}&appid={API key}
  public String getForecast4Days3HoursByCityId(String cityId) throws Exception {
    return getResponse("forecast?id=" + cityId);
  }

  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
  public String getForecast4Days3HoursByCoordinates(double lat, double lon) throws Exception {
    return getResponse("forecast?lat=" + lat + "&lon=" + lon);
  }

  //  By ZIP code: http://api.openweathermap.org/data/2.5/forecast?zip={zip code},{country
  // code}&appid={API key}
  public String getForecast4Days3HoursByZipCode(String zipCode, String countryCode) throws Exception {
    return getResponse("forecast?zip=" + zipCode + "," + countryCode);
  }

  //  One Call API (for current, minute, hourly, daily forecasts, and historical data):
  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&appid={API key}

  //fixme : api not available, subscription needed
  public String getOneCall(double lat, double lon) throws Exception {
    return getResponse("onecall?lat=" + lat + "&lon=" + lon);
  }


  //  Air Pollution Data with one data element in list:
  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API key}

  public String getPollutionData(double lat, double lon) throws Exception {
    return getResponse("air_pollution?lat=" + lat + "&lon=" + lon);
  }



    //  Weather Alerts:
  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&appid={API key} (included in
  // the One Call API response)
  //  These endpoints allow you to query various types of weather data, including current
  // conditions, forecasts, historical data, and air pollution levels. You can customize the queries
  // by replacing the placeholders (e.g., {city name}, {lat}, {lon}, {API key}) with actual values.

}
