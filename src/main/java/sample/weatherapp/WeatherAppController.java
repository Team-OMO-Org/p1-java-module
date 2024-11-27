package sample.weatherapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class WeatherAppController {

  @FXML private TextField cityTextField;

  @FXML private Label weatherLabel;

  @FXML public Button buttonGetWeather;

  @FXML public Label hourlyForecast;

  @FXML public ScrollPane scrollPane;

  @FXML public LineChart<String, Number> diagramHourlyForecast;

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
  private void handleButtonAction() {
    getWeather();
    getForecast();
  }

  private void getWeather() {

    String city = cityTextField.getText();

    try {
      String jsonResponse = apiClient.getCurrentWeatherByCityName(city);

      // String jsonResponse = apiClient.getCurrentWeatherByCoordinates(2.3488, 48.8534); // Paris
      // coordinates;
      //       String jsonResponse = apiClient.getWeatherForecastByCityName(city);

      // City id 2925177 for Freiburg im Breisgau for example
      // String jsonResponse = apiClient.getCurrentWeatherByCityId(city);

      //  Historical pollution data ->  January 1, 2023 00:00:00 GMT - January 7, 2023 00:00:00 GMT
      // String jsonResponse = apiClient.getHistoricalPollutionData(2.3488, 48.8534, 1672531200L,
      // 1673136000L);

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

  @FXML
  private void getForecast() {
    String city = cityTextField.getText();
    try {
      String jsonResponse = apiClient.getWeatherForecastByCityName(city);
      List<Forecast> forecasts = WeatherDataParser.parseForecastList(jsonResponse);
      updateForecastChart(forecasts);
    } catch (Exception e) {
      e.printStackTrace();
      weatherLabel.setText("Error fetching forecast data");
    }
  }

  private void updateForecastChart(List<Forecast> forecasts) {
    XYChart.Series<String, Number> tempSeries = new XYChart.Series<>();
    tempSeries.setName("Temperature");

    XYChart.Series<String, Number> windSpeedSeries = new XYChart.Series<>();
    windSpeedSeries.setName("Wind Speed");

    XYChart.Series<String, Number> pressureSeries = new XYChart.Series<>();
    pressureSeries.setName("Pressure (scaled)");

    XYChart.Series<String, Number> humiditySeries = new XYChart.Series<>();
    humiditySeries.setName("Humidity");

    DateTimeFormatter dtfAPI = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dtfChart = DateTimeFormatter.ofPattern("EEE d.\nHH:mm");

    for (Forecast forecast : forecasts) {
      LocalDateTime dateTime = LocalDateTime.parse(forecast.dtTxt(), dtfAPI);
      String xLabel = dateTime.format(dtfChart);

      tempSeries.getData().add(new XYChart.Data<>(xLabel, forecast.temp()));
      windSpeedSeries.getData().add(new XYChart.Data<>(xLabel, forecast.windSpeed()));
      pressureSeries.getData().add(new XYChart.Data<>(xLabel, forecast.pressure() / 10.0)); // Scale pressure
      humiditySeries.getData().add(new XYChart.Data<>(xLabel, forecast.humidity()));
    }

    CategoryAxis tempXAxis = new CategoryAxis();
    tempXAxis.setLabel(""); // Remove x-axis label
    tempXAxis.setCategories(FXCollections.observableArrayList(
        tempSeries.getData().stream().map(XYChart.Data::getXValue).toList()
    ));
    tempXAxis.setTickLabelRotation(45);

    CategoryAxis windSpeedXAxis = new CategoryAxis();
    windSpeedXAxis.setLabel(""); // Remove x-axis label
    windSpeedXAxis.setCategories(FXCollections.observableArrayList(
        windSpeedSeries.getData().stream().map(XYChart.Data::getXValue).toList()
    ));
    windSpeedXAxis.setTickLabelRotation(45);

    CategoryAxis pressureXAxis = new CategoryAxis();
    pressureXAxis.setLabel(""); // Remove x-axis label
    pressureXAxis.setCategories(FXCollections.observableArrayList(
        pressureSeries.getData().stream().map(XYChart.Data::getXValue).toList()
    ));
    pressureXAxis.setTickLabelRotation(45);

    CategoryAxis humidityXAxis = new CategoryAxis();
    humidityXAxis.setLabel(""); // Remove x-axis label
    humidityXAxis.setCategories(FXCollections.observableArrayList(
        humiditySeries.getData().stream().map(XYChart.Data::getXValue).toList()
    ));
    humidityXAxis.setTickLabelRotation(45);

    NumberAxis tempYAxis = new NumberAxis();
    tempYAxis.setLabel("Temperature (°C)");

    NumberAxis windSpeedYAxis = new NumberAxis();
    windSpeedYAxis.setLabel("Wind Speed (m/s)");

    NumberAxis pressureYAxis = new NumberAxis();
    pressureYAxis.setLabel("Pressure (hPa / 10)");

    NumberAxis humidityYAxis = new NumberAxis();
    humidityYAxis.setLabel("Humidity (%)");

    LineChart<String, Number> tempChart = new LineChart<>(tempXAxis, tempYAxis);
    tempChart.getData().add(tempSeries);
    tempChart.setLegendVisible(false);
    tempChart.setPrefWidth(2000); // Adjust width to ensure horizontal scrolling

    LineChart<String, Number> windSpeedChart = new LineChart<>(windSpeedXAxis, windSpeedYAxis);
    windSpeedChart.getData().add(windSpeedSeries);
    windSpeedChart.setLegendVisible(false);
    windSpeedChart.setPrefWidth(2000); // Adjust width to ensure horizontal scrolling

    LineChart<String, Number> pressureChart = new LineChart<>(pressureXAxis, pressureYAxis);
    pressureChart.getData().add(pressureSeries);
    pressureChart.setLegendVisible(false);
    pressureChart.setPrefWidth(2000); // Adjust width to ensure horizontal scrolling

    LineChart<String, Number> humidityChart = new LineChart<>(humidityXAxis, humidityYAxis);
    humidityChart.getData().add(humiditySeries);
    humidityChart.setLegendVisible(false);
    humidityChart.setPrefWidth(2000); // Adjust width to ensure horizontal scrolling

    TabPane tabPane = new TabPane();

    Tab tempTab = new Tab("Temperature", tempChart);
    tempTab.setClosable(false);
    Tab windSpeedTab = new Tab("Wind Speed", windSpeedChart);
    windSpeedTab.setClosable(false);
    Tab pressureTab = new Tab("Pressure", pressureChart);
    pressureTab.setClosable(false);
    Tab humidityTab = new Tab("Humidity", humidityChart);
    humidityTab.setClosable(false);

    tabPane.getTabs().addAll(tempTab, windSpeedTab, pressureTab, humidityTab);

    scrollPane.setFitToHeight(true);
    scrollPane.setContent(tabPane);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
  }
}
