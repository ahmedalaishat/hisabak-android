import * as React from 'react';

/** Circular icon-only button (app bars, list rows). Icon = Material Symbols ligature. */
export interface IconButtonProps {
  /** Material Symbols Rounded ligature, e.g. "notifications". */
  icon: string;
  /** @default "md" */
  size?: 'sm' | 'md' | 'lg';
  /** @default "plain" */
  variant?: 'plain' | 'soft' | 'accent';
  /** Use filled icon glyph. @default false */
  filled?: boolean;
  ariaLabel?: string;
  disabled?: boolean;
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  style?: React.CSSProperties;
}
export function IconButton(props: IconButtonProps): JSX.Element;
