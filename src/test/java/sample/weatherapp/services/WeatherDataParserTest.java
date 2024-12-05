package sample.weatherapp.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import sample.weatherapp.models.Forecast;
import sample.weatherapp.models.WeatherSummary;

class WeatherDataParserTest {

  @Test
  void parseWeatherDataWithCorrectResponse() throws IOException {
    String jsonResponse = "{ \"dt\": 1633036800, \"timezone\": 7200, \"name\": \"Berlin\", \"sys\": { \"country\": \"DE\" }, \"main\": { \"temp\": 293.15, \"feels_like\": 292.15, \"pressure\": 1013, \"humidity\": 80 }, \"weather\": [ { \"description\": \"clear sky\", \"icon\": \"01d\" } ], \"wind\": { \"speed\": 3.6, \"deg\": 200 } }";

    WeatherSummary weatherSummary = WeatherDataParser.parseWeatherData(jsonResponse);

    assertEquals("Berlin", weatherSummary.getCity());
    assertEquals("DE", weatherSummary.getCountry());
    assertEquals(20.0, weatherSummary.getTemperature(), 0.01);
    assertEquals(19.0, weatherSummary.getFeelsLikeTemperature(), 0.01); 
    assertEquals(1013, weatherSummary.getPressure());
    assertEquals(80, weatherSummary.getHumidity());
    assertEquals("clear sky", weatherSummary.getDescription());
    assertEquals("01d", weatherSummary.getIconId());
    assertEquals(3.6, weatherSummary.getWind().speed(), 0.01);
    assertEquals(200, weatherSummary.getWind().deg(), 0.01);
  }

  @Test
  void parseForecastListWithCorrectResponse() throws IOException {
    String jsonResponse = "{ \"list\": [ { \"dt\": 1633036800, \"main\": { \"temp\": 293.15, \"feels_like\": 292.15, \"temp_min\": 290.15, \"temp_max\": 295.15, \"pressure\": 1013, \"humidity\": 80 }, \"weather\": [ { \"main\": \"Clear\", \"description\": \"clear sky\", \"icon\": \"01d\" } ], \"clouds\": { \"all\": 0 }, \"wind\": { \"speed\": 3.6, \"deg\": 200 }, \"visibility\": 10000, \"pop\": 0.0, \"sys\": { \"pod\": \"d\" }, \"dt_txt\": \"2021-10-01 12:00:00\" } ] }";

    List<Forecast> forecasts = WeatherDataParser.parseForecastList(jsonResponse);

    assertEquals(1, forecasts.size());
    Forecast forecast = forecasts.get(0);
    assertEquals(1633036800L, forecast.dt());
    assertEquals(20.0, forecast.temp(), 0.01);
    assertEquals(19.0, forecast.feelsLike(), 0.01);
    assertEquals(17.0, forecast.tempMin(), 0.01);
    assertEquals(22.0, forecast.tempMax(), 0.01);
    assertEquals(1013, forecast.pressure());
    assertEquals(80, forecast.humidity());
    assertEquals("Clear", forecast.weatherMain());
    assertEquals("clear sky", forecast.weatherDescription());
    assertEquals("01d", forecast.weatherIcon());
    assertEquals(0, forecast.cloudsAll());
    assertEquals(3.6, forecast.windSpeed(), 0.01);
    assertEquals(200, forecast.windDeg());
    assertEquals(10000, forecast.visibility());
    assertEquals(0.0, forecast.pop(), 0.01);
    assertEquals("d", forecast.pod());
    assertEquals("2021-10-01 12:00:00", forecast.dtTxt());
  }
}
