import React from 'react';

/**
 * Circular icon-only button for app bars and rows. Material Symbols icon.
 */
export function IconButton({
  icon,
  size = 'md',
  variant = 'plain',
  filled = false,
  ariaLabel,
  disabled = false,
  onClick,
  style,
  ...rest
}) {
  const dims = { sm: 32, md: 40, lg: 44 };
  const iconSize = { sm: 18, md: 22, lg: 24 };
  const d = dims[size] || dims.md;

  const variants = {
    plain: { background: 'transparent', color: 'var(--text-secondary)' },
    soft: { background: 'var(--surface-sunken)', color: 'var(--text-primary)' },
    accent: { background: 'var(--accent-soft)', color: 'var(--accent-hover)' },
  };

  return (
    <button
      type="button"
      aria-label={ariaLabel}
      disabled={disabled}
      onClick={onClick}
      style={{
        display: 'inline-grid',
        placeItems: 'center',
        width: d,
        height: d,
        flex: 'none',
        border: 'none',
        borderRadius: 'var(--r-pill)',
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.4 : 1,
        transition: 'background var(--dur-fast), transform var(--dur-fast)',
        WebkitTapHighlightColor: 'transparent',
        ...variants[variant],
        ...style,
      }}
      onMouseDown={(e) => { e.currentTarget.style.transform = 'scale(0.92)'; }}
      onMouseUp={(e) => { e.currentTarget.style.transform = 'scale(1)'; }}
      onMouseLeave={(e) => { e.currentTarget.style.transform = 'scale(1)'; }}
      {...rest}
    >
      <span className={'material-symbols-rounded' + (filled ? ' is-filled' : '')} style={{ fontSize: iconSize[size] || 22 }}>{icon}</span>
    </button>
  );
}
