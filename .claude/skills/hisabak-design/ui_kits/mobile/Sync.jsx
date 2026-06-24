/* Sync — full-screen progress/result for a backup or restore (mirrors SyncScreen.kt).
   Phases: running (animated halo), done (check), failed (error + retry). kind = 'backup' | 'restore'. */
function Sync({ kind = 'backup', phase = 'running', onContinue, onRetry, onClose }) {
  const glyph = kind === 'backup' ? 'cloud_upload' : 'cloud_download';
  const NS = window.HisabakDesignSystem_aa2548;
  const { Button } = NS;

  const copy = {
    running: { title: kind === 'backup' ? 'Backing up…' : 'Restoring…', sub: kind === 'backup' ? 'Saving your data to Drive' : 'Bringing your data back' },
    done: { title: kind === 'backup' ? 'Backed up' : 'Restored', sub: kind === 'backup' ? 'Your Drive copy is up to date' : '128 records restored' },
    failed: { title: kind === 'backup' ? "Couldn't back up" : "Couldn't restore", sub: 'Check your connection and try again' },
  }[phase];

  const Halo = () => {
    if (phase === 'done') {
      return (
        <div style={{ width: 150, height: 150, display: 'grid', placeItems: 'center' }}>
          <div style={{ position: 'absolute', width: 132, height: 132, borderRadius: 999, border: '5px solid var(--accent)' }} />
          <div style={{ width: 96, height: 96, borderRadius: 999, background: 'var(--accent)', display: 'grid', placeItems: 'center' }}>
            <span className="material-symbols-rounded" style={{ fontSize: 52, color: '#fff' }}>check</span>
          </div>
        </div>
      );
    }
    if (phase === 'failed') {
      return (
        <div style={{ width: 96, height: 96, borderRadius: 999, background: 'var(--danger-soft)', display: 'grid', placeItems: 'center' }}>
          <span className="material-symbols-rounded" style={{ fontSize: 48, color: 'var(--danger)' }}>error</span>
        </div>
      );
    }
    return (
      <div style={{ width: 150, height: 150, display: 'grid', placeItems: 'center', position: 'relative' }}>
        <div className="sync-pulse" style={{ position: 'absolute', width: 132, height: 132, borderRadius: 999, background: 'var(--accent-soft)' }} />
        <div className="sync-spin" style={{ position: 'absolute', width: 132, height: 132, borderRadius: 999, border: '5px solid var(--accent-soft)', borderTopColor: 'var(--accent)' }} />
        <div style={{ width: 96, height: 96, borderRadius: 999, background: 'var(--accent-soft)', display: 'grid', placeItems: 'center' }}>
          <span className="material-symbols-rounded" style={{ fontSize: 44, color: 'var(--accent)' }}>{glyph}</span>
        </div>
      </div>
    );
  };

  return (
    <div style={{ position: 'absolute', inset: 0, background: 'var(--bg)', display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '0 28px', zIndex: 30 }}>
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 24 }}>
        <Halo />
        <div style={{ textAlign: 'center' }}>
          <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 21, color: 'var(--text-primary)' }}>{copy.title}</div>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 14, color: 'var(--text-secondary)', marginTop: 6 }}>{copy.sub}</div>
        </div>
      </div>
      <div style={{ width: '100%', paddingBottom: 28, display: 'flex', flexDirection: 'column', gap: 8 }}>
        {phase === 'done' && <Button fullWidth onClick={onContinue}>Continue</Button>}
        {phase === 'failed' && <React.Fragment>
          <Button fullWidth onClick={onRetry}>Try again</Button>
          <Button fullWidth variant="ghost" onClick={onClose}>Close</Button>
        </React.Fragment>}
      </div>
    </div>
  );
}
window.HisabakSync = Sync;
