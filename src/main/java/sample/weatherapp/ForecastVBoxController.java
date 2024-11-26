package sample.weatherapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ForecastVBoxController {

  private WeatherAppController parentController;

  @FXML private TableView<DailyForecast> forecastTableView;
  @FXML private TableColumn<DailyForecast, String> dateTimeColumn;
  @FXML private TableColumn<DailyForecast, Double> temperatureColumn;
  @FXML private TableColumn<DailyForecast, Integer> humidityColumn;
  @FXML private TableColumn<DailyForecast, String> descriptionColumn;
  @FXML private Label eightDayForecast;


  @FXML
  private void initialize() {

    forecastTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

    dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
    temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
    humidityColumn.setCellValueFactory(new PropertyValueFactory<>("humidity"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    dateTimeColumn.setPrefWidth(150);
    temperatureColumn.setPrefWidth(100);
    humidityColumn.setPrefWidth(100);
   // descriptionColumn.setPrefWidth(100);

    descriptionColumn.prefWidthProperty().bind(
        forecastTableView.widthProperty()
            .subtract(dateTimeColumn.prefWidthProperty())
            .subtract(temperatureColumn.prefWidthProperty())
            .subtract(humidityColumn.prefWidthProperty())
            .subtract(2) // Adjust for borders or padding if necessary
    );

//    dateTimeColumn.prefWidthProperty().bind(forecastTableView.widthProperty().multiply(0.25));
//    temperatureColumn.prefWidthProperty().bind(forecastTableView.widthProperty().multiply(0.25));
//    humidityColumn.prefWidthProperty().bind(forecastTableView.widthProperty().multiply(0.25));
 //   descriptionColumn.prefWidthProperty().bind(forecastTableView.widthProperty().multiply(0.25));
//
    forecastTableView.setPlaceholder(new Label(""));
  }

  public void updateForecast(String jsonResponse) {
    try {
      WeatherDailyForecastData forecastData = WeatherDataParser.parseWeatherForecastData(jsonResponse);
      forecastTableView.getItems().setAll(forecastData.forecasts());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

//  public void updateForecast(String forecast) {
//    eightDayForecast.setText(forecast);
//  }

  public void setParentController(WeatherAppController parentController) {
    this.parentController = parentController;
    // You can now access methods from the parent controller
    String data = parentController.getSomeData();
    eightDayForecast.setText(data);
  }
}