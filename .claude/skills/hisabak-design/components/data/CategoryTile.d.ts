import * as React from 'react';

/**
 * Category grid tile — colored icon, name, type badge, total. `addNew` renders the dashed placeholder.
 *
 * @startingPoint section="Lists" subtitle="Category grid tiles with icon + color" viewport="700x320"
 */
export interface CategoryTileProps {
  name?: string;
  /** Material Symbols ligature. @default "category" */
  icon?: string;
  /** Category color. @default "var(--cat-gray)" */
  color?: string;
  /** @default "expense" */
  type?: 'income' | 'expense' | 'savings' | 'investment';
  /** Preformatted monthly total, e.g. "SAR 1,240". */
  total?: string;
  onDelete?: () => void;
  onClick?: (e: React.MouseEvent) => void;
  /** Render the dashed "Add New" placeholder instead. @default false */
  addNew?: boolean;
  style?: React.CSSProperties;
}
export function CategoryTile(props: CategoryTileProps): JSX.Element;
