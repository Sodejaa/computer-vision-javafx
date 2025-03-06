## Author

Jaani Söderström

# Computer Vision App

A JavaFX application for real-time face and hand gesture detection using OpenCV. Supports webcam input and video file processing.

## Features

- Real-time face detection using OpenCV
- JavaFX-based GUI
- Webcam integration with `webcam-capture` library
- Switch betweenw **webcam feed** and **video input**

## Prerequisites

Ensure you have the following installed:

- **Java 19** or later
- **Maven**
- **JavaFX SDK** (Configured via Maven dependencies)
- **OpenCV** (Handled via Maven dependency)

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/Sodejaa/computer-vision-javafx.git
   cd computer-vision-javafx
   ```
2. Build the project using Maven:
   ```sh
   mvn clean install
   ```

## Running the Application

### **Using Command Line**

Run the following command from the project root:

```sh
mvn clean javafx:run
```

### **Using IntelliJ IDEA**

1. Open **Run → Edit Configurations**
2. Add a **Maven** or **Shell Script** configuration with:
   ```sh
   mvn clean javafx:run
   ```
3. Click **Run**

## Technologies Used

- **JavaFX** - UI framework
- **OpenCV** - Computer vision library
- **Maven** - Dependency management
- **webcam-capture** - Webcam integration

## Project Structure

```
computer-vision-fx/
│-- src/main/java/org/example/
│   |-- capture/
│   |-- │-- CaptureSource.java
│   |-- │-- CaptureWebcam.java
│   |-- │-- CaptureVideo.java
│   |-- controller/
│   |-- │-- CVFxController.java
│   │-- CVFxApp.java
│-- src/main/resources/
│   │-- haarcascade_frontalface_alt.xml
│   │-- steve-jobs-explaining-apps.mp4
│-- Dockerfile
│-- .gitignore
│-- pom.xml
│-- README.md

```

## Troubleshooting

### **"Module javafx.controls not found"**

Ensure you are running with Maven:

```sh
mvn clean javafx:run
```

### **"Error loading Haar cascade file!"**

Check that `haarcascade_frontalface_alt.xml` exists in `src/main/resources/`.

Sourc: https://geithub.com/opencv/opencv/blob/master/data/haarcascades/haarcascade_frontalface_alt.xml

## License

This project is licensed under the MIT License.

---
