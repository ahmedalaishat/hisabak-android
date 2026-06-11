# Hisabak — Mobile UI Kit

High-fidelity, interactive recreation of the Hisabak Android app. Open `index.html`.

- **Shell:** Android edge-to-edge phone frame (412dp) with status bar, brand `TopAppBar`, scrollable content, and a 5-tab `BottomNav`. A green FAB offers the primary "Add transaction" action where the screen has no inline Add button.
- **Theme toggle** (above the phone) switches the whole frame between light and dark — both are first-class.

## Screens
- `Dashboard.jsx` — net-worth hero + area chart + period chips, three stat pills, income/expense cards with sparklines, grouped income-vs-spending bars, expenses-by-category donut + legend, top-brands donut.
- `Transactions.jsx` — balance hero with income-ratio bar and Add button, income/expense summary, search, period chips, day-grouped transaction list, empty state.
- `AddTransactionSheet.jsx` — bottom sheet: big colored amount display, Expense/Income/Savings/Invest segmented control, brand chip picker, date, note, save/cancel.
- `SmsInbox.jsx` — auto-import status banner (on/off), paste-&-parse card with parsed preview, message list with Linked/Parsed/Unparsed status chips and Import actions.
- `Categories.jsx` — most-used highlight, income/expense summary, type filter, 2-column category grid with the dashed "Add New" tile.
- `BrandsPlaceholder` (inline in `index.html`) — brands list with most-used highlight and per-brand totals.

## Supporting files
- `mock.js` — shared mock data (categories, brands, transactions, SMS) + `money()` formatter. Plain global script (`window.HisabakMock`).
- `charts.jsx` — inline-SVG charts: `AreaChart`, `Sparkline`, `GroupedBars`, `DonutChart`, `LegendList`.

All screens compose the design-system primitives from `window.HisabakDesignSystem_aa2548` — they do not re-implement Button, Card, ListRow, etc. To reuse a screen, copy this folder and ensure `_ds_bundle.js` + `styles.css` resolve relative to it.
