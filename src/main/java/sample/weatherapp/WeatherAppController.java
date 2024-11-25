package sample.weatherapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @FXML
  private void getWeather() {
    String city = cityTextField.getText();
    String urlString = BASE_URL + city + "&appid=" + API_KEY;

    try {
      URL url = new URI(urlString).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      StringBuilder content = new StringBuilder();
      try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
        }

        System.out.println(content);
      }
      conn.disconnect();

      // Parse the JSON response using Jackson
      ObjectMapper mapper = new ObjectMapper();
      JsonNode json = mapper.readTree(content.toString());
      String cityName = json.get("name").asText();
      JsonNode main = json.get("main");
      double temp = main.get("temp").asDouble() - 273.15; // Convert from Kelvin to Celsius
      int humidity = main.get("humidity").asInt();
      String weatherDescription = json.get("weather").get(0).get("description").asText();

      // Display the weather data
      String weatherInfo = String.format("City: %s\nTemperature: %.2f°C\nHumidity: %d%%\nDescription: %s",
          cityName, temp, humidity, weatherDescription);

      weatherLabel.setText(weatherInfo);

    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
    }
  }
}