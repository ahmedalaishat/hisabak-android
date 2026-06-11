Persistent top app bar. `brand` shows the wordmark + avatar/bell on shell screens; `onBack` + `title` for detail screens.

```jsx
<TopAppBar brand showAvatar showBell avatarName="Layla Khan" />
<TopAppBar title="Add Transaction" onBack={...} />
```