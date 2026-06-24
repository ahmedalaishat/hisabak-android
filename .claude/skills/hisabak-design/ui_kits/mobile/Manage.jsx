/* Manage — the tab that merges Brands + Categories (mirrors ManageRoute.kt). Two count cards act as
   the switcher; the active list shows below; the FAB adds the active type. */
function Manage({ onEditBrand, onEditCategory, onAdd }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, SearchBar, ListRow, CategoryTile, AmountText } = NS;
  const M = window.HisabakMock;
  const [tab, setTab] = React.useState('brands');
  const [q, setQ] = React.useState('');

  const catName = id => (M.CATEGORIES.find(c => c.id === id) || {}).name || '';
  const catColor = id => (M.CATEGORIES.find(c => c.id === id) || {}).color || 'var(--cat-gray)';
  const totals = { carrefour: 1240, stc: 820, talabat: 540, uber: 388, netflix: 256, acme: 8200, starbucks: 210, amazon: 180 };

  const SwitchCard = ({ id, label, count, icon }) => {
    const on = tab === id;
    return (
      <Card padding={14} variant={on ? 'tinted' : 'default'} tint="var(--accent-soft)"
        onClick={() => setTab(id)}
        style={{ flex: 1, cursor: 'pointer', border: on ? '1px solid var(--accent)' : '1px solid var(--border)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <span style={{ width: 32, height: 32, borderRadius: 10, display: 'grid', placeItems: 'center', background: on ? 'var(--accent)' : 'var(--surface-sunken)' }}>
            <span className="material-symbols-rounded" style={{ fontSize: 18, color: on ? '#fff' : 'var(--text-secondary)' }}>{icon}</span>
          </span>
          <div>
            <div style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, fontSize: 18, color: 'var(--text-primary)' }}>{count}</div>
            <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-secondary)' }}>{label}</div>
          </div>
        </div>
      </Card>
    );
  };

  const brands = M.BRANDS.filter(b => b.name.toLowerCase().includes(q.toLowerCase()));
  const cats = M.CATEGORIES.filter(c => c.name.toLowerCase().includes(q.toLowerCase()));

  return (
    <div style={{ padding: '12px 16px 28px' }}>
      <div style={{ display: 'flex', gap: 12 }}>
        <SwitchCard id="brands" label="Brands" count={8} icon="storefront" />
        <SwitchCard id="categories" label="Categories" count={8} icon="category" />
      </div>

      <div style={{ marginTop: 14 }}>
        <SearchBar value={q} placeholder={tab === 'brands' ? 'Search brands' : 'Search categories'} onChange={e => setQ(e.target.value)} onClear={() => setQ('')} />
      </div>

      {tab === 'brands' ? (
        <Card padding={0} style={{ marginTop: 14, padding: '2px 14px' }}>
          {brands.map((b, i) => (
            <ListRow key={b.id} title={b.name} subtitle={catName(b.category)} leadingText={b.initial} color={catColor(b.category)}
              divider={i < brands.length - 1} onClick={() => onEditBrand(b)}
              trailing={<div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                <AmountText value={totals[b.id] || 0} tone="neutral" sign="never" size={14} />
                <span className="material-symbols-rounded" style={{ fontSize: 18, color: 'var(--text-tertiary)' }}>chevron_right</span>
              </div>} />
          ))}
        </Card>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginTop: 14 }}>
          {cats.map(c => (
            <CategoryTile key={c.id} name={c.name} icon={c.icon} color={c.color} type={c.type}
              total={M.money(c.total, { decimals: false })} onClick={() => onEditCategory(c)} onDelete={() => {}} />
          ))}
          <CategoryTile addNew onClick={onAdd} />
        </div>
      )}
    </div>
  );
}
window.HisabakManage = Manage;
