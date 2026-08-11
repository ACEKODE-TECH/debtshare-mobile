# Debtshare - KMP

**Split bills, no friendships** 🎉

![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![KMP](https://img.shields.io/badge/KMP-Multiplatform-grey?style=flat&logo=kotlin&logoColor=white&labelColor=7F52FF)
![Android](https://img.shields.io/badge/Android-SDK%2034-grey?style=flat&logo=android&logoColor=white&labelColor=3DDC84)
![AGP](https://img.shields.io/badge/AGP-9.3.1-grey?style=flat&labelColor=3DDC84)
![Swift](https://img.shields.io/badge/Swift-5.10%2B-grey?style=flat&logo=swift&logoColor=white&labelColor=FA7343)
![iOS](https://img.shields.io/badge/iOS-17%2B-grey?style=flat&logo=ios&logoColor=white&labelColor=000000)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.11.1-grey?style=flat&logo=jetpackcompose&logoColor=white&labelColor=4285F4)
![JUnit](https://img.shields.io/badge/JUnit5-1.3.0-grey?style=flat&logo=junit5&logoColor=white&labelColor=25A318)
![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.15-grey?style=flat&labelColor=25A318)
[![Coverage](https://img.shields.io/codecov/c/github/ACEKODE-TECH/debtshare-mobile/main?style=flat&logo=codecov&logoColor=white&labelColor=f01f7a&color=grey)](https://codecov.io/gh/ACEKODE-TECH/debtshare-mobile)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-grey?style=flat&logo=githubactions&logoColor=white&labelColor=2088FF)
![Develocity](https://img.shields.io/badge/Develocity-Build%20Cache-grey?style=flat&logo=gradle&logoColor=white&labelColor=02303A)
![Firebase Crashlytics](https://img.shields.io/badge/Firebase-Crashlytics-grey?style=flat&logo=firebase&logoColor=white&labelColor=FFCA28)
![Firebase App Distribution](https://img.shields.io/badge/Firebase-App%20Distribution-grey?style=flat&logo=firebase&logoColor=white&labelColor=FFCA28)
![License](https://img.shields.io/badge/License-Proprietary-grey?style=flat&labelColor=red)

## Overview

Debtshare is a cross-platform mobile application built with **Kotlin Multiplatform (KMP)** that makes it easy to split and manage shared expenses. Whether it's trip costs, restaurant bills, shared services, or any group expense, Debtshare simplifies calculating who owes whom.

## 🏗️ Architecture

The project follows a modular approach with Kotlin Multiplatform, enabling code sharing between platforms:

```
debtshare-mobile/
├── app/                    # Shared code (business logic)
│   ├── src/
│   │   ├── commonMain/    # Common code for iOS and Android
│   │   ├── androidMain/   # Android-specific code
│   │   └── iosMain/       # iOS-specific code
│   └── build.gradle.kts
│
├── androidApp/             # Android-specific application
│   ├── src/
│   └── build.gradle.kts
│
├── iosApp/                 # iOS-specific application
│   ├── Sources/
│   └── iosApp.xcodeproj
│
└── build.gradle.kts       # Project configuration
```

### Modular Structure

- **`app`**: Shared module containing:
  - Business logic
  - Data models
  - Repositories and services
  - Validation and utilities
  - Common unit tests

- **`androidApp`**: Android-specific application containing:
  - UI with Jetpack Compose Multiplatform
  - Android-specific API integration
  - Android lifecycle management

- **`iosApp`**: iOS-specific application containing:
  - UI with SwiftUI / Compose Multiplatform
  - iOS-specific API integration
  - Native iOS management

## 🛠️ Technologies

- **Kotlin Multiplatform (KMP)**: Shared code between Android and iOS
- **Compose Multiplatform**: Cross-platform declarative UI
- **Android**: Native support for Android devices
- **Swift / iOS**: Native support for iOS devices
- **JUnit**: Unit testing framework
- **JaCoCo**: Code coverage measurement (threshold: 90%)
- **CodeCov**: Code coverage tracking and reporting
- **GitHub Actions**: CI/CD automation
- **Gradle Build Cache**: Build performance optimization
- **Develocity**: Build insights and caching
- **Firebase Crashlytics**: Real-time crash reporting to monitor and analyze crashes in production
- **Firebase App Distribution**: Distributes debug and release builds to testers

## 🚀 Quick Start

### Prerequisites

- **JDK 17+**
- **Kotlin 2.4.10+**
- **Android SDK 34+** (AGP 9.3.1+)
- **Xcode 15+** (for iOS)
- **CocoaPods** (for iOS dependency management)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/ACEKODE-TECH/debtshare-mobile.git
   cd debtshare-mobile
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run tests**
   ```bash
   ./gradlew test
   ```

### Run on Specific Platform

#### Android

```bash
./gradlew :androidApp:run
```

#### iOS

```bash
cd iosApp
pod install
open iosApp.xcworkspace
# Build in Xcode
```

## 📋 Key Features

- ✅ **Create expense groups** for shared bills
- ✅ **Record expenses** with details and participants
- ✅ **Auto-calculate** who owes whom
- ✅ **Full transaction history**
- ✅ **Settle accounts** between friends
- ✅ **Cross-device synchronization**

## ✅ Testing & Validation

### Unit Tests

Tests are automatically executed as part of the CI/CD pipeline:

```bash
./gradlew test
```

### Code Coverage

The project maintains a code coverage threshold of **90%**:

```bash
./gradlew test --no-parallel
```

Coverage is automatically reported to **CodeCov** on every push through GitHub Actions workflows.

### Validation Workflows

The project uses **GitHub Actions** for continuous validation:

#### 🔄 CI/CD Pipeline

The following workflows run automatically:

1. **Build & Test**: Compiles the project and runs unit tests
   - Validates code compilation
   - Executes all unit tests
   - Verifies code formatting

2. **Code Coverage**: Verifies code coverage
   - Runs tests with coverage metrics
   - Reports results to CodeCov
   - Validates 90% threshold is maintained

3. **Android Build**: Generates production APK/AAB
   - Compiles Android application
   - Runs Android-specific validations

4. **iOS Build**: Validates iOS compilation
   - Compiles iOS framework
   - Runs iOS-specific validations

These workflows run on:
- ✅ Every push to `main` and `develop`
- ✅ Every Pull Request
- ✅ Every release tag

## 🏷️ Versioning

The app version is automatically extracted from Git tags and used for both development builds (Android Studio) and CI/CD pipelines.

### Release Flow

1. **Trigger Release**: Run `develop-to-main` workflow (manual dispatch)
   - Select bump type: `patch`, `minor`, or `major`
   - Workflow creates a Release PR from `develop` → `main`

2. **Merge to Main**: Merge the Release PR with a merge commit
   - PR title contains the proposed version (e.g., `Release v1.2.3`)

3. **Automatic Tagging**: Once merged, `create-main-tag` workflow automatically:
   - Creates a Git tag (e.g., `v1.2.3`) on `main`

4. **Build & Deploy**: All builds (local, Android Studio, CI/CD) automatically:
   - Read the latest Git tag
   - Use it as the app version
   - Support configuration cache

**Note**: Tags follow the format `v1.2.3` (the `v` prefix is automatically removed). If no tags exist, the app defaults to version `1.0.0`.

## 📝 License

This project is **proprietary** and **not for public use**. All rights reserved.

## 👥 Authors

**ACEKODE TECH** - [GitHub Organization](https://github.com/ACEKODE-TECH)

## 📞 Contact

For questions or suggestions, please open an issue in the repository or contact us at [info@acekode.com](mailto:info@acekode.com)

---

**Split bills, no friendships** 🎉
