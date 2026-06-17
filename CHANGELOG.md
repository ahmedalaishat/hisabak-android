# Changelog

All notable changes to Hisabak are documented here. Format based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/); this project follows
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- First-launch onboarding: an animated, premium walkthrough of the app's features
  (SMS auto-capture, on-device privacy, budgets & alerts, insights) ending with an
  SMS-permission primer.

### Changed
- Disabled Android auto/cloud backup so financial data truly never leaves the device,
  matching the privacy promise. Trade-off: no automatic restore on reinstall or device
  transfer.
- Release builds no longer wipe the database on an unexpected schema change — the Room
  schema is now exported and migrations are required, protecting on-device history. (Debug
  builds keep the destructive fallback for fast iteration.)

### Fixed
- Amounts with certain cents (e.g. `19.99`) were stored a cent short due to floating-point
  truncation; they now round correctly. Affects manual entry, SMS-parsed amounts, and
  category limits.
- Budget alerts now fire reliably for transactions captured from SMS while the app is closed,
  instead of occasionally being skipped when the process was reclaimed in the background.
- Deleting a brand that can't be removed now shows a message instead of doing nothing
  silently.
- An auto-captured SMS with no date in its text is now dated when the message arrived instead
  of the moment it was processed.
- A redelivered bank SMS (same text and time) is no longer imported twice.

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
