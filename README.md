# Hisabak

**Hisabak** (Arabic: *حسابك*, “your account”) is a personal finance tracker for Android that
turns your bank SMS alerts into a clean, organized view of your money — categorize spending,
set monthly budgets, and get notified before you overshoot.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Min SDK](https://img.shields.io/badge/minSdk-29-blue)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF)
![License](https://img.shields.io/badge/license-MIT-green)

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

## Build & run

**Requirements:** Android Studio (latest stable), JDK 17, Android SDK. `minSdk 29`, `targetSdk 36`.

```bash
git clone <your-repo-url>
cd Hisabak
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
