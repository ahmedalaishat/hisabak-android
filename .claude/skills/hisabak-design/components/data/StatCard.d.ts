import * as React from 'react';

/**
 * Stat pill / card — icon, label, value, optional delta. Totals & summaries.
 *
 * @startingPoint section="Cards" subtitle="Stat cards — totals, income/expense summaries" viewport="700x200"
 */
export interface StatCardProps {
  label: string;
  value: number;
  /** @default "AED" */
  currency?: string;
  /** Material Symbols ligature for the tile. */
  icon?: string;
  /** @default "neutral" */
  tone?: 'neutral' | 'income' | 'expense' | 'savings' | 'investment';
  /** Percent change vs last period; sign drives the up/down arrow + color. */
  delta?: number;
  /** Larger amount for income/expense hero cards. @default false */
  emphasis?: boolean;
  style?: React.CSSProperties;
}
export function StatCard(props: StatCardProps): JSX.Element;
