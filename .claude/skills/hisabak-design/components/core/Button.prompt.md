Pill-shaped action button — use for any tap-to-commit action; `primary` is the single green CTA per screen.

```jsx
<Button variant="primary" size="lg" fullWidth leadingIcon="add">Add Transaction</Button>
<Button variant="secondary">Cancel</Button>
<Button variant="ghost">Skip</Button>
```

Variants: `primary` (green, one per view), `secondary` (outlined neutral), `ghost` (text), `danger` (destructive). Sizes `sm` 36 · `md` 48 · `lg` 52. Icons are Material Symbols Rounded ligature names. Reserve `primary` for the main action — never two greens competing.
