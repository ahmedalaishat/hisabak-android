# Changelog

All notable changes to Hisabak are documented here. Format based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/); this project follows
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Automated `/feature` pipeline: turns a high-level requirement into a reviewed,
  tested, documented PR against `develop` (spec → design → code+tests → QA → docs → PR).
- JVM unit-test safeguard (77 tests) covering domain logic and ViewModels, with a
  Stop hook and GitHub Actions CI enforcing it on every change.

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
