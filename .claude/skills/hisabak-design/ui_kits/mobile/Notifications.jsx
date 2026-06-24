/* Notifications — list of alerts (budget limits, backup results) with unread dots, "Mark all read",
   and a real empty state. Mirrors NotificationsScreen.kt. Swipe-to-dismiss is shown as a hint row. */
function Notifications() {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, EmptyState } = NS;
  const M = window.HisabakMock;
  const [rows, setRows] = React.useState(M.NOTIFICATIONS);

  if (rows.length === 0) {
    return (
      <div style={{ height: '100%', display: 'grid', placeItems: 'center', padding: 24 }}>
        <EmptyState icon="notifications_none" title="No notifications yet"
          description="Budget alerts and backup updates will show up here." />
      </div>
    );
  }

  const hasUnread = rows.some(r => !r.read);

  return (
    <div style={{ padding: '12px 16px 28px', display: 'flex', flexDirection: 'column', gap: 12 }}>
      {hasUnread && (
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <span onClick={() => setRows(rs => rs.map(r => ({ ...r, read: true })))}
            style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 13.5, color: 'var(--accent)', cursor: 'pointer', padding: 4 }}>Mark all read</span>
        </div>
      )}
      {rows.map(r => {
        const tint = r.tone === 'expense' ? ['var(--expense-soft)', 'var(--expense)']
          : r.tone === 'warning' ? ['var(--warning-soft)', 'var(--warning)']
          : ['var(--accent-soft)', 'var(--accent)'];
        return (
          <Card key={r.id} padding={14}>
            <div style={{ display: 'flex', gap: 14, alignItems: 'flex-start' }}>
              <span style={{ width: 40, height: 40, flex: 'none', borderRadius: 999, background: tint[0], display: 'grid', placeItems: 'center' }}>
                <span className="material-symbols-rounded" style={{ fontSize: 20, color: tint[1] }}>{r.icon}</span>
              </span>
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ fontFamily: 'var(--font-sans)', fontWeight: r.read ? 500 : 600, fontSize: 15, color: 'var(--text-primary)' }}>{r.title}</div>
                <div style={{ fontFamily: 'var(--font-sans)', fontSize: 13.5, color: 'var(--text-secondary)', marginTop: 2 }}>{r.message}</div>
                <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-tertiary)', marginTop: 6 }}>{r.time}</div>
              </div>
              {!r.read && <span style={{ width: 9, height: 9, flex: 'none', borderRadius: 999, background: 'var(--accent)', marginTop: 6 }} />}
            </div>
          </Card>
        );
      })}
    </div>
  );
}
window.HisabakNotifications = Notifications;
