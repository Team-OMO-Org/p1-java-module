package sample.weatherapp.models;

public record DailyForecast(String dateTime, double temperature, int humidity, String description,
                            String weatherIcon) {

}