package sample.weatherapp.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigUtil {

  private static final String CONFIG_FILE = "/config.properties";
  private static Properties properties = new Properties();

  static {
    try (InputStream input = ConfigUtil.class.getResourceAsStream(CONFIG_FILE)) {
      if (input == null) {
        throw new IOException("Unable to find " + CONFIG_FILE);
      }
      properties.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static String getApiKey() {
    return properties.getProperty("api.key");
  }

  public static void setApiKey(String apiKey) {
    saveProperty("api.key", apiKey);
  }

  public static String getDefaultCity() {
    return properties.getProperty("default.city");
  }

  public static void setDefaultCity(String defaultCity) {
    saveProperty("default.city", defaultCity);
  }

  public static String getDefaultLocale() {
    return properties.getProperty("default.locale");
  }

  public static void setDefaultLocale(String defaultLocale) {
    saveProperty("default.locale", defaultLocale);
  }

  private static void saveProperty(String key, String value) {
    try (OutputStream output = new FileOutputStream(ConfigUtil.class.getResource(CONFIG_FILE).getPath())) {
      properties.setProperty(key, value);
      properties.store(output, null);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
