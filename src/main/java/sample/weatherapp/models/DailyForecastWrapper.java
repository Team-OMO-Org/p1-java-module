package sample.weatherapp.models;

import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DailyForecastWrapper {
  private final DailyForecast dailyForecast;
  private final SimpleDateFormat inputDateFormat;
  private final SimpleDateFormat outputDateFormat;
  private final NumberFormat temperatureFormat;
  private final NumberFormat humidityFormat;
  private final String description;

  public DailyForecastWrapper(DailyForecast dailyForecast, Locale locale) {
    ResourceBundle rb = ResourceBundle.getBundle("localization");

    this.dailyForecast = dailyForecast;
    this.inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
    this.outputDateFormat = new SimpleDateFormat("EEE, dd MMM   HH:mm", locale);
    this.temperatureFormat = NumberFormat.getNumberInstance(locale);
    this.humidityFormat = NumberFormat.getPercentInstance(locale);

    String descriptionKey = dailyForecast.description();
    boolean descriptionExists = rb.containsKey(descriptionKey);
    this.description = descriptionExists ? rb.getString(descriptionKey) : dailyForecast.description();

  }

  public String getDateTime() {
    try {
      Date date = inputDateFormat.parse(dailyForecast.dateTime());
      return outputDateFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return dailyForecast.dateTime(); // Return the original string if parsing fails
    }
  }

  public String getTemperature() {
    temperatureFormat.setMaximumFractionDigits(0);
    return temperatureFormat.format(dailyForecast.temperature()) + " °C";
  }

  public String getHumidity() {
    return humidityFormat.format(dailyForecast.humidity() / 100.0);
  }

  public String getDescription() {
    return this.description;
  }

  public Image getWeatherIcon() {
    String weatherIconFilePath = String.format("/sample/weatherapp/img/%s.png", dailyForecast.weatherIcon());
    Image image = null;
    try {
      image = new Image(getClass().getResourceAsStream(weatherIconFilePath));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return image;
  }
}