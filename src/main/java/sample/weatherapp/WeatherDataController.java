package sample.weatherapp;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class WeatherDataController {

  @FXML private TextFlow weatherTextFlow;
  private WeatherAppController weatherAppController;
  private ApiClient apiClient = new ApiClient();
  private ResourceBundle rb;

  public void setWeatherAppController(WeatherAppController weatherAppController) {
    this.weatherAppController = weatherAppController;
  }

  public void updateWeather(String city) {
    try {
      String jsonResponse = apiClient.getCurrentWeatherByCityName(city);
      updateWeatherDataFile(jsonResponse);

      WeatherData weatherData = WeatherDataParser.parseWeatherData(jsonResponse);

      displayWeatherData(weatherData);

    } catch (Exception e) {
      e.printStackTrace();
      weatherTextFlow.getChildren().clear();
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

    String iconId = weatherData.getIconId();
    String iconPath = "/sample/weatherapp/img/"+iconId +"@2x.png";
    Image icon = new Image(getClass().getResourceAsStream(iconPath));
    ImageView iconView = new ImageView(icon);
    iconView.setFitHeight(60);
    iconView.setFitWidth(60);
    iconView.setPreserveRatio(true);

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
            iconView,
            temperatureText,
            descriptionText,
            windText,
            humidityText);
  }

  public void updateWeatherDataFile(String jsonResponse) {
    Path filePath = Paths.get("src/main/resources/updatedWeatherData.json");
    try(FileWriter fileWriter = new FileWriter(filePath.toFile())){
      fileWriter.write(jsonResponse);
    }catch(IOException e){
      e.printStackTrace();
    }

  }
}
