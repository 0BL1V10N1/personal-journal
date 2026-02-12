# Contributing to Personal Journal

Thank you for your interest in contributing! Here's how to get started.

## Getting Started

1. Fork the repository
2. Clone your fork locally
3. Create a feature branch: `git checkout -b feature/my-feature`
4. Make your changes
5. Push to your fork and open a Pull Request

## Development Setup

- **JDK**: Version 17
- **IDE**: Android Studio (recommended)
- Open the project in Android Studio and allow Gradle to sync

## Code Quality

Before submitting a PR, ensure the following checks pass:

```bash
./gradlew ktlintCheck   # Code formatting
./gradlew detekt        # Static analysis
./gradlew testDebugUnitTest  # Unit tests
```

You can auto-format code with:

```bash
./gradlew ktlintFormat
```

## Pull Request Guidelines

- Keep PRs focused — one feature or fix per PR
- Follow the existing code style (enforced by ktlint and detekt)
- Include a clear description of the change
- Add or update tests when applicable
- Make sure CI passes before requesting review

## Reporting Issues

- Use the [Bug Report](https://github.com/0BL1V10N1/personal-journal/issues/new?template=bug_report.yml) template for bugs
- Use the [Feature Request](https://github.com/0BL1V10N1/personal-journal/issues/new?template=feature_request.yml) template for enhancements

## License

By contributing, you agree that your contributions will be licensed under the [MIT License](../LICENSE).
