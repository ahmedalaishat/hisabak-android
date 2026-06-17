Money display — tabular Geist Mono figures with signed coloring. Use for every amount.

```jsx
<AmountText value={8200} />            {/* +AED 8,200.00 green */}
<AmountText value={-342.75} />         {/* −AED 342.75 coral */}
<AmountText value={12450} tone="neutral" sign="never" size={40} /> {/* hero balance */}
```

`tone="auto"` colors by sign. Set `sign="never"` + `tone="neutral"` for plain balances.