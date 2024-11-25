package sample.weatherapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WeatherAppController {

  @FXML
  private TextField cityTextField;

  @FXML
  private Label weatherLabel;

  private ApiClient apiClient = new ApiClient();

  @FXML
  private void getWeather() {
    String city = cityTextField.getText();

    try {
      String jsonResponse = apiClient.getWeatherData(city);
      WeatherData weatherData = apiClient.parseWeatherData(jsonResponse);

      // Display the weather data
      String weatherInfo = String.format("City: %s\nTemperature: %.2f°C\nHumidity: %d%%\nDescription: %s",
          weatherData.getCityName(), weatherData.getTemperature(), weatherData.getHumidity(), weatherData.getDescription());

      weatherLabel.setText(weatherInfo);

    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
    }
  }
}