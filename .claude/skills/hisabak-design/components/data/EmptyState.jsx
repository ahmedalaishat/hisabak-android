import React from 'react';
import { Button } from '../core/Button.jsx';

/**
 * Empty state — big tinted icon, title, guidance, and a CTA. Every list screen
 * gets one. Keep copy short and action-oriented.
 */
export function EmptyState({
  icon = 'inbox',
  title = 'Nothing here yet',
  description,
  actionLabel,
  onAction,
  style,
  ...rest
}) {
  return (
    <div
      style={{
        display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center',
        gap: 12, padding: '40px 24px',
        ...style,
      }}
      {...rest}
    >
      <span style={{ display: 'grid', placeItems: 'center', width: 72, height: 72, borderRadius: 'var(--r-lg)', background: 'var(--accent-soft)', color: 'var(--accent)' }}>
        <span className="material-symbols-rounded" style={{ fontSize: 36 }}>{icon}</span>
      </span>
      <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 17, color: 'var(--text-primary)' }}>{title}</div>
      {description && <div style={{ fontFamily: 'var(--font-sans)', fontSize: 14, lineHeight: 1.5, color: 'var(--text-secondary)', maxWidth: 260 }}>{description}</div>}
      {actionLabel && <Button leadingIcon="add" onClick={onAction} style={{ marginTop: 6 }}>{actionLabel}</Button>}
    </div>
  );
}
