module sample.weatherapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.databind;
  requires java.prefs;

  opens sample.weatherapp to
      javafx.fxml;

  exports sample.weatherapp;
  exports sample.weatherapp.controllers;
  opens sample.weatherapp.controllers to javafx.fxml;
  exports sample.weatherapp.services;
  opens sample.weatherapp.services to javafx.fxml;
  exports sample.weatherapp.models;
  opens sample.weatherapp.models to javafx.fxml;
}
