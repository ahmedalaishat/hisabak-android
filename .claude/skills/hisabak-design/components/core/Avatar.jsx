import React from 'react';

/**
 * User avatar — initials on a tinted circle, or an image via `src`.
 */
export function Avatar({ name = '', src, size = 36, style, ...rest }) {
  const initials = name
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((w) => w[0])
    .join('')
    .toUpperCase();
  return (
    <div
      style={{
        width: size,
        height: size,
        flex: 'none',
        borderRadius: '50%',
        overflow: 'hidden',
        display: 'grid',
        placeItems: 'center',
        background: 'var(--accent-soft)',
        color: 'var(--accent-hover)',
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: size * 0.4,
        lineHeight: 1,
        boxShadow: 'inset 0 0 0 1px var(--border)',
        ...style,
      }}
      {...rest}
    >
      {src ? (
        <img src={src} alt={name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
      ) : (
        initials || '?'
      )}
    </div>
  );
}
