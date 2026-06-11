import * as React from 'react';

/** Empty state — tinted icon, title, guidance, CTA. One per list screen. */
export interface EmptyStateProps {
  /** Material Symbols ligature. @default "inbox" */
  icon?: string;
  title?: string;
  description?: string;
  /** CTA label; omit to hide the button. */
  actionLabel?: string;
  onAction?: () => void;
  style?: React.CSSProperties;
}
export function EmptyState(props: EmptyStateProps): JSX.Element;
