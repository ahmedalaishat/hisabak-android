import * as React from 'react';

/** Small status / category badge. Tone maps to semantic + financial colors. */
export interface BadgeProps {
  children?: React.ReactNode;
  /** @default "neutral" */
  tone?: 'neutral' | 'income' | 'expense' | 'savings' | 'investment' | 'success' | 'warning' | 'danger' | 'info';
  /** Show a leading status dot. @default false */
  dot?: boolean;
  style?: React.CSSProperties;
}
export function Badge(props: BadgeProps): JSX.Element;
