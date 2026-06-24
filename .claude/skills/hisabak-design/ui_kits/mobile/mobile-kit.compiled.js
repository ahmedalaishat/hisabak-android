/* AUTO-GENERATED from charts.jsx + the *.jsx screens via Babel — do not edit by hand. */
/* Hisabak charts — lightweight inline SVG. Colors come in as CSS-var strings. */

function AreaChart({
  data = [],
  color = 'var(--accent)',
  height = 120,
  fill = true
}) {
  const w = 320,
    h = height,
    pad = 6;
  const max = Math.max(...data),
    min = Math.min(...data);
  const span = max - min || 1;
  const pts = data.map((v, i) => {
    const x = pad + i / (data.length - 1) * (w - pad * 2);
    const y = pad + (1 - (v - min) / span) * (h - pad * 2);
    return [x, y];
  });
  const line = pts.map((p, i) => (i === 0 ? 'M' : 'L') + p[0].toFixed(1) + ' ' + p[1].toFixed(1)).join(' ');
  const area = line + ` L${(w - pad).toFixed(1)} ${h - pad} L${pad} ${h - pad} Z`;
  const gid = 'ag' + Math.random().toString(36).slice(2, 7);
  return /*#__PURE__*/React.createElement("svg", {
    viewBox: `0 0 ${w} ${h}`,
    width: "100%",
    height: h,
    preserveAspectRatio: "none",
    style: {
      display: 'block',
      color
    }
  }, /*#__PURE__*/React.createElement("defs", null, /*#__PURE__*/React.createElement("linearGradient", {
    id: gid,
    x1: "0",
    y1: "0",
    x2: "0",
    y2: "1"
  }, /*#__PURE__*/React.createElement("stop", {
    offset: "0%",
    stopColor: color,
    stopOpacity: "0.22"
  }), /*#__PURE__*/React.createElement("stop", {
    offset: "100%",
    stopColor: color,
    stopOpacity: "0"
  }))), fill && /*#__PURE__*/React.createElement("path", {
    d: area,
    fill: `url(#${gid})`
  }), /*#__PURE__*/React.createElement("path", {
    d: line,
    fill: "none",
    stroke: color,
    strokeWidth: "2.5",
    strokeLinecap: "round",
    strokeLinejoin: "round"
  }), /*#__PURE__*/React.createElement("circle", {
    cx: pts[pts.length - 1][0],
    cy: pts[pts.length - 1][1],
    r: "3.5",
    fill: color
  }));
}
function Sparkline({
  data = [],
  color = 'var(--accent)',
  height = 36,
  width = 90
}) {
  const max = Math.max(...data) || 1;
  const bw = width / (data.length * 1.6);
  const gap = bw * 0.6;
  return /*#__PURE__*/React.createElement("svg", {
    viewBox: `0 0 ${width} ${height}`,
    width: width,
    height: height,
    style: {
      display: 'block'
    }
  }, data.map((v, i) => {
    const bh = Math.max(2, v / max * (height - 2));
    return /*#__PURE__*/React.createElement("rect", {
      key: i,
      x: i * (bw + gap),
      y: height - bh,
      width: bw,
      height: bh,
      rx: bw / 2.5,
      fill: color,
      opacity: i === data.length - 1 ? 1 : 0.35
    });
  }));
}
function GroupedBars({
  data = [],
  height = 150
}) {
  // data: [{ label, income, expense }]
  const w = 320,
    h = height,
    pad = 18,
    axis = 16;
  const max = Math.max(...data.flatMap(d => [d.income, d.expense])) || 1;
  const groupW = (w - pad * 2) / data.length;
  const bw = Math.min(13, groupW / 3.4);
  return /*#__PURE__*/React.createElement("svg", {
    viewBox: `0 0 ${w} ${h}`,
    width: "100%",
    height: h,
    style: {
      display: 'block'
    }
  }, [0.25, 0.5, 0.75].map((g, i) => /*#__PURE__*/React.createElement("line", {
    key: i,
    x1: pad,
    x2: w - pad,
    y1: pad + g * (h - pad - axis),
    y2: pad + g * (h - pad - axis),
    stroke: "var(--divider)",
    strokeWidth: "1"
  })), data.map((d, i) => {
    const cx = pad + groupW * i + groupW / 2;
    const ih = d.income / max * (h - pad - axis);
    const eh = d.expense / max * (h - pad - axis);
    const base = h - axis;
    return /*#__PURE__*/React.createElement("g", {
      key: i
    }, /*#__PURE__*/React.createElement("rect", {
      x: cx - bw - 2,
      y: base - ih,
      width: bw,
      height: ih,
      rx: "3",
      fill: "var(--income)"
    }), /*#__PURE__*/React.createElement("rect", {
      x: cx + 2,
      y: base - eh,
      width: bw,
      height: eh,
      rx: "3",
      fill: "var(--expense)"
    }), /*#__PURE__*/React.createElement("text", {
      x: cx,
      y: h - 3,
      textAnchor: "middle",
      fontSize: "10",
      fontFamily: "var(--font-sans)",
      fill: "var(--text-tertiary)"
    }, d.label));
  }));
}
function DonutChart({
  segments = [],
  size = 132,
  thickness = 18,
  centerLabel,
  centerSub
}) {
  const r = (size - thickness) / 2;
  const c = 2 * Math.PI * r;
  const total = segments.reduce((s, x) => s + x.value, 0) || 1;
  let offset = 0;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'relative',
      width: size,
      height: size
    }
  }, /*#__PURE__*/React.createElement("svg", {
    viewBox: `0 0 ${size} ${size}`,
    width: size,
    height: size,
    style: {
      transform: 'rotate(-90deg)'
    }
  }, /*#__PURE__*/React.createElement("circle", {
    cx: size / 2,
    cy: size / 2,
    r: r,
    fill: "none",
    stroke: "var(--surface-sunken)",
    strokeWidth: thickness
  }), segments.map((s, i) => {
    const len = s.value / total * c;
    const el = /*#__PURE__*/React.createElement("circle", {
      key: i,
      cx: size / 2,
      cy: size / 2,
      r: r,
      fill: "none",
      stroke: s.color,
      strokeWidth: thickness,
      strokeLinecap: "round",
      strokeDasharray: `${Math.max(0, len - 3)} ${c - Math.max(0, len - 3)}`,
      strokeDashoffset: -offset
    });
    offset += len;
    return el;
  })), (centerLabel || centerSub) && /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center'
    }
  }, centerLabel && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontWeight: 700,
      fontSize: 18,
      color: 'var(--text-primary)',
      fontVariantNumeric: 'tabular-nums'
    }
  }, centerLabel), centerSub && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 11,
      color: 'var(--text-tertiary)'
    }
  }, centerSub)));
}
function LegendList({
  items = []
}) {
  const total = items.reduce((s, x) => s + x.value, 0) || 1;
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 12,
      flex: 1,
      minWidth: 0
    }
  }, items.map((it, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 10
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 9,
      height: 9,
      borderRadius: '50%',
      background: it.color,
      flex: 'none'
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13.5,
      color: 'var(--text-primary)',
      flex: 1,
      minWidth: 0,
      overflow: 'hidden',
      textOverflow: 'ellipsis',
      whiteSpace: 'nowrap'
    }
  }, it.label), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontSize: 13,
      fontWeight: 600,
      color: 'var(--text-secondary)',
      fontVariantNumeric: 'tabular-nums'
    }
  }, window.HisabakMock.money(it.value, {
    decimals: false
  })), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12,
      color: 'var(--text-tertiary)',
      width: 36,
      textAlign: 'right'
    }
  }, Math.round(it.value / total * 100), "%"))));
}
Object.assign(window, {
  AreaChart,
  Sparkline,
  GroupedBars,
  DonutChart,
  LegendList
});
/* Dashboard — mirrors DashboardScreen.kt: a period-chip row + a Summary / Trends / Categories
   segmented control, each its own scroll. Chart data matches the app's semantics:
   - Net worth / income-over-time / expense-over-time = CUMULATIVE running totals (area lines).
   - Income / Expenses KPI = per-bucket FLOW (bar sparkline) with the period total + trend.
   - Trends = income-vs-spending grouped bars, expenses-by-category & top-brands & income-sources donuts.
   - Categories = per-category spent vs monthly limit, expandable to a trend line.
   Returns scroll content; the shell provides app bar + nav. */
