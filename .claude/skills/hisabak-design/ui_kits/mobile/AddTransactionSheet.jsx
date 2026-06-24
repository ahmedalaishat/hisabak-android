/* Add Transaction — bottom sheet overlay. */
function AddTransactionSheet({ open, onClose }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Button, SegmentedControl, Chip, Input } = NS;
  const M = window.HisabakMock;
  const [type, setType] = React.useState('expense');
  const [amount, setAmount] = React.useState('342.75');
  const [brand, setBrand] = React.useState('carrefour');

  const typeColor = { expense: 'var(--expense)', income: 'var(--income)', savings: 'var(--savings)', investment: 'var(--investment)' }[type];

  return (
    <div style={{ position: 'absolute', inset: 0, zIndex: 30, pointerEvents: open ? 'auto' : 'none' }}>
      <div onClick={onClose} style={{ position: 'absolute', inset: 0, background: 'var(--scrim)', opacity: open ? 1 : 0, transition: 'opacity var(--dur-base)' }} />
      <div style={{
        position: 'absolute', left: 0, right: 0, bottom: 0,
        background: 'var(--surface)', borderTopLeftRadius: 'var(--r-xl)', borderTopRightRadius: 'var(--r-xl)',
        boxShadow: 'var(--shadow-lg)', padding: '10px 18px calc(18px + var(--navbar-inset))',
        transform: open ? 'translateY(0)' : 'translateY(102%)', transition: 'transform var(--dur-slow) var(--ease-emphasis)',
        maxHeight: '92%', overflowY: 'auto',
      }}>
        <div style={{ width: 40, height: 4, borderRadius: 2, background: 'var(--border-strong)', margin: '0 auto 14px' }} />
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 18 }}>
          <h2 style={{ margin: 0, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 18, color: 'var(--text-primary)' }}>Add Transaction</h2>
          <button onClick={onClose} aria-label="Close" style={{ border: 'none', background: 'var(--surface-sunken)', width: 30, height: 30, borderRadius: '50%', display: 'grid', placeItems: 'center', cursor: 'pointer', color: 'var(--text-secondary)' }}><span className="material-symbols-rounded" style={{ fontSize: 18 }}>close</span></button>
        </div>

        {/* Amount display */}
        <div style={{ display: 'flex', alignItems: 'baseline', justifyContent: 'center', gap: 6, padding: '12px 0 20px' }}>
          <Dirham size={30} color={typeColor} />
          <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, fontSize: 44, letterSpacing: '-0.02em', color: typeColor, fontVariantNumeric: 'tabular-nums' }}>{amount || '0.00'}</span>
        </div>

        <SegmentedControl value={type} onChange={setType} options={[
          { value: 'expense', label: 'Expense', tone: 'expense' },
          { value: 'income', label: 'Income', tone: 'income' },
          { value: 'savings', label: 'Savings', tone: 'savings' },
          { value: 'investment', label: 'Invest', tone: 'investment' },
        ]} />

        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 14, color: 'var(--text-secondary)', margin: '18px 0 8px' }}>Brand</div>
        <div style={{ display: 'flex', gap: 8, overflowX: 'auto', paddingBottom: 4 }}>
          {M.BRANDS.slice(0, 6).map(b => {
            const color = (M.CATEGORIES.find(c => c.id === b.category) || {}).color;
            return <Chip key={b.id} selected={brand === b.id} color={color} onClick={() => setBrand(b.id)}>{b.name}</Chip>;
          })}
          <Chip leadingIcon="add">New</Chip>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginTop: 18 }}>
          <div>
            <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 14, color: 'var(--text-secondary)', marginBottom: 8 }}>Date</div>
            <button style={{ width: '100%', height: 48, display: 'flex', alignItems: 'center', gap: 8, padding: '0 14px', border: '1px solid var(--border)', borderRadius: 'var(--r-md)', background: 'var(--surface)', cursor: 'pointer', fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}><span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-tertiary)' }}>calendar_today</span>Today</button>
          </div>
          <Input label="Amount" value={amount} onChange={e => setAmount(e.target.value)} leadingIcon="payments" />
        </div>

        <div style={{ marginTop: 16 }}><Input label="Note" multiline rows={2} placeholder="Add a note…" value="Weekly groceries" /></div>

        <div style={{ display: 'flex', gap: 12, marginTop: 22 }}>
          <Button variant="secondary" fullWidth onClick={onClose}>Cancel</Button>
          <Button fullWidth onClick={onClose}>Save</Button>
        </div>
      </div>
    </div>
  );
}
window.HisabakAddSheet = AddTransactionSheet;
