package sample.weatherapp;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

public class ForecastVBoxController {

  private WeatherAppController parentController;

  @FXML private TableView<DailyForecastWrapper> forecastTableView;
  @FXML private TableColumn<DailyForecastWrapper, String> dateTimeColumn;
 // @FXML private TableColumn<DailyForecastWrapper, Double> temperatureColumn;
  @FXML private TableColumn<DailyForecastWrapper, Integer> humidityColumn;
  @FXML private TableColumn<DailyForecastWrapper, String> descriptionColumn;
  @FXML private TableColumn<DailyForecastWrapper, Image> iconColumn;
  @FXML private Label eightDayForecast;


  @FXML
  private void initialize() {

    ResourceBundle bundle = ResourceBundle.getBundle("localization", Locale.getDefault());


    dateTimeColumn.setText(bundle.getString("dateTimeColumn"));
    iconColumn.setText(bundle.getString("iconColumn"));
    humidityColumn.setText(bundle.getString("humidityColumn"));
    descriptionColumn.setText(bundle.getString("descriptionColumn"));

    forecastTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

    dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

    iconColumn.setCellValueFactory(new PropertyValueFactory<>("weatherIcon"));
    iconColumn.setCellFactory(new Callback<TableColumn<DailyForecastWrapper, Image>, TableCell<DailyForecastWrapper, Image>>() {
      @Override
      public TableCell<DailyForecastWrapper, Image> call(TableColumn<DailyForecastWrapper, Image> param) {
        return new TableCell<DailyForecastWrapper, Image>() {
          private final ImageView imageView = new ImageView();
          private final Label temperatureLabel = new Label();
          private final HBox hBox = new HBox(5); // 5 is the spacing between icon and temperature

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

    //temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
    humidityColumn.setCellValueFactory(new PropertyValueFactory<>("humidity"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

    dateTimeColumn.setPrefWidth(150);
    iconColumn.setPrefWidth(140);
   // temperatureColumn.setPrefWidth(100);

    humidityColumn.setPrefWidth(110);

    descriptionColumn.setPrefWidth(200);

//    descriptionColumn.prefWidthProperty().bind(
//        forecastTableView.widthProperty()
//            .subtract(dateTimeColumn.prefWidthProperty())
//          //  .subtract(temperatureColumn.prefWidthProperty())
//            .subtract(humidityColumn.prefWidthProperty())
//            .subtract(2) // Adjust for borders or padding if necessary
//    );

    forecastTableView.setPlaceholder(new Label(""));
  }

  public void updateForecast(String jsonResponse) {
    try {
      WeatherDailyForecastData forecastData = WeatherDataParser.parseWeatherForecastData(jsonResponse);
      Locale locale = Locale.getDefault(); // or any other locale you want to use
      List<DailyForecastWrapper> wrappedForecasts = forecastData.forecasts().stream()
          .map(forecast -> new DailyForecastWrapper(forecast, locale))
          .collect(Collectors.toList());
      forecastTableView.getItems().setAll(wrappedForecasts);
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