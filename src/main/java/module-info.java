module sample.weatherapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.google.gson;

  opens sample.weatherapp to
      javafx.fxml;

  exports sample.weatherapp;
}
