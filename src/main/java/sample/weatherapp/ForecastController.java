package sample.weatherapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class ForecastController {

  @FXML
  private TabPane tabPane;
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private LineChart<String, Number> tempChart;
  @FXML
  private CategoryAxis tempXAxis;
  @FXML
  private NumberAxis tempYAxis;
  @FXML
  private LineChart<String, Number> windSpeedChart;
  @FXML
  private CategoryAxis windSpeedXAxis;
  @FXML
  private NumberAxis windSpeedYAxis;
  @FXML
  private LineChart<String, Number> pressureChart;
  @FXML
  private CategoryAxis pressureXAxis;
  @FXML
  private NumberAxis pressureYAxis;
  @FXML
  private LineChart<String, Number> humidityChart;
  @FXML
  private CategoryAxis humidityXAxis;
  @FXML
  private NumberAxis humidityYAxis;

  private ApiClient apiClient = new ApiClient();
  private ResourceBundle rb;
  private String[] chartNames;
  private String[] yAxisLabels;
  private WeatherAppController parentController;

  @FXML
  private void initialize() {
    Preferences prefs = Preferences.userNodeForPackage(WeatherAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));
    rb = ResourceBundle.getBundle("localization");

    initializeDiagramLabels();
  }

  void initializeDiagramLabels() {
    chartNames = new String[] {
        rb.getString("temperature"),
        rb.getString("windSpeed"),
        rb.getString("pressure"),
        rb.getString("humidity")
    };

    yAxisLabels = new String[] {
        rb.getString("temperatureC"),
        rb.getString("windSpeedMs"),
        rb.getString("pressureHpa10"),
        rb.getString("humidityPercent")
    };

    tempChart.setTitle(chartNames[0]);
    windSpeedChart.setTitle(chartNames[1]);
    pressureChart.setTitle(chartNames[2]);
    humidityChart.setTitle(chartNames[3]);

    tempYAxis.setLabel(yAxisLabels[0]);
    windSpeedYAxis.setLabel(yAxisLabels[1]);
    pressureYAxis.setLabel(yAxisLabels[2]);
    humidityYAxis.setLabel(yAxisLabels[3]);

    tabPane.getTabs().get(0).setText(chartNames[0]);
    tabPane.getTabs().get(1).setText(chartNames[1]);
    tabPane.getTabs().get(2).setText(chartNames[2]);
    tabPane.getTabs().get(3).setText(chartNames[3]);
  }

  @FXML
  void getForecast(TextField cityTextField) {
    String city = cityTextField.getText();
    try {
      String jsonResponse = apiClient.getWeatherForecastByCityName(city);
      List<Forecast> forecasts = WeatherDataParser.parseForecastList(jsonResponse);
      updateForecastChart(forecasts);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateForecastChart(List<Forecast> forecasts) {
    Function<Forecast, Number>[] dataExtractors = new Function[] {
        forecast -> ((Forecast)forecast).temp(),
        forecast -> ((Forecast)forecast).windSpeed(),
        forecast -> ((Forecast)forecast).pressure() / 10.0,
        forecast -> ((Forecast)forecast).humidity()
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

  public void setParentController(WeatherAppController parentController) {

    this.parentController = parentController;
  }
}
