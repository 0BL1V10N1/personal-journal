# 📔 Personal Journal

**Personal Journal** is a lightweight, modern Android application written in Kotlin. It allows users to create, date, and tag short journal entries in a clean and simple interface.

---

## ✨ Features

*   **📝 Create Entries**: Write entries with a title and content.
*   **🏷️ Tagging System**: Organize entries with removable chips (tags).
*   **📅 Date Picker**: Select dates easily using `MaterialDatePicker`.
*   **🎨 Material Design**: Built with Material Components for a modern look and feel.
---

## 🛠 Tech Stack & Libraries

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **Android SDK**: `compileSdk 36`, `minSdk 26`
*   **Code Quality**:
    *   🕵️ [Detekt](https://detekt.dev/) (Static Analysis)
    *   🧹 [KtLint](https://pinterest.github.io/ktlint/) (Formatting)

---

## ✅ Requirements

*   **JDK**: Version 17 (Project target: JVM 17)
*   **Android SDK**: API Level 36
*   **IDE**: Android Studio (Recommended)

---

## 🚀 Quick Start

1.  **Clone the repository**:
    ```bash
    git clone <repo-url>
    cd personal-journal
    ```

2.  **Open in Android Studio**:
    *   Select `Open` and navigate to the project folder.
    *   Allow Gradle to sync.

3.  **Run the app**:
    *   Connect a device or start an emulator.
    *   Click the **Run** button (▶️) or run:
        ```powershell
        .\gradlew.bat installDebug
        ```

---

## 💻 Command Line (PowerShell)

Use the included Gradle wrapper for consistent results.

| Task | Command | Description |
| :--- | :--- | :--- |
| **Build APK** | `.\gradlew.bat assembleDebug` | Builds the debug APK. |
| **Format Code** | `.\gradlew.bat ktlintFormat` | Formats all Kotlin files. |
| **Check Format**| `.\gradlew.bat ktlintCheck` | Checks formatting without changing files. |
| **Analyze** | `.\gradlew.bat detekt` | Runs static analysis for bugs/smells. |

---

## 🧹 Code Style & Quality

To ensure code consistency across all contributors:

1.  **Iterate with `.editorconfig`**: The root `.editorconfig` ensures consistent indentation and styling.
2.  **Linting**: We use **KtLint** for style enforcement and **Detekt** for deeper analysis.
3.  **Pre-commit**: It is recommended to run `./gradlew ktlintFormat` before pushing your code.

### Recommended Plugins (IntelliJ / Android Studio)
*   🟢 **Kotlin**
*   🤖 **Android**
*   🔍 **Detekt**
*   🧹 **KtLint**

---

## 🤝 Contributing

Contributions are welcome!

1.  Fork the project.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  **Run checks**: Make sure `.\gradlew.bat detekt` and `.\gradlew.bat ktlintCheck` pass.
4.  Commit your changes.
5.  Push to the branch.
6.  Open a Pull Request.


