import * as React from 'react';

/**
 * Filter / selection chip (period filters, type filters, brand & category pickers).
 *
 * @startingPoint section="Core" subtitle="Filter chips — selectable, color-dot, icon" viewport="700x120"
 */
export interface ChipProps {
  children?: React.ReactNode;
  /** @default false */
  selected?: boolean;
  /** Category color (hex or CSS var) — renders a leading dot. */
  color?: string;
  /** Material Symbols ligature. */
  leadingIcon?: string;
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  style?: React.CSSProperties;
}
export function Chip(props: ChipProps): JSX.Element;
