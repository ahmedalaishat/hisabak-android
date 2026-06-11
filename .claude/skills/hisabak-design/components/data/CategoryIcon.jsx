import React from 'react';

/**
 * Category icon tile — a Material icon on a tinted rounded square in the
 * category's color. A signature Hisabak pattern; reused in tiles, rows, pickers.
 */
export function CategoryIcon({ icon = 'category', color = 'var(--cat-gray)', size = 44, style, ...rest }) {
  return (
    <span
      style={{
        display: 'grid', placeItems: 'center', flex: 'none',
        width: size, height: size,
        borderRadius: 'var(--r-tile)',
        background: `color-mix(in srgb, ${color} 16%, transparent)`,
        color,
        ...style,
      }}
      {...rest}
    >
      <span className="material-symbols-rounded" style={{ fontSize: size * 0.5 }}>{icon}</span>
    </span>
  );
}
