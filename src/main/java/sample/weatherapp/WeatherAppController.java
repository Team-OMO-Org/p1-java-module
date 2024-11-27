package sample.weatherapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
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

public class WeatherAppController {

  @FXML private TextField cityTextField;

  @FXML private Label weatherLabel;

  @FXML public Button buttonGetWeather;

  @FXML public Label hourlyForecast;

  @FXML public ScrollPane scrollPane;

  @FXML public TabPane tabPane;

  @FXML public LineChart<String, Number> tempChart;
  @FXML public CategoryAxis tempXAxis;
  @FXML public NumberAxis tempYAxis;

  @FXML public LineChart<String, Number> windSpeedChart;
  @FXML public CategoryAxis windSpeedXAxis;
  @FXML public NumberAxis windSpeedYAxis;

  @FXML public LineChart<String, Number> pressureChart;
  @FXML public CategoryAxis pressureXAxis;
  @FXML public NumberAxis pressureYAxis;

  @FXML public LineChart<String, Number> humidityChart;
  @FXML public CategoryAxis humidityXAxis;
  @FXML public NumberAxis humidityYAxis;

  private ApiClient apiClient = new ApiClient();
  ResourceBundle rb;

  @FXML
  private void initialize() {
    rb = ResourceBundle.getBundle("localization");
    buttonGetWeather.setText(rb.getString("getWeather"));
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
      WeatherData weatherData = WeatherDataParser.parseWeatherData(jsonResponse);
      String weatherInfo = String.format(
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
    String[] chartNames = {"Temperature", "Wind Speed", "Pressure (scaled)", "Humidity"};
    String[] yAxisLabels = {"Temperature (°C)", "Wind Speed (m/s)", "Pressure (hPa / 10)", "Humidity (%)"};
    Function<Forecast, Number>[] dataExtractors = new Function[]{
        forecast -> ((Forecast) forecast).temp(),
        forecast -> ((Forecast) forecast).windSpeed(),
        forecast -> ((Forecast) forecast).pressure() / 10.0,
        forecast -> ((Forecast) forecast).humidity()
    };

    DateTimeFormatter dtfAPI = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dtfChart = DateTimeFormatter.ofPattern("EEE d.\nHH:mm");

    TabPane tabPane = new TabPane();

    for (int i = 0; i < chartNames.length; i++) {
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName(chartNames[i]);

      for (Forecast forecast : forecasts) {
        LocalDateTime dateTime = LocalDateTime.parse(forecast.dtTxt(), dtfAPI);
        String xLabel = dateTime.format(dtfChart);
        series.getData().add(new XYChart.Data<>(xLabel, dataExtractors[i].apply(forecast)));
      }

      CategoryAxis xAxis = new CategoryAxis();
      xAxis.setLabel("");
      xAxis.setCategories(FXCollections.observableArrayList(
          series.getData().stream().map(XYChart.Data::getXValue).toList()
      ));
      xAxis.setTickLabelRotation(45);

      NumberAxis yAxis = new NumberAxis();
      yAxis.setLabel(yAxisLabels[i]);

      LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
      chart.getData().add(series);
      chart.setLegendVisible(false);
      chart.setPrefWidth(2000);

      Tab tab = new Tab(chartNames[i], chart);
      tab.setClosable(false);
      tabPane.getTabs().add(tab);
    }

    scrollPane.setFitToHeight(true);
    scrollPane.setContent(tabPane);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
  }
}