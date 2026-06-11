/* SMS Inbox — auto-import status, paste & parse, message list. */
function SmsInbox() {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, Button, SearchBar, StatusChip, AmountText, Input, Badge } = NS;
  const M = window.HisabakMock;
  const [autoOn, setAutoOn] = React.useState(false);
  const [q, setQ] = React.useState('');

  const list = M.SMS.filter(s => s.body.toLowerCase().includes(q.toLowerCase()));

  return (
    <div style={{ padding: '8px 16px 24px' }}>
      {/* Auto-import banner */}
      {autoOn ? (
        <Card variant="tinted" tint="var(--income-soft)" padding={14} style={{ display: 'flex', alignItems: 'center', gap: 12, marginTop: 8 }}>
          <span className="material-symbols-rounded" style={{ color: 'var(--income)' }}>check_circle</span>
          <div style={{ flex: 1 }}><div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14, color: 'var(--text-primary)' }}>Auto-import active</div><div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>New bank SMS are parsed automatically.</div></div>
          <button onClick={() => setAutoOn(false)} style={{ border: 'none', background: 'transparent', color: 'var(--text-secondary)', fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 13, cursor: 'pointer' }}>Disable</button>
        </Card>
      ) : (
        <Card variant="tinted" tint="var(--warning-soft)" padding={14} style={{ display: 'flex', alignItems: 'center', gap: 12, marginTop: 8 }}>
          <span className="material-symbols-rounded" style={{ color: 'var(--warning)' }}>error</span>
          <div style={{ flex: 1 }}><div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14, color: 'var(--text-primary)' }}>Auto-import is disabled</div><div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>Turn it on to log transactions from SMS.</div></div>
          <Button size="sm" onClick={() => setAutoOn(true)}>Enable</Button>
        </Card>
      )}

      {/* Paste & parse */}
      <Card padding={16} style={{ marginTop: 12 }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)', marginBottom: 10 }}>Paste an SMS</div>
        <Input multiline rows={2} value="Purchase of SAR 89.00 at TALABAT on 31/05." placeholder="Paste a bank message…" />
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 12 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <Badge tone="info" dot>Talabat</Badge>
            <AmountText value={89} tone="expense" sign="never" size={15} />
          </div>
          <Button size="sm" leadingIcon="download">Parse &amp; Import</Button>
        </div>
      </Card>

      <div style={{ marginTop: 14 }}><SearchBar value={q} placeholder="Search messages" onChange={e => setQ(e.target.value)} onClear={() => setQ('')} /></div>

      {/* SMS list */}
      <div style={{ marginTop: 14, display: 'flex', flexDirection: 'column', gap: 10 }}>
        {list.map(s => (
          <Card key={s.id} padding={14}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 8 }}>
              <StatusChip status={s.status} />
              <span style={{ fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-tertiary)' }}>{s.time}</span>
            </div>
            <div style={{ fontFamily: 'var(--font-mono)', fontSize: 12.5, lineHeight: 1.5, color: 'var(--text-secondary)', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>{s.body}</div>
            {s.status !== 'unparsed' && (
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 12, paddingTop: 10, borderTop: '1px solid var(--divider)' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <span style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14, color: 'var(--text-primary)' }}>{s.brand}</span>
                  <AmountText value={-s.amount} tone="expense" sign="never" size={14} />
                </div>
                {s.status === 'parsed'
                  ? <Button size="sm" leadingIcon="download">Import</Button>
                  : <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 13, color: 'var(--income)' }}><span className="material-symbols-rounded" style={{ fontSize: 16 }}>check</span>Imported</span>}
              </div>
            )}
          </Card>
        ))}
      </div>
    </div>
  );
}
window.HisabakSms = SmsInbox;
