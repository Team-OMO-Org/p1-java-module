# WeatherApp

## Description

**WeatherApp** is a Java-based application designed to provide users with accurate, real-time  
weather forecasts and summaries. The app features an intuitive user interface built with JavaFX,  
enabling users to access detailed weather data, including temperature, wind speed, pressure, and  
humidity, for any specified location.  
The REST API used in this project is: https://openweathermap.org/api.  

### Key Features:

- **User-Friendly Interface**: Interactive and easy-to-navigate JavaFX interface. Both success and  
  failure are handled gracefully on virtual thread termination, the UI is responsively updated  
  accordingly and timely.  
- **Real-Time Weather Data**: Fetches up-to-date weather information using a reliable REST API.  
- **Detailed Weather Charts**: Graphs for temperature, wind speed, pressure, and humidity trends.  
- **Localization Support**: Multilingual capabilities with support for English, German, Ukrainian,  
  and Russian.  
- **Customizable Settings**: Users can configure their API key, default city, and unit preferences.  
- **Virtual Threads (Project Loom)**: Efficient data fetching for a smoother experience.  
- **Configuration File**: Stores essential user settings (API key, default city, locale).  
- **Clear Code Base**: Separating UI code from the back-end with multithreading using high  
  performance virtual threads and events.  

---

## Technologies Used

### Java

- Core object-oriented programming, supporting modular and scalable designs.  
- Leveraging modern Java features (e.g., **Records**, **Virtual Threads**,  
  **Model View Controller Pattern**).  
- Enhanced project structure focusing on well named packages and classes for improved collaboration  
  on the project opening up possibilities to be an open source and well accessible.  

### JavaFX

- Provides a rich set of libraries for developing visually stunning and high-performance UIs.  
- Splitting code in a Model View Controller (MVC) pattern in a modern design.

### Maven

- Used for project dependency management and building via a **POM** file.
- Provides Continuous Integration (CI) for project deployment.

### Virtual Threads

- Enhances application responsiveness by supporting lightweight, scalable concurrent tasks.

### CSS

- Provides custom styling for the JavaFX components, ensuring a consistent UI design.
- Allows to change the design template in future versions.
- Separates the styling from the rest of the code base.

### REST API Integration

- Connects to weather data providers, parsing JSON responses using **Jackson**.

### Dependency Injection

- Ensures a modular and testable codebase by abstracting dependencies.

### Localization

- Resource bundles for multiple languages, including localized error messages and prompts.

### Exception Handling

- Custom exceptions like `HttpResponseException` ensure robust error management.

### Testing

- Both manually and Unit Tests.

---

## Project Structure

- **Controllers**: Manage UI logic, user interactions, and data updates.
    - `MainAppController`, `ForecastTableController`, `WeatherSummaryController`, etc.

- **Models**: Represent weather data structures (e.g., `DailyForecast`, `WindData`).

- **Services**: Manage business logic, API calls, and data processing.

- **Views**: FXML files for the UI (e.g., `mainAppView.fxml`, `settingsDialogView.fxml`).

- **Resources**:
    - `styles/`: Custom CSS styles.
    - `localization/`: Property files for language support.
    - `config.properties`: Stores API keys and user preferences.

---

## Project Management

Throughout development, the team used the Jira project management tool, including project boards  
(Kanban), issues, and pull requests, to manage tasks, track progress, and collaborate effectively.  
This allowed for transparent tracking of milestones, clear assignment of responsibilities, and  
continuous integration of code, contributing to a smooth workflow and successful project completion.  

---

## Setup and Run Instructions

### Prerequisites

- **Java JDK 22 or higher**.
- **Maven 3.6 or higher**.

### Installation

0. Make an account and retrieve a free API key token from:
   https://openweathermap.org/api.

1. Clone the repository:
   ```bash
   git clone https://github.com/Team-OMO-Org/p1-java-module.git
   cd weatherapp
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```

### Configuration

Edit `config.properties`:

```properties
api.key=your_api_key_here
default.city=your_default_city_here
default.locale=your_default_locale_here
```

---

## Acknowledgements

Icons provided by [Flaticon](https://www.flaticon.com/).  
For more details, see the [official repository](https://github.com/Team-OMO-Org/p1-java-module).