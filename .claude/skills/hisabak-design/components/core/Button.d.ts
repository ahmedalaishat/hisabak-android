import * as React from 'react';

/**
 * Hisabak primary button — pill-shaped CTA.
 *
 * @startingPoint section="Core" subtitle="Pill button — primary, secondary, ghost, danger" viewport="700x140"
 */
export interface ButtonProps {
  children?: React.ReactNode;
  /** Visual style. @default "primary" */
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger';
  /** @default "md" */
  size?: 'sm' | 'md' | 'lg';
  /** Stretch to container width. @default false */
  fullWidth?: boolean;
  /** @default false */
  disabled?: boolean;
  /** Material Symbols Rounded ligature name, e.g. "add". */
  leadingIcon?: string;
  trailingIcon?: string;
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  style?: React.CSSProperties;
}

export function Button(props: ButtonProps): JSX.Element;