function Dashboard() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Chip,
    AmountText,
    ProgressBar,
    SegmentedControl
  } = NS;
  const M = window.HisabakMock;
  const [period, setPeriod] = React.useState('this_month');
  const [tab, setTab] = React.useState('summary');
  const [expanded, setExpanded] = React.useState(null);
  const periods = [['this_month', 'This month'], ['last_month', 'Last month'], ['this_year', 'This year'], ['last_year', 'Last year'], ['all', 'All time']];

  // ── mock snapshot (shapes mirror DashboardSnapshot) ──────────────────────────
  const netWorthSeries = [9100, 9400, 9250, 9900, 10400, 10200, 11100, 11600, 11400, 12050, 12200, 12450];
  const incomeSeries = [0, 0, 420, 420, 620, 8820, 8820, 9020, 9420]; // cumulative within period
  const expenseSeries = [343, 700, 1100, 1190, 3190, 3246, 5246, 6066, 6180]; // cumulative within period
  const incomeDaily = [0, 0, 420, 0, 200, 8200, 0, 200, 400]; // per-day flow
  const expenseDaily = [343, 357, 400, 90, 2000, 56, 2000, 820, 114];
  const overTime = [{
    label: 'Feb',
    income: 8200,
    expense: 5400
  }, {
    label: 'Mar',
    income: 8200,
    expense: 6100
  }, {
    label: 'Apr',
    income: 9400,
    expense: 5800
  }, {
    label: 'May',
    income: 8200,
    expense: 6400
  }, {
    label: 'Jun',
    income: 9420,
    expense: 6180
  }];
  const col = c => `var(--cat-${c})`;
  const expenseByCat = [{
    label: 'Rent',
    value: 3200,
    color: col('gray')
  }, {
    label: 'Groceries',
    value: 1240,
    color: col('orange')
  }, {
    label: 'Dining',
    value: 642,
    color: col('red')
  }, {
    label: 'Transport',
    value: 388,
    color: col('teal')
  }, {
    label: 'Entertainment',
    value: 256,
    color: col('purple')
  }];
  const topBrands = [{
    label: 'Carrefour',
    value: 1240,
    color: col('orange')
  }, {
    label: 'STC',
    value: 820,
    color: col('gray')
  }, {
    label: 'Talabat',
    value: 540,
    color: col('red')
  }, {
    label: 'Uber',
    value: 388,
    color: col('teal')
  }, {
    label: 'Netflix',
    value: 256,
    color: col('purple')
  }];
  const incomeSources = [{
    label: 'Salary',
    value: 8200,
    color: col('green')
  }];
  const catRows = [{
    name: 'Salary',
    color: col('green'),
    spent: 8200,
    trend: +6,
    series: [8200, 8200],
    type: 'income'
  }, {
    name: 'Rent',
    color: col('gray'),
    spent: 3200,
    trend: 0,
    series: [3200]
  }, {
    name: 'Savings',
    color: col('blue'),
    spent: 2000,
    trend: +5,
    series: [2000],
    type: 'savings'
  }, {
    name: 'Investment',
    color: col('purple'),
    spent: 1500,
    trend: +12,
    series: [1500],
    type: 'investment'
  }, {
    name: 'Groceries',
    color: col('orange'),
    spent: 1240,
    limit: 1500,
    trend: -8,
    series: [200, 540, 800, 1240]
  }, {
    name: 'Dining',
    color: col('red'),
    spent: 642,
    limit: 600,
    trend: +18,
    series: [120, 300, 480, 642]
  }, {
    name: 'Transport',
    color: col('teal'),
    spent: 388,
    trend: -3,
    series: [90, 200, 300, 388]
  }, {
    name: 'Entertainment',
    color: col('purple'),
    spent: 256,
    trend: +4,
    series: [56, 120, 200, 256]
  }];

  // ── shared bits ──────────────────────────────────────────────────────────────
  const DashCard = ({
    children,
    style
  }) => /*#__PURE__*/React.createElement(Card, {
    style: style
  }, children);
  const Trend = ({
    pct,
    positiveIsGood = true
  }) => {
    if (!pct) return null;
    const good = pct >= 0 === positiveIsGood;
    const color = good ? 'var(--income)' : 'var(--expense)';
    return /*#__PURE__*/React.createElement("span", {
      style: {
        display: 'inline-flex',
        alignItems: 'center',
        gap: 1,
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 12.5,
        color
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 15
      }
    }, pct >= 0 ? 'arrow_upward' : 'arrow_downward'), Math.abs(pct), "%");
  };
  const Section = ({
    title
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 15,
      color: 'var(--text-primary)',
      margin: '8px 2px 2px'
    }
  }, title);
  const FlowBars = ({
    data,
    color
  }) => {
    const max = Math.max(...data, 1);
    return /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'flex-end',
        gap: 3,
        height: 56,
        marginTop: 12
      }
    }, data.map((v, i) => /*#__PURE__*/React.createElement("div", {
      key: i,
      style: {
        flex: 1,
        height: `${Math.max(4, v / max * 100)}%`,
        background: color,
        opacity: i === data.length - 1 ? 1 : 0.4,
        borderRadius: 3
      }
    })));
  };
  const OverTimeCard = ({
    label,
    value,
    pct,
    positiveIsGood,
    series,
    color
  }) => /*#__PURE__*/React.createElement(DashCard, null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13,
      color: 'var(--text-secondary)'
    }
  }, label), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'flex-end',
      gap: 10,
      marginTop: 4
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: value,
    tone: "neutral",
    sign: "never",
    size: label === 'Net worth' ? 34 : 24,
    weight: 700
  }), /*#__PURE__*/React.createElement(Trend, {
    pct: pct,
    positiveIsGood: positiveIsGood
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      margin: '12px -4px 0'
    }
  }, /*#__PURE__*/React.createElement(AreaChart, {
    data: series,
    color: color,
    height: 96
  })));
  const Kpi = ({
    label,
    value,
    pct,
    positiveIsGood,
    color,
    data
  }) => /*#__PURE__*/React.createElement(DashCard, null, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13,
      color: 'var(--text-secondary)'
    }
  }, label), /*#__PURE__*/React.createElement(Trend, {
    pct: pct,
    positiveIsGood: positiveIsGood
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 6
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: value,
    tone: label === 'Income' ? 'income' : 'expense',
    sign: "never",
    size: 24,
    weight: 700
  })), /*#__PURE__*/React.createElement(FlowBars, {
    data: data,
    color: color
  }));
  const TotalPill = ({
    label,
    value,
    icon,
    bg,
    fg
  }) => /*#__PURE__*/React.createElement(Card, {
    padding: 12,
    style: {
      flex: 1
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 5,
      background: bg,
      borderRadius: 999,
      padding: '4px 8px'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 15,
      color: fg
    }
  }, icon), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 11,
      fontWeight: 600,
      color: fg
    }
  }, label)), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: value,
    tone: "neutral",
    sign: "never",
    size: 15,
    weight: 700
  })));
  const DonutCard = ({
    items
  }) => /*#__PURE__*/React.createElement(DashCard, null, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 18
    }
  }, /*#__PURE__*/React.createElement(DonutChart, {
    segments: items.slice(0, 5),
    size: 112,
    thickness: 16
  }), /*#__PURE__*/React.createElement(LegendList, {
    items: items.slice(0, 5)
  })));

  // ── tabs ──────────────────────────────────────────────────────────────────────
  const Summary = () => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement(Card, {
    variant: "tinted",
    tint: "var(--warning-soft)",
    padding: 14,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12,
      cursor: 'pointer'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 22,
      color: 'var(--warning)'
    }
  }, "error"), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 14,
      color: 'var(--text-primary)'
    }
  }, "3 uncategorized transactions"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "Set their brand's category so they count in your totals")), /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--text-tertiary)'
    }
  }, "chevron_right")), /*#__PURE__*/React.createElement(OverTimeCard, {
    label: "Net worth",
    value: 12450,
    pct: 8.2,
    positiveIsGood: true,
    series: netWorthSeries,
    color: "var(--accent)"
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 10
    }
  }, /*#__PURE__*/React.createElement(TotalPill, {
    label: "Cash",
    value: 6450,
    icon: "account_balance_wallet",
    bg: "var(--surface-sunken)",
    fg: "var(--text-secondary)"
  }), /*#__PURE__*/React.createElement(TotalPill, {
    label: "Savings",
    value: 4000,
    icon: "savings",
    bg: "var(--savings-soft)",
    fg: "var(--savings)"
  }), /*#__PURE__*/React.createElement(TotalPill, {
    label: "Invest",
    value: 2000,
    icon: "trending_up",
    bg: "var(--investment-soft)",
    fg: "var(--investment)"
  })), /*#__PURE__*/React.createElement(Kpi, {
    label: "Income",
    value: 9420,
    pct: 12,
    positiveIsGood: true,
    color: "var(--income)",
    data: incomeDaily
  }), /*#__PURE__*/React.createElement(Kpi, {
    label: "Expenses",
    value: 6180,
    pct: 4,
    positiveIsGood: false,
    color: "var(--expense)",
    data: expenseDaily
  }), /*#__PURE__*/React.createElement(OverTimeCard, {
    label: "Income over time",
    value: 9420,
    pct: 12,
    positiveIsGood: true,
    series: incomeSeries,
    color: "var(--income)"
  }), /*#__PURE__*/React.createElement(OverTimeCard, {
    label: "Expense over time",
    value: 6180,
    pct: 4,
    positiveIsGood: false,
    series: expenseSeries,
    color: "var(--expense)"
  }));
  const Trends = () => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement(Section, {
    title: "Income & spending"
  }), /*#__PURE__*/React.createElement(DashCard, null, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 16,
      marginBottom: 10
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 6,
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 9,
      height: 9,
      borderRadius: 3,
      background: 'var(--income)'
    }
  }), "Income"), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 6,
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 9,
      height: 9,
      borderRadius: 3,
      background: 'var(--expense)'
    }
  }), "Expenses")), /*#__PURE__*/React.createElement(GroupedBars, {
    data: overTime,
    height: 150
  })), /*#__PURE__*/React.createElement(Section, {
    title: "Expenses by category"
  }), /*#__PURE__*/React.createElement(DonutCard, {
    items: expenseByCat
  }), /*#__PURE__*/React.createElement(Section, {
    title: "Top brands"
  }), /*#__PURE__*/React.createElement(DonutCard, {
    items: topBrands
  }), /*#__PURE__*/React.createElement(Section, {
    title: "Income sources"
  }), /*#__PURE__*/React.createElement(DonutCard, {
    items: incomeSources
  }));
  const Categories = () => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 12
    }
  }, catRows.map(r => {
    const open = expanded === r.name;
    const over = r.limit && r.spent > r.limit;
    const pctOfLimit = r.limit ? Math.min(100, Math.round(r.spent / r.limit * 100)) : 0;
    return /*#__PURE__*/React.createElement(DashCard, {
      key: r.name
    }, /*#__PURE__*/React.createElement("div", {
      onClick: () => setExpanded(open ? null : r.name),
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 10,
        cursor: 'pointer'
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        width: 10,
        height: 10,
        borderRadius: 999,
        background: r.color
      }
    }), /*#__PURE__*/React.createElement("span", {
      style: {
        flex: 1,
        fontFamily: 'var(--font-sans)',
        fontSize: 15,
        color: 'var(--text-primary)'
      }
    }, r.name), /*#__PURE__*/React.createElement(AmountText, {
      value: r.spent,
      tone: "neutral",
      sign: "never",
      size: 14,
      weight: 600
    }), /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 20,
        color: 'var(--text-tertiary)'
      }
    }, open ? 'expand_less' : 'expand_more')), r.limit && /*#__PURE__*/React.createElement("div", {
      style: {
        marginTop: 10
      }
    }, /*#__PURE__*/React.createElement(ProgressBar, {
      value: pctOfLimit,
      tone: over ? 'expense' : 'income'
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        justifyContent: 'space-between',
        marginTop: 6,
        fontFamily: 'var(--font-sans)',
        fontSize: 11.5,
        color: over ? 'var(--expense)' : 'var(--text-secondary)'
      }
    }, /*#__PURE__*/React.createElement("span", null, over ? 'Over budget' : `${pctOfLimit}% of limit`), /*#__PURE__*/React.createElement("span", {
      style: {
        fontFamily: 'var(--font-mono)'
      }
    }, M.money(r.spent, {
      decimals: false
    }), " / ", M.money(r.limit, {
      decimals: false
    })))), open && /*#__PURE__*/React.createElement("div", {
      style: {
        marginTop: 12
      }
    }, /*#__PURE__*/React.createElement(Trend, {
      pct: r.trend,
      positiveIsGood: r.type !== undefined ? true : false
    }), r.series.length > 1 && /*#__PURE__*/React.createElement("div", {
      style: {
        margin: '8px -4px 0'
      }
    }, /*#__PURE__*/React.createElement(AreaChart, {
      data: r.series,
      color: r.color,
      height: 80
    }))));
  }), /*#__PURE__*/React.createElement(Card, {
    variant: "tinted",
    tint: "var(--warning-soft)",
    padding: 14,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 10,
      cursor: 'pointer'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 10,
      height: 10,
      borderRadius: 999,
      background: 'var(--cat-gray)'
    }
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      flex: 1,
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, "Uncategorized"), /*#__PURE__*/React.createElement(AmountText, {
    value: 220,
    tone: "neutral",
    sign: "never",
    size: 14,
    weight: 600
  }), /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--text-tertiary)'
    }
  }, "chevron_right")));
  return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'sticky',
      top: 0,
      zIndex: 5,
      background: 'var(--bg)',
      padding: '10px 16px 8px'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      overflowX: 'auto',
      paddingBottom: 2
    }
  }, periods.map(([v, l]) => /*#__PURE__*/React.createElement(Chip, {
    key: v,
    selected: period === v,
    onClick: () => setPeriod(v),
    style: {
      height: 30,
      fontSize: 13,
      padding: '0 12px',
      whiteSpace: 'nowrap'
    }
  }, l))), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement(SegmentedControl, {
    value: tab,
    onChange: setTab,
    options: [{
      value: 'summary',
      label: 'Summary'
    }, {
      value: 'trends',
      label: 'Trends'
    }, {
      value: 'categories',
      label: 'Categories'
    }]
  }))), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '4px 16px 24px'
    }
  }, tab === 'summary' ? /*#__PURE__*/React.createElement(Summary, null) : tab === 'trends' ? /*#__PURE__*/React.createElement(Trends, null) : /*#__PURE__*/React.createElement(Categories, null)));
}
window.HisabakDashboard = Dashboard;
/* Shared screen-level helpers the component bundle doesn't ship (Toggle, SettingsRow, FormSection,
   RadioRow, HeroDisc). Kept here so the new screens compose consistently. Exposed on window.HisabakExtras. */
