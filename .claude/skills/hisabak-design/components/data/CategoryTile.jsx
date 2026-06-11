import React from 'react';
import { CategoryIcon } from './CategoryIcon.jsx';
import { Badge } from '../core/Badge.jsx';

/**
 * Category grid tile (2-column grid on the Categories screen).
 * Colored icon, name, type badge, monthly total, and a delete affordance.
 * Pass `addNew` to render the dashed "Add New" placeholder tile.
 */
export function CategoryTile({
  name,
  icon = 'category',
  color = 'var(--cat-gray)',
  type = 'expense',          // income|expense|savings|investment
  total,                     // formatted string e.g. "SAR 1,240"
  onDelete,
  onClick,
  addNew = false,
  style,
  ...rest
}) {
  if (addNew) {
    return (
      <button
        type="button"
        onClick={onClick}
        style={{
          display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 8,
          minHeight: 132, padding: 16, cursor: 'pointer',
          border: '1.5px dashed var(--border-strong)', borderRadius: 'var(--r-md)',
          background: 'transparent', color: 'var(--text-secondary)',
          fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14,
          ...style,
        }}
        {...rest}
      >
        <span className="material-symbols-rounded" style={{ fontSize: 26 }}>add</span>
        Add New
      </button>
    );
  }
  const typeTone = { income: 'income', expense: 'expense', savings: 'savings', investment: 'investment' }[type] || 'neutral';
  return (
    <div
      onClick={onClick}
      style={{
        position: 'relative', display: 'flex', flexDirection: 'column', gap: 10,
        padding: 14, minHeight: 132,
        background: 'var(--surface)', borderRadius: 'var(--r-md)', boxShadow: 'var(--ring-card)',
        cursor: onClick ? 'pointer' : 'default',
        ...style,
      }}
      {...rest}
    >
      {onDelete && (
        <button type="button" aria-label="Delete" onClick={(e) => { e.stopPropagation(); onDelete(); }}
          style={{ position: 'absolute', top: 8, right: 8, display: 'grid', placeItems: 'center', width: 24, height: 24, border: 'none', borderRadius: '50%', background: 'transparent', color: 'var(--text-tertiary)', cursor: 'pointer' }}>
          <span className="material-symbols-rounded" style={{ fontSize: 18 }}>close</span>
        </button>
      )}
      <CategoryIcon icon={icon} color={color} size={40} />
      <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)' }}>{name}</div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 'auto' }}>
        <Badge tone={typeTone} style={{ height: 20, fontSize: 11 }}>{type[0].toUpperCase() + type.slice(1)}</Badge>
        {total && <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, fontSize: 13, color: 'var(--text-secondary)', fontVariantNumeric: 'tabular-nums' }}>{total}</span>}
      </div>
    </div>
  );
}
