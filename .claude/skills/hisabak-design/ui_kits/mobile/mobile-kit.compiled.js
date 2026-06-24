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
  }, it.label), /*#__PURE__*/React.createElement(Money, {
    value: it.value,
    size: 13,
    weight: 600,
    color: "var(--text-secondary)"
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12,
      color: 'var(--text-tertiary)',
      width: 36,
      textAlign: 'right'
    }
  }, Math.round(it.value / total * 100), "%"))));
}

/* Official UAE dirham symbol (traced from res/drawable/ic_dirham). The brand shows this glyph for
   money — never the text "AED". viewBox is wider than tall (1000×870). */
function Dirham({
  size = 14,
  color = 'currentColor',
  style
}) {
  return /*#__PURE__*/React.createElement("svg", {
    viewBox: "0 0 1000 870",
    height: size,
    width: size * 1.15,
    "aria-label": "dirham",
    style: {
      display: 'inline-block',
      flex: 'none',
      ...style
    }
  }, /*#__PURE__*/React.createElement("path", {
    fill: color,
    d: "m88.3 1c0.4 0.6 2.6 3.3 4.7 5.9 15.3 18.2 26.8 47.8 33 85.1 4.1 24.5 4.3 32.2 4.3 125.6v87h-41.8c-38.2 0-42.6-0.2-50.1-1.7-11.8-2.5-24-9.2-32.2-17.8-6.5-6.9-6.3-7.3-5.9 13.6 0.5 17.3 0.7 19.2 3.2 28.6 4 14.9 9.5 26 17.8 35.9 11.3 13.6 22.8 21.2 39.2 26.3 3.5 1 10.9 1.4 37.1 1.6l32.7 0.5v43.3 43.4l-46.1-0.3-46.3-0.3-8-3.2c-9.5-3.8-13.8-6.6-23.1-14.9l-6.8-6.1 0.4 19.1c0.5 17.7 0.6 19.7 3.1 28.7 8.7 31.8 29.7 54.5 57.4 61.9 6.9 1.9 9.6 2 38.5 2.4l30.9 0.4v89.6c0 54.1-0.3 94-0.8 100.8-0.5 6.2-2.1 17.8-3.5 25.9-6.5 37.3-18.2 65.4-35 83.6l-3.4 3.7h169.1c101.1 0 176.7-0.4 187.8-0.9 19.5-1 63-5.3 72.8-7.4 3.1-0.6 8.9-1.5 12.7-2.1 8.1-1.2 21.5-4 40.8-8.9 27.2-6.8 52-15.3 76.3-26.1 7.6-3.4 29.4-14.5 35.2-18 3.1-1.8 6.8-4 8.2-4.7 3.9-2.1 10.4-6.3 19.9-13.1 4.7-3.4 9.4-6.7 10.4-7.4 4.2-2.8 18.7-14.9 25.3-21 25.1-23.1 46.1-48.8 62.4-76.3 2.3-4 5.3-9 6.6-11.1 3.3-5.6 16.9-33.6 18.2-37.8 0.6-1.9 1.4-3.9 1.8-4.3 2.6-3.4 17.6-50.6 19.4-60.9 0.6-3.3 0.9-3.8 3.4-4.3 1.6-0.3 24.9-0.3 51.8-0.1 53.8 0.4 53.8 0.4 65.7 5.9 6.7 3.1 8.7 4.5 16.1 11.2 9.7 8.7 8.8 10.1 8.2-11.7-0.4-12.8-0.9-20.7-1.8-23.9-3.4-12.3-4.2-14.9-7.2-21.1-9.8-21.4-26.2-36.7-47.2-44l-8.2-3-33.4-0.4-33.3-0.5 0.4-11.7c0.4-15.4 0.4-45.9-0.1-61.6l-0.4-12.6 44.6-0.2c38.2-0.2 45.3 0 49.5 1.1 12.6 3.5 21.1 8.3 31.5 17.8l5.8 5.4v-14.8c0-17.6-0.9-25.4-4.5-37-7.1-23.5-21.1-41-41.1-51.8-13-7-13.8-7.2-58.5-7.5-26.2-0.2-39.9-0.6-40.6-1.2-0.6-0.6-1.1-1.6-1.1-2.4 0-0.8-1.5-7.1-3.5-13.9-23.4-82.7-67.1-148.4-131-197.1-8.7-6.7-30-20.8-38.6-25.6-3.3-1.9-6.9-3.9-7.8-4.5-4.2-2.3-28.3-14.1-34.3-16.6-3.6-1.6-8.3-3.6-10.4-4.4-35.3-15.3-94.5-29.8-139.7-34.3-7.4-0.7-17.2-1.8-21.7-2.2-20.4-2.3-48.7-2.6-209.4-2.6-135.8 0-169.9 0.3-169.4 1zm330.7 43.3c33.8 2 54.6 4.6 78.9 10.5 74.2 17.6 126.4 54.8 164.3 117 3.5 5.8 18.3 36 20.5 42.1 10.5 28.3 15.6 45.1 20.1 67.3 1.1 5.4 2.6 12.6 3.3 16 0.7 3.3 1 6.4 0.7 6.7-0.5 0.4-100.9 0.6-223.3 0.5l-222.5-0.2-0.3-128.5c-0.1-70.6 0-129.3 0.3-130.4l0.4-1.9h71.1c39 0 78 0.4 86.5 0.9zm297.5 350.3c0.7 4.3 0.7 77.3 0 80.9l-0.6 2.7-227.5-0.2-227.4-0.3-0.2-42.4c-0.2-23.3 0-42.7 0.2-43.1 0.3-0.5 97.2-0.8 227.7-0.8h227.2zm-10.2 171.7c0.5 1.5-1.9 13.8-6.8 33.8-5.6 22.5-13.2 45.2-20.9 62-3.8 8.6-13.3 27.2-15.6 30.7-1.1 1.6-4.3 6.7-7.1 11.2-18 28.2-43.7 53.9-73 72.9-10.7 6.8-32.7 18.4-38.6 20.2-1.2 0.3-2.5 0.9-3 1.3-0.7 0.6-9.8 4-20.4 7.8-19.5 6.9-56.6 14.4-86.4 17.5-19.3 1.9-22.4 2-96.7 2h-76.9v-129.7-129.8l220.9-0.4c121.5-0.2 221.6-0.5 222.4-0.7 0.9-0.1 1.8 0.5 2.1 1.2z"
  }));
}