(function () {
  function Toggle({
    checked,
    onChange,
    disabled
  }) {
    return /*#__PURE__*/React.createElement("button", {
      type: "button",
      role: "switch",
      "aria-checked": checked,
      disabled: disabled,
      onClick: () => !disabled && onChange && onChange(!checked),
      style: {
        width: 46,
        height: 28,
        flex: 'none',
        borderRadius: 999,
        border: 'none',
        cursor: disabled ? 'default' : 'pointer',
        padding: 3,
        background: checked ? 'var(--accent)' : 'var(--border-strong)',
        opacity: disabled ? 0.45 : 1,
        transition: 'background var(--dur-base)',
        position: 'relative'
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        display: 'block',
        width: 22,
        height: 22,
        borderRadius: 999,
        background: '#fff',
        transform: checked ? 'translateX(18px)' : 'translateX(0)',
        transition: 'transform var(--dur-base)',
        boxShadow: '0 1px 3px rgba(0,0,0,0.25)'
      }
    }));
  }
  function SettingsRow({
    icon,
    title,
    subtitle,
    value,
    onClick,
    trailing,
    divider
  }) {
    return /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
      onClick: onClick,
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 14,
        padding: '14px 16px',
        cursor: onClick ? 'pointer' : 'default'
      }
    }, icon && /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 22,
        color: 'var(--text-secondary)'
      }
    }, icon), /*#__PURE__*/React.createElement("div", {
      style: {
        flex: 1,
        minWidth: 0
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 15,
        color: 'var(--text-primary)'
      }
    }, title), subtitle && /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 12.5,
        color: 'var(--text-secondary)',
        marginTop: 2
      }
    }, subtitle)), trailing, !trailing && onClick && /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 4
      }
    }, value && /*#__PURE__*/React.createElement("span", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 13.5,
        color: 'var(--text-secondary)'
      }
    }, value), /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 20,
        color: 'var(--text-tertiary)'
      }
    }, "chevron_right"))), divider && /*#__PURE__*/React.createElement("div", {
      style: {
        height: 1,
        background: 'var(--divider)',
        marginLeft: icon ? 52 : 16
      }
    }));
  }
  function FormSection({
    label,
    children,
    style
  }) {
    return /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        gap: 10,
        ...style
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 13.5,
        color: 'var(--text-secondary)'
      }
    }, label), children);
  }
  function RadioRow({
    selected,
    label,
    onClick
  }) {
    return /*#__PURE__*/React.createElement("div", {
      onClick: onClick,
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 14,
        padding: '12px 16px',
        cursor: 'pointer'
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        width: 20,
        height: 20,
        borderRadius: 999,
        flex: 'none',
        border: `2px solid ${selected ? 'var(--accent)' : 'var(--border-strong)'}`,
        display: 'grid',
        placeItems: 'center'
      }
    }, selected && /*#__PURE__*/React.createElement("span", {
      style: {
        width: 10,
        height: 10,
        borderRadius: 999,
        background: 'var(--accent)'
      }
    })), /*#__PURE__*/React.createElement("span", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 15,
        color: 'var(--text-primary)'
      }
    }, label));
  }
  function Sheet({
    open,
    onClose,
    children
  }) {
    return /*#__PURE__*/React.createElement("div", {
      style: {
        position: 'absolute',
        inset: 0,
        zIndex: 40,
        pointerEvents: open ? 'auto' : 'none'
      }
    }, /*#__PURE__*/React.createElement("div", {
      onClick: onClose,
      style: {
        position: 'absolute',
        inset: 0,
        background: 'var(--scrim)',
        opacity: open ? 1 : 0,
        transition: 'opacity var(--dur-base)'
      }
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        position: 'absolute',
        left: 0,
        right: 0,
        bottom: 0,
        background: 'var(--surface)',
        borderTopLeftRadius: 'var(--r-xl)',
        borderTopRightRadius: 'var(--r-xl)',
        boxShadow: 'var(--shadow-lg)',
        padding: '10px 0 calc(16px + var(--navbar-inset))',
        transform: open ? 'translateY(0)' : 'translateY(102%)',
        transition: 'transform var(--dur-slow) var(--ease-emphasis)',
        maxHeight: '92%',
        overflowY: 'auto'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        width: 40,
        height: 4,
        borderRadius: 2,
        background: 'var(--border-strong)',
        margin: '0 auto 14px'
      }
    }), children));
  }
  function HeroDisc({
    icon,
    size = 88,
    iconSize = 44,
    tint = 'var(--accent-soft)',
    fg = 'var(--accent)'
  }) {
    return /*#__PURE__*/React.createElement("div", {
      style: {
        width: size,
        height: size,
        borderRadius: 999,
        background: tint,
        display: 'grid',
        placeItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: iconSize,
        color: fg
      }
    }, icon));
  }
  window.HisabakExtras = {
    Toggle,
    SettingsRow,
    FormSection,
    RadioRow,
    HeroDisc,
    Sheet
  };
})();
/* Settings — clean, grouped layout. Real app content only: passphrase reminder, then a
   "Backup & security" group (app lock + backup) and a "Preferences" group (theme + language).
   Each group is one card with a neutral leading icon tile; theme uses a visual preview selector. */
