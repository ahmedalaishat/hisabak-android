List row — transactions, brands, SMS. Leading colored avatar, title+subtitle, amount+meta (or custom `trailing`).

```jsx
<ListRow title="Carrefour" subtitle="Groceries" leadingText="C"
  color="var(--cat-orange)" amount={-342.75} meta="Today" />
```

Use `leadingIcon` instead of `leadingText` for income/system rows. `trailing` overrides the amount slot (e.g. an Import button).