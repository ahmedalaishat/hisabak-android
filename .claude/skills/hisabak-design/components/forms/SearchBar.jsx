import React from 'react';

/**
 * Full-width search bar — persistent on list screens.
 */
export function SearchBar({ value, placeholder = 'Search', onChange, onClear, style, ...rest }) {
  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 8,
        height: 44,
        padding: '0 12px',
        background: 'var(--surface-sunken)',
        border: '1px solid transparent',
        borderRadius: 'var(--r-pill)',
        ...style,
      }}
    >
      <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-tertiary)' }}>search</span>
      <input
        value={value}
        placeholder={placeholder}
        onChange={onChange}
        style={{
          flex: 1,
          minWidth: 0,
          border: 'none',
          outline: 'none',
          background: 'transparent',
          fontFamily: 'var(--font-sans)',
          fontSize: 15,
          color: 'var(--text-primary)',
        }}
        {...rest}
      />
      {value ? (
        <button type="button" onClick={onClear} aria-label="Clear" style={{ display: 'grid', placeItems: 'center', width: 24, height: 24, border: 'none', background: 'transparent', cursor: 'pointer', color: 'var(--text-tertiary)' }}>
          <span className="material-symbols-rounded" style={{ fontSize: 18 }}>close</span>
        </button>
      ) : null}
    </div>
  );
}
