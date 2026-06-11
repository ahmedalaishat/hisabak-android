# Hisabak — Claude Code Context

## What This App Is

**Hisabak** (Arabic: "your account") is a personal finance tracker for Android.
It auto-captures bank transactions from SMS messages, lets users categorize spending,
track budgets, and visualize financial trends.

Inspired by the [Hisabi](https://github.com/hisabi-app/hisabi) web app by Saleem Hadad.
Domain model mirrors Hisabi so concepts transfer cleanly.

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** Clean Architecture per feature (domain / data / presentation)
- **DI:** Koin
- **Async:** Kotlin Coroutines + Flow
- **State:** ViewModel + `collectAsStateWithLifecycle`
- **Charts:** Vico
- **Storage:** In-memory mock repositories (Room planned, not yet implemented)
- **Platform:** Android only, portrait, edge-to-edge

---

## Project Structure

```
com.hisabak
├── core/common/              shared value objects (Money, SyncMetadata, IDs)
├── ui/
│   ├── components/           shared Compose components
│   └── theme/                Material 3 theme (colors, typography, shapes)
└── feature/<name>/
    ├── domain/               entities, use cases, repository interfaces
    ├── data/                 repository implementations
    └── presentation/         Route (stateful) + Screen (stateless) + ViewModel
```

---

## Navigation

5-tab bottom navigation. Each tab manages its own internal nav state via sealed interfaces.

| Tab | Route | Internal screens |
|-----|-------|-----------------|
| Dashboard | DashboardRoute | Single screen |
| Transactions | TransactionsGraph | List ↔ Edit |
| SMS | SmsInboxRoute | Single screen |
| Brands | BrandsGraph | List ↔ Edit |
| Categories | CategoriesGraph | List ↔ Edit |

Pattern: `List` → tap row or "Create" button → `Edit(id?)` → Save/Cancel → back to `List`

---

## Domain Models

### Transaction
- `id`, `amount: Money`, `brandId`, `note?`, `occurredAt: Instant`, `sourceSmsId?`, `sync`

### Brand
- `id`, `name`, `categoryId?`, `sync`

### Category
- `id`, `name`, `type: CategoryType`, `color: String`, `icon: String`, `sync`
- `CategoryType`: INCOME | EXPENSES | SAVINGS | INVESTMENT
- `color` options: green, blue, orange, red, teal, purple, pink, gray
- `icon` options: wallet, cart, briefcase, car, utensils, piggy-bank, home, film, book, heart, gift, plane

### Budget
- `id`, `name`, `amount: Money`, `startAt`, `endAt?`, `saving`, `period`, `reoccurrence`, `categoryIds`, `sync`
- `Reoccurrence`: CUSTOM | DAILY | WEEKLY | MONTHLY | YEARLY

### Money
- `amountMinor: Long` (cents), `currency: Currency`

### SmsMessage
- `id`, `body`, `receivedAt: Instant`, plus parsed fields (brand, amount, status)

---

## Theme & Design Tokens

### Colors (Material 3)
- **Primary:** `#006E2C` (dark green)
- **PrimaryContainer:** `#00D95F` (bright green)
- **Background / Surface:** `#F2FCEE` (light green tint)
- **Error:** `#BA1A1A`

### Category Color Palette
| Key | Background | Foreground |
|-----|-----------|-----------|
| green | `#D1FAE5` | `#047857` |
| blue | `#DBEAFE` | `#2563EB` |
| orange | `#FFEDD5` | `#EA580C` |
| red | `#FEE2E2` | `#DC2626` |
| teal | `#CCFBF1` | `#0D9488` |
| purple | `#F3E8FF` | `#9333EA` |
| pink | `#FCE7F3` | `#DB2777` |
| gray | `#F3F4F6` | `#4B5563` |

### Typography (Inter font family)
- headlineSmall: 24sp SemiBold (page titles)
- titleLarge: 20sp SemiBold (section headers)
- titleMedium: 18sp SemiBold (emphasized numbers)
- titleSmall: 16sp Medium (row titles)
- labelLarge: 14sp Medium (chips, tabs)
- labelSmall: 11sp Medium

---

## Shared Components (`ui/components/`)

`HisabakTopBar`, `HisabakBottomNav`, `CreateActionButton`, `PrimaryPillButton`,
`SurfaceCard`, `IconTile`, `CircleIconTile`, `ListRow`, `StatCard`, `SearchField`,
`SectionHeader`, `FilterChipRow`, `GradientBanner`, `DarkPromoBanner`,
`EmptyStatePanel`, `ProgressBar`, `AreaLineChart`, `BarSparkline`, `DonutChart`

---

## Key Conventions

- No comments unless the WHY is non-obvious
- No error handling for impossible cases — trust domain guarantees
- No premature abstractions — add only what the current task requires
- Validate only at system boundaries (user input, external SMS)
- All entities carry `SyncMetadata` (prepared for future cloud sync)
- `rememberSaveable` keeps tab nav state alive across tab switches

---

## Active Design Work

A full redesign is in progress. See `DESIGN_BRIEF.md` for the complete
product and visual design specification to hand to a designer.

Current design issues being addressed:
- Too many competing visual weights — no clear hierarchy
- Green overused (loses semantic meaning)
- Charts cramped inside small cards
- Primary action (Add Transaction) not prominent enough
- Empty states lack guidance
