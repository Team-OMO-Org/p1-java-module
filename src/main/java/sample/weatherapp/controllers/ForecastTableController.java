package sample.weatherapp.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import sample.weatherapp.exceptions.HttpResponseException;
import sample.weatherapp.models.WeatherSummary;
import sample.weatherapp.services.WeatherApiClient;
import sample.weatherapp.models.DailyForecastWrapper;
import sample.weatherapp.models.DailyForecastRoot;
import sample.weatherapp.services.WeatherDataParser;

public class ForecastTableController {

  private MainAppController parentController;
  ResourceBundle bundle;

  @FXML private TableView<DailyForecastWrapper> forecastTableView;
  @FXML private TableColumn<DailyForecastWrapper, String> dateTimeColumn;
  // @FXML private TableColumn<DailyForecastWrapper, Double> temperatureColumn;
  @FXML private TableColumn<DailyForecastWrapper, Integer> humidityColumn;
  @FXML private TableColumn<DailyForecastWrapper, String> descriptionColumn;
  @FXML private TableColumn<DailyForecastWrapper, Image> iconColumn;
  @FXML private Label tableForecastLabel;

  @FXML
  private void initialize() {

    // Initialize the localization
    initLocalization();

    // Update the controls with the current locale
    updateControls();
  }

  private void initLocalization() {
    bundle = ResourceBundle.getBundle("localization", Locale.getDefault());
  }

  private void updateControls() {
    tableForecastLabel.setText(bundle.getString("getForecastLabel"));

    forecastTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

    dateTimeColumn.setText(bundle.getString("dateTimeColumn"));
    iconColumn.setText(bundle.getString("iconColumn"));
    humidityColumn.setText(bundle.getString("humidityColumn"));
    descriptionColumn.setText(bundle.getString("descriptionColumn"));

    dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
    iconColumn.setCellValueFactory(new PropertyValueFactory<>("weatherIcon"));
    iconColumn.setCellFactory(
        new Callback<
            TableColumn<DailyForecastWrapper, Image>, TableCell<DailyForecastWrapper, Image>>() {
          @Override
          public TableCell<DailyForecastWrapper, Image> call(
              TableColumn<DailyForecastWrapper, Image> param) {
            return new TableCell<DailyForecastWrapper, Image>() {
              private final ImageView imageView = new ImageView();
              private final Label temperatureLabel = new Label();
              private final HBox hBox =
                  new HBox(5); // 5 is the spacing between icon and temperature

              {
                hBox.getChildren().addAll(imageView, temperatureLabel);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
              }

              @Override
              protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setGraphic(null);
                } else {
                  DailyForecastWrapper forecast = getTableView().getItems().get(getIndex());
                  imageView.setImage(item);
                  imageView.setFitWidth(50);
                  imageView.setFitHeight(50);
                  temperatureLabel.setText(forecast.getTemperature());
                  setGraphic(hBox);
                }
              }
            };
          }
        });

    // temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
    humidityColumn.setCellValueFactory(new PropertyValueFactory<>("humidity"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    dateTimeColumn.setPrefWidth(130);
    iconColumn.setPrefWidth(140);
    // temperatureColumn.setPrefWidth(100);
    humidityColumn.setPrefWidth(110);
    descriptionColumn.setPrefWidth(170);

    forecastTableView.setPlaceholder(new Label(""));
  }

  protected void updateForecast(String city) {
    Locale locale = Locale.getDefault();
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    Task<List<DailyForecastWrapper>> task =
        new Task<>() {
          @Override
          protected List<DailyForecastWrapper> call() throws Exception {

            String httpResponse =
                parentController.getWeatherApiClient().getWeatherForecastByCityName(city);
            DailyForecastRoot forecastData = WeatherDataParser.parseWeatherForecastData(httpResponse);

            List<DailyForecastWrapper> wrappedForecasts =
                forecastData.forecasts().stream()
                    .map(forecast -> new DailyForecastWrapper(forecast, locale))
                    .collect(Collectors.toList());


            // Show we are using Virtual Threads
            System.out.println("Current thread: " + Thread.currentThread());

            return wrappedForecasts;
          }
        };

    // Update the UI with the fetched data
    task.setOnSucceeded(
        event -> {
          List<DailyForecastWrapper> wrappedForecasts = task.getValue();
          Platform.runLater(
              () -> {
                forecastTableView.getItems().setAll(wrappedForecasts);
              });
        });

    // Handle any exceptions that occurred during the task execution
    task.setOnFailed(
        event -> {
          Throwable exception = task.getException();
          exception.printStackTrace();
          Platform.runLater(this::initializeEmptyTable);
        });

    // Submit the task to the virtual thread executor
    executor.submit(task);
  }


  public void initializeEmptyTable() {
    forecastTableView.getItems().clear();
    forecastTableView.setPlaceholder(new Label("Error fetching weather data"));
  }

  public void setParentController(MainAppController parentController) {
    this.parentController = parentController;
  }
}
