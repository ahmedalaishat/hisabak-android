# Hisabak — Product Design Brief

## 1. What Is This App?

**Hisabak** (Arabic: "your account") is a personal finance tracker for Android.
It automatically captures bank transactions from SMS messages, lets users categorize
spending, track budgets, and analyze financial trends through charts and dashboards.

**Target user:** Someone who receives bank transaction SMS alerts and wants to
automatically log, categorize, and visualize their spending without manual entry.

**Core value proposition:** Zero-friction expense tracking via SMS automation +
clean analytics to understand where your money goes.

---

## 2. Platform Requirements

- **Platform:** Android only (Jetpack Compose / Material 3)
- **Design tool:** Figma (auto-layout, component variants, color/text styles)
- **Target screen width:** 360dp–412dp (phones only)
- **Orientation:** Portrait only
- **Dark mode:** Required — deliver light AND dark variants for every screen
- **System UI:** Edge-to-edge (status bar and nav bar overlap the app; layouts must account for safe area insets)

---

## 3. Navigation Shell

### Bottom Navigation — 5 tabs

| # | Tab Label | Icon (suggestion) |
|---|-----------|------------------|
| 1 | Dashboard | Chart / Home |
| 2 | Transactions | Receipt / List |
| 3 | SMS | Message / Inbox |
| 4 | Brands | Tag / Store |
| 5 | Categories | Grid / Folder |

- Icons: outlined when inactive, filled when active
- Active tab: primary color label + filled icon
- No badges initially

### Top App Bar (persistent across all screens)
- Left: app name "Hisabak" (logo or wordmark)
- Right: user avatar circle + notification bell icon

---

## 4. Screen Specifications

### 4.1 Dashboard

**Purpose:** Snapshot of financial health at a glance.

**Layout (scrollable, top to bottom):**

1. **Net Worth Hero Card**
   - Large net worth number (e.g. "AED 12,450")
   - Subtitle: "Net Worth" + date range label
   - Area line chart (full card width, subtle gradient fill below line)
   - Period selector chips below chart: Week / Month / Year / All

2. **Three Stat Pills (horizontal row)**
   - Total Cash | Total Savings | Total Investment
   - Each: label on top, bold number below, icon left
   - Equal width, scroll if needed

3. **Income Card**
   - Total income this period (large bold number, green)
   - "vs last period" percentage change with arrow (↑ green / ↓ red)
   - Small bar sparkline (right side or bottom)

4. **Expenses Card**
   - Same layout as income but red-tinted

5. **Income & Spending Over Time**
   - Section title + period filter chips
   - Grouped bar chart (income bars vs expense bars side by side)
   - Readable axis labels, legend

6. **Income by Category**
   - Section title
   - Donut chart (max 5 slices — group remainder into "Other")
   - Legend list below: color dot + name + amount + % of total

7. **Expenses by Category**
   - Same layout as income by category

8. **Category Trend**
   - Section title + dropdown selector (pick a category)
   - Line chart showing that category's spend over time

9. **Top Brands**
   - Section title
   - Donut chart (spending split by brand, max 5)
   - List of top brands: brand name + amount + color bar

---

### 4.2 Transactions — List

**Purpose:** View all transactions, add new ones, search/filter.

**Layout:**

1. **Balance Hero Card**
   - Total balance (very large number, centered or left-aligned)
   - Thin horizontal progress bar (green portion = income ratio)
   - "Add Transaction" button — prominent, full-width or pill-shaped
   - Date range label

2. **Income / Expenses Summary Row**
   - Two cards side by side
   - Income card: green icon, "Income" label, total amount
   - Expenses card: red icon, "Expenses" label, total amount

3. **Search Bar** — full-width, persistent

4. **Period Filter Chips** — Today / This Week / This Month / All Time

5. **Transaction List**
   - Each row:
     - Left: colored circle (category color) with brand initial or icon
     - Center: brand name (bold) + note/description (secondary, truncated)
     - Right: amount (green if income, red if expense) + date below
   - Swipe left to delete (with confirmation)
   - Tap to edit

6. **Empty State**
   - Illustration or large icon
   - "No transactions yet"
   - "Add your first transaction" button

---

### 4.3 Add / Edit Transaction (Bottom Sheet or Full Screen)

**Purpose:** Create or modify a single transaction.

**Fields:**

1. **Amount input** — large, centered, prominent
   - Currency symbol prefix
   - Numeric keyboard auto-opens
   - Consider a large display number style (like a calculator)

2. **Type toggle** — 4 options as segmented control or large tap targets:
   Expense | Income | Savings | Investment
   - Each has a color: red / green / blue / purple

