import * as React from 'react';

export interface NavTab {
  value: string;
  label: string;
  /** Material Symbols ligature. */
  icon: string;
}

/** Bottom navigation — 5 tabs, outlined→filled icon on the active accent tab. */
export interface BottomNavProps {
  /** Defaults to the 5 Hisabak tabs. */
  tabs?: NavTab[];
  /** @default "dashboard" */
  value?: string;
  onChange?: (value: string) => void;
  style?: React.CSSProperties;
}
export function BottomNav(props: BottomNavProps): JSX.Element;