function Settings({
  theme,
  onTheme,
  onOpenBackup,
  showReminder,
  onCheckPassphrase
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    SegmentedControl
  } = NS;
  const {
    Toggle
  } = window.HisabakExtras;
  const [sel, setSel] = React.useState(theme || 'system');
  const [lang, setLang] = React.useState('en');
  const [appLock, setAppLock] = React.useState(true);
  const [remembered, setRemembered] = React.useState(false);
  const reminderVisible = showReminder && !remembered;
  const Tile = ({
    icon
  }) => /*#__PURE__*/React.createElement("span", {
    style: {
      width: 38,
      height: 38,
      borderRadius: 11,
      background: 'var(--surface-sunken)',
      display: 'grid',
      placeItems: 'center',
      flex: 'none'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--text-secondary)'
    }
  }, icon));
  const Head = ({
    icon,
    title,
    hint,
    trailing,
    onClick
  }) => /*#__PURE__*/React.createElement("div", {
    onClick: onClick,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12,
      cursor: onClick ? 'pointer' : 'default'
    }
  }, /*#__PURE__*/React.createElement(Tile, {
    icon: icon
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, title), hint && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)',
      marginTop: 1
    }
  }, hint)), trailing);
  const Label = ({
    children
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 12,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
      color: 'var(--text-secondary)',
      padding: '6px 2px 0'
    }
  }, children);
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '14px 16px 28px',
      display: 'flex',
      flexDirection: 'column',
      gap: 14
    }
  }, reminderVisible && /*#__PURE__*/React.createElement(Card, {
    padding: 0
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      padding: 16,
      display: 'flex',
      flexDirection: 'column',
      gap: 10
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 26,
      height: 26,
      borderRadius: 999,
      background: 'var(--danger)',
      display: 'grid',
      placeItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 16,
      color: '#fff'
    }
  }, "priority_high")), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, "Do you still remember your passphrase?")), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13.5,
      color: 'var(--text-secondary)'
    }
  }, "It's the only key to your encrypted backup. There's no way to recover it if it's lost.")), /*#__PURE__*/React.createElement("div", {
    style: {
      height: 1,
      background: 'var(--divider)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    onClick: () => setRemembered(true),
    style: {
      padding: '14px 16px',
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--accent)',
      cursor: 'pointer'
    }
  }, "Yes, I remember it"), /*#__PURE__*/React.createElement("div", {
    style: {
      height: 1,
      background: 'var(--divider)'
    }
  }), /*#__PURE__*/React.createElement("div", {
    onClick: onCheckPassphrase,
    style: {
      padding: '14px 16px',
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--accent)',
      cursor: 'pointer'
    }
  }, "Check my passphrase")), /*#__PURE__*/React.createElement(Label, null, "Backup & security"), /*#__PURE__*/React.createElement(Card, null, /*#__PURE__*/React.createElement(Head, {
    icon: "lock",
    title: "App lock",
    hint: "Require fingerprint, face, or PIN to open Hisabak",
    trailing: /*#__PURE__*/React.createElement(Toggle, {
      checked: appLock,
      onChange: setAppLock
    })
  })), /*#__PURE__*/React.createElement(Card, {
    onClick: onOpenBackup,
    style: {
      cursor: 'pointer'
    }
  }, /*#__PURE__*/React.createElement(Head, {
    icon: "cloud_sync",
    title: "Backup & restore",
    hint: "On \xB7 Weekly \xB7 last backup 2h ago",
    trailing: /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 22,
        color: 'var(--text-tertiary)'
      }
    }, "chevron_right")
  })), /*#__PURE__*/React.createElement(Label, null, "Preferences"), /*#__PURE__*/React.createElement(Card, null, /*#__PURE__*/React.createElement(Head, {
    icon: "palette",
    title: "Theme",
    hint: "Choose how Hisabak looks"
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(SegmentedControl, {
    options: [{
      value: 'system',
      label: 'System'
    }, {
      value: 'light',
      label: 'Light'
    }, {
      value: 'dark',
      label: 'Dark'
    }],
    value: sel,
    onChange: v => {
      setSel(v);
      onTheme && onTheme(v);
    }
  }))), /*#__PURE__*/React.createElement(Card, null, /*#__PURE__*/React.createElement(Head, {
    icon: "translate",
    title: "Language",
    hint: "Numbers follow your language"
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(SegmentedControl, {
    options: [{
      value: 'en',
      label: 'English'
    }, {
      value: 'ar',
      label: 'العربية'
    }],
    value: lang,
    onChange: setLang
  }))));
}
window.HisabakSettings = Settings;
/* Backup & restore — mirrors BackupScreen.kt. Two states: not-enabled (hero + benefits + "Turn on")
   and enabled (last-backup card, "Back up now", auto-backup period, encryption toggle + passphrase,
   "Turn off backup"). The demo toggles enabled inline; "Back up now" launches the Sync screen. */
