# Changelog

All notable changes to Hisabak are documented here. Format based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/); this project follows
[Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- A fresh install now starts with a small set of starter categories (Salary, Groceries,
  Dining, Transport, Shopping, Savings, Investments) so you can record a transaction right
  away. They're ordinary categories you can rename or delete.

### Fixed
- Capturing a transaction by sharing or selecting bank-SMS text now completes reliably even
  if the brief capture screen closes before the save finishes.
- Amount fields now accept a comma as the decimal separator, so entering cents works on
  keyboards that use `,` (e.g. many non-English locales).

## [1.3.0] — 2026-06-18

### Added
- First-launch onboarding: an animated, premium walkthrough of the app's features
  (SMS auto-capture, on-device privacy, budgets & alerts, insights) ending with an
  SMS-permission primer.
- Transaction-recorded confirmation: when a bank SMS is captured and saved, a notification
  confirms it with the amount and brand. A categorized brand shows its category name and
  glyph and taps through to that category on the dashboard; an uncategorized brand says so
  and taps through to its editor so you can categorize it.
- Add a transaction by **sharing** a bank SMS into Hisabak, or by **selecting its text** and
  tapping Hisabak in the selection menu — both run the same parser as auto-capture. These need
  no permissions and work alongside the existing manual paste.

### Changed
- SMS **auto-capture** (reading bank texts in the background) now ships in the staging build
  only; the Play build is SMS-free to comply with Google Play's restricted-permission policy and
  captures transactions by sharing a bank SMS, selecting its text, or pasting it. Onboarding and
  the SMS tab adapt their copy and drop the SMS-permission prompt accordingly.
- Disabled Android auto/cloud backup so financial data truly never leaves the device,
  matching the privacy promise. Trade-off: no automatic restore on reinstall or device
  transfer.
- Release builds no longer wipe the database on an unexpected schema change — the Room
  schema is now exported and migrations are required, protecting on-device history. (Debug
  builds keep the destructive fallback for fast iteration.)
- Amounts are now shown compactly everywhere: thousands as `K`, millions as `M` (both to
  two decimals) — e.g. `AED 990,853` reads as `990.85K`. Applies to the dashboard, stat
  cards, transactions, charts, and budgets. The amount you type when editing stays exact.

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
- Removed a non-functional "See all" link from the brands list header (it did nothing; the
  list already shows every brand).
- The SMS "auto-import is disabled" card no longer cramps its title (it was breaking
  "Auto-import" mid-word); the badge and Enable button now sit on their own row.

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
