import * as React from 'react';

/** Full-width pill search bar for list screens. */
export interface SearchBarProps {
  value?: string;
  /** @default "Search" */
  placeholder?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onClear?: () => void;
  style?: React.CSSProperties;
}
export function SearchBar(props: SearchBarProps): JSX.Element;
