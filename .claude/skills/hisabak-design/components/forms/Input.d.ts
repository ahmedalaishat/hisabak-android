import * as React from 'react';

/** Text / textarea input with label, leading icon, helper & error states. */
export interface InputProps {
  label?: string;
  value?: string;
  placeholder?: string;
  /** Material Symbols ligature shown at the left. */
  leadingIcon?: string;
  helper?: string;
  /** Error message — turns the field red and overrides helper. */
  error?: string;
  /** @default "text" */
  type?: string;
  /** @default false */
  multiline?: boolean;
  /** @default 3 */
  rows?: number;
  onChange?: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  style?: React.CSSProperties;
}
export function Input(props: InputProps): JSX.Element;
