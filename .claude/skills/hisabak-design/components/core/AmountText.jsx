import React from 'react';

/**
 * Money display with tabular figures and signed coloring.
 * Renders "+AED 8,200.00" green for income, "−AED 342.75" coral for expense.
 * Pass `value` as a number (major units) or a preformatted string via `text`.
 */
export function AmountText({
  value,
  text,
  currency = 'AED',
  sign = 'auto',          // 'auto' | 'always' | 'never'
  tone = 'auto',          // 'auto' | 'income' | 'expense' | 'savings' | 'investment' | 'neutral'
  size = 16,
  weight = 600,
  style,
  ...rest
}) {
  const n = typeof value === 'number' ? value : 0;
  const resolvedTone = tone === 'auto' ? (n < 0 ? 'expense' : 'income') : tone;
  const colors = {
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)',
    neutral: 'var(--text-primary)',
  };
  const abs = Math.abs(n).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  let prefix = '';
  if (sign === 'always' || (sign === 'auto' && tone !== 'neutral')) prefix = n < 0 ? '−' : '+';
  const body = text != null ? text : `${prefix}${currency} ${abs}`;
  return (
    <span
      style={{
        fontFamily: 'var(--font-mono)',
        fontVariantNumeric: 'tabular-nums',
        fontWeight: weight,
        fontSize: size,
        letterSpacing: '-0.02em',
        lineHeight: 1.1,
        color: colors[resolvedTone] || 'var(--text-primary)',
        whiteSpace: 'nowrap',
        ...style,
      }}
      {...rest}
    >
      {body}
    </span>
  );
}
