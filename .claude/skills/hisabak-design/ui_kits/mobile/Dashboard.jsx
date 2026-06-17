/* Dashboard — financial snapshot. Returns scroll content (shell provides app bar + nav). */
function Dashboard() {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Card, Chip, AmountText, StatCard } = NS;
  const M = window.HisabakMock;
  const [period, setPeriod] = React.useState('month');

  const netSeries = [9100, 9400, 9250, 9900, 10400, 10200, 11100, 11600, 11400, 12050, 12200, 12450];
  const overTime = [
    { label: 'Feb', income: 8200, expense: 5400 },
    { label: 'Mar', income: 8200, expense: 6100 },
    { label: 'Apr', income: 9400, expense: 5800 },
    { label: 'May', income: 8200, expense: 6400 },
    { label: 'Jun', income: 9420, expense: 6180 },
  ];
  const expenseCats = M.CATEGORIES.filter(c => c.type === 'expense').slice(0, 5)
    .map(c => ({ label: c.name, value: c.total, color: c.color }));
  const topBrands = [
    { label: 'Carrefour', value: 1240, color: 'var(--cat-orange)' },
    { label: 'STC', value: 820, color: 'var(--cat-gray)' },
    { label: 'Talabat', value: 540, color: 'var(--cat-red)' },
    { label: 'Uber', value: 388, color: 'var(--cat-teal)' },
    { label: 'Netflix', value: 256, color: 'var(--cat-purple)' },
  ];

  const Section = ({ title, action, children }) => (
    <div style={{ marginTop: 22 }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 12 }}>
        <h2 style={{ margin: 0, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 17, color: 'var(--text-primary)' }}>{title}</h2>
        {action}
      </div>
      {children}
    </div>
  );

  return (
    <div style={{ padding: '8px 16px 24px' }}>
      {/* Net worth hero */}
      <Card variant="hero" padding={18} style={{ marginTop: 8 }}>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 13, color: 'var(--text-secondary)' }}>Net Worth</div>
        <div style={{ display: 'flex', alignItems: 'baseline', gap: 10, marginTop: 4 }}>
          <AmountText value={12450} tone="neutral" sign="never" size={36} weight={700} />
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 2, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 13, color: 'var(--income)' }}>
            <span className="material-symbols-rounded" style={{ fontSize: 16 }}>arrow_upward</span>8.2%
          </span>
        </div>
        <div style={{ margin: '14px -4px 4px' }}>
          <AreaChart data={netSeries} color="var(--accent)" height={104} />
        </div>
        <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
          {['week', 'month', 'year', 'all'].map(p => (
            <Chip key={p} selected={period === p} onClick={() => setPeriod(p)} style={{ height: 30, fontSize: 13, padding: '0 12px' }}>
              {p[0].toUpperCase() + p.slice(1)}
            </Chip>
          ))}
        </div>
      </Card>

      {/* Three stat pills */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)', gap: 10, marginTop: 12 }}>
        <StatCard label="Cash" value={6450} icon="account_balance_wallet" tone="neutral" />
        <StatCard label="Savings" value={4000} icon="savings" tone="savings" />
        <StatCard label="Invest" value={2000} icon="trending_up" tone="investment" />
      </div>

      {/* Income / Expenses */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginTop: 12 }}>
        <Card padding={14}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 13, color: 'var(--text-secondary)' }}>Income</span>
            <span style={{ display: 'inline-flex', alignItems: 'center', fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 12, color: 'var(--income)' }}><span className="material-symbols-rounded" style={{ fontSize: 15 }}>arrow_upward</span>12%</span>
          </div>
          <div style={{ marginTop: 8 }}><AmountText value={9420} tone="income" sign="never" size={22} weight={700} /></div>
          <div style={{ marginTop: 10 }}><Sparkline data={[5,6,5,7,6,8,7,9]} color="var(--income)" /></div>
        </Card>
        <Card padding={14}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontFamily: 'var(--font-sans)', fontWeight: 500, fontSize: 13, color: 'var(--text-secondary)' }}>Expenses</span>
            <span style={{ display: 'inline-flex', alignItems: 'center', fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 12, color: 'var(--income)' }}><span className="material-symbols-rounded" style={{ fontSize: 15 }}>arrow_downward</span>4%</span>
          </div>
          <div style={{ marginTop: 8 }}><AmountText value={6180} tone="expense" sign="never" size={22} weight={700} /></div>
          <div style={{ marginTop: 10 }}><Sparkline data={[7,6,8,5,6,7,5,6]} color="var(--expense)" /></div>
        </Card>
      </div>

      <Section title="Income & spending">
        <Card padding={16}>
          <div style={{ display: 'flex', gap: 16, marginBottom: 10 }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}><span style={{ width: 9, height: 9, borderRadius: 3, background: 'var(--income)' }} />Income</span>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}><span style={{ width: 9, height: 9, borderRadius: 3, background: 'var(--expense)' }} />Expenses</span>
          </div>
          <GroupedBars data={overTime} height={150} />
        </Card>
      </Section>

      <Section title="Expenses by category">
        <Card padding={16}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 18 }}>
            <DonutChart segments={expenseCats} centerLabel={M.money(2546 + 388, { decimals: false }).replace('AED ', '')} centerSub="Total" />
            <LegendList items={expenseCats} />
          </div>
        </Card>
      </Section>

      <Section title="Top brands">
        <Card padding={16}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 18 }}>
            <DonutChart segments={topBrands} centerLabel="5" centerSub="brands" thickness={16} size={120} />
            <LegendList items={topBrands} />
          </div>
        </Card>
      </Section>
    </div>
  );
}
window.HisabakDashboard = Dashboard;
