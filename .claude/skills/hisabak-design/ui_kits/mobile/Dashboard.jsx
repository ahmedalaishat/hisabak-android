/* Dashboard — mirrors DashboardScreen.kt: a period-chip row + a Summary / Trends / Categories
   segmented control, each its own scroll. Chart data matches the app's semantics:
   - Net worth / income-over-time / expense-over-time = CUMULATIVE running totals (area lines).
   - Income / Expenses KPI = per-bucket FLOW (bar sparkline) with the period total + trend.
   - Trends = income-vs-spending grouped bars, expenses-by-category & top-brands & income-sources donuts.
   - Categories = per-category spent vs monthly limit, expandable to a trend line.
   Returns scroll content; the shell provides app bar + nav. */
function Dashboard() {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, Chip, ProgressBar, SegmentedControl } = NS;
  const M = window.HisabakMock;
  const [period, setPeriod] = React.useState('this_month');
  const [tab, setTab] = React.useState('summary');
  const [expanded, setExpanded] = React.useState(null);

  const periods = [
    ['this_month', 'This month'], ['last_month', 'Last month'],
    ['this_year', 'This year'], ['last_year', 'Last year'], ['all', 'All time'],
  ];

  // ── mock snapshot (shapes mirror DashboardSnapshot) ──────────────────────────
  const netWorthSeries = [9100, 9400, 9250, 9900, 10400, 10200, 11100, 11600, 11400, 12050, 12200, 12450];
  const incomeSeries = [0, 0, 420, 420, 620, 8820, 8820, 9020, 9420];        // cumulative within period
  const expenseSeries = [343, 700, 1100, 1190, 3190, 3246, 5246, 6066, 6180]; // cumulative within period
  const incomeDaily = [0, 0, 420, 0, 200, 8200, 0, 200, 400];                 // per-day flow
  const expenseDaily = [343, 357, 400, 90, 2000, 56, 2000, 820, 114];
  const overTime = [
    { label: 'Feb', income: 8200, expense: 5400 }, { label: 'Mar', income: 8200, expense: 6100 },
    { label: 'Apr', income: 9400, expense: 5800 }, { label: 'May', income: 8200, expense: 6400 },
    { label: 'Jun', income: 9420, expense: 6180 },
  ];
  const col = (c) => `var(--cat-${c})`;
  const expenseByCat = [
    { label: 'Rent', value: 3200, color: col('gray') }, { label: 'Groceries', value: 1240, color: col('orange') },
    { label: 'Dining', value: 642, color: col('red') }, { label: 'Transport', value: 388, color: col('teal') },
    { label: 'Entertainment', value: 256, color: col('purple') },
  ];
  const topBrands = [
    { label: 'Carrefour', value: 1240, color: col('orange') }, { label: 'STC', value: 820, color: col('gray') },
    { label: 'Talabat', value: 540, color: col('red') }, { label: 'Uber', value: 388, color: col('teal') },
    { label: 'Netflix', value: 256, color: col('purple') },
  ];
  const incomeSources = [{ label: 'Salary', value: 8200, color: col('green') }];
  const catRows = [
    { name: 'Salary', color: col('green'), spent: 8200, trend: +6, series: [8200, 8200], type: 'income' },
    { name: 'Rent', color: col('gray'), spent: 3200, trend: 0, series: [3200] },
    { name: 'Savings', color: col('blue'), spent: 2000, trend: +5, series: [2000], type: 'savings' },
    { name: 'Investment', color: col('purple'), spent: 1500, trend: +12, series: [1500], type: 'investment' },
    { name: 'Groceries', color: col('orange'), spent: 1240, limit: 1500, trend: -8, series: [200, 540, 800, 1240] },
    { name: 'Dining', color: col('red'), spent: 642, limit: 600, trend: +18, series: [120, 300, 480, 642] },
    { name: 'Transport', color: col('teal'), spent: 388, trend: -3, series: [90, 200, 300, 388] },
    { name: 'Entertainment', color: col('purple'), spent: 256, trend: +4, series: [56, 120, 200, 256] },
  ];

  // ── shared bits ──────────────────────────────────────────────────────────────
  const DashCard = ({ children, style }) => <Card style={style}>{children}</Card>;

  const Trend = ({ pct, positiveIsGood = true }) => {
    if (!pct) return null;
    const good = (pct >= 0) === positiveIsGood;
    const color = good ? 'var(--income)' : 'var(--expense)';
    return (
      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 1, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 12.5, color }}>
        <span className="material-symbols-rounded" style={{ fontSize: 15 }}>{pct >= 0 ? 'arrow_upward' : 'arrow_downward'}</span>{Math.abs(pct)}%
      </span>
    );
  };

  const Section = ({ title }) => (
    <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)', margin: '8px 2px 2px' }}>{title}</div>
  );

  const FlowBars = ({ data, color }) => {
    const max = Math.max(...data, 1);
    return (
      <div style={{ display: 'flex', alignItems: 'flex-end', gap: 3, height: 56, marginTop: 12 }}>
        {data.map((v, i) => (
          <div key={i} style={{ flex: 1, height: `${Math.max(4, (v / max) * 100)}%`, background: color, opacity: i === data.length - 1 ? 1 : 0.4, borderRadius: 3 }} />
        ))}
      </div>
    );
  };

  const OverTimeCard = ({ label, value, pct, positiveIsGood, series, color }) => (
    <DashCard>
      <div style={{ fontFamily: 'var(--font-sans)', fontSize: 13, color: 'var(--text-secondary)' }}>{label}</div>
      <div style={{ display: 'flex', alignItems: 'flex-end', gap: 10, marginTop: 4 }}>
        <Money value={value} tone="neutral" size={label === 'Net worth' ? 34 : 24} weight={700} />
        <Trend pct={pct} positiveIsGood={positiveIsGood} />
      </div>
      <div style={{ margin: '12px -4px 0' }}><AreaChart data={series} color={color} height={96} /></div>
    </DashCard>
  );

  const Kpi = ({ label, value, pct, positiveIsGood, color, data }) => (
    <DashCard>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span style={{ fontFamily: 'var(--font-sans)', fontSize: 13, color: 'var(--text-secondary)' }}>{label}</span>
        <Trend pct={pct} positiveIsGood={positiveIsGood} />
      </div>
      <div style={{ marginTop: 6 }}><Money value={value} tone={label === 'Income' ? 'income' : 'expense'} size={24} weight={700} /></div>
      <FlowBars data={data} color={color} />
    </DashCard>
  );

  const TotalPill = ({ label, value, icon, bg, fg }) => (
    <Card padding={12} style={{ flex: 1, minWidth: 0 }}>
      <div style={{ display: 'inline-flex', alignItems: 'center', gap: 5, background: bg, borderRadius: 999, padding: '4px 8px', maxWidth: '100%' }}>
        <span className="material-symbols-rounded" style={{ fontSize: 15, color: fg, flex: 'none' }}>{icon}</span>
        <span style={{ fontFamily: 'var(--font-sans)', fontSize: 11, fontWeight: 600, color: fg, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{label}</span>
      </div>
      <div style={{ marginTop: 8 }}><Money value={value} tone="neutral" size={15} weight={700} /></div>
    </Card>
  );

  const DonutCard = ({ items }) => (
    <DashCard>
      <div style={{ display: 'flex', alignItems: 'center', gap: 18 }}>
        <DonutChart segments={items.slice(0, 5)} size={112} thickness={16} />
        <LegendList items={items.slice(0, 5)} />
      </div>
    </DashCard>
  );

  // ── tabs ──────────────────────────────────────────────────────────────────────
  const Summary = () => (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <Card variant="tinted" tint="var(--warning-soft)" padding={14} style={{ display: 'flex', alignItems: 'center', gap: 12, cursor: 'pointer' }}>
        <span className="material-symbols-rounded" style={{ fontSize: 22, color: 'var(--warning)' }}>error</span>
        <div style={{ flex: 1 }}>
          <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14, color: 'var(--text-primary)' }}>3 uncategorized transactions</div>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>Set their brand's category so they count in your totals</div>
        </div>
        <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-tertiary)' }}>chevron_right</span>
      </Card>

      <OverTimeCard label="Net worth" value={12450} pct={8.2} positiveIsGood series={netWorthSeries} color="var(--accent)" />

      <div style={{ display: 'flex', gap: 10 }}>
        <TotalPill label="Cash" value={6450} icon="account_balance_wallet" bg="var(--surface-sunken)" fg="var(--text-secondary)" />
        <TotalPill label="Savings" value={4000} icon="savings" bg="var(--savings-soft)" fg="var(--savings)" />
        <TotalPill label="Invest" value={2000} icon="trending_up" bg="var(--investment-soft)" fg="var(--investment)" />
      </div>

      <Kpi label="Income" value={9420} pct={12} positiveIsGood color="var(--income)" data={incomeDaily} />
      <Kpi label="Expenses" value={6180} pct={4} positiveIsGood={false} color="var(--expense)" data={expenseDaily} />

      <OverTimeCard label="Income over time" value={9420} pct={12} positiveIsGood series={incomeSeries} color="var(--income)" />
      <OverTimeCard label="Expense over time" value={6180} pct={4} positiveIsGood={false} series={expenseSeries} color="var(--expense)" />
    </div>
  );

  const Trends = () => (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <Section title="Income & spending" />
      <DashCard>
        <div style={{ display: 'flex', gap: 16, marginBottom: 10 }}>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}><span style={{ width: 9, height: 9, borderRadius: 3, background: 'var(--income)' }} />Income</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}><span style={{ width: 9, height: 9, borderRadius: 3, background: 'var(--expense)' }} />Expenses</span>
        </div>
        <GroupedBars data={overTime} height={150} />
      </DashCard>
      <Section title="Expenses by category" />
      <DonutCard items={expenseByCat} />
      <Section title="Top brands" />
      <DonutCard items={topBrands} />
      <Section title="Income sources" />
      <DonutCard items={incomeSources} />
    </div>
  );

  const Categories = () => (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      {catRows.map((r) => {
        const open = expanded === r.name;
        const over = r.limit && r.spent > r.limit;
        const pctOfLimit = r.limit ? Math.min(100, Math.round((r.spent / r.limit) * 100)) : 0;
        return (
          <DashCard key={r.name}>
            <div onClick={() => setExpanded(open ? null : r.name)} style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer' }}>
              <span style={{ width: 10, height: 10, borderRadius: 999, background: r.color }} />
              <span style={{ flex: 1, fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>{r.name}</span>
              <Money value={r.spent} tone="neutral" size={14} weight={600} />
              <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-tertiary)' }}>{open ? 'expand_less' : 'expand_more'}</span>
            </div>
            {r.limit && (
              <div style={{ marginTop: 10 }}>
                <ProgressBar value={pctOfLimit} tone={over ? 'expense' : 'income'} />
                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 6, fontFamily: 'var(--font-sans)', fontSize: 11.5, color: over ? 'var(--expense)' : 'var(--text-secondary)' }}>
                  <span>{over ? 'Over budget' : `${pctOfLimit}% of limit`}</span>
                  <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}><Money value={r.spent} size={11.5} weight={500} color="currentColor" /> / <Money value={r.limit} size={11.5} weight={500} color="currentColor" /></span>
                </div>
              </div>
            )}
            {open && (
              <div style={{ marginTop: 12 }}>
                <Trend pct={r.trend} positiveIsGood={r.type !== undefined ? true : false} />
                {r.series.length > 1 && <div style={{ margin: '8px -4px 0' }}><AreaChart data={r.series} color={r.color} height={80} /></div>}
              </div>
            )}
          </DashCard>
        );
      })}
      <Card variant="tinted" tint="var(--warning-soft)" padding={14} style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer' }}>
        <span style={{ width: 10, height: 10, borderRadius: 999, background: 'var(--cat-gray)' }} />
        <span style={{ flex: 1, fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>Uncategorized</span>
        <Money value={220} tone="neutral" size={14} weight={600} />
        <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--text-tertiary)' }}>chevron_right</span>
      </Card>
    </div>
  );

  return (
    <div>
      <div style={{ position: 'sticky', top: 0, zIndex: 5, background: 'var(--bg)', padding: '10px 16px 8px' }}>
        <div className="no-scrollbar" style={{ display: 'flex', gap: 8, overflowX: 'auto', paddingBottom: 2 }}>
          {periods.map(([v, l]) => <Chip key={v} selected={period === v} onClick={() => setPeriod(v)} style={{ height: 30, fontSize: 13, padding: '0 12px', whiteSpace: 'nowrap' }}>{l}</Chip>)}
        </div>
        <div style={{ marginTop: 8 }}>
          <SegmentedControl value={tab} onChange={setTab} options={[{ value: 'summary', label: 'Summary' }, { value: 'trends', label: 'Trends' }, { value: 'categories', label: 'Categories' }]} />
        </div>
      </div>
      <div style={{ padding: '4px 16px 24px' }}>
        {tab === 'summary' ? <Summary /> : tab === 'trends' ? <Trends /> : <Categories />}
      </div>
    </div>
  );
}
window.HisabakDashboard = Dashboard;
