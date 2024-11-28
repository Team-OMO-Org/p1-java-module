package sample.weatherapp;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
  private ForecastTableController forecastTableController;
  ResourceBundle rb;

  @FXML
  private void initialize() {
    // Initialize the localization
    initLocalization();

    // Update the controls with the current locale
    updateControls();

    //Align child containers
    alignContainers();

    //Initialize child containers
    initializeChildContainers();
  }

  private void initLocalization() {
    rb = ResourceBundle.getBundle("localization", Locale.getDefault());
    //  rb = ResourceBundle.getBundle("localization", new Locale("uk", "UA"));
  }
  private void updateControls() {
    buttonGetWeather.setText(rb.getString("getWeather"));
  }

  private void alignContainers() {
    HBox.setHgrow(diagramVBox, Priority.ALWAYS);
    HBox.setHgrow(forecastContainerVBox, Priority.ALWAYS);

    VBox.setVgrow(currentWeatherHBox, Priority.ALWAYS);
    VBox.setVgrow(containerHBox, Priority.ALWAYS);

    forecastContainerVBox.setPrefWidth(550);
    forecastContainerVBox.setMinWidth(550);
    forecastContainerVBox.setMaxWidth(550);
  }

  private void initializeChildContainers() {
    initializeForecastTable();
  }

  private void initializeForecastTable() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/sample/weatherapp/forecastTable.fxml"));
      loader.setResources(rb);
      VBox forecastVBox = loader.load();
      ForecastTableController forecastController = loader.getController();

      this.forecastTableController = forecastController;
      forecastController.setParentController(this);
      forecastContainerVBox.getChildren().add(forecastVBox);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void getWeather() {
    String city = cityTextField.getText();

    try {
      // Update child controllers
      forecastTableController.updateForecast(city);

    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
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
