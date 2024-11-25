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
//     String jsonResponse = apiClient.getCurrentWeatherByCityName(city);
     // String jsonResponse = apiClient.getCurrentWeatherByCoordinates(2.3488, 48.8534); // Paris coordinates;
      //String jsonResponse = apiClient.getWeatherForecastByCityName(city);

      // City id 2925177 for Freiburg im Breisgau for example
      //String jsonResponse = apiClient.getCurrentWeatherByCityId(city);

      //  Historical data ->  January 1, 2023 00:00:00 GMT - January 7, 2023 00:00:00 GMT
      String jsonResponse = apiClient.getHistoricalData(2.3488, 48.8534, 1672531200L, 1673136000L);

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