package sample.weatherapp.exceptions;

import javafx.application.Platform;
import sample.weatherapp.controllers.MainAppController;

public class ExceptionHandler {

  private static String lastExceptionMessage = "";

  public static void handleException(MainAppController uiController, Throwable exception) {
     if (lastExceptionMessage.equals(exception.getMessage())) {
      return;
    }
    exception.printStackTrace();
    String errorMessage = (exception instanceof HttpResponseException) ? exception.getMessage()
        : "Error fetching weather data";
    lastExceptionMessage = exception.getMessage();
    // Display an error alert
    Platform.runLater(() -> uiController.displayError(errorMessage));
  }
}
