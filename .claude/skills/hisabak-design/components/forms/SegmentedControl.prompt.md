Segmented control — transaction type toggle and period selectors. Each option can carry its own active tone.

```jsx
<SegmentedControl value={type} onChange={setType} options={[
  {value:'expense', label:'Expense', tone:'expense'},
  {value:'income',  label:'Income',  tone:'income'},
]} />
```