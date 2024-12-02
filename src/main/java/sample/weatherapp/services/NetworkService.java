package sample.weatherapp.services;

import java.io.IOException;
import sample.weatherapp.exceptions.HttpResponseException;

public interface NetworkService {

  String getResponse(String url) throws HttpResponseException, IOException;
}
