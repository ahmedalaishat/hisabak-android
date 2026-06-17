# Changelog

All notable changes to Hisabak are documented here. Format based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/); this project follows
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.0]

### Added
- Separate `staging` and `production` build flavors (`com.hisabak.staging` /
  `com.hisabak`) so both can be installed side by side; staging is labelled "Hisabak STG".
- CI/CD: GitHub Actions run the unit suite on every PR, distribute staging builds to
  testers via Firebase App Distribution, and publish production releases to Google Play.
- JVM unit-test safeguard (77 tests) covering domain logic and ViewModels.

### Changed
- Production installs no longer seed demo data — they start empty (demo data is
  staging-only).

### Fixed
- SMS template parsing treated a template's trailing `.` as a regex wildcard, which
  dropped the imported transaction's time-of-day (it defaulted to midnight).

## [1.1.0]

### Added
- Premium UI polish and category-limit notifications.

## [1.0.0]

### Added
- Initial release: SMS-driven transaction capture, categorization, budgets, and
  financial trend visualizations.
