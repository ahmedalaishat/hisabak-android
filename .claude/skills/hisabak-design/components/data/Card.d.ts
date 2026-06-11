import * as React from 'react';

/** Surface container — base card for the app. */
export interface CardProps {
  children?: React.ReactNode;
  /** @default "default" */
  variant?: 'default' | 'hero' | 'flat' | 'tinted';
  /** Background color when variant="tinted". */
  tint?: string;
  /** px @default 16 */
  padding?: number;
  onClick?: (e: React.MouseEvent<HTMLDivElement>) => void;
  style?: React.CSSProperties;
}
export function Card(props: CardProps): JSX.Element;
