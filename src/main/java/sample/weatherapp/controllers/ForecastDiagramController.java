package sample.weatherapp.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.weatherapp.exceptions.ExceptionHandler;
import sample.weatherapp.exceptions.HttpResponseException;
import sample.weatherapp.models.Forecast;
import sample.weatherapp.models.WeatherSummary;
import sample.weatherapp.services.WeatherDataParser;

public class ForecastDiagramController {

  @FXML private TabPane tabPane;
  @FXML private ScrollPane scrollPane;
  @FXML private LineChart<String, Number> tempChart;
  @FXML private CategoryAxis tempXAxis;
  @FXML private NumberAxis tempYAxis;
  @FXML private LineChart<String, Number> windSpeedChart;
  @FXML private CategoryAxis windSpeedXAxis;
  @FXML private NumberAxis windSpeedYAxis;
  @FXML private LineChart<String, Number> pressureChart;
  @FXML private CategoryAxis pressureXAxis;
  @FXML private NumberAxis pressureYAxis;
  @FXML private LineChart<String, Number> humidityChart;
  @FXML private CategoryAxis humidityXAxis;
  @FXML private NumberAxis humidityYAxis;
  @FXML public Label forecastDiagramLabel;

  private ResourceBundle rb;
  private String[] chartNames;
  private String[] yAxisLabels;
  private MainAppController parentController;

  private final DateTimeFormatter dtfAPI = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private final DateTimeFormatter dtfChart =
      DateTimeFormatter.ofPattern("EEE, dd MMM\n h:mm a", Locale.getDefault());

  @FXML
  private void initialize() {

    Preferences prefs = Preferences.userNodeForPackage(MainAppController.class);
    String localeString = prefs.get("locale", "en_US");
    Locale.setDefault(Locale.forLanguageTag(localeString.replace('_', '-')));
    rb = ResourceBundle.getBundle("localization");

    initializeDiagramLabels();
  }

  void initializeDiagramLabels() {

    rb = ResourceBundle.getBundle("localization");

    forecastDiagramLabel.setText(rb.getString("forecastDiagramLabel"));

    chartNames =
        new String[] {
          rb.getString("temperature"),
          rb.getString("windSpeed"),
          rb.getString("pressure"),
          rb.getString("humidity")
        };

    yAxisLabels =
        new String[] {
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

  // Method to fetch weather data using virtual threads
  void updateForecast(TextField cityTextField) {

    String city = cityTextField.getText();
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    Task<List<Forecast>> task =
        new Task<>() {
          @Override
          protected List<Forecast> call() throws HttpResponseException, IOException {

            // Show we are using Virtual Threads
            System.out.println("Current thread: " + Thread.currentThread());

            String jsonResponse =
                parentController.getWeatherApiClient().getWeatherForecastByCityName(city);
            List<Forecast> forecasts = WeatherDataParser.parseForecastList(jsonResponse);

            return forecasts;
          }
        };

    // Update the UI with the fetched data
    task.setOnSucceeded(
        event -> {
          List<Forecast> forecasts = task.getValue();
          Platform.runLater(
              () -> {
                var dataExtractors = initializeEmptyCharts();
                plotForecastValues(forecasts, dataExtractors);
              });
        });

    // Handle any exceptions that occurred during the task execution
    task.setOnFailed(
        event -> {
          Throwable exception = task.getException();
          ExceptionHandler.handleException(parentController, exception);
          Platform.runLater(this::initializeEmptyCharts);
        });

    // Submit the task to the virtual thread executor
    executor.submit(task);

    // FixMe: get Executor from main
    if (executor != null) {
      executor.shutdown();
    }
  }

  private Function<Forecast, Number>[] initializeEmptyCharts() {
    Function<Forecast, Number>[] dataExtractors =
        new Function[] {
          forecast -> ((Forecast) forecast).temp(),
          forecast -> ((Forecast) forecast).windSpeed(),
          forecast -> ((Forecast) forecast).pressure() / 10.0,
          forecast -> ((Forecast) forecast).humidity()
        };

    tabPane.getTabs().clear();

    for (int i = 0; i < chartNames.length; i++) {
      CategoryAxis xAxis = new CategoryAxis();
      xAxis.setLabel("");
      xAxis.setTickLabelRotation(45);

      NumberAxis yAxis = new NumberAxis();
      yAxis.setLabel(yAxisLabels[i]);

      LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
      chart.setTitle(chartNames[i]);
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

    return dataExtractors;
  }

  private void plotForecastValues(
      List<Forecast> forecasts, Function<Forecast, Number>[] dataExtractors) {
    for (int i = 0; i < chartNames.length; i++) {
      XYChart.Series<String, Number> series = new XYChart.Series<>();
      series.setName(chartNames[i]);

      for (Forecast forecast : forecasts) {
        LocalDateTime dateTime = LocalDateTime.parse(forecast.dtTxt(), dtfAPI);
        // define x tick label output here
        String xLabel = dateTime.format(dtfChart);
        ;
        series.getData().add(new XYChart.Data<>(xLabel, dataExtractors[i].apply(forecast)));
      }

      CategoryAxis xAxis = new CategoryAxis();
      xAxis.setLabel("");
      xAxis.setCategories(
          FXCollections.observableArrayList(
              series.getData().stream().map(XYChart.Data::getXValue).toList()));
      xAxis.setTickLabelRotation(45);

      NumberAxis yAxis = new NumberAxis();
      yAxis.setLabel(yAxisLabels[i]);

      LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
      chart.setTitle(chartNames[i]);
      chart.setLegendVisible(false);
      chart.setPrefWidth(2000);
      chart.getData().add(series);

      Tab tab = tabPane.getTabs().get(i);
      tab.setContent(chart);
    }
  }

  public void setParentController(MainAppController parentController) {

    this.parentController = parentController;
  }
}
