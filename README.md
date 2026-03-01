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
  <img src="https://img.shields.io/badge/License-MIT-blue" alt="License" />
</p>

---

## 🎬 Demo

<p align="center">
  <video src="https://github.com/user-attachments/assets/606ada91-2eb2-472d-b6e8-0d3efcf42907" width="300" controls></video>
</p>

> *If the video doesn't load in your browser, you
can [download it directly](https://github.com/user-attachments/assets/606ada91-2eb2-472d-b6e8-0d3efcf42907).*

---

## ✨ Features

|     | Feature             | Description                                               |
|-----|---------------------|-----------------------------------------------------------|
| 📝  | **Create Entries**  | Write entries with a title and content                    |
| 🏷️ | **Tagging System**  | Organize entries with removable chips (tags)              |
| 📅  | **Date Picker**     | Select dates easily using `MaterialDatePicker`            |
| 🎨  | **Material Design** | Built with Material Components for a modern look and feel |

---

## 🛠 Tech Stack & Libraries

| Category            | Details                                          |
|:--------------------|:-------------------------------------------------|
| **Language**        | [Kotlin](https://kotlinlang.org/)                |
| **Android SDK**     | `compileSdk 36` · `minSdk 26`                    |
| **Static Analysis** | 🕵️ [Detekt](https://detekt.dev/)                |
| **Formatting**      | 🧹 [KtLint](https://pinterest.github.io/ktlint/) |

---

## 🚀 Getting Started

### ✅ Requirements

| Requirement | Version                      |
|:------------|:-----------------------------|
| JDK         | 17                           |
| Android SDK | API Level 36                 |
| IDE         | Android Studio (recommended) |

### 📥 Installation

```bash
# 1. Clone the repository
git clone <repo-url>
cd personal-journal
```

### ▶️ Run the App

1. Open in **Android Studio** → `Open` → select project folder → let Gradle sync
2. Connect a device or start an emulator
3. Click the **Run** button (▶️) or run:

```powershell
.\gradlew.bat installDebug
```

---

## 💻 Command Line (PowerShell)

Use the included Gradle wrapper for consistent results.

### 🔨 Build & Run

| Task               | Command                       | Description          |
|:-------------------|:------------------------------|:---------------------|
| 🔨 **Build APK**   | `.\gradlew.bat assembleDebug` | Builds the debug APK |
| ▶️ **Install App** | `.\gradlew.bat installDebug`  | Installs on device   |

### 🧹 Code Quality

| Task                | Command                      | Description                              |
|:--------------------|:-----------------------------|:-----------------------------------------|
| ✏️ **Format Code**  | `.\gradlew.bat ktlintFormat` | Formats all Kotlin files                 |
| 🔎 **Check Format** | `.\gradlew.bat ktlintCheck`  | Checks formatting without changing files |
| 🧪 **Analyze**      | `.\gradlew.bat detekt`       | Runs static analysis for bugs/smells     |

---

## 🧹 Code Style & Quality

> To ensure code consistency across all contributors:

### 📏 Rules

1. **`.editorconfig`** — Ensures consistent indentation and styling.
2. **Linting** — **KtLint** for style enforcement, **Detekt** for deeper analysis.
3. **Pre-commit** — Run `.\gradlew.bat ktlintFormat` before pushing your code.

### 📦 Recommended Plugins (IntelliJ / Android Studio)

<details>
<summary>See plugins list</summary>

- 🟢 **Kotlin**
- 🤖 **Android**
- 🔍 **Detekt**
- 🧹 **KtLint**

</details>

---

## 🤝 Contributing

Contributions are welcome! Here's how to get started:

1. **Fork** the project
2. **Create** your feature branch — `git checkout -b feature/AmazingFeature`
3. **Run checks** — Make sure `.\gradlew.bat detekt` and `.\gradlew.bat ktlintCheck` pass
4. **Commit** your changes
5. **Push** to the branch
6. **Open** a Pull Request

---

<p align="center">
  Made with ❤️ in Kotlin
</p>


