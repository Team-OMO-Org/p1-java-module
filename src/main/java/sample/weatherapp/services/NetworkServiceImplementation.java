package sample.weatherapp.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import sample.weatherapp.exceptions.HttpResponseException;

public class NetworkServiceImplementation implements NetworkService {

  @Override
  public String getResponse(String urlString) throws HttpResponseException, IOException {

    String result = "";
    HttpURLConnection conn = null;
    try {
      URL url = new URI(urlString).toURL();
      System.out.println("COMPLETE_URL: " + url);

      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
          }
          result = content.toString();
        }
      } else {
        StringBuilder errorContent = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            errorContent.append(inputLine);
          }
        }
          throw new HttpResponseException(responseCode, errorContent.toString());
      }
    } catch (URISyntaxException e) {
        throw new IOException("Invalid URL syntax: " + urlString, e);
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return result;
  }
}
