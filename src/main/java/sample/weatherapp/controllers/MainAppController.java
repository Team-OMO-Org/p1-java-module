package sample.weatherapp.controllers;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.prefs.Preferences;
import javafx.stage.Stage;
import sample.weatherapp.exceptions.ExceptionHandler;
import sample.weatherapp.services.NetworkServiceImplementation;
import sample.weatherapp.services.WeatherApiClient;

public class MainAppController {

  @FXML public VBox diagramVBox;
  @FXML public HBox containerHBox;
  @FXML public HBox currentWeatherHBox;
  @FXML public VBox forecastContainerVBox;
  @FXML private TextField cityTextField;
  @FXML private Button buttonGetWeather;
  @FXML public VBox forecastVBox;
  @FXML private HBox parentWeatherDataBox;
  @FXML public Label cityLabel;

  private WeatherApiClient weatherApiClient =
      new WeatherApiClient(new NetworkServiceImplementation());

  private ForecastTableController forecastTableController;
  private ResourceBundle rb;
  private ForecastDiagramController forecastDiagramController;
  private WeatherSummaryController weatherDataController;

  @FXML
  private void initialize() {
    // Initialize the localization
    loadLocalization();

    // Align child containers
    configureLayouts();

    // Initialize child containers
    initializeChildContainers();

    // Update the controls with the current locale
    updateControls();
  }

  private void updateLayouts(){
    loadLocalization();

    // Update the controls with the current locale
    updateControls();

    // Update each container

  }
  private void loadLocalization() {
    Preferences prefs = Preferences.userNodeForPackage(MainAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));
    rb = ResourceBundle.getBundle("localization");
  }

  private void configureLayouts() {
    HBox.setHgrow(forecastVBox, Priority.ALWAYS);
    HBox.setHgrow(forecastContainerVBox, Priority.ALWAYS);

    VBox.setVgrow(parentWeatherDataBox, Priority.ALWAYS);
    VBox.setVgrow(containerHBox, Priority.ALWAYS);

    forecastContainerVBox.setPrefWidth(550);
    forecastContainerVBox.setMinWidth(550);
    forecastContainerVBox.setMaxWidth(550);
  }

  private void updateControls() {
    cityLabel.setText(rb.getString("cityLabel"));
    buttonGetWeather.setText(rb.getString("getWeather"));
    forecastDiagramController.initializeDiagramLabels();
    weatherDataController.updateSummaryView();
    forecastTableController.updateForecastTable(cityTextField.getText());

  }

  @FXML
  private void onGetWeatherButtonClick() {
    getWeather(cityTextField);
  }

  private void getWeather(TextField cityTextField) {
    weatherDataController.updateWeather(cityTextField.getText());
    forecastTableController.updateForecast(cityTextField.getText());
    forecastDiagramController.updateForecast(cityTextField);
  }

  private void initializeChildContainers() {
    initForecastTable();
    initWeatherSummary();
    initForecastDiagram();
  }

  private void initForecastTable() {
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/sample/weatherapp/views/forecastTableView.fxml"));
      loader.setResources(rb);
      VBox forecastVBox = loader.load();
      ForecastTableController forecastChildController = loader.getController();

      this.forecastTableController = forecastChildController;
      forecastChildController.setParentController(this);
      forecastContainerVBox.getChildren().add(forecastVBox);
    } catch (IOException e) {
      ExceptionHandler.handleException(this, e);
    }
  }
/*
  private void refreshLabels() {

    buttonGetWeather.setText(rb.getString("getWeather"));
    forecastDiagramController.initializeDiagramLabels();
  }*/

  private void initForecastDiagram() {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource("/sample/weatherapp/views/forecastDiagramView.fxml"));
      loader.setResources(rb);
      VBox myVBox = loader.load();
      ForecastDiagramController forecastController = loader.getController();

      this.forecastDiagramController = forecastController;
      forecastController.setParentController(this);
      forecastVBox.getChildren().add(myVBox);
    } catch (IOException e) {
      ExceptionHandler.handleException(this, e);
    }
  }

  private void initWeatherSummary() {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource("/sample/weatherapp/views/weatherSummaryView.fxml"));

      HBox weatherDataBox = loader.load();
      WeatherSummaryController wdc = loader.getController();
      this.weatherDataController = wdc;
      weatherDataController.setParentController(this);

      parentWeatherDataBox.getChildren().add(weatherDataBox);

    } catch (IOException e) {
      ExceptionHandler.handleException(this, e);
    }
  }

  @FXML
  private void openSettingsDialog() {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource("/sample/weatherapp/views/settingsDialogView.fxml"));
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(rb.getString("settings"));
      stage.setScene(new Scene(root));
      stage.showAndWait();

      updateLayouts(); // Reload the locale settings
    } catch (IOException e) {
      ExceptionHandler.handleException(this, e);
    }
  }

  public void displayError(String message) {
    Alert alert = new Alert(AlertType.ERROR); // Create an error alert
    alert.setTitle("Error fetching weather data"); // Set the title
    alert.setHeaderText(null); // Optional: Remove header text
    alert.setContentText(message); // Set the error message
       alert.showAndWait(); // Display the alert and wait for user response
  }

  public WeatherApiClient getWeatherApiClient() {
    return weatherApiClient;
  }

  public void onGetWeatherKeyPressed(KeyEvent keyEvent) {
    switch (keyEvent.getCode()) {
      case ENTER: // Check if Enter key was pressed
        onGetWeatherButtonClick();
        break;
      default:
        break;
    }
  }
}
