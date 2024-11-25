package sample.weatherapp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAppController {

  @FXML
  private TextField cityTextField;

  @FXML
  private Label weatherLabel;

  private static final String API_KEY = System.getenv("API_KEY");
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

 // http://api.openweathermap.org/data/2.5/weather?q=berlin&appid=1edfdf11865d75ba15938c2b30d2fda9
//  {"coord":{"lon":13.4105,"lat":52.5244},"weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04d"}],"base":"stations","main":{"temp":276.43,"feels_like":271.75,"temp_min":275.51,"temp_max":277.58,"pressure":996,"humidity":79,"sea_level":996,"grnd_level":990},"visibility":10000,"wind":{"speed":6.26,"deg":236,"gust":9.83},"clouds":{"all":75},"dt":1732192844,"sys":{"type":2,"id":2011538,"country":"DE","sunrise":1732171157,"sunset":1732201540},"timezone":3600,"id":2950159,"name":"Berlin","cod":200}

  @FXML
  private void getWeather() {
    String city = cityTextField.getText();
    String urlString = BASE_URL + city + "&appid=" + API_KEY;

    try {
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      StringBuilder content= new StringBuilder();
      try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
        }

        System.out.println(content);
        // Close connections
      }
        conn.disconnect();

      // Parse the JSON response using Gson
      JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();
      String cityName = json.get("name").getAsString();
      JsonObject main = json.getAsJsonObject("main");
      double temp = main.get("temp").getAsDouble() - 273.15; // Convert from Kelvin to Celsius
      int humidity = main.get("humidity").getAsInt();
      String weatherDescription = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

      // Display the weather data
      String weatherInfo = String.format("City: %s\nTemperature: %.2f°C\nHumidity: %d%%\nDescription: %s",
          cityName, temp, humidity, weatherDescription);

      // Display the weather data (for simplicity, just display the raw JSON response)
      weatherLabel.setText(weatherInfo);

    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
    }
  }
}