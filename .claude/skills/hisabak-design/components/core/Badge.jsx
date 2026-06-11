import React from 'react';

/**
 * Small status/category badge. Tones map to semantic + financial colors.
 * tone: neutral | income | expense | savings | investment | success | warning | danger | info
 */
export function Badge({ children, tone = 'neutral', dot = false, style, ...rest }) {
  const map = {
    neutral:    ['var(--surface-sunken)', 'var(--text-secondary)'],
    income:     ['var(--income-soft)', 'var(--income)'],
    expense:    ['var(--expense-soft)', 'var(--expense)'],
    savings:    ['var(--savings-soft)', 'var(--savings)'],
    investment: ['var(--investment-soft)', 'var(--investment)'],
    success:    ['var(--income-soft)', 'var(--success)'],
    warning:    ['var(--warning-soft)', 'var(--warning)'],
    danger:     ['var(--danger-soft)', 'var(--danger)'],
    info:       ['var(--info-soft)', 'var(--info)'],
  };
  const [bg, fg] = map[tone] || map.neutral;
  return (
    <span
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 6,
        height: 24,
        padding: '0 10px',
        borderRadius: 'var(--r-pill)',
        background: bg,
        color: fg,
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 12,
        lineHeight: 1,
        whiteSpace: 'nowrap',
        ...style,
      }}
      {...rest}
    >
      {dot && <span style={{ width: 6, height: 6, borderRadius: '50%', background: fg }} />}
      {children}
    </span>
  );
}
