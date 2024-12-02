package sample.weatherapp.exceptions;

public class HttpResponseException extends Exception {
  private int statusCode;

  public HttpResponseException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
