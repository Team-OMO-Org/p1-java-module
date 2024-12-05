package sample.weatherapp.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sample.weatherapp.exceptions.ExceptionHandler;
import sample.weatherapp.exceptions.HttpResponseException;
import sample.weatherapp.models.WeatherSummary;
import sample.weatherapp.services.WeatherDataParser;

public class WeatherSummaryController {

  @FXML private TextFlow weatherTextFlow;
  @FXML private Text dataTime;
  @FXML private Text cityCountry;
  @FXML private ImageView icon;
  @FXML private Text temperatureText;
  @FXML private Text description;
  @FXML private ImageView iconWind;
  @FXML private Text wind;
  @FXML private ImageView iconPressure;
  @FXML private Text pressure;
  @FXML private Text humidity;
  private MainAppController parentController;
  private ResourceBundle rb;
  private final String updatedWeatherDataFile =
      "src/main/resources/sample/weatherapp/data/updatedWeatherData.json";
  private final String iconBasePath = "/sample/weatherapp/img/";

  @FXML
  private void initialize() {
    initLocalization();
    initSummaryView();
  }

  public void setParentController(MainAppController parentController) {
    this.parentController = parentController;
  }

  private void initLocalization() {
    rb = ResourceBundle.getBundle("localization", Locale.getDefault());
  }

  // update Summary View for default city (localization using last city name)
  public void updateSummaryView(String city) {
    rb = ResourceBundle.getBundle("localization");
    // updateWeather(getCityFromWeatherDataFile()); //updated from file for the last searched city
    updateWeather(city);
  }

  public void initSummaryView() {

    weatherTextFlow.getChildren().clear();

    dataTime.setText(rb.getString("dataTime") + "\n");
    cityCountry.setText(rb.getString("cityCountry") + "\n");

    weatherTextFlow.getChildren().addAll(dataTime, cityCountry);
  }

  // Method to fetch weather data using virtual threads
  protected void updateWeather(String city) {
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    Task<WeatherSummary> task =
        new Task<>() {
          @Override
          protected WeatherSummary call() throws HttpResponseException, IOException {

            String jsonResponse =
                parentController.getWeatherApiClient().getCurrentWeatherByCityName(city);
            updateWeatherDataFile(jsonResponse);

            WeatherSummary weatherSummary = WeatherDataParser.parseWeatherData(jsonResponse);

            // Show we are using Virtual Threads
            System.out.println("Current thread: " + Thread.currentThread());

            return weatherSummary;
          }
        };

    // Update the UI with the fetched data
    task.setOnSucceeded(
        event -> {
          WeatherSummary weatherSummary = task.getValue();
          Platform.runLater(
              () -> {
                displayWeatherData(weatherSummary);
              });
        });

    // Handle any exceptions that occurred during the task execution
    task.setOnFailed(
        event -> {
          Throwable exception = task.getException();
          exception.printStackTrace();
          Platform.runLater(
              () -> {
                weatherTextFlow.getChildren().clear();
                weatherTextFlow.getChildren().add(new Text("Error fetching weather data"));
                ExceptionHandler.handleException(parentController, exception);
              });
        });

    // Submit the task to the virtual thread executor
    executor.submit(task);
  }

  private void displayWeatherData(WeatherSummary weatherData) {
    weatherTextFlow.getChildren().clear();

    // last updated time from API
    dataTime.setText(getDataTimeItem(weatherData.getDateTime()));

    cityCountry.setText(getCityCountryItem(weatherData.getCity(), weatherData.getCountry()));

    String iconId = weatherData.getIconId();
    Image iconImg = getImageItemById(iconId);
    icon.setImage(iconImg);

    temperatureText.setText(String.format(getTemperatureItem(weatherData.getTemperature())));

    String descriptionKey = weatherData.getDescription();
    description.setText(getDescriptionItem(descriptionKey, weatherData.getFeelsLikeTemperature()));

    Image windImg = getImageItemById("wind");
    iconWind.setImage(windImg);

    wind.setText(getWindItem(weatherData.getWind().speed(), weatherData.getWind().getDirection()));

    Image pressureImg = getImageItemById("pressure");
    iconPressure.setImage(pressureImg);

    pressure.setText(getPressureItem(weatherData.getPressure()));

    humidity.setText(getHumidityItem(weatherData.getHumidity()));

    weatherTextFlow
        .getChildren()
        .addAll(
            dataTime,
            cityCountry,
            icon,
            temperatureText,
            description,
            iconWind,
            wind,
            iconPressure,
            pressure,
            humidity);
  }

  public void updateWeatherDataFile(String jsonResponse) {

    Path filePath = Paths.get(updatedWeatherDataFile);
    try {
      Files.createDirectories(filePath.getParent());
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {

        writer.write(jsonResponse);
      }
    } catch (IOException e) {
      ExceptionHandler.handleException(parentController, e);
    }
  }

  // return last searched city from file
  public String getCityFromWeatherDataFile() {
    Path filePath = Paths.get(updatedWeatherDataFile);
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(reader);
      JsonNode cityNode = rootNode.path("name");
      return cityNode.asText();

    } catch (IOException e) {
      // ExceptionHandler.handleException(parentController, e);
      return "berlin";
    }
  }

  private String getDataTimeItem(LocalDateTime localDateTime) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, HH:mm");
    return localDateTime.format(dateFormatter) + "\n";
  }

  private String getCityCountryItem(String city, String country) {
    return city + ", " + country + "\n";
  }

  private Image getImageItemById(String iconId) {
    String iconPath = iconBasePath + iconId + ".png";
    return new Image(getClass().getResourceAsStream(iconPath));
  }

  private String getTemperatureItem(double temperature) {
    return String.format(" %.2f°C\n", temperature);
  }

  private String getDescriptionItem(String descriptionKey, double temperature) {
    return rb.getString(descriptionKey)
        + ". "
        + rb.getString("feelsLike")
        + " "
        + String.format("%.2f°C\n", temperature);
  }

  private String getWindItem(double windSpeed, String windDir) {
    return String.format(" %.1f", windSpeed)
        + rb.getString("speedMeasurUnits")
        + " "
        + rb.getObject(windDir)
        + "\t\t";
  }

  private String getPressureItem(int pressure) {
    return " " + pressure + rb.getString("pressureMeasurUnits") + "\n";
  }

  private String getHumidityItem(int humidity) {
    return rb.getString("humidity") + ": " + humidity + "%\n";
  }
}
