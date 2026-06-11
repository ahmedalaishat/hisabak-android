import React from 'react';
import { AmountText } from '../core/AmountText.jsx';

/**
 * List row — transactions, brands, SMS. A leading colored avatar (initial or
 * Material icon), a title + subtitle, and a right slot (amount + meta, or custom).
 */
export function ListRow({
  title,
  subtitle,
  leadingText,           // initial(s) inside the colored circle
  leadingIcon,           // Material Symbols ligature inside the circle
  color = 'var(--cat-gray)',
  amount,                // number → rendered via AmountText
  amountTone = 'auto',
  meta,                  // small text under the amount (e.g. date)
  trailing,              // custom right-side node (overrides amount/meta)
  onClick,
  divider = true,
  style,
  ...rest
}) {
  return (
    <div
      onClick={onClick}
      style={{
        display: 'flex', alignItems: 'center', gap: 12,
        padding: '12px 4px',
        borderBottom: divider ? '1px solid var(--divider)' : 'none',
        cursor: onClick ? 'pointer' : 'default',
        ...style,
      }}
      {...rest}
    >
      <span style={{
        display: 'grid', placeItems: 'center', flex: 'none',
        width: 40, height: 40, borderRadius: '50%',
        background: `color-mix(in srgb, ${color} 14%, transparent)`,
        color,
        fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 15,
      }}>
        {leadingIcon
          ? <span className="material-symbols-rounded" style={{ fontSize: 20 }}>{leadingIcon}</span>
          : (leadingText || '?')}
      </span>

      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 15, color: 'var(--text-primary)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{title}</div>
        {subtitle && <div style={{ fontFamily: 'var(--font-sans)', fontSize: 13, color: 'var(--text-secondary)', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', marginTop: 1 }}>{subtitle}</div>}
      </div>

      {trailing != null ? trailing : (amount != null && (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 2, flex: 'none' }}>
          <AmountText value={amount} tone={amountTone} />
          {meta && <span style={{ fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-tertiary)' }}>{meta}</span>}
        </div>
      ))}
    </div>
  );
}
