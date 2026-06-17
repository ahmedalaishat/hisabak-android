import React from 'react';
import { AmountText } from '../core/AmountText.jsx';

/**
 * Compact stat pill / card: icon + label + value, with an optional delta.
 * Use for Total Cash / Savings / Investment and the Income / Expense summaries.
 */
export function StatCard({
  label,
  value,
  currency = 'AED',
  icon,
  tone = 'neutral',          // colors the icon tile + value
  delta,                     // e.g. +12.4  (percent vs last period)
  emphasis = false,          // larger amount for income/expense hero cards
  style,
  ...rest
}) {
  const toneColor = {
    neutral: 'var(--text-primary)', income: 'var(--income)', expense: 'var(--expense)',
    savings: 'var(--savings)', investment: 'var(--investment)',
  };
  const toneSoft = {
    neutral: 'var(--surface-sunken)', income: 'var(--income-soft)', expense: 'var(--expense-soft)',
    savings: 'var(--savings-soft)', investment: 'var(--investment-soft)',
  };
  const deltaUp = (delta || 0) >= 0;
  return (
    <div
      style={{
        display: 'flex', flexDirection: 'column', gap: 10,
        padding: 14, borderRadius: 'var(--r-md)',
        background: 'var(--surface)', boxShadow: 'var(--ring-card)',
        ...style,
      }}
      {...rest}
    >
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        {icon && (
          <span style={{ display: 'grid', placeItems: 'center', width: 32, height: 32, borderRadius: 'var(--r-sm)', background: toneSoft[tone], color: toneColor[tone] }}>
            <span className="material-symbols-rounded" style={{ fontSize: 18 }}>{icon}</span>
          </span>
        )}
        {delta != null && (
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 2, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 12.5, color: deltaUp ? 'var(--income)' : 'var(--expense)' }}>
            <span className="material-symbols-rounded" style={{ fontSize: 16 }}>{deltaUp ? 'arrow_upward' : 'arrow_downward'}</span>
            {Math.abs(delta)}%
          </span>
        )}
      </div>
      <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 13, color: 'var(--text-secondary)' }}>{label}</div>
      <AmountText
        value={value}
        currency={currency}
        sign="never"
        tone={tone === 'neutral' ? 'neutral' : tone}
        size={emphasis ? 26 : 18}
        weight={700}
      />
    </div>
  );
}