/* Money — dirham glyph + compact tabular figure (e.g. 6,450 → 6.45K). Reads LTR. */
function Money({
  value = 0,
  tone = 'neutral',
  sign = 'never',
  size = 15,
  weight = 700,
  color
}) {
  const toneKey = tone === 'auto' ? value < 0 ? 'expense' : 'income' : tone;
  const c = color || {
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)',
    neutral: 'var(--text-primary)'
  }[toneKey] || 'var(--text-primary)';
  const a = Math.abs(value);
  const num = a >= 1e6 ? (a / 1e6).toFixed(2) + 'M' : a >= 1000 ? (a / 1000).toFixed(2) + 'K' : a.toFixed(2);
  const prefix = sign === 'always' || sign === 'auto' && value !== 0 ? value < 0 ? '−' : '+' : '';
  return /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 3,
      color: c,
      direction: 'ltr'
    }
  }, prefix && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: weight,
      fontSize: size
    }
  }, prefix), /*#__PURE__*/React.createElement(Dirham, {
    size: size * 0.82,
    color: c
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontWeight: weight,
      fontSize: size,
      fontVariantNumeric: 'tabular-nums'
    }
  }, num));
}
Object.assign(window, {
  AreaChart,
  Sparkline,
  GroupedBars,
  DonutChart,
  LegendList,
  Dirham,
  Money
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
  }, /*#__PURE__*/React.createElement(Money, {
    value: value,
    tone: "neutral",
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
  }, /*#__PURE__*/React.createElement(Money, {
    value: value,
    tone: label === 'Income' ? 'income' : 'expense',
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
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 5,
      background: bg,
      borderRadius: 999,
      padding: '4px 8px',
      maxWidth: '100%'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 15,
      color: fg,
      flex: 'none'
    }
  }, icon), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 11,
      fontWeight: 600,
      color: fg,
      overflow: 'hidden',
      textOverflow: 'ellipsis',
      whiteSpace: 'nowrap'
    }
  }, label)), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement(Money, {
    value: value,
    tone: "neutral",
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
    }, r.name), /*#__PURE__*/React.createElement(Money, {
      value: r.spent,
      tone: "neutral",
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
        display: 'inline-flex',
        alignItems: 'center',
        gap: 4
      }
    }, /*#__PURE__*/React.createElement(Money, {
      value: r.spent,
      size: 11.5,
      weight: 500,
      color: "currentColor"
    }), " / ", /*#__PURE__*/React.createElement(Money, {
      value: r.limit,
      size: 11.5,
      weight: 500,
      color: "currentColor"
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
  }, "Uncategorized"), /*#__PURE__*/React.createElement(Money, {
    value: 220,
    tone: "neutral",
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
    className: "no-scrollbar",
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
/* Transactions — balance hero, summary, search, filtered list. */
function Transactions({
  onAdd
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Button,
    SearchBar,
    Chip,
    ListRow,
    ProgressBar,
    EmptyState
  } = NS;
  const M = window.HisabakMock;
  const [q, setQ] = React.useState('');
  const [period, setPeriod] = React.useState('month');
  const catColor = id => (M.CATEGORIES.find(c => c.id === id) || {}).color || 'var(--cat-gray)';
  const catName = id => (M.CATEGORIES.find(c => c.id === id) || {}).name || '';
  const filtered = M.TX.filter(t => t.brand.toLowerCase().includes(q.toLowerCase()) || t.note.toLowerCase().includes(q.toLowerCase()));
  const groups = filtered.reduce((acc, t) => {
    (acc[t.day] = acc[t.day] || []).push(t);
    return acc;
  }, {});
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '8px 16px 24px'
    }
  }, /*#__PURE__*/React.createElement(Card, {
    variant: "hero",
    padding: 18,
    style: {
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 13,
      color: 'var(--text-secondary)'
    }
  }, "Total Balance \xB7 June"), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 4
    }
  }, /*#__PURE__*/React.createElement(Money, {
    value: 12450,
    tone: "neutral",
    size: 34,
    weight: 700
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      margin: '14px 0 6px'
    }
  }, /*#__PURE__*/React.createElement(ProgressBar, {
    value: 60,
    tone: "income",
    height: 8
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      fontFamily: 'var(--font-sans)',
      fontSize: 12,
      color: 'var(--text-tertiary)'
    }
  }, /*#__PURE__*/React.createElement("span", null, "60% income ratio"), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 4
    }
  }, /*#__PURE__*/React.createElement(Money, {
    value: 9420,
    size: 12,
    weight: 600,
    color: "var(--text-tertiary)"
  }), " in \xB7 ", /*#__PURE__*/React.createElement(Money, {
    value: 6180,
    size: 12,
    weight: 600,
    color: "var(--text-tertiary)"
  }), " out")), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    size: "lg",
    leadingIcon: "add",
    onClick: onAdd,
    style: {
      marginTop: 14
    }
  }, "Add Transaction")), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: 12,
      marginTop: 12
    }
  }, /*#__PURE__*/React.createElement(Card, {
    padding: 14,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'grid',
      placeItems: 'center',
      width: 36,
      height: 36,
      borderRadius: 'var(--r-sm)',
      background: 'var(--income-soft)',
      color: 'var(--income)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded"
  }, "south_west")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "Income"), /*#__PURE__*/React.createElement(Money, {
    value: 9420,
    tone: "income",
    size: 16
  }))), /*#__PURE__*/React.createElement(Card, {
    padding: 14,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'grid',
      placeItems: 'center',
      width: 36,
      height: 36,
      borderRadius: 'var(--r-sm)',
      background: 'var(--expense-soft)',
      color: 'var(--expense)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded"
  }, "north_east")), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "Expenses"), /*#__PURE__*/React.createElement(Money, {
    value: 6180,
    tone: "expense",
    size: 16
  })))), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(SearchBar, {
    value: q,
    placeholder: "Search transactions",
    onChange: e => setQ(e.target.value),
    onClear: () => setQ('')
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      marginTop: 12,
      overflowX: 'auto',
      paddingBottom: 2
    }
  }, [['today', 'Today'], ['week', 'This Week'], ['month', 'This Month'], ['all', 'All Time']].map(([v, l]) => /*#__PURE__*/React.createElement(Chip, {
    key: v,
    selected: period === v,
    onClick: () => setPeriod(v)
  }, l))), filtered.length === 0 ? /*#__PURE__*/React.createElement(Card, {
    padding: 0,
    style: {
      marginTop: 16
    }
  }, /*#__PURE__*/React.createElement(EmptyState, {
    icon: "receipt_long",
    title: "No matches",
    description: "No transactions match your search. Try a different term."
  })) : Object.entries(groups).map(([day, items]) => /*#__PURE__*/React.createElement("div", {
    key: day,
    style: {
      marginTop: 18
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 12,
      letterSpacing: '0.04em',
      textTransform: 'uppercase',
      color: 'var(--text-tertiary)',
      marginBottom: 6,
      paddingLeft: 2
    }
  }, day), /*#__PURE__*/React.createElement(Card, {
    padding: 0,
    style: {
      padding: '2px 14px'
    }
  }, items.map((t, i) => /*#__PURE__*/React.createElement(ListRow, {
    key: t.id,
    title: t.brand,
    subtitle: catName(t.cat) + ' · ' + t.note,
    leadingText: t.brand[0],
    color: catColor(t.cat),
    divider: i < items.length - 1,
    trailing: /*#__PURE__*/React.createElement("div", {
      style: {
        textAlign: 'right'
      }
    }, /*#__PURE__*/React.createElement(Money, {
      value: t.amount,
      tone: "auto",
      sign: "auto",
      size: 14,
      weight: 700
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 11,
        color: 'var(--text-tertiary)',
        marginTop: 2
      }
    }, t.date))
  }))))));
}
window.HisabakTransactions = Transactions;
/* Add Transaction — bottom sheet overlay. */
function AddTransactionSheet({
  open,
  onClose
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Button,
    SegmentedControl,
    Chip,
    Input
  } = NS;
  const M = window.HisabakMock;
  const [type, setType] = React.useState('expense');
  const [amount, setAmount] = React.useState('342.75');
  const [brand, setBrand] = React.useState('carrefour');
  const typeColor = {
    expense: 'var(--expense)',
    income: 'var(--income)',
    savings: 'var(--savings)',
    investment: 'var(--investment)'
  }[type];
  return /*#__PURE__*/React.createElement("div", {
    style: {
      position: 'absolute',
      inset: 0,
      zIndex: 30,
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
      padding: '10px 18px calc(18px + var(--navbar-inset))',
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
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginBottom: 18
    }
  }, /*#__PURE__*/React.createElement("h2", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 18,
      color: 'var(--text-primary)'
    }
  }, "Add Transaction"), /*#__PURE__*/React.createElement("button", {
    onClick: onClose,
    "aria-label": "Close",
    style: {
      border: 'none',
      background: 'var(--surface-sunken)',
      width: 30,
      height: 30,
      borderRadius: '50%',
      display: 'grid',
      placeItems: 'center',
      cursor: 'pointer',
      color: 'var(--text-secondary)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18
    }
  }, "close"))), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'baseline',
      justifyContent: 'center',
      gap: 6,
      padding: '12px 0 20px'
    }
  }, /*#__PURE__*/React.createElement(Dirham, {
    size: 30,
    color: typeColor
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontWeight: 700,
      fontSize: 44,
      letterSpacing: '-0.02em',
      color: typeColor,
      fontVariantNumeric: 'tabular-nums'
    }
  }, amount || '0.00')), /*#__PURE__*/React.createElement(SegmentedControl, {
    value: type,
    onChange: setType,
    options: [{
      value: 'expense',
      label: 'Expense',
      tone: 'expense'
    }, {
      value: 'income',
      label: 'Income',
      tone: 'income'
    }, {
      value: 'savings',
      label: 'Savings',
      tone: 'savings'
    }, {
      value: 'investment',
      label: 'Invest',
      tone: 'investment'
    }]
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 14,
      color: 'var(--text-secondary)',
      margin: '18px 0 8px'
    }
  }, "Brand"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      overflowX: 'auto',
      paddingBottom: 4
    }
  }, M.BRANDS.slice(0, 6).map(b => {
    const color = (M.CATEGORIES.find(c => c.id === b.category) || {}).color;
    return /*#__PURE__*/React.createElement(Chip, {
      key: b.id,
      selected: brand === b.id,
      color: color,
      onClick: () => setBrand(b.id)
    }, b.name);
  }), /*#__PURE__*/React.createElement(Chip, {
    leadingIcon: "add"
  }, "New")), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: 12,
      marginTop: 18
    }
  }, /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 14,
      color: 'var(--text-secondary)',
      marginBottom: 8
    }
  }, "Date"), /*#__PURE__*/React.createElement("button", {
    style: {
      width: '100%',
      height: 48,
      display: 'flex',
      alignItems: 'center',
      gap: 8,
      padding: '0 14px',
      border: '1px solid var(--border)',
      borderRadius: 'var(--r-md)',
      background: 'var(--surface)',
      cursor: 'pointer',
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--text-tertiary)'
    }
  }, "calendar_today"), "Today")), /*#__PURE__*/React.createElement(Input, {
    label: "Amount",
    value: amount,
    onChange: e => setAmount(e.target.value),
    leadingIcon: "payments"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 16
    }
  }, /*#__PURE__*/React.createElement(Input, {
    label: "Note",
    multiline: true,
    rows: 2,
    placeholder: "Add a note\u2026",
    value: "Weekly groceries"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 12,
      marginTop: 22
    }
  }, /*#__PURE__*/React.createElement(Button, {
    variant: "secondary",
    fullWidth: true,
    onClick: onClose
  }, "Cancel"), /*#__PURE__*/React.createElement(Button, {
    fullWidth: true,
    onClick: onClose
  }, "Save"))));
}
window.HisabakAddSheet = AddTransactionSheet;
/* SMS Inbox — auto-import status, paste & parse, message list. */
function SmsInbox() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Button,
    SearchBar,
    StatusChip,
    Input,
    Badge
  } = NS;
  const M = window.HisabakMock;
  const [autoOn, setAutoOn] = React.useState(false);
  const [q, setQ] = React.useState('');
  const list = M.SMS.filter(s => s.body.toLowerCase().includes(q.toLowerCase()));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '8px 16px 24px'
    }
  }, autoOn ? /*#__PURE__*/React.createElement(Card, {
    variant: "tinted",
    tint: "var(--income-soft)",
    padding: 14,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12,
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      color: 'var(--income)'
    }
  }, "check_circle"), /*#__PURE__*/React.createElement("div", {
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
  }, "Auto-import active"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "New bank SMS are parsed automatically.")), /*#__PURE__*/React.createElement("button", {
    onClick: () => setAutoOn(false),
    style: {
      border: 'none',
      background: 'transparent',
      color: 'var(--text-secondary)',
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 13,
      cursor: 'pointer'
    }
  }, "Disable")) : /*#__PURE__*/React.createElement(Card, {
    variant: "tinted",
    tint: "var(--warning-soft)",
    padding: 14,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12,
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
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
  }, "Auto-import is disabled"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "Turn it on to log transactions from SMS.")), /*#__PURE__*/React.createElement(Button, {
    size: "sm",
    onClick: () => setAutoOn(true)
  }, "Enable")), /*#__PURE__*/React.createElement(Card, {
    padding: 16,
    style: {
      marginTop: 12
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 15,
      color: 'var(--text-primary)',
      marginBottom: 10
    }
  }, "Paste an SMS"), /*#__PURE__*/React.createElement(Input, {
    multiline: true,
    rows: 2,
    value: "Purchase of AED 89.00 at TALABAT on 31/05.",
    placeholder: "Paste a bank message\u2026"
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginTop: 12
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8
    }
  }, /*#__PURE__*/React.createElement(Badge, {
    tone: "info",
    dot: true
  }, "Talabat"), /*#__PURE__*/React.createElement(Money, {
    value: 89,
    tone: "expense",
    sign: "never",
    size: 15
  })), /*#__PURE__*/React.createElement(Button, {
    size: "sm",
    leadingIcon: "download"
  }, "Parse & Import"))), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(SearchBar, {
    value: q,
    placeholder: "Search messages",
    onChange: e => setQ(e.target.value),
    onClear: () => setQ('')
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14,
      display: 'flex',
      flexDirection: 'column',
      gap: 10
    }
  }, list.map(s => /*#__PURE__*/React.createElement(Card, {
    key: s.id,
    padding: 14
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginBottom: 8
    }
  }, /*#__PURE__*/React.createElement(StatusChip, {
    status: s.status
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12,
      color: 'var(--text-tertiary)'
    }
  }, s.time)), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontSize: 12.5,
      lineHeight: 1.5,
      color: 'var(--text-secondary)',
      display: '-webkit-box',
      WebkitLineClamp: 2,
      WebkitBoxOrient: 'vertical',
      overflow: 'hidden'
    }
  }, s.body), s.status !== 'unparsed' && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginTop: 12,
      paddingTop: 10,
      borderTop: '1px solid var(--divider)'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 14,
      color: 'var(--text-primary)'
    }
  }, s.brand), /*#__PURE__*/React.createElement(Money, {
    value: s.amount,
    tone: "expense",
    sign: "never",
    size: 14
  })), s.status === 'parsed' ? /*#__PURE__*/React.createElement(Button, {
    size: "sm",
    leadingIcon: "download"
  }, "Import") : /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 4,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 13,
      color: 'var(--income)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 16
    }
  }, "check"), "Imported"))))));
}
window.HisabakSms = SmsInbox;
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
    CategoryTile
  } = NS;
  const compact = n => n >= 1e6 ? (n / 1e6).toFixed(2) + 'M' : n >= 1000 ? (n / 1000).toFixed(2) + 'K' : String(n);
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
    }, /*#__PURE__*/React.createElement(Money, {
      value: totals[b.id] || 0,
      tone: "neutral",
      size: 14,
      weight: 600
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
    total: compact(c.total),
    onClick: () => onEditCategory(c),
    onDelete: () => {}
  })), /*#__PURE__*/React.createElement(CategoryTile, {
    addNew: true,
    onClick: onAdd
  })));
}
window.HisabakManage = Manage;
/* Onboarding — mirrors OnboardingScreen.kt / OnboardingPages.kt: a 6-page pager where each page is a
   hero illustration (top) over a left-aligned overline / title / subtitle block, with animated dots
   + a primary CTA ("Next" → "Get started"). Copy is the app's real onboarding strings. */
