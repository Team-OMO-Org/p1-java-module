package sample.weatherapp;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.prefs.Preferences;

public class SettingsDialogController {

  @FXML
  private ComboBox<String> localeComboBox;

  private Preferences prefs;

  @FXML
  private void initialize() {
    prefs = Preferences.userNodeForPackage(SettingsDialogController.class);
    localeComboBox.getItems().addAll("en_US", "de_DE", "uk_UA", "ru_UA");
    localeComboBox.setValue(prefs.get("locale", "en_US"));
  }

  @FXML
  private void handleSaveAction() {
    String selectedLocale = localeComboBox.getValue();
    prefs.put("locale", selectedLocale);
    Stage stage = (Stage) localeComboBox.getScene().getWindow();
    stage.close();
  }
}