function Backup({
  onBackupNow,
  onOpenPassphrase,
  period = 'Weekly',
  onOpenPeriod
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Button
  } = NS;
  const {
    Toggle,
    SettingsRow,
    HeroDisc
  } = window.HisabakExtras;
  const [enabled, setEnabled] = React.useState(true);
  const [encrypt, setEncrypt] = React.useState(true);
  const openPass = onOpenPassphrase || (() => {});
  const openPeriod = onOpenPeriod || (() => {});
  const Header = () => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      textAlign: 'center',
      gap: 8
    }
  }, /*#__PURE__*/React.createElement(HeroDisc, {
    icon: "cloud_upload",
    tint: "var(--accent-soft)"
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 21,
      color: 'var(--text-primary)',
      marginTop: 8
    }
  }, "Back up to Google Drive"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 14,
      color: 'var(--text-secondary)',
      maxWidth: 280
    }
  }, "Keep a private copy of your data in your own Drive, ready to restore on any device."));
  const Benefit = ({
    icon,
    title,
    sub
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 14,
      alignItems: 'flex-start'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      width: 38,
      height: 38,
      flex: 'none',
      borderRadius: 12,
      background: 'var(--accent-soft)',
      display: 'grid',
      placeItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--accent)'
    }
  }, icon)), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13.5,
      color: 'var(--text-secondary)',
      marginTop: 2
    }
  }, sub)));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '20px 16px 28px',
      display: 'flex',
      flexDirection: 'column',
      gap: 22
    }
  }, /*#__PURE__*/React.createElement(Header, null), !enabled ? /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 18
    }
  }, /*#__PURE__*/React.createElement(Benefit, {
    icon: "lock",
    title: "Encrypted end to end",
    sub: "Optionally lock your backup with a passphrase only you know."
  }), /*#__PURE__*/React.createElement(Benefit, {
    icon: "cloud_download",
    title: "Restore anywhere",
    sub: "Reinstall and pick up exactly where you left off."
  }), /*#__PURE__*/React.createElement(Benefit, {
    icon: "schedule",
    title: "Automatic backups",
    sub: "Set it once and Hisabak keeps your Drive copy current."
  })), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    onClick: () => setEnabled(true)
  }, "Turn on backup")) : /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement(Card, null, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 16
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 44,
      color: 'var(--accent)'
    }
  }, "cloud_sync"), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, "Last backup 2 hours ago"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13.5,
      color: 'var(--text-secondary)',
      marginTop: 2
    }
  }, "48 KB")))), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    onClick: onBackupNow
  }, "Back up now"), /*#__PURE__*/React.createElement(Card, {
    padding: 0
  }, /*#__PURE__*/React.createElement(SettingsRow, {
    icon: "schedule",
    title: "Automatic backups",
    subtitle: period === 'Never' ? 'Off — back up manually' : 'Runs quietly in the background',
    value: period,
    onClick: openPeriod,
    divider: true
  }), /*#__PURE__*/React.createElement(SettingsRow, {
    icon: "lock",
    title: "Encrypt backup",
    subtitle: "Lock it with a passphrase",
    trailing: /*#__PURE__*/React.createElement(Toggle, {
      checked: encrypt,
      onChange: v => {
        setEncrypt(v);
        if (v) openPass();
      }
    }),
    divider: encrypt
  }), encrypt && /*#__PURE__*/React.createElement(SettingsRow, {
    icon: "key",
    title: "Passphrase",
    subtitle: "Set \u2014 keep it safe",
    value: "Change",
    onClick: openPass
  })), /*#__PURE__*/React.createElement("div", {
    onClick: () => setEnabled(false),
    style: {
      textAlign: 'center',
      padding: 12,
      fontFamily: 'var(--font-sans)',
      fontSize: 14,
      color: 'var(--expense)',
      cursor: 'pointer'
    }
  }, "Turn off backup")));
}
window.HisabakBackup = Backup;
/* Notifications — list of alerts (budget limits, backup results) with unread dots, "Mark all read",
   and a real empty state. Mirrors NotificationsScreen.kt. Swipe-to-dismiss is shown as a hint row. */
function Notifications() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    EmptyState
  } = NS;
  const M = window.HisabakMock;
  const [rows, setRows] = React.useState(M.NOTIFICATIONS);
  if (rows.length === 0) {
    return /*#__PURE__*/React.createElement("div", {
      style: {
        height: '100%',
        display: 'grid',
        placeItems: 'center',
        padding: 24
      }
    }, /*#__PURE__*/React.createElement(EmptyState, {
      icon: "notifications_none",
      title: "No notifications yet",
      description: "Budget alerts and backup updates will show up here."
    }));
  }
  const hasUnread = rows.some(r => !r.read);
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '12px 16px 28px',
      display: 'flex',
      flexDirection: 'column',
      gap: 12
    }
  }, hasUnread && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'flex-end'
    }
  }, /*#__PURE__*/React.createElement("span", {
    onClick: () => setRows(rs => rs.map(r => ({
      ...r,
      read: true
    }))),
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 13.5,
      color: 'var(--accent)',
      cursor: 'pointer',
      padding: 4
    }
  }, "Mark all read")), rows.map(r => {
    const tint = r.tone === 'expense' ? ['var(--expense-soft)', 'var(--expense)'] : r.tone === 'warning' ? ['var(--warning-soft)', 'var(--warning)'] : ['var(--accent-soft)', 'var(--accent)'];
    return /*#__PURE__*/React.createElement(Card, {
      key: r.id,
      padding: 14
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        gap: 14,
        alignItems: 'flex-start'
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        width: 40,
        height: 40,
        flex: 'none',
        borderRadius: 999,
        background: tint[0],
        display: 'grid',
        placeItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 20,
        color: tint[1]
      }
    }, r.icon)), /*#__PURE__*/React.createElement("div", {
      style: {
        flex: 1,
        minWidth: 0
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontWeight: r.read ? 500 : 600,
        fontSize: 15,
        color: 'var(--text-primary)'
      }
    }, r.title), /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 13.5,
        color: 'var(--text-secondary)',
        marginTop: 2
      }
    }, r.message), /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 12,
        color: 'var(--text-tertiary)',
        marginTop: 6
      }
    }, r.time)), !r.read && /*#__PURE__*/React.createElement("span", {
      style: {
        width: 9,
        height: 9,
        flex: 'none',
        borderRadius: 999,
        background: 'var(--accent)',
        marginTop: 6
      }
    })));
  }));
}
window.HisabakNotifications = Notifications;
/* Category edit — name, type (segmented, semantic tones), monthly limit (expense only), color
   swatches, icon chips, live preview, save. Mirrors CategoryEditScreen.kt. */
