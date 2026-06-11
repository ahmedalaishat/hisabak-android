import React from 'react';

/**
 * Surface container. The base card for the app.
 * variant: default (white surface + hairline) | hero (larger radius, soft shadow)
 *          | flat (no shadow) | tinted (pass `tint` color for soft background).
 */
export function Card({ children, variant = 'default', tint, padding = 16, onClick, style, ...rest }) {
  const base = {
    borderRadius: variant === 'hero' ? 'var(--r-lg)' : 'var(--r-md)',
    padding,
    background: 'var(--surface)',
    boxShadow: 'var(--ring-card)',
  };
  const variants = {
    default: {},
    hero: { boxShadow: 'var(--shadow-card)', borderRadius: 'var(--r-lg)' },
    flat: { boxShadow: 'none', border: '1px solid var(--border)' },
    tinted: { background: tint || 'var(--accent-soft)', boxShadow: 'none' },
  };
  return (
    <div
      onClick={onClick}
      style={{
        ...base,
        ...variants[variant],
        cursor: onClick ? 'pointer' : 'default',
        transition: 'box-shadow var(--dur-base), transform var(--dur-fast)',
        ...style,
      }}
      {...rest}
    >
      {children}
    </div>
  );
}
