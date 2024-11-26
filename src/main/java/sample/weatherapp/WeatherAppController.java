package sample.weatherapp;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class WeatherAppController {

  @FXML public VBox diagramVBox;
  @FXML public HBox containerHBox;
  @FXML public HBox currentWeatherHBox;
  @FXML public VBox forecastContainerVBox;

  @FXML private TextField cityTextField;

  @FXML private Label weatherLabel;

  @FXML
  public Button buttonGetWeather;

  private ApiClient apiClient = new ApiClient();
  private ForecastVBoxController forecastController;
  ResourceBundle rb;

  @FXML
  private void initialize() {

    HBox.setHgrow(diagramVBox, Priority.ALWAYS);
    HBox.setHgrow(forecastContainerVBox, Priority.ALWAYS);

    VBox.setVgrow(currentWeatherHBox, Priority.ALWAYS);
    VBox.setVgrow(containerHBox, Priority.ALWAYS);

    forecastContainerVBox.setPrefWidth(600);
    forecastContainerVBox.setMinWidth(600);
    forecastContainerVBox.setMaxWidth(600);

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/weatherapp/forecastVBox.fxml"));
      VBox forecastVBox = loader.load();
      ForecastVBoxController forecastController = loader.getController();

      this.forecastController = forecastController;
      forecastController.setParentController(this);
      forecastContainerVBox.getChildren().add(forecastVBox);
    } catch (IOException e) {
      e.printStackTrace();
    }

//    Locale.setDefault(new Locale("ru","UA"));

    rb = ResourceBundle.getBundle("localization");
    buttonGetWeather.setText(rb.getString("getWeather"));

//    ResourceBundle rb = ResourceBundle.getBundle("localization", new Locale("uk", "UA"));

  }
  public String getSomeData() {
    return "5 Days Forecast";
  }

  @FXML
  private void getWeather() {
    String city = cityTextField.getText();

    try {
       String jsonResponse = apiClient.getCurrentWeatherByCityName(city);

      // String jsonResponse = apiClient.getCurrentWeatherByCoordinates(2.3488, 48.8534); // Paris
      // coordinates;
      // String jsonResponse = apiClient.getWeatherForecastByCityName(city);

      // City id 2925177 for Freiburg im Breisgau for example
      // String jsonResponse = apiClient.getCurrentWeatherByCityId(city);

      //  Historical pollution data ->  January 1, 2023 00:00:00 GMT - January 7, 2023 00:00:00 GMT
      //String jsonResponse = apiClient.getHistoricalPollutionData(2.3488, 48.8534, 1672531200L, 1673136000L);

      // Timemachine -> does not work, check documentation
      // String jsonResponse = apiClient.getTimemachineData(2.3488, 48.8534, 1673136000L);

      // String jsonResponse = apiClient.getForecast4Days3HoursByCityId("2925177");

      // String jsonResponse = apiClient.getForecast4Days3HoursByCoordinates(2.3488, 48.8534);

      // String jsonResponse = apiClient.getForecast4Days3HoursByZipCode("10115", "DE");

      // Does not work
      // String jsonResponse = apiClient.getOneCall(2.3488, 48.8534);

      // pollution data for Paris
      // String jsonResponse = apiClient.getPollutionData(2.3488, 48.8534);

      WeatherData weatherData = WeatherDataParser.parseWeatherData(jsonResponse);

      // Display the weather data
      String weatherInfo =
          String.format(
              "City: %s\nTemperature: %.2f°C\nHumidity: %d%%\nDescription: %s",
              weatherData.cityName(),
              weatherData.temperature(),
              weatherData.humidity(),
              weatherData.description());

      weatherLabel.setText(weatherInfo);

      // Update the forecast in the child controller
      String forecastJsonResponse = apiClient.getForecast4Days3HoursByCityId(city);
      forecastController.updateForecast(forecastJsonResponse);

    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
    }
  }
}
