import * as React from 'react';

export interface SegmentOption {
  value: string;
  label: string;
  /** Material Symbols ligature. */
  icon?: string;
  /** Active color for this segment. */
  tone?: 'income' | 'expense' | 'savings' | 'investment' | 'accent';
}

/** Segmented control — transaction type toggle, period selectors. */
export interface SegmentedControlProps {
  options: SegmentOption[];
  value?: string;
  onChange?: (value: string) => void;
  style?: React.CSSProperties;
}
export function SegmentedControl(props: SegmentedControlProps): JSX.Element;
