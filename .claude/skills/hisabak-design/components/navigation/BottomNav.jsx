import React from 'react';

const DEFAULT_TABS = [
  { value: 'dashboard',    label: 'Dashboard',    icon: 'donut_small' },
  { value: 'transactions', label: 'Transactions', icon: 'receipt_long' },
  { value: 'sms',          label: 'SMS',          icon: 'sms' },
  { value: 'brands',       label: 'Brands',       icon: 'sell' },
  { value: 'categories',   label: 'Categories',   icon: 'category' },
];

/**
 * Bottom navigation — 5 tabs. Inactive tabs use the outlined icon + muted label;
 * the active tab fills the icon and switches to the accent green.
 */
export function BottomNav({ tabs = DEFAULT_TABS, value = 'dashboard', onChange, style, ...rest }) {
  return (
    <nav
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${tabs.length}, 1fr)`,
        alignItems: 'stretch',
        height: 64,
        background: 'var(--nav-bg)',
        borderTop: '1px solid var(--divider)',
        ...style,
      }}
      {...rest}
    >
      {tabs.map((t) => {
        const active = t.value === value;
        return (
          <button
            key={t.value}
            type="button"
            aria-current={active ? 'page' : undefined}
            onClick={() => onChange && onChange(t.value)}
            style={{
              display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 3,
              border: 'none', background: 'transparent', cursor: 'pointer',
              color: active ? 'var(--nav-icon-active)' : 'var(--nav-icon)',
              WebkitTapHighlightColor: 'transparent',
              transition: 'color var(--dur-fast)',
            }}
          >
            <span className={'material-symbols-rounded' + (active ? ' is-filled' : '')} style={{ fontSize: 24 }}>{t.icon}</span>
            <span style={{ fontFamily: 'var(--font-sans)', fontWeight: active ? 600 : 500, fontSize: 11, letterSpacing: '0.01em', color: active ? 'var(--nav-label-active)' : 'var(--nav-label)' }}>{t.label}</span>
          </button>
        );
      })}
    </nav>
  );
}
