package sample.weatherapp;

import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WeatherAppController {

  @FXML private TextField cityTextField;

  @FXML private Label weatherLabel;

  @FXML
  public Button buttonGetWeather;

  @FXML
  public Label hourlyForecast;

  @FXML
  public LineChart<Number, Number> diagramHourlyForecast;

  private ApiClient apiClient = new ApiClient();
  ResourceBundle rb;

  @FXML
  private void initialize() {

//    Locale.setDefault(new Locale("ru","UA"));

    rb = ResourceBundle.getBundle("localization");
    buttonGetWeather.setText(rb.getString("getWeather"));

//    ResourceBundle rb = ResourceBundle.getBundle("localization", new Locale("uk", "UA"));

  }

  @FXML
  private void getWeather() {

    forecastDataAndDiagram();

    String city = cityTextField.getText();

    try {
       String jsonResponse = apiClient.getCurrentWeatherByCityName(city);

      // String jsonResponse = apiClient.getCurrentWeatherByCoordinates(2.3488, 48.8534); // Paris
      // coordinates;
//       String jsonResponse = apiClient.getWeatherForecastByCityName(city);

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

    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching weather data");
    }
  }

  private void forecastDataAndDiagram() {

    try {
      List<Forecast> forecasts = getForecast();
      hourlyForecast.setText("Forecast");
      diagramHourlyForecast = createForecastChart(forecasts);
    } catch (Exception e) {
      e.printStackTrace();
      hourlyForecast.setText("Error fetching weather data");
    }
  }

  private List<Forecast> getForecast() throws Exception {

    String city = cityTextField.getText();

    String jsonResponse = apiClient.getWeatherForecastByCityName(city);
    List<Forecast> forecasts = WeatherDataParser.parseForecastList(jsonResponse);

    return forecasts;
//    diagramHourlyForecast.setText(forecast.toString());
  }

  private static LineChart<Number, Number> createForecastChart(List<Forecast> forecasts) {

    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Time");
    yAxis.setLabel("Temperature (°C)");

    LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Hourly Weather Forecast");

    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName("Temperature");

    for (int i = 0; i < forecasts.size(); i++) {
      Forecast forecast = forecasts.get(i);
      series.getData().add(new XYChart.Data<>(i, forecast.temp()));
    }

    lineChart.getData().add(series);
    return lineChart;
  }
}
