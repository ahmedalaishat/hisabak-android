/* Backup & restore — mirrors BackupScreen.kt. Two states: not-enabled (hero + benefits + "Turn on")
   and enabled (last-backup card, "Back up now", auto-backup period, encryption toggle + passphrase,
   "Turn off backup"). The demo toggles enabled inline; "Back up now" launches the Sync screen. */
function Backup({ onBackupNow, onOpenPassphrase, period = 'Weekly', onOpenPeriod }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, Button } = NS;
  const { Toggle, SettingsRow, HeroDisc } = window.HisabakExtras;
  const [enabled, setEnabled] = React.useState(true);
  const [encrypt, setEncrypt] = React.useState(true);
  const openPass = onOpenPassphrase || (() => {});
  const openPeriod = onOpenPeriod || (() => {});

  const Header = () => (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center', gap: 8 }}>
      <HeroDisc icon="cloud_upload" tint="var(--accent-soft)" />
      <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 21, color: 'var(--text-primary)', marginTop: 8 }}>Back up to Google Drive</div>
      <div style={{ fontFamily: 'var(--font-sans)', fontSize: 14, color: 'var(--text-secondary)', maxWidth: 280 }}>Keep a private copy of your data in your own Drive, ready to restore on any device.</div>
    </div>
  );

  const Benefit = ({ icon, title, sub }) => (
    <div style={{ display: 'flex', gap: 14, alignItems: 'flex-start' }}>
      <span style={{ width: 38, height: 38, flex: 'none', borderRadius: 12, background: 'var(--accent-soft)', display: 'grid', placeItems: 'center' }}>
        <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--accent)' }}>{icon}</span>
      </span>
      <div>
        <div style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>{title}</div>
        <div style={{ fontFamily: 'var(--font-sans)', fontSize: 13.5, color: 'var(--text-secondary)', marginTop: 2 }}>{sub}</div>
      </div>
    </div>
  );

  return (
    <div style={{ padding: '20px 16px 28px', display: 'flex', flexDirection: 'column', gap: 22 }}>
      <Header />

      {!enabled ? (
        <React.Fragment>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 18 }}>
            <Benefit icon="lock" title="Encrypted end to end" sub="Optionally lock your backup with a passphrase only you know." />
            <Benefit icon="cloud_download" title="Restore anywhere" sub="Reinstall and pick up exactly where you left off." />
            <Benefit icon="schedule" title="Automatic backups" sub="Set it once and Hisabak keeps your Drive copy current." />
          </div>
          <Button fullWidth onClick={() => setEnabled(true)}>Turn on backup</Button>
        </React.Fragment>
      ) : (
        <React.Fragment>
          <Card>
            <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
              <span className="material-symbols-rounded" style={{ fontSize: 44, color: 'var(--accent)' }}>cloud_sync</span>
              <div>
                <div style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>Last backup 2 hours ago</div>
                <div style={{ fontFamily: 'var(--font-sans)', fontSize: 13.5, color: 'var(--text-secondary)', marginTop: 2 }}>48 KB</div>
              </div>
            </div>
          </Card>

          <Button fullWidth onClick={onBackupNow}>Back up now</Button>

          <Card padding={0}>
            <SettingsRow icon="schedule" title="Automatic backups"
              subtitle={period === 'Never' ? 'Off — back up manually' : 'Runs quietly in the background'}
              value={period} onClick={openPeriod} divider />
            <SettingsRow icon="lock" title="Encrypt backup" subtitle="Lock it with a passphrase"
              trailing={<Toggle checked={encrypt} onChange={(v) => { setEncrypt(v); if (v) openPass(); }} />} divider={encrypt} />
            {encrypt && (
              <SettingsRow icon="key" title="Passphrase" subtitle="Set — keep it safe" value="Change" onClick={openPass} />
            )}
          </Card>

          <div onClick={() => setEnabled(false)} style={{ textAlign: 'center', padding: 12, fontFamily: 'var(--font-sans)', fontSize: 14, color: 'var(--expense)', cursor: 'pointer' }}>Turn off backup</div>
        </React.Fragment>
      )}
    </div>
  );
}
window.HisabakBackup = Backup;
