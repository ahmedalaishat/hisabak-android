/* Settings — clean, grouped layout. Real app content only: passphrase reminder, then a
   "Backup & security" group (app lock + backup) and a "Preferences" group (theme + language).
   Each group is one card with a neutral leading icon tile; theme uses a visual preview selector. */
function Settings({ theme, onTheme, onOpenBackup, showReminder, onCheckPassphrase }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, SegmentedControl } = NS;
  const { Toggle } = window.HisabakExtras;
  const [sel, setSel] = React.useState(theme || 'system');
  const [lang, setLang] = React.useState('en');
  const [appLock, setAppLock] = React.useState(true);
  const [remembered, setRemembered] = React.useState(false);
  const reminderVisible = showReminder && !remembered;

  const Tile = ({ icon }) => (
    <span style={{ width: 38, height: 38, borderRadius: 11, background: 'var(--surface-sunken)', display: 'grid', placeItems: 'center', flex: 'none' }}>
      <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-secondary)' }}>{icon}</span>
    </span>
  );

  const Head = ({ icon, title, hint, trailing, onClick }) => (
    <div onClick={onClick} style={{ display: 'flex', alignItems: 'center', gap: 12, cursor: onClick ? 'pointer' : 'default' }}>
      <Tile icon={icon} />
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)' }}>{title}</div>
        {hint && <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)', marginTop: 1 }}>{hint}</div>}
      </div>
      {trailing}
    </div>
  );

  const Label = ({ children }) => (
    <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 12, letterSpacing: '0.05em', textTransform: 'uppercase', color: 'var(--text-secondary)', padding: '6px 2px 0' }}>{children}</div>
  );

  return (
    <div style={{ padding: '14px 16px 28px', display: 'flex', flexDirection: 'column', gap: 14 }}>
      {reminderVisible && (
        <Card padding={0}>
          <div style={{ padding: 16, display: 'flex', flexDirection: 'column', gap: 10 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <span style={{ width: 26, height: 26, borderRadius: 999, background: 'var(--danger)', display: 'grid', placeItems: 'center' }}>
                <span className="material-symbols-rounded" style={{ fontSize: 16, color: '#fff' }}>priority_high</span>
              </span>
              <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)' }}>Do you still remember your passphrase?</div>
            </div>
            <div style={{ fontFamily: 'var(--font-sans)', fontSize: 13.5, color: 'var(--text-secondary)' }}>It's the only key to your encrypted backup. There's no way to recover it if it's lost.</div>
          </div>
          <div style={{ height: 1, background: 'var(--divider)' }} />
          <div onClick={() => setRemembered(true)} style={{ padding: '14px 16px', fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--accent)', cursor: 'pointer' }}>Yes, I remember it</div>
          <div style={{ height: 1, background: 'var(--divider)' }} />
          <div onClick={onCheckPassphrase} style={{ padding: '14px 16px', fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--accent)', cursor: 'pointer' }}>Check my passphrase</div>
        </Card>
      )}

      <Label>Backup &amp; security</Label>
      <Card>
        <Head icon="lock" title="App lock" hint="Require fingerprint, face, or PIN to open Hisabak"
          trailing={<Toggle checked={appLock} onChange={setAppLock} />} />
      </Card>
      <Card onClick={onOpenBackup} style={{ cursor: 'pointer' }}>
        <Head icon="cloud_sync" title="Backup & restore" hint="On · Weekly · last backup 2h ago"
          trailing={<span className="material-symbols-rounded" style={{ fontSize: 22, color: 'var(--text-tertiary)' }}>chevron_right</span>} />
      </Card>

      <Label>Preferences</Label>
      <Card>
        <Head icon="palette" title="Theme" hint="Choose how Hisabak looks" />
        <div style={{ marginTop: 14 }}>
          <SegmentedControl
            options={[{ value: 'system', label: 'System' }, { value: 'light', label: 'Light' }, { value: 'dark', label: 'Dark' }]}
            value={sel}
            onChange={(v) => { setSel(v); onTheme && onTheme(v); }}
          />
        </div>
      </Card>
      <Card>
        <Head icon="translate" title="Language" hint="Numbers follow your language" />
        <div style={{ marginTop: 14 }}>
          <SegmentedControl options={[{ value: 'en', label: 'English' }, { value: 'ar', label: 'العربية' }]} value={lang} onChange={setLang} />
        </div>
      </Card>
    </div>
  );
}
window.HisabakSettings = Settings;
