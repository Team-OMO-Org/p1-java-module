package sample.weatherapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WeatherAppController {

  @FXML
  private Button buttonGetWeather;
  @FXML
  private Label weatherLabel;
  @FXML
  private TextField cityTextField;

  @FXML
  public VBox forecastVBox;

  private ApiClient apiClient = new ApiClient();
  private ResourceBundle rb;
  private ForecastController forecastController;

  @FXML
  private void initialize() {
    Preferences prefs = Preferences.userNodeForPackage(WeatherAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));
    rb = ResourceBundle.getBundle("localization");
    buttonGetWeather.setText(rb.getString("getWeather"));

    initForecastDiagram();
  }

  private void initializeLabels() {
    Preferences prefs = Preferences.userNodeForPackage(WeatherAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));

    buttonGetWeather.setText(rb.getString("getWeather"));
    forecastController.initializeDiagramLabels();
  }

  private void initForecastDiagram() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/sample/weatherapp/ForecastLayout.fxml"));
      loader.setResources(rb);
      VBox myVBox = loader.load();
      ForecastController forecastController = loader.getController();

      this.forecastController = forecastController;
      forecastController.setParentController(this);
      forecastVBox.getChildren().add(myVBox);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @FXML
  private void handleButtonAction() {
    getWeather();
    forecastController.getForecast(cityTextField);
  }

  private void getWeather() {
    String city = cityTextField.getText();
    try {
      String jsonResponse = apiClient.getCurrentWeatherByCityName(city);
      WeatherData weatherData = WeatherDataParser.parseWeatherData(jsonResponse);
      String weatherInfo = String.format(
          "City: %s\nTemperatuprivatere: %.2f°C\nHumidity: %d%%\nDescription: %s",
          weatherData.cityName(),
          weatherData.temperature(),
          weatherData.humidity(),
          weatherData.description()
      );
      weatherLabel.setText(weatherInfo);
    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
    }
  }

  @FXML
  private void openSettingsDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsDialog.fxml"));
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(rb.getString("settings"));
      stage.setScene(new Scene(root));
      stage.showAndWait();
      initializeLabels(); // Reload the locale settings
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}