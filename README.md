<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Personal Journal Logo" width="120" />
</p>

<h1 align="center">📔 Personal Journal</h1>

<p align="center">
  <strong>A lightweight, modern Android journal app — written in Kotlin.</strong><br/>
  Create, date, and tag short journal entries in a clean and simple interface.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Android-API%2026+-3DDC84?logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Material%20Design-3-757575?logo=materialdesign&logoColor=white" alt="Material Design" />
  <img src="https://img.shields.io/github/license/0BL1V10N1/personal-journal.svg" alt="License" />
</p>

## 🎬 Demo

<p align="center">
    <video src="https://github.com/user-attachments/assets/0a6dbbaf-9d34-4d95-943c-4dfba0ed6e49" width="300" controls></video>
</p>

## ✨ Features

|     | Feature             | Description                                               |
|-----|---------------------|-----------------------------------------------------------|
| 📝  | **Create Entries**  | Write entries with a title and content                    |
| 🏷️ | **Tagging System**  | Organize entries with removable chips (tags)              |
| 📅  | **Date Picker**     | Select dates easily using `MaterialDatePicker`            |
| 🎨  | **Material Design** | Built with Material Components for a modern look and feel |

## 🛠 Tech Stack & Libraries

| Category            | Details                                          |
|:--------------------|:-------------------------------------------------|
| **Language**        | [Kotlin](https://kotlinlang.org/)                |
| **Android SDK**     | `compileSdk 36` · `minSdk 26`                    |
| **Static Analysis** | 🕵️ [Detekt](https://detekt.dev/)                |
| **Formatting**      | 🧹 [KtLint](https://pinterest.github.io/ktlint/) |

## 🚀 Getting Started

- ### ✅ Requirements

  | Requirement | Version                      |
  |:------------|:-----------------------------|
  | JDK         | 17                           |
  | Android SDK | API Level 36                 |
  | IDE         | Android Studio (recommended) |

- ### 📥 Installation

    ```bash
    git clone https://github.com/0BL1V10N1/personal-journal.git
    cd personal-journal
    ```

- ### ▶️ Run the App

    - Open in **Android Studio** → `Open` → select project folder → let Gradle sync
    - Connect a device or start an emulator
    - Click the **Run** button (▶️) or run:

    	```powershell
    	.\gradlew.bat installDebug
    	```

## 💻 Command Line (PowerShell)

- ### 🔨 Build & Run

  | Task               | Command                       | Description          |
  |:-------------------|:------------------------------|:---------------------|
  | 🔨 **Build APK**   | `.\gradlew.bat assembleDebug` | Builds the debug APK |
  | ▶️ **Install App** | `.\gradlew.bat installDebug`  | Installs on device   |

- ### 🧹 Code Quality

  | Task                | Command                      | Description                              |
  |:--------------------|:-----------------------------|:-----------------------------------------|
  | ✏️ **Format Code**  | `.\gradlew.bat ktlintFormat` | Formats all Kotlin files                 |
  | 🔎 **Check Format** | `.\gradlew.bat ktlintCheck`  | Checks formatting without changing files |
  | 🧪 **Analyze**      | `.\gradlew.bat detekt`       | Runs static analysis for bugs/smells     |

## 🤝 Contributing

Contributions are welcome! Here's how to get started:

1. **Fork** the project
2. **Create** your feature branch — `git checkout -b feature/AmazingFeature`
3. **Run checks** — Make sure `.\gradlew.bat detekt` and `.\gradlew.bat ktlintCheck` pass
4. **Commit** your changes
5. **Push** to the branch
6. **Open** a Pull Request

## 👥 Contributors

<a href="https://github.com/0BL1V10N1/personal-journal/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=0BL1V10N1/personal-journal" />
</a>

---

<p align="center">
  Made with ❤️ in Kotlin
</p>


