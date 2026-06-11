import React from 'react';

/**
 * Hisabak primary button. Pill-shaped, calm, confident.
 * Variants: primary (green CTA), secondary (outlined), ghost (text), danger.
 */
export function Button({
  children,
  variant = 'primary',
  size = 'md',
  fullWidth = false,
  disabled = false,
  leadingIcon,
  trailingIcon,
  onClick,
  style,
  ...rest
}) {
  const sizes = {
    sm: { height: 36, padding: '0 14px', font: 14 },
    md: { height: 48, padding: '0 20px', font: 15 },
    lg: { height: 52, padding: '0 24px', font: 16 },
  };
  const s = sizes[size] || sizes.md;

  const variants = {
    primary: {
      background: 'var(--accent)',
      color: 'var(--accent-on)',
      boxShadow: 'var(--shadow-accent)',
      border: '1px solid transparent',
    },
    secondary: {
      background: 'var(--surface)',
      color: 'var(--text-primary)',
      border: '1px solid var(--border-strong)',
    },
    ghost: {
      background: 'transparent',
      color: 'var(--accent-hover)',
      border: '1px solid transparent',
    },
    danger: {
      background: 'var(--danger)',
      color: '#fff',
      border: '1px solid transparent',
    },
  };

  return (
    <button
      type="button"
      disabled={disabled}
      onClick={onClick}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        height: s.height,
        padding: s.padding,
        width: fullWidth ? '100%' : 'auto',
        borderRadius: 'var(--r-pill)',
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: s.font,
        lineHeight: 1,
        cursor: disabled ? 'not-allowed' : 'pointer',
        opacity: disabled ? 0.45 : 1,
        transition: 'transform var(--dur-fast) var(--ease-standard), background var(--dur-fast), filter var(--dur-fast)',
        WebkitTapHighlightColor: 'transparent',
        ...variants[variant],
        ...style,
      }}
      onMouseDown={(e) => { if (!disabled) e.currentTarget.style.transform = 'scale(var(--press-scale))'; }}
      onMouseUp={(e) => { e.currentTarget.style.transform = 'scale(1)'; }}
      onMouseLeave={(e) => { e.currentTarget.style.transform = 'scale(1)'; }}
      {...rest}
    >
      {leadingIcon && <span className="material-symbols-rounded" style={{ fontSize: 20 }}>{leadingIcon}</span>}
      {children}
      {trailingIcon && <span className="material-symbols-rounded" style={{ fontSize: 20 }}>{trailingIcon}</span>}
    </button>
  );
}
