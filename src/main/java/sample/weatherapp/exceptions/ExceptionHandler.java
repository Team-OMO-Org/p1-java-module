package sample.weatherapp.exceptions;

import javafx.application.Platform;
import sample.weatherapp.controllers.MainAppController;

public class ExceptionHandler {

  private static String lastExceptionMessage = "";

  public static void handleException(MainAppController uiController, Throwable throwable) {
    if (!(throwable instanceof Exception) || lastExceptionMessage.equals(throwable.getMessage())) {
      return;
    }
     throwable.printStackTrace();
    String errorMessage = (throwable instanceof HttpResponseException) ? throwable.getMessage()
        : "Error fetching weather data";
    lastExceptionMessage = throwable.getMessage();
    // Display an error alert
    Platform.runLater(() -> uiController.displayError(errorMessage));
  }
}
