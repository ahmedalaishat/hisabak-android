import * as React from 'react';

/** Circular avatar — initials from `name` or an image via `src`. */
export interface AvatarProps {
  name?: string;
  src?: string;
  /** px diameter. @default 36 */
  size?: number;
  style?: React.CSSProperties;
}
export function Avatar(props: AvatarProps): JSX.Element;
