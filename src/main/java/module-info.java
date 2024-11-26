module sample.weatherapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.databind;

  opens sample.weatherapp to
      javafx.fxml;

  exports sample.weatherapp;
}
