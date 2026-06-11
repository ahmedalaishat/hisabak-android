Full-width pill search bar — persistent on every list screen, directly under the header summary.

```jsx
<SearchBar value={q} onChange={e=>setQ(e.target.value)} onClear={()=>setQ('')} />
```