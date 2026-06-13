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

## Design System

This app uses the **Hisabak design system**. When building or editing UI, follow these rules.
The full system (tokens, component specs, screen prototypes) lives in
`.claude/skills/hisabak-design/`; the Compose translation is in that skill's `compose/` folder
(copy it into the app module — see `compose/README.md`).

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

## Active Design Work

A full redesign is in progress. See `DESIGN_BRIEF.md` for the complete product and
visual design specification to hand to a designer.

Current design issues being addressed:
- Too many competing visual weights — no clear hierarchy
- Green overused (loses semantic meaning)
- Charts cramped inside small cards
- Primary action (Add Transaction) not prominent enough
- Empty states lack guidance
