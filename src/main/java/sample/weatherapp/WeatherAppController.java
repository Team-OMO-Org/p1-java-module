package sample.weatherapp;

import java.io.IOException;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.prefs.Preferences;
import javafx.stage.Stage;


public class WeatherAppController {

  @FXML public VBox diagramVBox;
  @FXML public HBox containerHBox;
  @FXML public HBox currentWeatherHBox;
  @FXML public VBox forecastContainerVBox;
  @FXML private TextField cityTextField;
  @FXML private Button buttonGetWeather;
  @FXML public VBox forecastVBox;
  @FXML private HBox parentWeatherDataBox;

  private ApiClient apiClient = new ApiClient();
  private ForecastTableController forecastTableController;
  private ResourceBundle rb;
  private ForecastController forecastController;
  private WeatherDataController weatherDataController;


  @FXML
  private void initialize() {
    // Initialize the localization
    loadLocalization();

    // Update the controls with the current locale
    updateControls();

    //Align child containers
    alignContainers();

    //Initialize child containers
    initializeChildContainers();
  }

  private void loadLocalization() {
    Preferences prefs = Preferences.userNodeForPackage(WeatherAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));
    rb = ResourceBundle.getBundle("localization");
  }

  private void alignContainers() {
    HBox.setHgrow(forecastVBox, Priority.ALWAYS);
    HBox.setHgrow(forecastContainerVBox, Priority.ALWAYS);

    VBox.setVgrow(parentWeatherDataBox, Priority.ALWAYS);
    VBox.setVgrow(containerHBox, Priority.ALWAYS);

    forecastContainerVBox.setPrefWidth(550);
    forecastContainerVBox.setMinWidth(550);
    forecastContainerVBox.setMaxWidth(550);
  }

  private void updateControls() {
   buttonGetWeather.setText(rb.getString("getWeather"));
  }

  @FXML
  private void handleButtonAction() {
    getWeather();
    forecastController.getForecast(cityTextField);
  }

  private void getWeather() {

    String city = cityTextField.getText();
    weatherDataController.updateWeather(city);
    forecastTableController.updateForecast(city);
  }

  private void initializeChildContainers() {
    initializeForecastTable();
    initializeWeatherBox();
    initForecastDiagram();
  }

  private void initializeForecastTable() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/sample/weatherapp/forecastTable.fxml"));
      loader.setResources(rb);
      VBox forecastVBox = loader.load();
      ForecastTableController forecastChildController = loader.getController();

      this.forecastTableController = forecastChildController;
      forecastChildController.setParentController(this);
      forecastContainerVBox.getChildren().add(forecastVBox);
    } catch (IOException e) {
      e.printStackTrace();
    }
    }

    private void initializeLabels() {

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

   public void showError(String message) {
    Alert alert = new Alert(AlertType.ERROR);       // Create an error alert
    alert.setTitle(rb.getString("fetchingError"));  // Set the title
    alert.setHeaderText(null);                      // Optional: Remove header text
    String errorCode = rb.getString("errorCode") + message.replace("error", "");
    alert.setContentText(errorCode);                  // Set the error message

    alert.showAndWait();                            // Display the alert and wait for user response
  }

}

