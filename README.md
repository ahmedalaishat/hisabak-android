<p align="center">
  <img src="docs/app-icon.svg" width="96" height="96" alt="Hisabak app icon">
</p>

<h1 align="center">Hisabak</h1>

<p align="center">
  A personal finance tracker for Android that turns your bank SMS alerts into a clean,
  organized view of your money — categorize spending, set monthly budgets, and get notified
  before you overshoot.
</p>

<p align="center">
  <a href="../../actions/workflows/test.yml"><img src="../../actions/workflows/test.yml/badge.svg" alt="CI"></a>
  <a href="../../releases"><img src="https://img.shields.io/github/v/release/ahmedalaishat/hisabak-android?color=0B7A5B&label=release" alt="Release"></a>
  <img src="https://img.shields.io/badge/platform-Android-3DDC84" alt="Platform">
  <img src="https://img.shields.io/badge/minSdk-29-blue" alt="Min SDK">
  <img src="https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF" alt="Kotlin">
  <img src="https://img.shields.io/badge/license-MIT-green" alt="License">
</p>

---

## Screenshots

<table>
  <tr>
    <td align="center"><img src="docs/screenshots/dashboard.png" width="220"><br><sub>Dashboard</sub></td>
    <td align="center"><img src="docs/screenshots/budgets.png" width="220"><br><sub>Budgets</sub></td>
    <td align="center"><img src="docs/screenshots/notifications.png" width="220"><br><sub>Budget alerts</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/screenshots/transactions.png" width="220"><br><sub>Transactions</sub></td>
    <td align="center"><img src="docs/screenshots/sms.png" width="220"><br><sub>SMS inbox</sub></td>
    <td align="center"><img src="docs/screenshots/manage.png" width="220"><br><sub>Manage</sub></td>
  </tr>
</table>

> Shown in dark theme. Light and dark are both first-class — every screen is designed for both.

---

## Download

Grab the signed APK from the [**Releases**](../../releases) page and install it on any
Android 10+ (API 29) device:

1. Download `hisabak-v1.1.0.apk` from the latest release.
2. Open it on the device (allow “install unknown apps” if prompted), or run
   `adb install hisabak-v1.1.0.apk`.

It's a small (~3 MB) R8-minified build signed with a debug key for easy testing — not a Play
Store release.

---

## Features

- **SMS auto-capture** — parse bank SMS into transactions automatically (with permission), or
  paste a message to import it on the spot.
- **Budgets with alerts** — set a monthly limit per category and get notified at **50% / 80% /
  100%**. Alerts arrive as an Android notification *and* an in-app entry; tapping one opens the
  dashboard with that category expanded.
- **Dashboard** — net worth with cash / savings / investment breakdown, income & expense trends,
  category and brand breakdowns, and period filters (this/last month, this/last year, all time)
  across Summary, Trends, and Categories tabs.
- **Transactions** — searchable, filterable list (by brand, category, date range), with
  uncategorized spending surfaced for quick cleanup.
- **Organize** — categories (income / expense / savings / investment) with colors and icons, and
  brands mapped to categories. Safe deletion with brand-merge and confirmation.
- **Notifications center** — unread badge on the bell, swipe-to-dismiss, and mark-all-read.
- **Offline-first** — all data is stored locally on-device (Room). Light & dark themes, polished
  motion, edge-to-edge.

---

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Navigation:** Jetpack Navigation 3
- **Persistence:** Room (local, offline-first)
- **DI:** Koin
- **Async:** Coroutines + Flow
- **State:** ViewModel + `collectAsStateWithLifecycle`
- **Charts:** Vico

---

## Architecture

Feature-by-layer, with clean architecture inside each feature:

```
com.hisabak
├── core/                                shared primitives (Money, Clock, DomainResult, Room db)
├── ui/                                  design system: theme, motion, shared components
└── feature/<name>/
    ├── domain/        entities, use cases, repository interfaces
    ├── data/          Room repository implementations + mappers
    └── presentation/  stateful Route + stateless Screen + ViewModel (MVI-style)
```

Budget alerts are driven by a small reactive engine (`CategoryLimitMonitor`) that observes
transactions and limits and fires once per threshold per category per month — so manually added
and SMS-imported transactions are both covered through a single path.

---

## Testing & quality

The domain logic and ViewModels are covered by **77 JVM unit tests** (money math, the SMS
template parser, budget/limit logic, and ViewModel state) that run on the plain JVM — no
emulator needed:

```bash
./gradlew testProdDebugUnitTest
```

Quality is enforced, not just hoped for:

- **CI** — every pull request and push to `develop`/`main` runs the suite via GitHub Actions.
- **Branch protection** — a red suite can't be merged into the shared branches.
- **Local guard** — a pre-finish hook runs the tests on any change that touches Kotlin.

Tests use hand-written fakes over a small harness (`com.hisabak.testutil`) rather than a
mocking framework. See [`docs/testing.md`](docs/testing.md) for the full strategy.

---

## Build & run

**Requirements:** Android Studio (latest stable), JDK 17, Android SDK. `minSdk 29`, `targetSdk 36`.

```bash
git clone https://github.com/ahmedalaishat/hisabak-android.git
cd hisabak-android
./gradlew installDebug      # build and install on a connected device/emulator
# or open the project in Android Studio and Run
```

A fresh install seeds demo data so the dashboard, charts, and budgets are populated immediately.

---

## Inspiration

Hisabak is inspired by [**Hisabi**](https://github.com/hisabi-app/hisabi) — a self-hosted Laravel
personal-finance web app by Saleem Hadad. The domain model (transactions, brands, categories,
budgets, SMS ingestion, dashboard metrics) mirrors Hisabi's so concepts map cleanly between the two.

---

## License

[MIT](LICENSE).

---

<p align="center">
  Built by <strong>Ahmad Alaishat</strong> · <a href="https://github.com/ahmedalaishat">GitHub</a> · <a href="https://www.linkedin.com/in/ahmedalaishat">LinkedIn</a>
</p>
