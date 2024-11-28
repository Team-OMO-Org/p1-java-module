package sample.weatherapp;

import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class WeatherDataController {

  @FXML private TextFlow weatherTextFlow;
  private WeatherAppController weatherAppController;
  private ApiClient apiClient = new ApiClient();

  public void setWeatherAppController(WeatherAppController weatherAppController) {
    this.weatherAppController = weatherAppController;
  }

  public void updateWeather(String city) {
    try {
      String jsonResponse = apiClient.getCurrentWeatherByCityName(city);

      WeatherData weatherData = WeatherDataParser.parseWeatherData(jsonResponse);

      displayWeatherData(weatherData);

    } catch (Exception e) {
      e.printStackTrace();
      weatherTextFlow.getChildren().addAll(new Text("Error fetching weather data"));
    }
  }

  private void displayWeatherData(WeatherData weatherData) {
    weatherTextFlow.getChildren().clear();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, hh:mma");
    // last updated time from API
    Text dateTimeText = new Text(weatherData.getDateTime().format(dateFormatter) + "\n");
    dateTimeText.getStyleClass().addAll("text-default", "text-date");

    Text cityCountryText = new Text(weatherData.getCity() + ", " + weatherData.getCountry() + "\n");
    cityCountryText.getStyleClass().addAll("text-default", "text-city-country");

    /* Image icon = new Image("path/to/icon.png");
    ImageView iconView = new ImageView(icon);
    iconView.setFitHeight(20); // Set the desired height
    iconView.setPreserveRatio(true);*/

    Text temperatureText = new Text(String.format("%.2f°C\n", weatherData.getTemperature()));
    temperatureText.getStyleClass().addAll("text-default", "text-temperature");

    String description =
        weatherData.getDescription().substring(0, 1).toUpperCase()
            + weatherData.getDescription().substring(1);
    Text descriptionText =
        new Text(
            description
                + ". Feels like "
                + String.format("%.2f°C\n", weatherData.getFeelsLikeTemperature()));
    descriptionText.getStyleClass().add("text-default");

    Text windText =
        new Text(weatherData.getWind().toString() + "\t\t" + weatherData.getPressure() + "hPa\n");
    windText.getStyleClass().addAll("text-default", "text-wind-humidity");

    Text humidityText = new Text("Humidity: " + weatherData.getHumidity() + "%\n");
    humidityText.getStyleClass().addAll("text-default", "text-wind-humidity");

    weatherTextFlow
        .getChildren()
        .addAll(
            dateTimeText,
            cityCountryText,
            temperatureText,
            descriptionText,
            windText,
            humidityText);
  }

  @FXML
  public void initStyles() {
    // Load the CSS file and apply it to the TextFlow
    weatherTextFlow
        .getScene()
        .getStylesheets()
        .add(getClass().getResource("/sample/weatherapp/styles.css").toExternalForm());
  }
}
