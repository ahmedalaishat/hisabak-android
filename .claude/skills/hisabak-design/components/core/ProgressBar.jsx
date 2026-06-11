import React from 'react';

/**
 * Thin progress / ratio bar. Used under the balance hero (income ratio) and
 * on category summaries. `tone` colors the filled portion.
 */
export function ProgressBar({ value = 0, tone = 'income', height = 6, track, style, ...rest }) {
  const colors = {
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)',
    accent: 'var(--accent)',
  };
  const pct = Math.max(0, Math.min(100, value));
  return (
    <div
      style={{
        width: '100%',
        height,
        borderRadius: 'var(--r-pill)',
        background: track || 'var(--surface-sunken)',
        overflow: 'hidden',
        ...style,
      }}
      {...rest}
    >
      <div
        style={{
          width: pct + '%',
          height: '100%',
          borderRadius: 'var(--r-pill)',
          background: colors[tone] || colors.income,
          transition: 'width var(--dur-slow) var(--ease-standard)',
        }}
      />
    </div>
  );
}
