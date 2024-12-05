## Acknowledgements

- [Settings icons created by Pixel perfect - Flaticon](https://www.flaticon.com/free-icons/settings)
- [Wind direction icons created by Vector Stall - Flaticon](https://www.flaticon.com/free-icons/wind-direction)
- [Barometer icons created by Good Ware - Flaticon](https://www.flaticon.com/free-icons/barometer)

# WeatherApp

## Description
WeatherApp is a comprehensive Java-based application designed to provide users with accurate and up-to-date weather forecasts and summaries. The application allows users to view detailed weather data, including temperature, wind speed, pressure, and humidity for a specified city.

### Key Features:
- **User-Friendly Interface**: The application features an intuitive and easy-to-navigate interface built with JavaFX, ensuring a seamless user experience.
- **Real-Time Weather Data**: WeatherApp fetches real-time weather data from a reliable external API, ensuring that users always have access to the latest weather information.
- **Detailed Weather Charts**: The application displays weather data in various charts and diagrams, including line charts for temperature, wind speed, pressure, and humidity, providing a clear visual representation of the weather trends.
- **Localization Support**: WeatherApp supports multiple languages, allowing users to view weather information in their preferred language.
- **Customizable Settings**: Users can easily configure their API key and default city through the settings dialog, ensuring personalized weather updates.
- **Virtual Threads**: The application leverages virtual threads (Project Loom) for efficient and scalable concurrent data fetching, enhancing performance and responsiveness.

WeatherApp is an ideal tool for anyone looking to stay informed about the weather conditions in their city or any other location around the world.
## Technologies Used

### Java
Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible. It is widely used for building enterprise-scale applications and is known for its portability across platforms. This project uses Java 17 or higher, leveraging new features such as pattern matching for instanceof, records, and sealed classes.

### JavaFX
JavaFX is a software platform for creating and delivering desktop applications, as well as rich internet applications (RIAs) that can run across a wide variety of devices. It provides a set of graphics and media packages that enable developers to design, create, test, debug, and deploy rich client applications that operate consistently across diverse platforms.

### Maven
Maven is a build automation tool used primarily for Java projects. It addresses two aspects of building software: first, it describes how software is built, and second, it describes its dependencies. Maven uses a Project Object Model (POM) file to manage project dependencies, build configurations, and other project-related tasks.

### Virtual Threads (Project Loom)
Project Loom is an initiative by the OpenJDK community to introduce lightweight, user-mode threads, called virtual threads, to the Java platform. Virtual threads aim to simplify concurrent programming by providing a high-throughput, low-latency threading model that scales efficiently with modern hardware.

### FXML
FXML is an XML-based language used to define the user interface of a JavaFX application. It allows developers to separate the UI design from the application logic, making it easier to maintain and update the UI. FXML files are typically used in conjunction with JavaFX controllers to handle user interactions.

### CSS
Cascading Style Sheets (CSS) is a style sheet language used for describing the presentation of a document written in a markup language like HTML or XML. In JavaFX, CSS is used to style the user interface components, allowing developers to create visually appealing and consistent UIs.

### REST API Service
The application fetches weather data from an external REST API service. This allows the application to provide real-time weather updates by making HTTP requests to the API and processing the JSON responses.

### Jackson
Jackson is a high-performance JSON processor for Java. It is used in this project to parse JSON responses from the REST API into Java objects, making it easier to work with the data.

### Dependency Injection
The project uses dependency injection to manage the creation and injection of dependencies. This helps in creating a more modular and testable codebase by decoupling the creation of objects from their usage.

### Coding to an Interface
The project follows the principle of coding to an interface, which means that the code depends on abstractions (interfaces) rather than concrete implementations. This makes the code more flexible and easier to maintain.

### File Handling
The application includes file handling capabilities to read and write configuration files, such as the `config.properties` file used for storing the API key and default city.

### Configuration
The application uses a `config.properties` file to store configuration settings. This file is read at startup to configure the application with the necessary API key and default city.

### Exception Handling with Custom Exceptions
The project includes custom exception handling to manage errors gracefully. Custom exceptions are used to provide more specific error messages and to handle different types of errors appropriately.

### Localization
The application supports localization in four different languages: English, Spanish, French, and German. Resource bundles are used to provide localized strings for the user interface, allowing users to view the application in their preferred language.


## Setup and Run Instructions

### Prerequisites
Before you begin, ensure you have the following software installed on your system:
- **Java Development Kit (JDK) 17 or higher**: You can download it from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or use an open-source alternative like [AdoptOpenJDK](https://adoptopenjdk.net/).
- **Maven 3.6 or higher**: You can download it from the [Maven website](https://maven.apache.org/download.cgi). Follow the installation instructions provided on the website to set it up on your system.

### Clone the Repository
To get a copy of the project up and running on your local machine, follow these steps:

1. **Open a terminal**: You can use Command Prompt, PowerShell, or any terminal emulator of your choice.
2. **Navigate to the directory** where you want to clone the repository.
3. **Run the following command** to clone the repository:
    ```sh
    git clone https://github.com/yourusername/weatherapp.git
    cd weatherapp
    ```

### Build the Project
Once you have cloned the repository, you need to build the project using Maven. Follow these steps:

1. **Navigate to the project directory** (if you are not already there):
    ```sh
    cd weatherapp
    ```
2. **Run the following command** to clean and build the project:
    ```sh
    mvn clean install
    ```
   This command will download all the necessary dependencies, compile the source code, and package the application.

### Run the Application
After successfully building the project, you can run the application using Maven. Follow these steps:

1. **Run the following command** to start the application:
    ```sh
    mvn javafx:run
    ```
   This command will launch the JavaFX application.

### Configuration
The application requires an API key and a default city to fetch weather data. These configurations are stored in a `config.properties` file. Follow these steps to configure the application:

1. **Locate the `config.properties` file** in the `resources` directory.
2. **Open the file** in a text editor.
3. **Update the following properties** with your own values:
    ```properties
    api.key=your_api_key_here
    default.city=your_default_city_here
    ```
4. **Save the file** and close the text editor.

### Usage
1. **Launch the application** using the run instructions provided above.
2. **Enter your API key and default city** in the settings dialog.
3. **Save the settings** to apply the changes.
4. **Enter a city name** in the main interface to fetch and display the weather forecast.

### License
This project is licensed under the MIT License.