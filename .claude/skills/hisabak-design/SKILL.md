---
name: hisabak-design
description: Use this skill to generate well-branded interfaces and assets for Hisabak, either for production or throwaway prototypes/mocks/etc. Contains essential design guidelines, colors, type, fonts, assets, and UI kit components for prototyping.
user-invocable: true
---

Read the README.md file within this skill, and explore the other available files.
If creating visual artifacts (slides, mocks, throwaway prototypes, etc), copy assets out and create static HTML files for the user to view. If working on production code, you can copy assets and read the rules here to become an expert in designing with this brand.
If the user invokes this skill without any other guidance, ask them what they want to build or design, ask some questions, and act as an expert designer who outputs HTML artifacts _or_ production code, depending on the need.

## Quick reference

- **Hisabak** = Android personal-finance tracker. SMS-automated expense capture + calm analytics. English-first, dark mode required, phones only.
- **Voice:** plain, calm, "you", sentence case, no emoji. Money is always `SAR 1,234.56` with tabular figures; income `+` green, expense `−` coral.
- **Green is meaningful, never decorative** — income, the one primary action, the active nav tab. Backgrounds are neutral gray, never green.
- **Fonts:** DM Sans (UI) + Geist Mono (amounts). **Icons:** Material Symbols Rounded.

## Files
- `styles.css` — link this; it `@import`s all tokens + fonts.
- `tokens/` — colors (light+dark), typography, spacing, elevation.
- `components/` — React primitives (`core/`, `forms/`, `data/`, `navigation/`). Each has a `.d.ts` contract and `.prompt.md` usage note.
- `ui_kits/mobile/` — interactive Android app recreation (Dashboard, Transactions, Add, SMS, Brands, Categories) — read these screens to see the components composed correctly.
- `assets/` — logo, wordmark (light/dark), app icon.
- `guidelines/` — foundation specimen cards.

## Using components
Link `styles.css`, load `_ds_bundle.js`, then read components off `window.HisabakDesignSystem_aa2548`, e.g. `const { Button, ListRow, AmountText } = window.HisabakDesignSystem_aa2548;`. For static mocks, copy `assets/` and the relevant CSS in; reference Material Symbols Rounded for icons.