3. **Brand picker**
   - Horizontal scrollable chip row showing available brands
   - Each chip has a small color dot (brand's category color)
   - "+ New Brand" chip at end

4. **Date selector**
   - Button showing current date (defaults to today)
   - Taps to open date picker dialog

5. **Note field** — optional, multiline, placeholder "Add a note…"

6. **Save button** — full-width, primary color, bottom of form
7. **Cancel** — text button above save or top-left X

---

### 4.4 SMS Inbox — List

**Purpose:** View incoming bank SMS messages, review parsed data, import as transactions.

**Layout:**

1. **Auto-Import Status Banner**
   - If OFF: amber/warning colored card — "Auto-import is disabled" + "Enable" button
   - If ON: subtle success card — "Auto-import active"

2. **Paste & Parse Card**
   - Title: "Paste an SMS"
   - Multiline text input
   - "Parse & Import" button
   - Parsing result preview (brand + amount extracted, highlighted)

3. **Search Bar**

4. **SMS List**
   - Each row:
     - First 2 lines of SMS body (truncated)
     - Received date/time (secondary)
     - Status chip:
       - **Linked** — green chip (already imported)
       - **Parsed** — blue chip (detected amount/brand, ready to import)
       - **Unparsed** — gray chip (could not extract data)
     - If parsed: brand name + amount in bold green
     - If parsed: "Import" button (small, right side)
   - Swipe left to delete

5. **Empty State**
   - Phone/SMS icon
   - "No messages yet"
   - Instructions on enabling auto-capture

---

### 4.5 Brands — List

**Purpose:** Manage merchants and businesses where money is spent.

**Layout:**

1. **Header row** — "Brands" title + "New Brand" button (right)

2. **Insight Pill Row**
   - Total brands count | Total categories count

3. **Search Bar**

4. **Most Used Brand Card**
   - Highlighted card (slightly elevated or tinted)
   - Star icon + "Most Used" label
   - Brand name (large) + category chip + total amount spent

5. **Category Filter Chips** — All + each category name (horizontal scroll)

6. **Brand List**
   - Each row:
     - Left: colored dot (category color)
     - Center: brand name (bold) + category name (secondary)
     - Right: total amount + edit icon
   - Swipe left to delete

7. **Empty State**

---

### 4.6 Brand — Add / Edit

**Fields:**

1. **Name** — text input, autofocus, validation error below
2. **Category** — filter chip selector (shows all categories with color dots)
   - Selecting a chip links brand to that category
   - "None" chip for uncategorized
3. **Save / Cancel buttons**

---

### 4.7 Categories — List

**Purpose:** Manage spending categories (type, color, icon).

**Layout:**

1. **Header row** — "Categories" title + "New Category" button

2. **Search Bar**

3. **Most Used Category Card** — highlighted, shows top category with icon tile + amount

4. **Income vs Expenses Summary**
   - Two stat cards: Income total (green) + Expenses total (red)
   - Each with a thin progress bar

5. **Type Filter Chips** — All / Expenses / Income / Savings / Investment

6. **2-Column Category Grid**
   - Each tile:
     - Colored icon (icon on tinted rounded-square background)
     - Category name (bold)
     - Type label (small chip)
     - Monthly total (secondary)
     - Delete button (top-right corner, small X)
   - Last tile: dashed border "Add New" placeholder

---

### 4.8 Category — Add / Edit

**Fields:**

1. **Name** — text input, autofocus
2. **Type** — 4 large chips: Income / Expenses / Savings / Investment
   (each colored: green / red / blue / purple)
3. **Color picker** — 8 color swatches in a row, checkmark on selected:
   green, blue, orange, red, teal, purple, pink, gray
4. **Icon picker** — 12 icon chips in a 4×3 grid:
   wallet, cart, briefcase, car, food/fork, piggy-bank, home, film, book, heart, gift, plane
5. **Live preview tile** — updates in real-time as user picks color + icon
6. **Save / Cancel buttons**

---

## 5. Data Summary (for designer awareness)

| Entity | Key fields shown in UI |
|--------|----------------------|
| Transaction | Amount, Brand, Date, Note, Type (income/expense/savings/investment) |
| Brand | Name, Category |
| Category | Name, Type, Color, Icon |
| Budget | Name, Amount, Period, Categories |
| SMS Message | Body, Date, Parsed brand, Parsed amount, Status |

**Money:** Always displayed with currency symbol prefix. Stored as minor units (e.g. 1000 = AED 10.00).

---

## 6. Visual Design Direction

### 6.1 Color System

| Role | Direction |
|------|-----------|
| Primary CTA | Strong green — buttons, active states, links |
| Income | Green — reserve this meaning, don't use green decoratively |
| Expenses | Red / coral |
| Savings | Blue |
| Investment | Purple |
| Background | Light neutral (`#F6F7F9` range) — NOT green-tinted |
| Surface / Cards | White (`#FFFFFF`) |
| Text primary | Near-black (`#111827`) |
| Text secondary | Medium gray (`#6B7280`) |
| Dividers / borders | Light gray (`#E5E7EB`) |

**Critical rule:** Do not use green for decoration. Reserve it for:
- Positive financial values (income, positive trends)
- The primary action button
- The active bottom nav tab

### 6.2 Typography

- **Font family:** Geometric sans-serif — Inter, DM Sans, or Nunito
- **Number displays:** Tabular figures (`tnum` font feature) so numbers align in lists
- **Scale:**

| Role | Size | Weight |
|------|------|--------|
| Page title | 24sp | SemiBold |
| Section title | 18sp | SemiBold |
| Hero amount | 32–40sp | Bold |
| List amount | 16sp | SemiBold |
| Row title | 16sp | Medium |
| Secondary text | 13sp | Regular |
| Chip / tab label | 13–14sp | Medium |

### 6.3 Spacing & Layout

- Horizontal page margin: **16dp**
- Card internal padding: **16dp**
- Card corner radius: **12dp**
- Between cards: **12dp** gap
- Section title bottom margin: **8dp**

### 6.4 Elevation / Depth

- Prefer **background color differentiation** over heavy shadows
- Page background: `#F6F7F9`
- Cards: `#FFFFFF` (white lifts naturally off the background)
- Elevated dialogs / bottom sheets: slight shadow (`0 4dp 16dp rgba(0,0,0,0.08)`)

### 6.5 Iconography

- Use a consistent icon set: **Material Symbols Rounded** or **Lucide**
- Category icons sit inside a **colored rounded-square tile** (icon + tinted background — this is a distinguishing pattern of the app, keep it)
- Bottom nav: outlined (inactive) → filled (active)

### 6.6 Charts

| Chart type | Where used | Notes |
|-----------|-----------|-------|
| Area line chart | Net worth, category trend | Single line, gradient fill, clean axis |
| Grouped bar chart | Income vs expense over time | Two colors, clear legend |
| Horizontal bar list | Category breakdowns | More readable than donut for 5+ items |
| Donut chart | Brand breakdown, max 5 segments | Group rest into "Other" |
| Bar sparkline | Income/expense stat cards | Inline micro-chart, 8–12 bars |

### 6.7 Empty States

Every list screen needs an empty state:
- A simple illustration or large centered icon (not stock photo)
- A short title: "No [thing] yet"
- A short subtitle with guidance
- A CTA button (e.g. "Add your first transaction")

---

## 7. Design Problems to Solve (Current App Critique)

1. **No visual hierarchy** — every card competes equally for attention; hero numbers don't stand out
2. **Color overuse** — green used for backgrounds, borders, charts, buttons, and badges simultaneously
3. **Charts too small** — donut charts with 6+ slices crammed into 160dp cards; unreadable
4. **Primary action buried** — "Add Transaction" is not consistently reachable
5. **Typography too uniform** — amounts look the same weight as labels
6. **Empty states are bare** — just text, no guidance or visual
7. **Cards too similar** — no differentiation between hero cards and supporting cards
8. **SMS screen unclear** — "Linked" vs "Unparsed" states not visually distinct enough

---

## 8. Key UX Principles

1. **Calm clarity** — finance apps must feel trustworthy, not flashy
2. **Number hierarchy** — amounts are the most important data; make them unmistakably prominent
3. **One-thumb reachability** — primary actions (Add Transaction) accessible from bottom navigation zone
4. **Progressive disclosure** — show the summary first, details on demand
5. **Purposeful color** — every color should mean something specific; no decorative color

---

## 9. Deliverables Checklist

### Foundations
- [ ] Color style guide (semantic tokens + their hex values for light + dark)
- [ ] Typography scale (all text styles defined as Figma text styles)
- [ ] Spacing / grid system (8dp base grid, 16dp margin)
- [ ] Elevation / shadow system

### Components
- [ ] Buttons (primary pill, secondary outlined, text, destructive)
- [ ] Input fields (text, search, amount/number)
- [ ] Cards (hero card, stat card, list card, promo banner)
- [ ] Chips / filter chips (default, selected, category-colored)
- [ ] List rows (transaction row, brand row, SMS row)
- [ ] Category icon tile (color + icon variants for all 8 colors × 12 icons)
- [ ] Status chips (Linked, Parsed, Unparsed)
- [ ] Bottom sheet / modal template
- [ ] Date picker style
- [ ] Empty state template
- [ ] Top app bar
- [ ] Bottom navigation bar

### Screens — Light Mode
- [ ] Dashboard
- [ ] Transactions List
- [ ] Add/Edit Transaction
- [ ] SMS Inbox
- [ ] Brands List
- [ ] Add/Edit Brand
- [ ] Categories List
- [ ] Add/Edit Category

### Screens — Dark Mode
- [ ] All 8 screens above in dark theme

### Extras
- [ ] Chart style examples (area line, grouped bar, donut, sparkline)
- [ ] Micro-interaction notes (swipe-to-delete, parse animation, screen transitions)
- [ ] App icon concept

---

## 10. Screens Priority Order

Design in this order (highest impact first):

1. Dashboard — sets the visual language and most complex
2. Transactions List — most used screen day-to-day
3. Add Transaction — most frequent action
4. Navigation shell (top bar + bottom nav) — applied to all
5. SMS Inbox — unique differentiating feature
6. Category List + Edit — fun icon/color picker
7. Brands List + Edit
