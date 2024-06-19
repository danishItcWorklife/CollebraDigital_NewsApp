# Android - Clean Architecture - Kotlin
This repository aims to apply Clean Architecture principles to Android development. It includes a News Application that fetches current news from the https://newsapi.org/ API.

## Clean Architecture
https://blog.cleancoder.com/uncle-bob/images/2012-08-13-the-clean-architecture/CleanArchitecture.jpg
Clean architecture emphasizes separation of concerns, promoting loosely coupled code for enhanced testability and flexibility. The project is structured into three main modules: presentation, data, and domain.

* __Presentation__: This layer utilizes the Android Framework, MVVM pattern, and DI module. It depends on the domain for accessing use cases and on DI for dependency injection.
* __Domain__: This layer encapsulates business logic and contains use cases responsible for invoking the appropriate repository or data source.
* __Data__: Responsible for selecting the correct data source for the domain layer, this module implements repositories declared in the domain. It manages tasks such as checking if data in a database is up to date and fetching it from a service if necessary.

## Functionality
The app offers:
1. Biometric authentication for user fingerprint login.
2. Retrieval of current news data from https://newsapi.org/ and display using `LazyColumn` based on news category.
3. Offline functionality that fetches data from the local database when there is no internet connection.

## Architecture
The app follows clean architecture principles using the MVVM (Model-View-ViewModel) design pattern. MVVM ensures better separation of concerns, easier testing, and lifecycle awareness.

### UI
This app utilizes Jetpack Compose for building the UI.

### Model
The model is generated from JSON data into Kotlin data classes. Additionally, entity classes are used for Room database with type converters for custom object data storage and retrieval.

### ViewModel
`MainViewModel.kt` is utilized for fetching today's news and communicates network call status (Loading, Success, Error) using a sealed class.

### Dependency Injection
The app employs Dagger Hilt for dependency injection. The `ApplicationModule.kt` class provides singleton references for Retrofit, OkHttpClient, Repository, etc.

### Network
The network layer consists of the Repository and ApiService:
- `NewsApi`: An interface containing suspend functions for Retrofit API calls.
- `NewsRepositoryImpl`: Implements the remote/local repository calls.

## Building
To successfully run and test the application, an API key is required:
1. Visit https://newsapi.org/ and click on `Get Api Key`.
2. Define the API Key in gradle.properties:
    - Open gradle.properties in your project's root.
    - Add `API_KEY=your_actual_api_key_here`.
3. Access the API Key in build.gradle:
    - Use `buildConfigField("String", "API_KEY", "\"${API_KEY}\"")`.
4. Sync Project with Gradle by clicking `Sync Now` after making updates.

To run the project, open it in Android Studio and click `Run`.
Android Studio version used for building the project: Android Studio Iguana | 2023.2.1.

The Gradle plugin used in the project requires Java 17.0.9 to run. Set the Gradle JDK in `Preferences -> Build Tools -> Gradle -> Gradle JDK`.

The project utilizes [Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) build scripts for managing dependencies and plugins.

## Tech Stack
Here's a list of dependencies used in the project:

---

### Dependencies

1. [AndroidX Core KTX](https://developer.android.com/jetpack/androidx/releases/appcompat) - Core AndroidX library with Kotlin extensions.
2. [JUnit](https://developer.android.com/training/testing/junit) - Framework for writing repeatable tests.
3. [AndroidX JUnit](https://developer.android.com/jetpack/androidx/releases/test) - AndroidX test libraries for JUnit.
4. [Espresso](https://developer.android.com/training/testing/espresso) - UI testing framework.
5. [AndroidX Lifecycle Runtime KTX](https://developer.android.com/jetpack/androidx/releases/lifecycle) - AndroidX Lifecycle Library extensions for Kotlin.
6. [AndroidX Activity Compose](https://developer.android.com/jetpack/androidx/releases/activity) - AndroidX Activity library for Jetpack Compose.
7. [AndroidX Compose BOM](https://developer.android.com/jetpack/compose/setup) - Bill of Materials for managing Jetpack Compose dependencies.
8. [AndroidX Compose UI](https://developer.android.com/jetpack/compose) - UI library for Jetpack Compose.
9. [AndroidX Compose UI Test](https://developer.android.com/jetpack/compose/testing) - UI testing library for Jetpack Compose.
10. [AndroidX Biometric](https://developer.android.com/training/sign-in/biometric-auth) - Biometric authentication library.
11. [AndroidX Compose Material3](https://developer.android.com/jetpack/compose/material3) - Material Design components for Jetpack Compose.
12. [Room Persistence Library](https://developer.android.com/training/data-storage/room) - SQLite object mapping library.
13. [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency injection library for Android.
14. [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client for Android and Java.
15. [OkHttp](https://square.github.io/okhttp/) - HTTP client for Android and Java applications.
16. [Gson](https://github.com/google/gson) - Library for converting Java Objects into their JSON representation and vice versa.
17. [AndroidX Arch Core Testing](https://developer.android.com/training/testing/fundamentals) - Testing library for AndroidX Architecture Components.
18. [ViewModel Compose](https://developer.android.com/topic/libraries/architecture/viewmodel) - ViewModel library for Jetpack Compose.
19. [Lifecycle Extensions](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Extensions for AndroidX Lifecycle.
20. [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - ViewModel library for AndroidX Lifecycle.
21. [Lifecycle LiveData](https://developer.android.com/training/testing/fundamentals) - LiveData extensions for AndroidX Lifecycle.
22. [Lifecycle Runtime](https://developer.android.com/training/testing/fundamentals) - Runtime components for AndroidX Lifecycle.
23. [Kotlin Coroutines Test](https://developer.android.com/training/testing/fundamentals) - Testing library for Kotlin Coroutines.
24. [Kotlin Coroutines Android](https://developer.android.com/training/testing/fundamentals) - Android library for Kotlin Coroutines.
25. [Kotlin Coroutines Core](https://developer.android.com/training/testing/fundamentals) - Core library for Kotlin Coroutines.
26. [Mockito Core](https://developer.android.com/training/testing/unit-testing) - Core library for Mockito.
27. [Mockk](https://developer.android.com/training/testing/fundamentals) - Mocking library for Kotlin.