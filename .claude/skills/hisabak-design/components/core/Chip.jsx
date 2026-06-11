import React from 'react';

/**
 * Filter / selection chip. Used for period filters, type filters, brand pickers.
 * Pass `color` (a category hex/var) to render a leading color dot.
 */
export function Chip({
  children,
  selected = false,
  color,
  leadingIcon,
  onClick,
  style,
  ...rest
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 7,
        height: 36,
        padding: color || leadingIcon ? '0 14px 0 10px' : '0 14px',
        borderRadius: 'var(--r-pill)',
        fontFamily: 'var(--font-sans)',
        fontWeight: 500,
        fontSize: 14,
        lineHeight: 1,
        cursor: 'pointer',
        whiteSpace: 'nowrap',
        transition: 'background var(--dur-fast), border-color var(--dur-fast), color var(--dur-fast)',
        WebkitTapHighlightColor: 'transparent',
        background: selected ? 'var(--accent)' : 'var(--surface)',
        color: selected ? 'var(--accent-on)' : 'var(--text-secondary)',
        border: selected ? '1px solid transparent' : '1px solid var(--border)',
        ...style,
      }}
      {...rest}
    >
      {color && (
        <span style={{ width: 8, height: 8, borderRadius: '50%', background: color, flex: 'none' }} />
      )}
      {leadingIcon && <span className="material-symbols-rounded" style={{ fontSize: 18 }}>{leadingIcon}</span>}
      {children}
    </button>
  );
}
