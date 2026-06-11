import * as React from 'react';

/** Money display — tabular Geist Mono figures, signed coloring (income green / expense coral). */
export interface AmountTextProps {
  /** Amount in major units; negative = expense. */
  value?: number;
  /** Preformatted string; overrides `value`. */
  text?: string;
  /** @default "SAR" */
  currency?: string;
  /** @default "auto" */
  sign?: 'auto' | 'always' | 'never';
  /** @default "auto" (negative→expense, positive→income) */
  tone?: 'auto' | 'income' | 'expense' | 'savings' | 'investment' | 'neutral';
  /** px @default 16 */
  size?: number;
  /** @default 600 */
  weight?: number;
  style?: React.CSSProperties;
}
export function AmountText(props: AmountTextProps): JSX.Element;