function CategoryEdit() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Input,
    SegmentedControl,
    Button,
    CategoryIcon
  } = NS;
  const {
    FormSection
  } = window.HisabakExtras;
  const [name, setName] = React.useState('Groceries');
  const [type, setType] = React.useState('expense');
  const [color, setColor] = React.useState('var(--cat-orange)');
  const [icon, setIcon] = React.useState('shopping_cart');
  const palette = ['var(--cat-green)', 'var(--cat-blue)', 'var(--cat-orange)', 'var(--cat-red)', 'var(--cat-teal)', 'var(--cat-purple)', 'var(--cat-pink)', 'var(--cat-gray)'];
  const icons = ['account_balance_wallet', 'shopping_cart', 'work', 'directions_car', 'restaurant', 'savings', 'home', 'movie', 'menu_book', 'favorite', 'card_giftcard', 'flight'];
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '20px 16px 28px',
      display: 'flex',
      flexDirection: 'column',
      gap: 20
    }
  }, /*#__PURE__*/React.createElement(Input, {
    label: "Name",
    value: name,
    onChange: e => setName(e.target.value)
  }), /*#__PURE__*/React.createElement(FormSection, {
    label: "Type"
  }, /*#__PURE__*/React.createElement(SegmentedControl, {
    options: [{
      value: 'income',
      label: 'Income',
      tone: 'income'
    }, {
      value: 'expense',
      label: 'Expenses',
      tone: 'expense'
    }, {
      value: 'savings',
      label: 'Savings',
      tone: 'savings'
    }, {
      value: 'investment',
      label: 'Invest',
      tone: 'investment'
    }],
    value: type,
    onChange: setType
  })), type === 'expense' && /*#__PURE__*/React.createElement(Input, {
    label: "Monthly limit",
    value: "2,000",
    leadingIcon: "payments",
    helper: "Get an alert as you approach it",
    onChange: () => {}
  }), /*#__PURE__*/React.createElement(FormSection, {
    label: "Color"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 10,
      overflowX: 'auto',
      paddingBottom: 2
    }
  }, palette.map(c => /*#__PURE__*/React.createElement("button", {
    key: c,
    onClick: () => setColor(c),
    style: {
      width: 36,
      height: 36,
      flex: 'none',
      borderRadius: 10,
      background: c,
      cursor: 'pointer',
      border: c === color ? '2px solid var(--text-primary)' : '2px solid transparent',
      display: 'grid',
      placeItems: 'center'
    }
  }, c === color && /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18,
      color: '#fff'
    }
  }, "check"))))), /*#__PURE__*/React.createElement(FormSection, {
    label: "Icon"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 10,
      overflowX: 'auto',
      paddingBottom: 2
    }
  }, icons.map(ic => /*#__PURE__*/React.createElement("button", {
    key: ic,
    onClick: () => setIcon(ic),
    style: {
      width: 44,
      height: 44,
      flex: 'none',
      borderRadius: 12,
      cursor: 'pointer',
      background: `color-mix(in srgb, ${color} 16%, transparent)`,
      border: ic === icon ? '1px solid var(--accent)' : '1px solid var(--border)',
      display: 'grid',
      placeItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color
    }
  }, ic))))), /*#__PURE__*/React.createElement(FormSection, {
    label: "Preview"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 14,
      background: 'var(--surface)',
      border: '1px solid var(--border)',
      borderRadius: 12,
      padding: 16
    }
  }, /*#__PURE__*/React.createElement(CategoryIcon, {
    icon: icon,
    color: color,
    size: 44
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, name || 'Category name'))), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true
  }, "Save"));
}
window.HisabakCategoryEdit = CategoryEdit;
/* Brand edit — name + a category picker (color-dot chips, incl. "None"). Mirrors BrandEditScreen.kt. */
function BrandEdit() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Input,
    Button,
    Chip
  } = NS;
  const {
    FormSection
  } = window.HisabakExtras;
  const M = window.HisabakMock;
  const [name, setName] = React.useState('Carrefour');
  const [cat, setCat] = React.useState('groceries');
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '20px 16px 28px',
      display: 'flex',
      flexDirection: 'column',
      gap: 20
    }
  }, /*#__PURE__*/React.createElement(Input, {
    label: "Brand name",
    value: name,
    onChange: e => setName(e.target.value)
  }), /*#__PURE__*/React.createElement(FormSection, {
    label: "Category"
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      flexWrap: 'wrap'
    }
  }, /*#__PURE__*/React.createElement(Chip, {
    selected: cat === null,
    onClick: () => setCat(null)
  }, "None"), M.CATEGORIES.map(c => /*#__PURE__*/React.createElement(Chip, {
    key: c.id,
    selected: cat === c.id,
    color: c.color,
    onClick: () => setCat(c.id)
  }, c.name)))), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true
  }, "Save"));
}
window.HisabakBrandEdit = BrandEdit;
/* Manage — the tab that merges Brands + Categories (mirrors ManageRoute.kt). Two count cards act as
   the switcher; the active list shows below; the FAB adds the active type. */
function Manage({
  onEditBrand,
  onEditCategory,
  onAdd
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    SearchBar,
    ListRow,
    CategoryTile,
    AmountText
  } = NS;
  const M = window.HisabakMock;
  const [tab, setTab] = React.useState('brands');
  const [q, setQ] = React.useState('');
  const catName = id => (M.CATEGORIES.find(c => c.id === id) || {}).name || '';
  const catColor = id => (M.CATEGORIES.find(c => c.id === id) || {}).color || 'var(--cat-gray)';
  const totals = {
    carrefour: 1240,
    stc: 820,
    talabat: 540,
    uber: 388,
    netflix: 256,
    acme: 8200,
    starbucks: 210,
    amazon: 180
  };
  const SwitchCard = ({
    id,
    label,
    count,
    icon
  }) => {
    const on = tab === id;
    return /*#__PURE__*/React.createElement(Card, {
      padding: 14,
      variant: on ? 'tinted' : 'default',
      tint: "var(--accent-soft)",
      onClick: () => setTab(id),
      style: {
        flex: 1,
        cursor: 'pointer',
        border: on ? '1px solid var(--accent)' : '1px solid var(--border)'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 10
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        width: 32,
        height: 32,
        borderRadius: 10,
        display: 'grid',
        placeItems: 'center',
        background: on ? 'var(--accent)' : 'var(--surface-sunken)'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 18,
        color: on ? '#fff' : 'var(--text-secondary)'
      }
    }, icon)), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-mono)',
        fontWeight: 700,
        fontSize: 18,
        color: 'var(--text-primary)'
      }
    }, count), /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 12,
        color: 'var(--text-secondary)'
      }
    }, label))));
  };
  const brands = M.BRANDS.filter(b => b.name.toLowerCase().includes(q.toLowerCase()));
  const cats = M.CATEGORIES.filter(c => c.name.toLowerCase().includes(q.toLowerCase()));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '12px 16px 28px'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement(SwitchCard, {
    id: "brands",
    label: "Brands",
    count: 8,
    icon: "storefront"
  }), /*#__PURE__*/React.createElement(SwitchCard, {
    id: "categories",
    label: "Categories",
    count: 8,
    icon: "category"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(SearchBar, {
    value: q,
    placeholder: tab === 'brands' ? 'Search brands' : 'Search categories',
    onChange: e => setQ(e.target.value),
    onClear: () => setQ('')
  })), tab === 'brands' ? /*#__PURE__*/React.createElement(Card, {
    padding: 0,
    style: {
      marginTop: 14,
      padding: '2px 14px'
    }
  }, brands.map((b, i) => /*#__PURE__*/React.createElement(ListRow, {
    key: b.id,
    title: b.name,
    subtitle: catName(b.category),
    leadingText: b.initial,
    color: catColor(b.category),
    divider: i < brands.length - 1,
    onClick: () => onEditBrand(b),
    trailing: /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 8
      }
    }, /*#__PURE__*/React.createElement(AmountText, {
      value: totals[b.id] || 0,
      tone: "neutral",
      sign: "never",
      size: 14
    }), /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 18,
        color: 'var(--text-tertiary)'
      }
    }, "chevron_right"))
  }))) : /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: 12,
      marginTop: 14
    }
  }, cats.map(c => /*#__PURE__*/React.createElement(CategoryTile, {
    key: c.id,
    name: c.name,
    icon: c.icon,
    color: c.color,
    type: c.type,
    total: M.money(c.total, {
      decimals: false
    }),
    onClick: () => onEditCategory(c),
    onDelete: () => {}
  })), /*#__PURE__*/React.createElement(CategoryTile, {
    addNew: true,
    onClick: onAdd
  })));
}
window.HisabakManage = Manage;
/* Onboarding — 6-page intro pager with skip, animated dots, and a primary CTA (mirrors
   OnboardingScreen.kt). The last page's CTA finishes (and, in the SMS build, primes capture). */
