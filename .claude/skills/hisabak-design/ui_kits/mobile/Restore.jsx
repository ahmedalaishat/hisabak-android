/* Restore — one-time post-onboarding offer to bring data back from Drive (mirrors RestoreScreen.kt).
   Two panes: Intro (connect / skip) and Passphrase (the found backup is encrypted → enter it).
   "Connect" advances to the passphrase pane; "Restore" hands off to the Sync flow via onConnect. */
function Restore({ onConnect, onSkip }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Button, Input } = NS;
  const { HeroDisc } = window.HisabakExtras;
  const [view, setView] = React.useState('intro');
  const [pass, setPass] = React.useState('');

  const Pane = ({ icon, overline, title, subtitle, children, primary, primaryEnabled = true, onPrimary }) => (
    <div style={{ position: 'absolute', inset: 0, background: 'var(--bg)', display: 'flex', flexDirection: 'column', padding: '0 28px', zIndex: 30 }}>
      <div style={{ flex: 1, display: 'grid', placeItems: 'center' }}>
        <HeroDisc icon={icon} size={120} iconSize={56} tint="var(--accent-soft)" />
      </div>
      <div style={{ paddingBottom: 8 }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 12, letterSpacing: '0.06em', textTransform: 'uppercase', color: 'var(--accent)' }}>{overline}</div>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 26, color: 'var(--text-primary)', marginTop: 12, letterSpacing: '-0.02em' }}>{title}</div>
        <div style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-secondary)', marginTop: 12, lineHeight: 1.5 }}>{subtitle}</div>
        {children}
      </div>
      <div style={{ paddingBottom: 28, paddingTop: 20, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <Button fullWidth disabled={!primaryEnabled} onClick={onPrimary}>{primary}</Button>
        <Button fullWidth variant="ghost" onClick={onSkip}>Not now</Button>
      </div>
    </div>
  );

  if (view === 'passphrase') {
    return (
      <Pane icon="lock" overline="Restore" title="Enter your passphrase"
        subtitle="This backup is encrypted. Enter the passphrase you set to unlock it."
        primary="Restore" primaryEnabled={pass.length > 0} onPrimary={onConnect}>
        <Input label="Passphrase" type="password" value={pass} onChange={e => setPass(e.target.value)} style={{ marginTop: 18 }} />
      </Pane>
    );
  }

  return (
    <Pane icon="cloud_download" overline="Restore" title="Bring your data back"
      subtitle="Connect the Google account you backed up with and we'll restore your transactions, brands, and categories."
      primary="Connect Google Drive" onPrimary={() => setView('passphrase')} />
  );
}
window.HisabakRestore = Restore;