function Onboarding({
  onFinish
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Button
  } = NS;
  const [page, setPage] = React.useState(0);
  const Preview = ({
    children,
    width = 300,
    style
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      width,
      maxWidth: '100%',
      background: 'var(--surface)',
      border: '1px solid var(--border)',
      borderRadius: 18,
      padding: 18,
      ...style
    }
  }, children);
  const Tile = ({
    icon,
    color
  }) => /*#__PURE__*/React.createElement("span", {
    style: {
      width: 44,
      height: 44,
      flex: 'none',
      borderRadius: 13,
      background: `color-mix(in srgb, ${color} 15%, transparent)`,
      display: 'grid',
      placeItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 22,
      color
    }
  }, icon));
  const Bars = () => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'flex-end',
      gap: 6,
      height: 34,
      marginTop: 12
    }
  }, [14, 20, 13, 26, 18, 30, 34].map((h, i) => /*#__PURE__*/React.createElement("div", {
    key: i,
    style: {
      width: 9,
      height: h,
      borderRadius: 4,
      background: [3, 5, 6].includes(i) ? 'var(--accent)' : 'var(--accent-soft)'
    }
  })));
  const Guarantee = ({
    text
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 10,
      padding: '7px 0'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--accent)'
    }
  }, "check_circle"), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 14,
      color: 'var(--text-primary)'
    }
  }, text));
  const Recap = ({
    icon,
    color,
    title,
    sub
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12
    }
  }, /*#__PURE__*/React.createElement(Tile, {
    icon: icon,
    color: color
  }), /*#__PURE__*/React.createElement("div", null, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 14.5,
      color: 'var(--text-primary)'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, sub)));
  const pages = [{
    overline: 'Welcome to Hisabak',
    title: 'All your money, in one calm place.',
    subtitle: 'Track spending, set budgets, and see where every dirham goes — without the busywork.',
    hero: /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        width: 84,
        height: 84,
        borderRadius: 22,
        background: 'var(--accent)',
        display: 'grid',
        placeItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 40,
        color: '#fff'
      }
    }, "trending_up")), /*#__PURE__*/React.createElement(Preview, {
      style: {
        marginTop: 28,
        textAlign: 'center'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 11,
        letterSpacing: '0.06em',
        color: 'var(--text-secondary)'
      }
    }, "NET WORTH"), /*#__PURE__*/React.createElement("div", {
      style: {
        marginTop: 8,
        display: 'flex',
        justifyContent: 'center'
      }
    }, /*#__PURE__*/React.createElement(Money, {
      value: 842500,
      tone: "neutral",
      size: 32,
      weight: 700
    })), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        justifyContent: 'center'
      }
    }, /*#__PURE__*/React.createElement(Bars, null))))
  }, {
    overline: 'SMS auto-capture',
    title: 'Your bank texts become transactions.',
    subtitle: 'Hisabak reads the alert, pulls out the amount and merchant, and files it — automatically.',
    hero: /*#__PURE__*/React.createElement("div", {
      style: {
        width: 300,
        maxWidth: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 12
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        alignSelf: 'flex-start',
        background: 'var(--surface-sunken)',
        borderRadius: '16px 16px 16px 4px',
        padding: '12px 16px',
        fontFamily: 'var(--font-mono)',
        fontSize: 12.5,
        color: 'var(--text-primary)',
        lineHeight: 1.4
      }
    }, "Purchase of AED 1,250.00 with card 1234 at Lulu, Abu Dhabi."), /*#__PURE__*/React.createElement(Preview, null, /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 12
      }
    }, /*#__PURE__*/React.createElement(Tile, {
      icon: "shopping_cart",
      color: "var(--cat-orange)"
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        flex: 1
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 15,
        color: 'var(--text-primary)'
      }
    }, "Lulu"), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'inline-flex',
        alignItems: 'center',
        gap: 4,
        fontFamily: 'var(--font-sans)',
        fontSize: 12,
        color: 'var(--accent)'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 14
      }
    }, "auto_awesome"), "parsed automatically")), /*#__PURE__*/React.createElement(Money, {
      value: 1250,
      tone: "expense",
      size: 16,
      weight: 700
    }))))
  }, {
    overline: 'Private by design',
    title: 'Your data never leaves your device.',
    subtitle: 'Everything is stored locally — no account, no cloud, no sync. Your finances are yours alone.',
    hero: /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        width: 88,
        height: 88,
        borderRadius: 999,
        background: 'var(--accent-soft)',
        display: 'grid',
        placeItems: 'center'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded is-filled",
      style: {
        fontSize: 44,
        color: 'var(--accent)'
      }
    }, "lock")), /*#__PURE__*/React.createElement(Preview, {
      style: {
        marginTop: 24
      }
    }, /*#__PURE__*/React.createElement(Guarantee, {
      text: "Stored on this device"
    }), /*#__PURE__*/React.createElement(Guarantee, {
      text: "Never synced to a server"
    }), /*#__PURE__*/React.createElement(Guarantee, {
      text: "No account, no tracking"
    })))
  }, {
    overline: 'Budgets & alerts',
    title: 'Know before you overspend.',
    subtitle: 'Set a monthly limit per category. We nudge you at 80%, 90%, and 100% — in-app and on your phone.',
    hero: /*#__PURE__*/React.createElement(Preview, null, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 11,
        letterSpacing: '0.06em',
        color: 'var(--text-secondary)'
      }
    }, "JUNE BUDGET"), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 12,
        margin: '12px 0'
      }
    }, /*#__PURE__*/React.createElement(Tile, {
      icon: "restaurant",
      color: "var(--cat-red)"
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        flex: 1,
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 15,
        color: 'var(--text-primary)'
      }
    }, "Dining"), /*#__PURE__*/React.createElement(Money, {
      value: 510,
      tone: "neutral",
      size: 14,
      weight: 600
    })), /*#__PURE__*/React.createElement("div", {
      style: {
        height: 8,
        borderRadius: 999,
        background: 'var(--surface-sunken)',
        overflow: 'hidden'
      }
    }, /*#__PURE__*/React.createElement("div", {
      style: {
        width: '85%',
        height: '100%',
        background: 'var(--warning)'
      }
    })), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        justifyContent: 'space-between',
        marginTop: 8,
        fontFamily: 'var(--font-sans)',
        fontSize: 12,
        color: 'var(--warning)'
      }
    }, /*#__PURE__*/React.createElement("span", null, "85% of budget"), /*#__PURE__*/React.createElement("span", {
      style: {
        color: 'var(--text-secondary)',
        display: 'inline-flex',
        alignItems: 'center',
        gap: 4
      }
    }, /*#__PURE__*/React.createElement(Money, {
      value: 90,
      size: 12,
      weight: 600,
      color: "var(--text-secondary)"
    }), " left")))
  }, {
    overline: 'Insights',
    title: 'See exactly where it goes.',
    subtitle: 'Net-worth trends, income vs spending, and a clean breakdown by category and brand.',
    hero: /*#__PURE__*/React.createElement(Preview, null, /*#__PURE__*/React.createElement("div", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontSize: 11,
        letterSpacing: '0.06em',
        color: 'var(--text-secondary)'
      }
    }, "NET WORTH \xB7 6 MONTHS"), /*#__PURE__*/React.createElement("div", {
      style: {
        margin: '8px -4px 12px'
      }
    }, /*#__PURE__*/React.createElement(AreaChart, {
      data: [9100, 9600, 9400, 10200, 11100, 12450],
      color: "var(--accent)",
      height: 84
    })), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        alignItems: 'center',
        gap: 16
      }
    }, /*#__PURE__*/React.createElement(DonutChart, {
      segments: [{
        value: 32,
        color: 'var(--cat-gray)'
      }, {
        value: 22,
        color: 'var(--cat-orange)'
      }, {
        value: 12,
        color: 'var(--cat-red)'
      }, {
        value: 8,
        color: 'var(--cat-teal)'
      }],
      size: 72,
      thickness: 12
    }), /*#__PURE__*/React.createElement("div", {
      style: {
        display: 'flex',
        flexDirection: 'column',
        gap: 6
      }
    }, [['Rent', 'var(--cat-gray)'], ['Groceries', 'var(--cat-orange)'], ['Dining', 'var(--cat-red)']].map(([l, c]) => /*#__PURE__*/React.createElement("span", {
      key: l,
      style: {
        display: 'inline-flex',
        alignItems: 'center',
        gap: 7,
        fontFamily: 'var(--font-sans)',
        fontSize: 12.5,
        color: 'var(--text-secondary)'
      }
    }, /*#__PURE__*/React.createElement("span", {
      style: {
        width: 8,
        height: 8,
        borderRadius: 999,
        background: c
      }
    }), l)))))
  }, {
    overline: "You're all set",
    title: 'Ready when you are.',
    subtitle: 'Turn on SMS auto-capture to log transactions the moment they happen — or add them by hand anytime.',
    hero: /*#__PURE__*/React.createElement("div", {
      style: {
        width: 300,
        maxWidth: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 16
      }
    }, /*#__PURE__*/React.createElement(Recap, {
      icon: "bolt",
      color: "var(--accent)",
      title: "Automatic capture",
      sub: "from your bank SMS"
    }), /*#__PURE__*/React.createElement(Recap, {
      icon: "lock",
      color: "var(--savings)",
      title: "Private by design",
      sub: "your data stays on this device"
    }), /*#__PURE__*/React.createElement(Recap, {
      icon: "account_balance_wallet",
      color: "var(--cat-orange)",
      title: "Smart budgets",
      sub: "alerts before you overshoot"
    }), /*#__PURE__*/React.createElement(Recap, {
      icon: "insights",
      color: "var(--investment)",
      title: "Clear insights",
      sub: "trends, categories, brands"
    }))
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
      padding: '0 28px',
      minHeight: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center'
    }
  }, p.hero), /*#__PURE__*/React.createElement("div", {
    style: {
      paddingBottom: 8
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 11.5,
      letterSpacing: '0.06em',
      textTransform: 'uppercase',
      color: 'var(--accent)'
    }
  }, p.overline), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 25,
      color: 'var(--text-primary)',
      marginTop: 12,
      letterSpacing: '-0.02em',
      lineHeight: 1.2
    }
  }, p.title), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-secondary)',
      marginTop: 12,
      lineHeight: 1.5
    }
  }, p.subtitle))), /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '12px 24px 28px',
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