function Onboarding({
  onFinish
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Button
  } = NS;
  const {
    HeroDisc
  } = window.HisabakExtras;
  const [page, setPage] = React.useState(0);
  const pages = [{
    icon: 'savings',
    tint: 'var(--accent-soft)',
    fg: 'var(--accent)',
    title: 'Welcome to Hisabak',
    body: 'Your calm companion for understanding where your money goes.'
  }, {
    icon: 'sms',
    tint: 'var(--savings-soft)',
    fg: 'var(--savings)',
    title: 'Capture from SMS',
    body: 'Hisabak reads your bank alerts and logs transactions automatically — no typing.'
  }, {
    icon: 'lock',
    tint: 'var(--accent-soft)',
    fg: 'var(--accent)',
    title: 'Private by design',
    body: 'Your data is encrypted on your device and never leaves without your say.'
  }, {
    icon: 'donut_small',
    tint: 'var(--investment-soft)',
    fg: 'var(--investment)',
    title: 'Set budgets',
    body: 'Give each category a limit and get a nudge before you overspend.'
  }, {
    icon: 'insights',
    tint: 'var(--expense-soft)',
    fg: 'var(--expense)',
    title: 'See the trends',
    body: 'Clean charts show income, spending, and net worth over time.'
  }, {
    icon: 'check_circle',
    tint: 'var(--accent-soft)',
    fg: 'var(--accent)',
    title: "You're all set",
    body: 'Add your first transaction or let an SMS do it for you.'
  }];
  const isLast = page === pages.length - 1;
  const p = pages[page];
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: 'var(--bg)',
      display: 'flex',
      flexDirection: 'column',
      zIndex: 30
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      height: 48,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'flex-end',
      padding: '0 16px'
    }
  }, !isLast && /*#__PURE__*/React.createElement("span", {
    onClick: () => setPage(pages.length - 1),
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 14,
      color: 'var(--text-secondary)',
      cursor: 'pointer',
      padding: 8
    }
  }, "Skip")), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      textAlign: 'center',
      padding: '0 36px',
      gap: 28
    }
  }, /*#__PURE__*/React.createElement(HeroDisc, {
    icon: p.icon,
    size: 132,
    iconSize: 60,
    tint: p.tint,
    fg: p.fg
  }), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 26,
      color: 'var(--text-primary)',
      letterSpacing: '-0.02em'
    }
  }, p.title), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 15.5,
      color: 'var(--text-secondary)',
      marginTop: 12,
      lineHeight: 1.5
    }
  }, p.body))), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '0 24px 28px',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      gap: 20
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 7
    }
  }, pages.map((_, i) => /*#__PURE__*/React.createElement("span", {
    key: i,
    style: {
      height: 7,
      width: i === page ? 22 : 7,
      borderRadius: 999,
      background: i === page ? 'var(--accent)' : 'var(--border-strong)',
      transition: 'width var(--dur-base), background var(--dur-base)'
    }
  }))), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    onClick: () => isLast ? onFinish() : setPage(page + 1)
  }, isLast ? 'Get started' : 'Next')));
}
window.HisabakOnboarding = Onboarding;
/* Restore — one-time post-onboarding offer to bring data back from Drive (mirrors RestoreScreen.kt).
   Two panes: Intro (connect / skip) and Passphrase (the found backup is encrypted → enter it).
   "Connect" advances to the passphrase pane; "Restore" hands off to the Sync flow via onConnect. */
function Restore({
  onConnect,
  onSkip
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Button,
    Input
  } = NS;
  const {
    HeroDisc
  } = window.HisabakExtras;
  const [view, setView] = React.useState('intro');
  const [pass, setPass] = React.useState('');
  const Pane = ({
    icon,
    overline,
    title,
    subtitle,
    children,
    primary,
    primaryEnabled = true,
    onPrimary
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: 'var(--bg)',
      display: 'flex',
      flexDirection: 'column',
      padding: '0 28px',
      zIndex: 30
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      display: 'grid',
      placeItems: 'center'
    }
  }, /*#__PURE__*/React.createElement(HeroDisc, {
    icon: icon,
    size: 120,
    iconSize: 56,
    tint: "var(--accent-soft)"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      paddingBottom: 8
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 12,
      letterSpacing: '0.06em',
      textTransform: 'uppercase',
      color: 'var(--accent)'
    }
  }, overline), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 26,
      color: 'var(--text-primary)',
      marginTop: 12,
      letterSpacing: '-0.02em'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-secondary)',
      marginTop: 12,
      lineHeight: 1.5
    }
  }, subtitle), children), /*#__PURE__*/React.createElement("div", {
    style: {
      paddingBottom: 28,
      paddingTop: 20,
      display: 'flex',
      flexDirection: 'column',
      gap: 8
    }
  }, /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    disabled: !primaryEnabled,
    onClick: onPrimary
  }, primary), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    variant: "ghost",
    onClick: onSkip
  }, "Not now")));
  if (view === 'passphrase') {
    return /*#__PURE__*/React.createElement(Pane, {
      icon: "lock",
      overline: "Restore",
      title: "Enter your passphrase",
      subtitle: "This backup is encrypted. Enter the passphrase you set to unlock it.",
      primary: "Restore",
      primaryEnabled: pass.length > 0,
      onPrimary: onConnect
    }, /*#__PURE__*/React.createElement(Input, {
      label: "Passphrase",
      type: "password",
      value: pass,
      onChange: e => setPass(e.target.value),
      style: {
        marginTop: 18
      }
    }));
  }
  return /*#__PURE__*/React.createElement(Pane, {
    icon: "cloud_download",
    overline: "Restore",
    title: "Bring your data back",
    subtitle: "Connect the Google account you backed up with and we'll restore your transactions, brands, and categories.",
    primary: "Connect Google Drive",
    onPrimary: () => setView('passphrase')
  });
}
window.HisabakRestore = Restore;
/* Sync — full-screen progress/result for a backup or restore (mirrors SyncScreen.kt).
   Phases: running (animated halo), done (check), failed (error + retry). kind = 'backup' | 'restore'. */
function Sync({
  kind = 'backup',
  phase = 'running',
  onContinue,
  onRetry,
  onClose
}) {
  const glyph = kind === 'backup' ? 'cloud_upload' : 'cloud_download';
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Button
  } = NS;
  const copy = {
    running: {
      title: kind === 'backup' ? 'Backing up…' : 'Restoring…',
      sub: kind === 'backup' ? 'Saving your data to Drive' : 'Bringing your data back'
    },
    done: {
      title: kind === 'backup' ? 'Backed up' : 'Restored',
      sub: kind === 'backup' ? 'Your Drive copy is up to date' : '128 records restored'
    },
    failed: {
      title: kind === 'backup' ? "Couldn't back up" : "Couldn't restore",
      sub: 'Check your connection and try again'
    }
  }[phase];
  const Halo = () => {
    if (phase === 'done') {
      return /*#__PURE__*/React.createElement("div", {
        style: {
          width: 150,
          height: 150,
          display: 'grid',
          placeItems: 'center'
        }
      }, /*#__PURE__*/React.createElement("div", {
        style: {
          position: 'absolute',
          width: 132,
          height: 132,
          borderRadius: 999,
          border: '5px solid var(--accent)'
        }
      }), /*#__PURE__*/React.createElement("div", {
        style: {
          width: 96,
          height: 96,
          borderRadius: 999,
          background: 'var(--accent)',
          display: 'grid',
          placeItems: 'center'
        }
      }, /*#__PURE__*/React.createElement("span", {
        className: "material-symbols-rounded",
        style: {
          fontSize: 52,
          color: '#fff'
        }
      }, "check")));
    }
    if (phase === 'failed') {
      return /*#__PURE__*/React.createElement("div", {
        style: {
          width: 96,
          height: 96,
          borderRadius: 999,
          background: 'var(--danger-soft)',
          display: 'grid',
          placeItems: 'center'
        }
      }, /*#__PURE__*/React.createElement("span", {
        className: "material-symbols-rounded",
        style: {
          fontSize: 48,
          color: 'var(--danger)'
        }
      }, "error"));
    }
    return /*#__PURE__*/React.createElement("div", {
      style: {
        width: 150,
        height: 150,
        display: 'grid',
        placeItems: 'center',
        position: 'relative'
      }
    }, /*#__PURE__*/React.createElement("div", {
      className: "sync-pulse",
      style: {
        position: 'absolute',
        width: 132,
        height: 132,
        borderRadius: 999,
        background: 'var(--accent-soft)'
      }
    }), /*#__PURE__*/React.createElement("div", {
      className: "sync-spin",
      style: {
        position: 'absolute',
        width: 132,
        height: 132,
        borderRadius: 999,
        border: '5px solid var(--accent-soft)',
        borderTopColor: 'var(--accent)'
      }
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        width: 96,
        height: 96,
        borderRadius: 999,
        background: 'var(--accent-soft)',
        display: 'grid',
        placeItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 44,
        color: 'var(--accent)'
      }
    }, glyph)));
  };
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      background: 'var(--bg)',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      padding: '0 28px',
      zIndex: 30
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      gap: 24
    }
  }, /*#__PURE__*/React.createElement(Halo, null), /*#__PURE__*/React.createElement("div", {
    style: {
      textAlign: 'center'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 21,
      color: 'var(--text-primary)'
    }
  }, copy.title), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 14,
      color: 'var(--text-secondary)',
      marginTop: 6
    }
  }, copy.sub))), /*#__PURE__*/React.createElement("div", {
    style: {
      width: '100%',
      paddingBottom: 28,
      display: 'flex',
      flexDirection: 'column',
      gap: 8
    }
  }, phase === 'done' && /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    onClick: onContinue
  }, "Continue"), phase === 'failed' && /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    onClick: onRetry
  }, "Try again"), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    variant: "ghost",
    onClick: onClose
  }, "Close"))));
}
window.HisabakSync = Sync;
/* Passphrase bottom sheets — set/change (from Backup) and verify (from the Settings reminder).
   Mirror PassphraseSheet (BackupScreen.kt) and PassphraseVerifySheet (SettingsScreen.kt). */
