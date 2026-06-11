import * as React from 'react';

/** Persistent top app bar — brand wordmark + avatar/bell, or back + title. */
export interface TopAppBarProps {
  title?: string;
  /** Show the Hisabak wordmark instead of a title. @default false */
  brand?: boolean;
  /** Show a back arrow and handle the tap. */
  onBack?: () => void;
  /** @default false */
  showAvatar?: boolean;
  avatarName?: string;
  /** @default false */
  showBell?: boolean;
  onBell?: () => void;
  onAvatar?: () => void;
  /** Custom right-side nodes (rendered before bell/avatar). */
  actions?: React.ReactNode;
  style?: React.CSSProperties;
}
export function TopAppBar(props: TopAppBarProps): JSX.Element;
