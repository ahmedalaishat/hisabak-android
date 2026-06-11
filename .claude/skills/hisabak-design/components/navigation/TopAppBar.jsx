import React from 'react';
import { IconButton } from '../core/IconButton.jsx';
import { Avatar } from '../core/Avatar.jsx';

/**
 * Persistent top app bar. Either a wordmark (left) + avatar & bell (right) for
 * the main shell, or a back arrow + centered title for detail screens.
 */
export function TopAppBar({
  title,
  brand = false,            // show the Hisabak wordmark instead of a title
  onBack,                   // show a back arrow
  showAvatar = false,
  avatarName = '',
  showBell = false,
  onBell,
  onAvatar,
  actions,                  // custom right-side nodes
  style,
  ...rest
}) {
  return (
    <header
      style={{
        display: 'flex', alignItems: 'center', gap: 8,
        height: 56, padding: '0 8px 0 8px',
        background: 'var(--nav-bg)',
        borderBottom: '1px solid var(--divider)',
        ...style,
      }}
      {...rest}
    >
      {onBack && <IconButton icon="arrow_back" ariaLabel="Back" onClick={onBack} />}

      {brand ? (
        <div style={{ display: 'flex', alignItems: 'center', gap: 9, paddingLeft: onBack ? 0 : 8 }}>
          <span style={{ display: 'grid', placeItems: 'center', width: 28, height: 28, borderRadius: 8, background: 'var(--accent)' }}>
            <span className="material-symbols-rounded" style={{ fontSize: 18, color: '#fff' }}>show_chart</span>
          </span>
          <span style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 19, letterSpacing: '-0.02em', color: 'var(--text-primary)' }}>Hisabak</span>
        </div>
      ) : (
        <h1 style={{ flex: 1, margin: 0, paddingLeft: onBack ? 0 : 8, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 18, color: 'var(--text-primary)', textAlign: onBack ? 'center' : 'left' }}>{title}</h1>
      )}

      <div style={{ flex: 1 }} />

      <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
        {actions}
        {showBell && <IconButton icon="notifications" ariaLabel="Notifications" onClick={onBell} />}
        {showAvatar && (
          <button type="button" onClick={onAvatar} aria-label="Account" style={{ border: 'none', background: 'transparent', padding: 2, cursor: 'pointer' }}>
            <Avatar name={avatarName} size={34} />
          </button>
        )}
      </div>
    </header>
  );
}
