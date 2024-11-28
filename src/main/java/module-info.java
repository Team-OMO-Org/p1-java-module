module sample.weatherapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.databind;
  requires java.prefs;

  opens sample.weatherapp to
      javafx.fxml;

  exports sample.weatherapp;
}
