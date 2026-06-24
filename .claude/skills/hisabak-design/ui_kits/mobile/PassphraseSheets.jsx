/* Passphrase bottom sheets — set/change (from Backup) and verify (from the Settings reminder).
   Mirror PassphraseSheet (BackupScreen.kt) and PassphraseVerifySheet (SettingsScreen.kt). */
(function () {
  const NS = window.HisabakDesignSystem_aa2548;

  function Sheet({ open, onClose, children }) {
    return (
      <div style={{ position: 'absolute', inset: 0, zIndex: 40, pointerEvents: open ? 'auto' : 'none' }}>
        <div onClick={onClose} style={{ position: 'absolute', inset: 0, background: 'var(--scrim)', opacity: open ? 1 : 0, transition: 'opacity var(--dur-base)' }} />
        <div style={{
          position: 'absolute', left: 0, right: 0, bottom: 0, background: 'var(--surface)',
          borderTopLeftRadius: 'var(--r-xl)', borderTopRightRadius: 'var(--r-xl)', boxShadow: 'var(--shadow-lg)',
          padding: '10px 18px calc(20px + var(--navbar-inset))',
          transform: open ? 'translateY(0)' : 'translateY(102%)', transition: 'transform var(--dur-slow) var(--ease-emphasis)',
          maxHeight: '92%', overflowY: 'auto',
        }}>
          <div style={{ width: 40, height: 4, borderRadius: 2, background: 'var(--border-strong)', margin: '0 auto 16px' }} />
          {children}
        </div>
      </div>
    );
  }

  const Title = ({ children }) => <h2 style={{ margin: 0, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 18, color: 'var(--text-primary)' }}>{children}</h2>;
  const Body = ({ children, center }) => <p style={{ margin: 0, fontFamily: 'var(--font-sans)', fontSize: 14, lineHeight: 1.5, color: 'var(--text-secondary)', textAlign: center ? 'center' : 'left' }}>{children}</p>;

  // Set / change the backup passphrase.
  function PassphraseSheet({ open, onClose, onSave }) {
    const { Input, Button } = NS;
    const [pass, setPass] = React.useState('');
    const [confirm, setConfirm] = React.useState('');
    const tooShort = pass.length > 0 && pass.length < 8;
    const mismatch = confirm.length > 0 && confirm !== pass;
    const canSave = pass.length >= 8 && pass === confirm;
    const done = () => { onSave && onSave(); setPass(''); setConfirm(''); };
    return (
      <Sheet open={open} onClose={onClose}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
          <Title>Set a passphrase</Title>
          <Body>This passphrase encrypts your backup. If you lose it, your backup can't be recovered — there's no reset.</Body>
          <Body><span style={{ fontSize: 13, color: 'var(--text-tertiary)' }}>It applies to your next backup onwards.</span></Body>
          <Input label="Passphrase" type="password" value={pass} onChange={e => setPass(e.target.value)}
            error={tooShort ? 'Use at least 8 characters' : undefined} />
          <Input label="Confirm passphrase" type="password" value={confirm} onChange={e => setConfirm(e.target.value)}
            error={mismatch ? "Passphrases don't match" : undefined} />
          <Button fullWidth disabled={!canSave} onClick={done}>Save passphrase</Button>
        </div>
      </Sheet>
    );
  }

  // Verify the existing passphrase (Settings reminder → "Check my passphrase"), with a success state.
  function PassphraseVerifySheet({ open, onClose, onChangePassphrase }) {
    const { Input, Button } = NS;
    const [input, setInput] = React.useState('');
    const [wrong, setWrong] = React.useState(false);
    const [success, setSuccess] = React.useState(false);
    const reset = () => { setInput(''); setWrong(false); setSuccess(false); };
    const close = () => { reset(); onClose && onClose(); };
    // Mock rule so both outcomes are visible: 8+ chars verifies, shorter shows the wrong state.
    const verify = () => (input.length >= 8 ? setSuccess(true) : setWrong(true));
    return (
      <Sheet open={open} onClose={close}>
        {success ? (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 14, paddingBottom: 6 }}>
            <div style={{ width: 72, height: 72, borderRadius: 999, background: 'var(--accent)', display: 'grid', placeItems: 'center', marginTop: 4 }}>
              <span className="material-symbols-rounded" style={{ fontSize: 40, color: '#fff' }}>check</span>
            </div>
            <Title>You remember it</Title>
            <Body center>That's the right passphrase. Your backup is safe and recoverable.</Body>
            <Button fullWidth onClick={close}>Done</Button>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
            <Title>Check your passphrase</Title>
            <Body>Enter your backup passphrase to confirm you still have it. We never store it in readable form.</Body>
            <Input label="Passphrase" type="password" value={input}
              onChange={e => { setInput(e.target.value); setWrong(false); }}
              error={wrong ? "That doesn't match your saved passphrase" : undefined} />
            <Button fullWidth disabled={!input} onClick={verify}>Verify</Button>
            {wrong && (
              <span onClick={onChangePassphrase} style={{ textAlign: 'center', fontFamily: 'var(--font-sans)', fontSize: 14, color: 'var(--accent)', cursor: 'pointer', padding: 4 }}>Change passphrase instead</span>
            )}
          </div>
        )}
      </Sheet>
    );
  }

  window.HisabakPassphraseSheet = PassphraseSheet;
  window.HisabakPassphraseVerifySheet = PassphraseVerifySheet;
})();
