/* Transactions — balance hero, summary, search, filtered list. */
function Transactions({ onAdd }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, Button, SearchBar, Chip, ListRow, ProgressBar, AmountText, EmptyState } = NS;
  const M = window.HisabakMock;
  const [q, setQ] = React.useState('');
  const [period, setPeriod] = React.useState('month');

  const catColor = (id) => (M.CATEGORIES.find(c => c.id === id) || {}).color || 'var(--cat-gray)';
  const catName = (id) => (M.CATEGORIES.find(c => c.id === id) || {}).name || '';
  const filtered = M.TX.filter(t => t.brand.toLowerCase().includes(q.toLowerCase()) || t.note.toLowerCase().includes(q.toLowerCase()));
  const groups = filtered.reduce((acc, t) => { (acc[t.day] = acc[t.day] || []).push(t); return acc; }, {});

  return (
    <div style={{ padding: '8px 16px 24px' }}>
      {/* Balance hero */}
      <Card variant="hero" padding={18} style={{ marginTop: 8 }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 13, color: 'var(--text-secondary)' }}>Total Balance · June</div>
        <div style={{ marginTop: 4 }}><AmountText value={12450} tone="neutral" sign="never" size={34} weight={700} /></div>
        <div style={{ margin: '14px 0 6px' }}><ProgressBar value={60} tone="income" height={8} /></div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-tertiary)' }}>
          <span>60% income ratio</span><span>SAR 9,420 in · SAR 6,180 out</span>
        </div>
        <Button fullWidth size="lg" leadingIcon="add" onClick={onAdd} style={{ marginTop: 14 }}>Add Transaction</Button>
      </Card>

      {/* Income / expense summary */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginTop: 12 }}>
        <Card padding={14} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <span style={{ display: 'grid', placeItems: 'center', width: 36, height: 36, borderRadius: 'var(--r-sm)', background: 'var(--income-soft)', color: 'var(--income)' }}><span className="material-symbols-rounded">south_west</span></span>
          <div><div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>Income</div><AmountText value={9420} tone="income" sign="never" size={16} /></div>
        </Card>
        <Card padding={14} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <span style={{ display: 'grid', placeItems: 'center', width: 36, height: 36, borderRadius: 'var(--r-sm)', background: 'var(--expense-soft)', color: 'var(--expense)' }}><span className="material-symbols-rounded">north_east</span></span>
          <div><div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>Expenses</div><AmountText value={6180} tone="expense" sign="never" size={16} /></div>
        </Card>
      </div>

      <div style={{ marginTop: 14 }}><SearchBar value={q} placeholder="Search transactions" onChange={e => setQ(e.target.value)} onClear={() => setQ('')} /></div>

      <div style={{ display: 'flex', gap: 8, marginTop: 12, overflowX: 'auto', paddingBottom: 2 }}>
        {[['today', 'Today'], ['week', 'This Week'], ['month', 'This Month'], ['all', 'All Time']].map(([v, l]) => (
          <Chip key={v} selected={period === v} onClick={() => setPeriod(v)}>{l}</Chip>
        ))}
      </div>

      {filtered.length === 0 ? (
        <Card padding={0} style={{ marginTop: 16 }}>
          <EmptyState icon="receipt_long" title="No matches" description="No transactions match your search. Try a different term." />
        </Card>
      ) : (
        Object.entries(groups).map(([day, items]) => (
          <div key={day} style={{ marginTop: 18 }}>
            <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 12, letterSpacing: '0.04em', textTransform: 'uppercase', color: 'var(--text-tertiary)', marginBottom: 6, paddingLeft: 2 }}>{day}</div>
            <Card padding={0} style={{ padding: '2px 14px' }}>
              {items.map((t, i) => (
                <ListRow key={t.id} title={t.brand} subtitle={catName(t.cat) + ' · ' + t.note}
                  leadingText={t.brand[0]} color={catColor(t.cat)}
                  amount={t.amount} meta={t.date} divider={i < items.length - 1} />
              ))}
            </Card>
          </div>
        ))
      )}
    </div>
  );
}
window.HisabakTransactions = Transactions;
