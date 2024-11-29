# 📱 Xplore Android App

Xplore is a feature-rich quiz application built for Android, designed to provide a seamless and engaging experience for users to answer quiz questions and view their scores. Powered by Kotlin, MVVM architecture, and modern libraries like Retrofit and Hilt, it communicates with the XploreAPI for fetching quiz data and submitting answers.

## 🚀 Features

- **Dynamic Quiz Questions**: Fetch questions from the XploreAPI and display them with real-time interactions.
- **Categorized Questions**: Questions grouped by difficulty levels (easy, medium, hard).
- **Interactive UI**: User-friendly design with engaging visuals and animations.
- **Score Tracking**: View quiz scores instantly with detailed feedback.
- **Offline Support**: Cache data locally for improved performance.
- **Dependency Injection**: Powered by Hilt for streamlined dependency management.

## 🛠️ Requirements

- **Android Studio**: Latest version recommended.
- **Minimum SDK**: 26
- **Kotlin**: Version 1.9 or later

## 📦 Installation

1. Clone the repository:
 ```bash
 git clone https://github.com/your-username/XploreAndroidApp.git
 cd XploreAndroidApp
 ```
2.	Open the project in Android Studio.
3.	Sync the Gradle files to download dependencies:
```bash
./gradlew sync
```
4.	Set up the API URL in the local.properties or as a configuration:
```bash
API_URL=https://your-xplore-api-url.com
```
5.	Build and run the project on an emulator or device.

## 📄 Architecture

The Xplore app follows the MVVM (Model-View-ViewModel) architecture for clean code structure and easy maintenance:
- **Model**: Handles the data layer and interacts with the XploreAPI.
- **View**: Provides the UI layer to display questions and scores.
- **ViewModel**: Manages UI-related data and interacts with the repository.

## 🛠️ Built With

- **Kotlin**: Modern, concise, and type-safe programming language.
- **MVVM Architecture**: Clear separation of concerns for better scalability.
- **Retrofit**: For network requests to the XploreAPI.
- **Hilt**: Dependency injection framework for streamlined DI.
- **Jetpack** Components: LiveData, ViewModel, and Navigation.

## 🧩 Key Modules

- **Networking**: Retrofit is configured with dynamic endpoints for flexibility.
- **Repository Pattern**: Centralized data source for clean and testable logic.
- **UI Components**: Activities, Fragments, and Views optimized for engagement.
- **Data Models**: Kotlin data classes for question and score handling.

## 🤝 Contributing

1.	Fork the repository.
2.	Create a new branch:
```bash
git checkout -b feature-name
```
3.	Make your changes and commit them:
```bash
git commit -m "Add feature-name"
```
4.	Push to the branch:
```bash
git push origin feature-name
```
5.	Create a pull request.

## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.
