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
- **Storage:** Room (SQLite) — `Room*Repository` impls per feature's `data/`, entities/DAOs/
  mappers in `data/local/`, and the database in `core/data/local/` (`HisabakDatabase`)
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

**Jetpack Navigation 3** (`com.hisabak.nav`). 4-tab bottom navigation, each tab a
top-level destination with its own back stack. State is retained per tab when
switching; the user always exits the app through the **Dashboard** (home) tab.

- `NavKeys.kt` — destination keys: `DashboardKey`, `TransactionsKey`, `SmsKey`,
  `ManageKey` (top-level) + `TransactionEditKey/BrandEditKey/CategoryEditKey(id)` (children).
- `NavigationState.kt` — `NavigationState` (one back stack per tab), `Navigator`
  (`navigate`/`goBack`: back from a tab → home; back from home → exit), and `toEntries()`
  which wires the saveable-state + **ViewModel-store** entry decorators. The ViewModel
  decorator scopes each screen's ViewModel to its `NavEntry`, so a popped destination's
  ViewModel is cleared — re-entering a screen starts fresh (no stale state).
- `BottomSheetScene.kt` — `BottomSheetSceneStrategy`; the transaction add/edit destination
  is marked with `bottomSheet()` metadata so it renders as a modal bottom sheet overlay.
- `MainActivity.kt` — `Scaffold` + `NavDisplay`. Top bar / bottom nav / FAB are derived
  from the current destination.

| Tab | Top-level key | Internal screens |
|-----|---------------|-----------------|
| Dashboard | DashboardKey | Single screen |
| Transactions | TransactionsKey | List → Edit (bottom sheet) |
| SMS | SmsKey | Single screen |
| Manage | ManageKey | Brands/Categories list → Edit (full screen) |

Pattern: `List` → tap row or FAB → push `Edit(id?)` destination → Save/Cancel calls
`navigator.goBack()` → back to `List`.

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

## Design System

This app uses the **Hisabak design system**. When building or editing UI, follow these rules.
The full system (tokens, component specs, screen prototypes) lives in
`.claude/skills/hisabak-design/`. **The Compose app is the source of truth** — the tokens are
already translated into `ui/theme/` (`Color.kt`, `Type.kt`, `Shape.kt`, `Motion.kt`,
`HisabakTheme.kt`) and shared components in `ui/components/`. For production UI, use those
directly; see the design skill's `compose-bridge.md` for the token/component → Kotlin map.
Use the HTML/CSS kit only for throwaway visual mockups.

### Foundations

- **Theme:** wrap content in `HisabakTheme { }`. Light **and** dark are first-class — every
  screen must look right in both. Never hardcode hex; use `MaterialTheme.colorScheme.*`,
  `HisabakTheme.colors.*`, `HisabakType.*`, `Spacing.*`, `Sizing.*`.
- **Type:** DM Sans for UI; **Geist Mono with tabular figures for every amount**
  (`HisabakType.amount` / `amountLarge` / `amountHero`). Amounts align in columns —
  don't use the sans font for money.
- **Spacing:** 8dp grid. 16dp page margin, 16dp card padding, 12dp between cards.
  Touch targets ≥ 44dp.
- **Shape:** 12dp default card radius, 16dp hero/sheet, pill for buttons/chips/badges,
  14dp category icon tiles.
- **Depth:** prefer surface contrast over shadows. Flat cards use a 1dp outline; reserve
  real elevation for sheets, dialogs, menus, and the FAB.

### Color — the cardinal rule

**Green (`primary`) is meaningful, never decorative.** Reserve it for exactly three things:
1. money-positive values (income, positive trends)
2. the single primary action on a screen
3. the active bottom-nav tab

Everything else is neutral or a *semantic finance color*:
`income` green · `expense` coral · `savings` blue · `investment` purple.
Backgrounds are neutral gray (`background`), never green-tinted.
Category dots/tiles use the 8 `cat*` colors.

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

### Icons

Material Symbols Rounded — outlined when inactive, **filled** when active (bottom nav, stars).
Category icons sit on a tinted rounded-square tile in the category color.

### Voice & Copy

- Address the user as **you**. **Sentence case** for buttons/labels; Title Case only for
  proper screen names ("SMS Inbox"). No emoji.
- Money is always `AED 1,234.56` with tabular figures. Income shows `+`, expenses the true
  minus `−` (U+2212), both colored. Hero balances drop the sign and use neutral text.
- Every list screen needs a real empty state: icon + "No … yet" + one-line guidance + a CTA.

---

## Shared Components (`ui/components/`)

Reuse existing Composables; extend them to match the spec rather than forking.
Match prototypes in `.claude/skills/hisabak-design/components/` and `ui_kits/mobile/`.
Each component has a `.prompt.md` (what/when + usage) and `.d.ts` (props) — read those first.

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
- Don't invent new screens/flows from scratch — match existing Hisabak designs;
  if a design doesn't exist yet, leave a `// TODO: design` note rather than guessing

### UI Don'ts

- No green backgrounds, no decorative gradients, no glassmorphism, no emoji,
  no colored left-border accent stripes

---

## Testing

JVM unit tests guard the domain logic and ViewModels. Full guide: `docs/testing.md`.

- Run `./gradlew testProdDebugUnitTest`. **Keep the suite green before finishing any change.**
  A Stop hook (`.claude/settings.json` → `.claude/hooks/run-tests.sh`) runs this
  automatically whenever Kotlin files changed and blocks on failure.
- **New feature → new tests.** When you add or change a use case, repository, ViewModel,
  or any business logic, add or update its test **in the same change**.
- Tests live in `app/src/test/…` mirroring `main`. Reuse the harness in
  `com.hisabak.testutil` (`TestClock`, `MainDispatcherRule`, `Fake*` repositories,
  `TestData`) rather than a mocking framework; build the real use case around a fake repo.
- Currently out of scope (no tests required): Compose UI, Room DAOs, navigation.

---

## Automated feature pipeline

Use the **`/feature`** skill (`.claude/skills/feature/SKILL.md`) to take a high-level
requirement to a reviewed PR: `/feature "<requirement>"`. It runs spec → design → branch →
code+tests → QA → docs → PR autonomously, then **stops for one review gate** — it merges
into `develop` only when the user says **"ship it"** (never auto-merge). Per-feature
spec+design land in `docs/features/<slug>.md`; user-visible changes update `CHANGELOG.md`.

---

## Active Design Work

A full redesign is in progress. See `DESIGN_BRIEF.md` for the complete product and
visual design specification to hand to a designer.

Current design issues being addressed:
- Too many competing visual weights — no clear hierarchy
- Green overused (loses semantic meaning)
- Charts cramped inside small cards
- Primary action (Add Transaction) not prominent enough
- Empty states lack guidance
