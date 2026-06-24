# Hisabak — Mobile UI Kit

High-fidelity, interactive recreation of the Hisabak Android app. Open `index.html`.

- **Shell:** Android edge-to-edge phone frame (412dp) with status bar, brand `TopAppBar`, scrollable content, and the 5-tab `BottomNav`. A green FAB offers the primary "Add transaction" action where the screen has no inline Add button. Pushed screens (backup, edit, notifications) swap the top bar for a back arrow + title; full-screen flows (onboarding, restore, sync) cover the frame.
- **Navigation matches the app:** the five tabs are **Dashboard · Transactions · SMS · Manage · Settings** (Brands + Categories live together under Manage). The bell opens Notifications; Settings → Backup & restore; Manage rows → the edit screens.
- **Theme toggle** (above the phone) switches the whole frame between light and dark — both are first-class. The toolbar also previews the three full-screen flows: **Onboarding**, **Restore**, **Sync**.

## Screens (tabs)
- `Dashboard.jsx` — period-chip row + a **Summary / Trends / Categories** segmented control (matches `DashboardScreen.kt`). Summary: net-worth hero (cumulative area), Cash/Savings/Invest pills, Income & Expenses KPIs (daily-flow bars), income/expense over-time (cumulative). Trends: income-vs-spending grouped bars, expenses-by-category & top-brands & income-sources donuts. Categories: per-category spend vs monthly limit (with over-budget state), expandable to a trend line. Chart data follows the app's semantics (cumulative vs flow).
- `Transactions.jsx` — balance hero with income-ratio bar and Add button, income/expense summary, search, period chips, day-grouped transaction list, empty state.
- `SmsInbox.jsx` — auto-import status banner (on/off), paste-&-parse card with parsed preview, message list with Linked/Parsed/Unparsed status chips and Import actions.
- `Manage.jsx` — Brands/Categories count-card switcher, search, brand list (per-brand totals) or 2-column category grid with the dashed "Add New" tile, FAB adds the active type.
- `Settings.jsx` — appearance (theme segmented), language, security (app-lock toggle), data (backup & restore row), and the conditional passphrase-reminder card.

## Screens (pushed / sub-flows)
- `Backup.jsx` — backup & restore: not-enabled hero + benefits + "Turn on"; enabled state with last-backup card, "Back up now", auto-backup period, encryption toggle + passphrase row, "Turn off".
- `PeriodSheet.jsx` — auto-backup period radio sheet (Never / Daily / Weekly / Monthly), opened from Backup's "Automatic backups" row.
- `Notifications.jsx` — budget/backup alerts with unread dots and "Mark all read"; real empty state.
- `BrandEdit.jsx` — name + color-dot category picker.
- `CategoryEdit.jsx` — name, type (semantic segmented), monthly limit (expense only), color swatches, icon chips, live preview.
- `PassphraseSheets.jsx` — two bottom sheets: **set/change** the backup passphrase (passphrase + confirm, min-length & match validation) and **verify** it (from the Settings reminder), with a success state. Reached from Backup (Passphrase row / Encrypt toggle) and Settings (reminder → "Check my passphrase"). The Restore flow's encrypted-backup passphrase entry lives in `Restore.jsx`.
- `AddTransactionSheet.jsx` — bottom sheet: big colored amount display, Expense/Income/Savings/Invest segmented control, brand chip picker, date, note, save/cancel.

## Screens (full-screen flows)
- `Onboarding.jsx` — 6-page intro pager with skip, animated dots, primary CTA.
- `Restore.jsx` — one-time post-onboarding "bring your data back" offer (connect / skip).
- `Sync.jsx` — full-screen backup/restore progress: animated halo (running) → check (done) / error (failed).

## Supporting files
- `mock.js` — shared mock data (categories, brands, transactions, SMS, notifications) + `money()` formatter. Plain global script (`window.HisabakMock`).
- `charts.jsx` — inline-SVG charts: `AreaChart`, `Sparkline`, `GroupedBars`, `DonutChart`, `LegendList`.
- `extras.jsx` — screen-level helpers the component bundle doesn't ship: `Toggle`, `SettingsRow`, `FormSection`, `RadioRow`, `HeroDisc` (`window.HisabakExtras`).

All screens compose the design-system primitives from `window.HisabakDesignSystem_aa2548` — they do not re-implement Button, Card, ListRow, etc. To reuse a screen, copy this folder and ensure `_ds_bundle.js` + `styles.css` resolve relative to it.

## Rebuilding `mobile-kit.compiled.js`

`Transactions`, `AddTransactionSheet`, `SmsInbox`, and `Categories` ship precompiled inside
`_ds_bundle.js`, so they render even when `index.html` is opened directly (`file://`), where the
browser blocks in-browser Babel from fetching sibling `.jsx`. Everything else is precompiled to
**`mobile-kit.compiled.js`** — a plain script `index.html` loads normally (so it works over `file://`
too): the rewritten **`Dashboard.jsx`** (which overrides the bundle's stale dashboard) and the
**`charts.jsx`** helpers it needs, plus the screens added after the bundle was generated (`Manage`,
`Settings`, `Backup`, `Notifications`, `BrandEdit`, `CategoryEdit`, `Onboarding`, `Restore`, `Sync`,
`PassphraseSheets`, `PeriodSheet`, and `extras.jsx`). The `.jsx` files remain the source of truth.

After editing any of those `.jsx`, regenerate the compiled file (order: `charts.jsx` + `Dashboard.jsx` first):

```sh
curl -s https://unpkg.com/@babel/standalone@7.29.0/babel.min.js -o /tmp/babel.js
cat charts.jsx Dashboard.jsx extras.jsx Settings.jsx Backup.jsx Notifications.jsx CategoryEdit.jsx \
    BrandEdit.jsx Manage.jsx Onboarding.jsx Restore.jsx Sync.jsx PassphraseSheets.jsx PeriodSheet.jsx > /tmp/combined.jsx
node -e 'const B=require("/tmp/babel.js"),fs=require("fs");fs.writeFileSync("mobile-kit.compiled.js","/* AUTO-GENERATED from charts.jsx + the *.jsx screens via Babel — do not edit by hand. */\n"+B.transform(fs.readFileSync("/tmp/combined.jsx","utf8"),{presets:["react"]}).code+"\n")'
```

(When these screens are folded into a regenerated `_ds_bundle.js`, this step and file can go away.)
Serving the folder over HTTP instead (`python3 -m http.server` from the skill root) loads every
`.jsx` live and skips the compiled file entirely.
