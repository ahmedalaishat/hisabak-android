Bottom navigation — 5 tabs (Dashboard, Transactions, SMS, Brands, Categories). Active tab fills its icon + turns green.

```jsx
<BottomNav value={tab} onChange={setTab} />
```

Defaults to the Hisabak tabs; pass `tabs` to override.