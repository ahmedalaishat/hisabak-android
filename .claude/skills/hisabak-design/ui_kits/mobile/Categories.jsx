/* Categories — most-used highlight, summary, type filter, 2-col grid. */
function Categories({ onAdd }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, Button, SearchBar, Chip, CategoryTile, CategoryIcon, AmountText, ProgressBar, Badge } = NS;
  const M = window.HisabakMock;
  const [q, setQ] = React.useState('');
  const [filter, setFilter] = React.useState('all');

  const cats = M.CATEGORIES.filter(c =>
    (filter === 'all' || c.type === filter) && c.name.toLowerCase().includes(q.toLowerCase()));

  return (
    <div style={{ padding: '8px 16px 24px' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 8 }}>
        <h1 style={{ margin: 0, fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 22, letterSpacing: '-0.02em', color: 'var(--text-primary)' }}>Categories</h1>
        <Button size="sm" leadingIcon="add" onClick={onAdd}>New</Button>
      </div>

      {/* Most used */}
      <Card variant="tinted" tint="var(--accent-soft)" padding={16} style={{ marginTop: 14, display: 'flex', alignItems: 'center', gap: 14 }}>
        <CategoryIcon icon="shopping_cart" color="var(--cat-orange)" size={48} />
        <div style={{ flex: 1 }}>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 11.5, letterSpacing: '0.04em', textTransform: 'uppercase', color: 'var(--accent-hover)' }}><span className="material-symbols-rounded is-filled" style={{ fontSize: 14 }}>star</span>Most used</div>
          <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 18, color: 'var(--text-primary)', marginTop: 2 }}>Groceries</div>
        </div>
        <AmountText value={1240} tone="neutral" sign="never" size={18} weight={700} />
      </Card>

      {/* Income vs expenses */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginTop: 12 }}>
        <Card padding={14}>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>Income</div>
          <div style={{ margin: '4px 0 10px' }}><AmountText value={9420} tone="income" sign="never" size={18} weight={700} /></div>
          <ProgressBar value={60} tone="income" />
        </Card>
        <Card padding={14}>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>Expenses</div>
          <div style={{ margin: '4px 0 10px' }}><AmountText value={6180} tone="expense" sign="never" size={18} weight={700} /></div>
          <ProgressBar value={40} tone="expense" />
        </Card>
      </div>

      <div style={{ marginTop: 14 }}><SearchBar value={q} placeholder="Search categories" onChange={e => setQ(e.target.value)} onClear={() => setQ('')} /></div>

      <div style={{ display: 'flex', gap: 8, marginTop: 12, overflowX: 'auto', paddingBottom: 2 }}>
        {[['all', 'All'], ['expense', 'Expenses'], ['income', 'Income'], ['savings', 'Savings'], ['investment', 'Invest']].map(([v, l]) => (
          <Chip key={v} selected={filter === v} onClick={() => setFilter(v)}>{l}</Chip>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginTop: 14 }}>
        {cats.map(c => (
          <CategoryTile key={c.id} name={c.name} icon={c.icon} color={c.color} type={c.type}
            total={M.money(c.total, { decimals: false })} onDelete={() => {}} />
        ))}
        <CategoryTile addNew onClick={onAdd} />
      </div>
    </div>
  );
}
window.HisabakCategories = Categories;
