package sample.weatherapp;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.weatherapp.utils.ConfigUtil;

public class WeatherAppMain extends Application {

  static ResourceBundle rb;

  @Override
  public void init() throws Exception {}

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (ConfigUtil.getDefaultLocale() == null) {
      ConfigUtil.setDefaultLocale("en_US");
    }
    Locale.setDefault(Locale.forLanguageTag(ConfigUtil.getDefaultLocale()));
    rb = ResourceBundle.getBundle("localization", Locale.getDefault());

    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mainAppView.fxml"));
    loader.setResources(rb);
    Parent root = loader.load();

   // Parent root = FXMLLoader.load(getClass().getResource("mainAppView.fxml"));
    Scene scene = new Scene(root, 1_200, 800);
    primaryStage.setTitle("Weather App");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
