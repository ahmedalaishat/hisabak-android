import * as React from 'react';

/** Thin ratio / progress bar; tone colors the filled portion. */
export interface ProgressBarProps {
  /** 0–100 */
  value?: number;
  /** @default "income" */
  tone?: 'income' | 'expense' | 'savings' | 'investment' | 'accent';
  /** px @default 6 */
  height?: number;
  /** Track color override. */
  track?: string;
  style?: React.CSSProperties;
}
export function ProgressBar(props: ProgressBarProps): JSX.Element;
