package sample.weatherapp.services;

import java.io.IOException;
import sample.weatherapp.exceptions.HttpResponseException;

public class WeatherApiClient {

  private NetworkService networkService;
  private static final String API_KEY = System.getenv("API_KEY");
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

  public WeatherApiClient(NetworkService networkService) {
    this.networkService = networkService;
  }

  public String getResponse(String endpoint)  throws HttpResponseException, IOException {
    return networkService.getResponse(BASE_URL + endpoint + "&appid=" + API_KEY);
  }

  public String getCurrentWeatherByCityName(String city) throws HttpResponseException, IOException {
    return getResponse("weather?q=" + city);
  }

  //  The OpenWeatherMap API provides a variety of endpoints to query different types of weather
  // data. Here are some of the main endpoints you can use:
  //  Current Weather Data:
  //  By city ID: weather?id={city ID}&appid={API key} 2925177

  public String getCurrentWeatherByCityId(String cityId) throws HttpResponseException, IOException {
    return getResponse("weather?id=" + cityId);
  }

  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

  public String getCurrentWeatherByCoordinates(double lat, double lon) throws HttpResponseException, IOException {
    return getResponse("weather?lat=" + lat + "&lon=" + lon);
  }

  //  By ZIP code: http://api.openweathermap.org/data/2.5/weather?zip={zip code},{country
  // code}&appid={API key}
  //      5 Day / 3 Hour Forecast:
  //  By city name: http://api.openweathermap.org/data/2.5/forecast?q={city name}&appid={API key}

  public String getWeatherForecastByCityName(String city) throws HttpResponseException, IOException {
    return getResponse("forecast?q=" + city);
  }

  //  Historical data:
  // http://api.openweathermap.org/data/2.5/air_pollution/history?lat={lat}&lon={lon}&start={start}&end={end}&appid={API key}

  public String getHistoricalPollutionData(double lat, double lon, long start, long end)
      throws HttpResponseException, IOException {
    return getResponse(
        "air_pollution/history?lat=" + lat + "&lon=" + lon + "&start=" + start + "&end=" + end);
  }

  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/timemachine?lat={lat}&lon={lon}&dt={time}&appid={API
  // key}
  public String getTimemachineData(double lat, double lon, long time) throws HttpResponseException, IOException {
    return getResponse("timemachine?lat=" + lat + "&lon=" + lon + "&dt=" + time);
  }

  // 3 hourly forecast for 4 days from now including today
  //  By city ID: http://api.openweathermap.org/data/2.5/forecast?q={city}&appid={API key}
  public String getForecast4Days3HoursByCityId(String cityId) throws HttpResponseException, IOException {
    return getResponse("forecast?id=" + cityId);
  }

  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
  public String getForecast4Days3HoursByCoordinates(double lat, double lon) throws HttpResponseException, IOException {
    return getResponse("forecast?lat=" + lat + "&lon=" + lon);
  }

  //  By ZIP code: http://api.openweathermap.org/data/2.5/forecast?zip={zip code},{country
  // code}&appid={API key}
  public String getForecast4Days3HoursByZipCode(String zipCode, String countryCode)
      throws HttpResponseException, IOException {
    return getResponse("forecast?zip=" + zipCode + "," + countryCode);
  }

  //  One Call API (for current, minute, hourly, daily forecasts, and historical data):
  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&appid={API key}

  // fixme : api not available, subscription needed
  public String getOneCall(double lat, double lon) throws HttpResponseException, IOException {
    return getResponse("onecall?lat=" + lat + "&lon=" + lon);
  }

  //  Air Pollution Data with one data element in list:
  //  By geographic coordinates:
  // http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={API key}

  public String getPollutionData(double lat, double lon) throws HttpResponseException, IOException {
    return getResponse("air_pollution?lat=" + lat + "&lon=" + lon);
  }
}
