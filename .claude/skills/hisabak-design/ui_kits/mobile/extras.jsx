/* Shared screen-level helpers the component bundle doesn't ship (Toggle, SettingsRow, FormSection,
   RadioRow, HeroDisc). Kept here so the new screens compose consistently. Exposed on window.HisabakExtras. */
(function () {
  function Toggle({ checked, onChange, disabled }) {
    return (
      <button
        type="button"
        role="switch"
        aria-checked={checked}
        disabled={disabled}
        onClick={() => !disabled && onChange && onChange(!checked)}
        style={{
          width: 46, height: 28, flex: 'none', borderRadius: 999, border: 'none', cursor: disabled ? 'default' : 'pointer',
          padding: 3, background: checked ? 'var(--accent)' : 'var(--border-strong)',
          opacity: disabled ? 0.45 : 1, transition: 'background var(--dur-base)', position: 'relative',
        }}
      >
        <span style={{
          display: 'block', width: 22, height: 22, borderRadius: 999, background: '#fff',
          transform: checked ? 'translateX(18px)' : 'translateX(0)', transition: 'transform var(--dur-base)',
          boxShadow: '0 1px 3px rgba(0,0,0,0.25)',
        }} />
      </button>
    );
  }

  function SettingsRow({ icon, title, subtitle, value, onClick, trailing, divider }) {
    return (
      <div>
        <div onClick={onClick} style={{
          display: 'flex', alignItems: 'center', gap: 14, padding: '14px 16px',
          cursor: onClick ? 'pointer' : 'default',
        }}>
          {icon && <span className="material-symbols-rounded" style={{ fontSize: 22, color: 'var(--text-secondary)' }}>{icon}</span>}
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>{title}</div>
            {subtitle && <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)', marginTop: 2 }}>{subtitle}</div>}
          </div>
          {trailing}
          {!trailing && onClick && (
            <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
              {value && <span style={{ fontFamily: 'var(--font-sans)', fontSize: 13.5, color: 'var(--text-secondary)' }}>{value}</span>}
              <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-tertiary)' }}>chevron_right</span>
            </div>
          )}
        </div>
        {divider && <div style={{ height: 1, background: 'var(--divider)', marginLeft: icon ? 52 : 16 }} />}
      </div>
    );
  }

  function FormSection({ label, children, style }) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 10, ...style }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 13.5, color: 'var(--text-secondary)' }}>{label}</div>
        {children}
      </div>
    );
  }

  function RadioRow({ selected, label, onClick }) {
    return (
      <div onClick={onClick} style={{ display: 'flex', alignItems: 'center', gap: 14, padding: '12px 16px', cursor: 'pointer' }}>
        <span style={{
          width: 20, height: 20, borderRadius: 999, flex: 'none',
          border: `2px solid ${selected ? 'var(--accent)' : 'var(--border-strong)'}`,
          display: 'grid', placeItems: 'center',
        }}>
          {selected && <span style={{ width: 10, height: 10, borderRadius: 999, background: 'var(--accent)' }} />}
        </span>
        <span style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>{label}</span>
      </div>
    );
  }

  function Sheet({ open, onClose, children }) {
    return (
      <div style={{ position: 'absolute', inset: 0, zIndex: 40, pointerEvents: open ? 'auto' : 'none' }}>
        <div onClick={onClose} style={{ position: 'absolute', inset: 0, background: 'var(--scrim)', opacity: open ? 1 : 0, transition: 'opacity var(--dur-base)' }} />
        <div style={{
          position: 'absolute', left: 0, right: 0, bottom: 0, background: 'var(--surface)',
          borderTopLeftRadius: 'var(--r-xl)', borderTopRightRadius: 'var(--r-xl)', boxShadow: 'var(--shadow-lg)',
          padding: '10px 0 calc(16px + var(--navbar-inset))',
          transform: open ? 'translateY(0)' : 'translateY(102%)', transition: 'transform var(--dur-slow) var(--ease-emphasis)',
          maxHeight: '92%', overflowY: 'auto',
        }}>
          <div style={{ width: 40, height: 4, borderRadius: 2, background: 'var(--border-strong)', margin: '0 auto 14px' }} />
          {children}
        </div>
      </div>
    );
  }

  function HeroDisc({ icon, size = 88, iconSize = 44, tint = 'var(--accent-soft)', fg = 'var(--accent)' }) {
    return (
      <div style={{ width: size, height: size, borderRadius: 999, background: tint, display: 'grid', placeItems: 'center' }}>
        <span className="material-symbols-rounded" style={{ fontSize: iconSize, color: fg }}>{icon}</span>
      </div>
    );
  }

  window.HisabakExtras = { Toggle, SettingsRow, FormSection, RadioRow, HeroDisc, Sheet };
})();
