import * as React from 'react';

/** Category icon tile — Material icon on a tinted rounded square in the category color. */
export interface CategoryIconProps {
  /** Material Symbols ligature. @default "category" */
  icon?: string;
  /** Category color (hex or CSS var). @default "var(--cat-gray)" */
  color?: string;
  /** px @default 44 */
  size?: number;
  style?: React.CSSProperties;
}
export function CategoryIcon(props: CategoryIconProps): JSX.Element;
