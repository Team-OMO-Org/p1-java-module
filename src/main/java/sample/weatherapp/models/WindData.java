package sample.weatherapp.models;

public record WindData(double speed, double deg) {
  private static final String[] DIRECTIONS = {
    "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW",
    "NNW"
  };

  public String convertToDirection() {
    double step = 22.5;
    int index = (int) ((deg + 11.25) / step);
    if (index == 16) {
      index = 0;
    }
    return DIRECTIONS[index];
  }

  public String getDirection() {
    return convertToDirection();
  }

  @Override
  public String toString() {
    return String.format("%.1fm/s %s", speed, convertToDirection());
  }
}
