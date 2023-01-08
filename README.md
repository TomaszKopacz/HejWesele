# Android Template

(Not so) simple template for Android projects at Miquido. The goal is to reduce time from jumping into a new project to
the first line of project-related code. The template collects best practices for dealing with various common tasks
and issues that occur in Android projects.

## Features

1. Clean multi-module Gradle setup
1. Extracted dependencies and versions
1. Basic build types configuration
1. Basic R8 configuration
1. Static code analysis with detekt, ktlint and Android Lint
1. Unit tests setup with junit4, mockito-kotlin and Truth
1. Instrumented unit tests with Hilt injection support
1. UI tests
1. Test suites
1. Code coverage setup
1. Detecting memory leaks with LeakCanary
1. Checking licenses of dependencies with Licensee
1. Continuous integration setup
1. Publishing to Firebase App Distribution
1. Running tests on Firebase Test Lab  
1. Publishing to Play Store
1. Firebase configuration
1. Firebase Analytics
1. Firebase Crashlytics
1. Firebase Performance Monitoring
1. Firebase Remote Config
1. Firebase Cloud Messaging
1. Recommended modules structure
1. Test environments configuration
1. Dependency injection (Hilt)
1. Networking (OkHttp + Retrofit + EitherNet)
1. JSON (de)serialization (Moshi)
1. Encrypted local database (SqlDelight + SqlCipher + AndroidX Security-Crypto)
1. Plain local key-value storage (AndroidX DataStore)
1. Schedule work (AndroidX WorkManager)
1. Custom Tabs
1. Square Logcat
1. DateTime provider and formatter (kotlinx-datetime)
1. Highly modular app architecture based on Jetpack Compose, MVVM (AndroidX ViewModel), Coroutines and AndroidX Navigation

## Documentation

- [Getting started](./docs/gettingstarted.md)
- [Build system, testing, quality](./docs/buildsystem-testing-quality.md)
- [Firebase](./docs/firebase.md)
- [Modules structure](./docs/modules.md)
- [Core and common functionalities](./docs/core-and-common-functionalities.md)
- [Architecture](./docs/architecture.md)
- [Screens and flow](./docs/screens-and-flow.md)
- [Contributing](./CONTRIBUTING.md)

## Check it out

To download the application, visit [this link](https://appdistribution.firebase.dev/i/faf06fea47324e52) on your device and proceed 
with the provided instruction.

## Resources

- [Jira](https://miquido.atlassian.net/browse/ANDT)
