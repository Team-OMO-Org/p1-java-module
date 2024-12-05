package sample.weatherapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.Preferences;
import sample.weatherapp.utils.ConfigUtil;

public class SettingsDialogController {

  @FXML public TextField apiKeyTextField;
  @FXML public TextField defaultCityTextField;
  @FXML private ComboBox<String> localeComboBox;

  private Preferences prefs;

  @FXML
  private void initialize() {
    prefs = Preferences.userNodeForPackage(SettingsDialogController.class);
    localeComboBox.getItems().addAll("en_US", "de_DE", "uk_UA", "ru_UA");
   // localeComboBox.setValue(prefs.get("locale", "en_US"));
    localeComboBox.setValue(ConfigUtil.getDefaultLocale());
    apiKeyTextField.setText(ConfigUtil.getApiKey());
    defaultCityTextField.setText(ConfigUtil.getDefaultCity());
  }

  @FXML
  private void handleSaveAction() {
    String selectedLocale = localeComboBox.getValue();
    String apiKey = apiKeyTextField.getText();
    String defaultCity = defaultCityTextField.getText();
    ConfigUtil.setApiKey(apiKey);
    ConfigUtil.setDefaultCity(defaultCity);
    ConfigUtil.setDefaultLocale(selectedLocale);
    prefs.put("locale", selectedLocale);
    Stage stage = (Stage) localeComboBox.getScene().getWindow();
    stage.close();
  }
}