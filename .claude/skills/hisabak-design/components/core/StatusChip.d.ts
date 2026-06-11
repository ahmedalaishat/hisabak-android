import * as React from 'react';

/** SMS parse-status chip — linked / parsed / unparsed, each color + icon distinct. */
export interface StatusChipProps {
  /** @default "parsed" */
  status?: 'linked' | 'parsed' | 'unparsed';
  style?: React.CSSProperties;
}
export function StatusChip(props: StatusChipProps): JSX.Element;
