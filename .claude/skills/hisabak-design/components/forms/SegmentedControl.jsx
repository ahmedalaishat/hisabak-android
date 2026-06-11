import React from 'react';

/**
 * Segmented control — for the Expense/Income/Savings/Investment type toggle and
 * period selectors. Each option may carry its own active color via `tones`.
 * options: [{ value, label, tone? }]  tone ∈ income|expense|savings|investment|accent
 */
export function SegmentedControl({ options = [], value, onChange, style, ...rest }) {
  const toneColor = {
    income: 'var(--income)', expense: 'var(--expense)',
    savings: 'var(--savings)', investment: 'var(--investment)',
    accent: 'var(--accent)',
  };
  return (
    <div
      role="tablist"
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${options.length}, 1fr)`,
        gap: 4,
        padding: 4,
        background: 'var(--surface-sunken)',
        borderRadius: 'var(--r-md)',
        ...style,
      }}
      {...rest}
    >
      {options.map((o) => {
        const active = o.value === value;
        const accent = toneColor[o.tone] || 'var(--accent)';
        return (
          <button
            key={o.value}
            type="button"
            role="tab"
            aria-selected={active}
            onClick={() => onChange && onChange(o.value)}
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: 6,
              height: 38,
              border: 'none',
              borderRadius: 'var(--r-sm)',
              cursor: 'pointer',
              fontFamily: 'var(--font-sans)',
              fontWeight: 600,
              fontSize: 14,
              transition: 'all var(--dur-fast) var(--ease-standard)',
              background: active ? 'var(--surface)' : 'transparent',
              color: active ? accent : 'var(--text-secondary)',
              boxShadow: active ? 'var(--shadow-xs)' : 'none',
            }}
          >
            {o.icon && <span className="material-symbols-rounded" style={{ fontSize: 18 }}>{o.icon}</span>}
            {o.label}
          </button>
        );
      })}
    </div>
  );
}