(function () {
  const NS = window.HisabakDesignSystem_aa2548;
  function Sheet({
    open,
    onClose,
    children
  }) {
    return /*#__PURE__*/React.createElement("div", {
      style: {
        position: 'absolute',
        inset: 0,
        zIndex: 40,
        pointerEvents: open ? 'auto' : 'none'
      }
    }, /*#__PURE__*/React.createElement("div", {
      onClick: onClose,
      style: {
        position: 'absolute',
        inset: 0,
        background: 'var(--scrim)',
        opacity: open ? 1 : 0,
        transition: 'opacity var(--dur-base)'
      }
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        position: 'absolute',
        left: 0,
        right: 0,
        bottom: 0,
        background: 'var(--surface)',
        borderTopLeftRadius: 'var(--r-xl)',
        borderTopRightRadius: 'var(--r-xl)',
        boxShadow: 'var(--shadow-lg)',
        padding: '10px 18px calc(20px + var(--navbar-inset))',
        transform: open ? 'translateY(0)' : 'translateY(102%)',
        transition: 'transform var(--dur-slow) var(--ease-emphasis)',
        maxHeight: '92%',
        overflowY: 'auto'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        width: 40,
        height: 4,
        borderRadius: 2,
        background: 'var(--border-strong)',
        margin: '0 auto 16px'
      }
    }), children));
  }
  const Title = ({
    children
  }) => /*#__PURE__*/React.createElement("h2", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 18,
      color: 'var(--text-primary)'
    }
  }, children);
  const Body = ({
    children,
    center
  }) => /*#__PURE__*/React.createElement("p", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-sans)',
      fontSize: 14,
      lineHeight: 1.5,
      color: 'var(--text-secondary)',
      textAlign: center ? 'center' : 'left'
    }
  }, children);

  // Set / change the backup passphrase.
  function PassphraseSheet({
    open,
    onClose,
    onSave
  }) {
    const {
      Input,
      Button
    } = NS;
    const [pass, setPass] = React.useState('');
    const [confirm, setConfirm] = React.useState('');
    const tooShort = pass.length > 0 && pass.length < 8;
    const mismatch = confirm.length > 0 && confirm !== pass;
    const canSave = pass.length >= 8 && pass === confirm;
    const done = () => {
      onSave && onSave();
      setPass('');
      setConfirm('');
    };
    return /*#__PURE__*/React.createElement(Sheet, {
      open: open,
      onClose: onClose
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        gap: 14
      }
    }, /*#__PURE__*/React.createElement(Title, null, "Set a passphrase"), /*#__PURE__*/React.createElement(Body, null, "This passphrase encrypts your backup. If you lose it, your backup can't be recovered \u2014 there's no reset."), /*#__PURE__*/React.createElement(Body, null, /*#__PURE__*/React.createElement("span", {
      style: {
        fontSize: 13,
        color: 'var(--text-tertiary)'
      }
    }, "It applies to your next backup onwards.")), /*#__PURE__*/React.createElement(Input, {
      label: "Passphrase",
      type: "password",
      value: pass,
      onChange: e => setPass(e.target.value),
      error: tooShort ? 'Use at least 8 characters' : undefined
    }), /*#__PURE__*/React.createElement(Input, {
      label: "Confirm passphrase",
      type: "password",
      value: confirm,
      onChange: e => setConfirm(e.target.value),
      error: mismatch ? "Passphrases don't match" : undefined
    }), /*#__PURE__*/React.createElement(Button, {
      fullWidth: true,
      disabled: !canSave,
      onClick: done
    }, "Save passphrase")));
  }

  // Verify the existing passphrase (Settings reminder → "Check my passphrase"), with a success state.
  function PassphraseVerifySheet({
    open,
    onClose,
    onChangePassphrase
  }) {
    const {
      Input,
      Button
    } = NS;
    const [input, setInput] = React.useState('');
    const [wrong, setWrong] = React.useState(false);
    const [success, setSuccess] = React.useState(false);
    const reset = () => {
      setInput('');
      setWrong(false);
      setSuccess(false);
    };
    const close = () => {
      reset();
      onClose && onClose();
    };
    // Mock rule so both outcomes are visible: 8+ chars verifies, shorter shows the wrong state.
    const verify = () => input.length >= 8 ? setSuccess(true) : setWrong(true);
    return /*#__PURE__*/React.createElement(Sheet, {
      open: open,
      onClose: close
    }, success ? /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 14,
        paddingBottom: 6
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        width: 72,
        height: 72,
        borderRadius: 999,
        background: 'var(--accent)',
        display: 'grid',
        placeItems: 'center',
        marginTop: 4
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 40,
        color: '#fff'
      }
    }, "check")), /*#__PURE__*/React.createElement(Title, null, "You remember it"), /*#__PURE__*/React.createElement(Body, {
      center: true
    }, "That's the right passphrase. Your backup is safe and recoverable."), /*#__PURE__*/React.createElement(Button, {
      fullWidth: true,
      onClick: close
    }, "Done")) : /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        gap: 14
      }
    }, /*#__PURE__*/React.createElement(Title, null, "Check your passphrase"), /*#__PURE__*/React.createElement(Body, null, "Enter your backup passphrase to confirm you still have it. We never store it in readable form."), /*#__PURE__*/React.createElement(Input, {
      label: "Passphrase",
      type: "password",
      value: input,
      onChange: e => {
        setInput(e.target.value);
        setWrong(false);
      },
      error: wrong ? "That doesn't match your saved passphrase" : undefined
    }), /*#__PURE__*/React.createElement(Button, {
      fullWidth: true,
      disabled: !input,
      onClick: verify
    }, "Verify"), wrong && /*#__PURE__*/React.createElement("span", {
      onClick: onChangePassphrase,
      style: {
        textAlign: 'center',
        fontFamily: 'var(--font-sans)',
        fontSize: 14,
        color: 'var(--accent)',
        cursor: 'pointer',
        padding: 4
      }
    }, "Change passphrase instead")));
  }
  window.HisabakPassphraseSheet = PassphraseSheet;
  window.HisabakPassphraseVerifySheet = PassphraseVerifySheet;
})();
/* Auto-backup period picker — radio bottom sheet (mirrors PeriodSheet in BackupScreen.kt).
   Options: Never, Daily, Weekly, Monthly (default Never in the app; the kit defaults to Weekly). */
function PeriodSheet({
  open,
  selected,
  onSelect,
  onClose
}) {
  const {
    Sheet,
    RadioRow
  } = window.HisabakExtras;
  const options = ['Never', 'Daily', 'Weekly', 'Monthly'];
  return /*#__PURE__*/React.createElement(Sheet, {
    open: open,
    onClose: onClose
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 18,
      color: 'var(--text-primary)',
      padding: '0 16px 8px'
    }
  }, "Automatic backups"), options.map(o => /*#__PURE__*/React.createElement(RadioRow, {
    key: o,
    label: o,
    selected: selected === o,
    onClick: () => onSelect(o)
  })));
}
window.HisabakPeriodSheet = PeriodSheet;
