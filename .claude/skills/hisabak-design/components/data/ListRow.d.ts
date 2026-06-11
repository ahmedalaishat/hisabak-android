import * as React from 'react';

/**
 * List row — transaction / brand / SMS. Leading colored avatar, title+subtitle, right slot.
 *
 * @startingPoint section="Lists" subtitle="Transaction / brand list rows" viewport="700x240"
 */
export interface ListRowProps {
  title: string;
  subtitle?: string;
  /** Initial(s) inside the colored circle. */
  leadingText?: string;
  /** Material Symbols ligature inside the circle (overrides leadingText). */
  leadingIcon?: string;
  /** Category color (hex or CSS var). @default "var(--cat-gray)" */
  color?: string;
  /** Amount in major units → rendered as colored tabular figure. */
  amount?: number;
  /** @default "auto" */
  amountTone?: 'auto' | 'income' | 'expense' | 'savings' | 'investment' | 'neutral';
  /** Small meta under the amount, e.g. a date. */
  meta?: string;
  /** Custom right-side node; overrides amount/meta. */
  trailing?: React.ReactNode;
  onClick?: (e: React.MouseEvent<HTMLDivElement>) => void;
  /** @default true */
  divider?: boolean;
  style?: React.CSSProperties;
}
export function ListRow(props: ListRowProps): JSX.Element;
