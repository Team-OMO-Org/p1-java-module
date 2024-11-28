package sample.weatherapp;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.util.Locale;
import java.util.prefs.Preferences;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WeatherAppController {

  @FXML
  private Button buttonGetWeather;

  @FXML
  private TextField cityTextField;

  @FXML
  public VBox forecastVBox;

  @FXML
  private HBox parentWeatherDataBox;

  private ApiClient apiClient = new ApiClient();
  private ResourceBundle rb;
  private ForecastController forecastController;
  private WeatherDataController weatherDataController;

  @FXML
  private void initialize() {
    loadLocalization();
    buttonGetWeather.setText(rb.getString("getWeather"));

    initializeWeatherBox();
    initForecastDiagram();
  }

  private void initializeLabels() {
    loadLocalization();

    buttonGetWeather.setText(rb.getString("getWeather"));
    forecastController.initializeDiagramLabels();
  }

  private void loadLocalization() {
    Preferences prefs = Preferences.userNodeForPackage(WeatherAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));
    rb = ResourceBundle.getBundle("localization");
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
    weatherDataController.updateWeather(city);
  }

  private void initializeWeatherBox() {
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/sample/weatherapp/weatherData.fxml"));

      HBox weatherDataBox = loader.load();
      WeatherDataController wdc = loader.getController();
      this.weatherDataController = wdc;
      weatherDataController.setWeatherAppController(this);

      parentWeatherDataBox.getChildren().add(weatherDataBox);
    } catch (IOException e) {
      e.printStackTrace();
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