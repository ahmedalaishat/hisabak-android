import React from 'react';

/**
 * Text input with optional label, leading icon, helper / error text.
 */
export function Input({
  label,
  value,
  placeholder,
  leadingIcon,
  helper,
  error,
  type = 'text',
  multiline = false,
  rows = 3,
  onChange,
  style,
  ...rest
}) {
  const hasError = Boolean(error);
  const field = {
    width: '100%',
    minHeight: multiline ? undefined : 48,
    padding: leadingIcon ? '0 14px 0 42px' : '13px 14px',
    border: `1px solid ${hasError ? 'var(--danger)' : 'var(--border)'}`,
    borderRadius: 'var(--r-md)',
    background: 'var(--surface)',
    color: 'var(--text-primary)',
    fontFamily: 'var(--font-sans)',
    fontSize: 15,
    lineHeight: 1.4,
    outline: 'none',
    transition: 'border-color var(--dur-fast), box-shadow var(--dur-fast)',
    resize: multiline ? 'vertical' : undefined,
    boxSizing: 'border-box',
  };
  const onFocus = (e) => { e.target.style.borderColor = hasError ? 'var(--danger)' : 'var(--accent)'; e.target.style.boxShadow = `0 0 0 3px ${hasError ? 'var(--danger-soft)' : 'var(--focus-ring)'}`; };
  const onBlur = (e) => { e.target.style.borderColor = hasError ? 'var(--danger)' : 'var(--border)'; e.target.style.boxShadow = 'none'; };
  return (
    <label style={{ display: 'flex', flexDirection: 'column', gap: 6, ...style }}>
      {label && <span style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 14, color: 'var(--text-secondary)' }}>{label}</span>}
      <span style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
        {leadingIcon && <span className="material-symbols-rounded" style={{ position: 'absolute', left: 12, fontSize: 20, color: 'var(--text-tertiary)', pointerEvents: 'none' }}>{leadingIcon}</span>}
        {multiline
          ? <textarea rows={rows} value={value} placeholder={placeholder} onChange={onChange} onFocus={onFocus} onBlur={onBlur} style={{ ...field, paddingTop: 12, paddingBottom: 12 }} {...rest} />
          : <input type={type} value={value} placeholder={placeholder} onChange={onChange} onFocus={onFocus} onBlur={onBlur} style={field} {...rest} />}
      </span>
      {(helper || error) && <span style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: hasError ? 'var(--danger)' : 'var(--text-tertiary)' }}>{error || helper}</span>}
    </label>
  );
}
