/* Hisabak charts — lightweight inline SVG. Colors come in as CSS-var strings. */

function AreaChart({ data = [], color = 'var(--accent)', height = 120, fill = true }) {
  const w = 320, h = height, pad = 6;
  const max = Math.max(...data), min = Math.min(...data);
  const span = max - min || 1;
  const pts = data.map((v, i) => {
    const x = pad + (i / (data.length - 1)) * (w - pad * 2);
    const y = pad + (1 - (v - min) / span) * (h - pad * 2);
    return [x, y];
  });
  const line = pts.map((p, i) => (i === 0 ? 'M' : 'L') + p[0].toFixed(1) + ' ' + p[1].toFixed(1)).join(' ');
  const area = line + ` L${(w - pad).toFixed(1)} ${h - pad} L${pad} ${h - pad} Z`;
  const gid = 'ag' + Math.random().toString(36).slice(2, 7);
  return (
    <svg viewBox={`0 0 ${w} ${h}`} width="100%" height={h} preserveAspectRatio="none" style={{ display: 'block', color }}>
      <defs>
        <linearGradient id={gid} x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor={color} stopOpacity="0.22" />
          <stop offset="100%" stopColor={color} stopOpacity="0" />
        </linearGradient>
      </defs>
      {fill && <path d={area} fill={`url(#${gid})`} />}
      <path d={line} fill="none" stroke={color} strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" />
      <circle cx={pts[pts.length - 1][0]} cy={pts[pts.length - 1][1]} r="3.5" fill={color} />
    </svg>
  );
}

function Sparkline({ data = [], color = 'var(--accent)', height = 36, width = 90 }) {
  const max = Math.max(...data) || 1;
  const bw = width / (data.length * 1.6);
  const gap = bw * 0.6;
  return (
    <svg viewBox={`0 0 ${width} ${height}`} width={width} height={height} style={{ display: 'block' }}>
      {data.map((v, i) => {
        const bh = Math.max(2, (v / max) * (height - 2));
        return <rect key={i} x={i * (bw + gap)} y={height - bh} width={bw} height={bh} rx={bw / 2.5} fill={color} opacity={i === data.length - 1 ? 1 : 0.35} />;
      })}
    </svg>
  );
}

function GroupedBars({ data = [], height = 150 }) {
  // data: [{ label, income, expense }]
  const w = 320, h = height, pad = 18, axis = 16;
  const max = Math.max(...data.flatMap(d => [d.income, d.expense])) || 1;
  const groupW = (w - pad * 2) / data.length;
  const bw = Math.min(13, groupW / 3.4);
  return (
    <svg viewBox={`0 0 ${w} ${h}`} width="100%" height={h} style={{ display: 'block' }}>
      {[0.25, 0.5, 0.75].map((g, i) => (
        <line key={i} x1={pad} x2={w - pad} y1={pad + g * (h - pad - axis)} y2={pad + g * (h - pad - axis)} stroke="var(--divider)" strokeWidth="1" />
      ))}
      {data.map((d, i) => {
        const cx = pad + groupW * i + groupW / 2;
        const ih = (d.income / max) * (h - pad - axis);
        const eh = (d.expense / max) * (h - pad - axis);
        const base = h - axis;
        return (
          <g key={i}>
            <rect x={cx - bw - 2} y={base - ih} width={bw} height={ih} rx="3" fill="var(--income)" />
            <rect x={cx + 2} y={base - eh} width={bw} height={eh} rx="3" fill="var(--expense)" />
            <text x={cx} y={h - 3} textAnchor="middle" fontSize="10" fontFamily="var(--font-sans)" fill="var(--text-tertiary)">{d.label}</text>
          </g>
        );
      })}
    </svg>
  );
}

function DonutChart({ segments = [], size = 132, thickness = 18, centerLabel, centerSub }) {
  const r = (size - thickness) / 2;
  const c = 2 * Math.PI * r;
  const total = segments.reduce((s, x) => s + x.value, 0) || 1;
  let offset = 0;
  return (
    <div style={{ position: 'relative', width: size, height: size }}>
      <svg viewBox={`0 0 ${size} ${size}`} width={size} height={size} style={{ transform: 'rotate(-90deg)' }}>
        <circle cx={size / 2} cy={size / 2} r={r} fill="none" stroke="var(--surface-sunken)" strokeWidth={thickness} />
        {segments.map((s, i) => {
          const len = (s.value / total) * c;
          const el = (
            <circle key={i} cx={size / 2} cy={size / 2} r={r} fill="none"
              stroke={s.color} strokeWidth={thickness} strokeLinecap="round"
              strokeDasharray={`${Math.max(0, len - 3)} ${c - Math.max(0, len - 3)}`}
              strokeDashoffset={-offset} />
          );
          offset += len;
          return el;
        })}
      </svg>
      {(centerLabel || centerSub) && (
        <div style={{ position: 'absolute', inset: 0, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
          {centerLabel && <div style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, fontSize: 18, color: 'var(--text-primary)', fontVariantNumeric: 'tabular-nums' }}>{centerLabel}</div>}
          {centerSub && <div style={{ fontFamily: 'var(--font-sans)', fontSize: 11, color: 'var(--text-tertiary)' }}>{centerSub}</div>}
        </div>
      )}
    </div>
  );
}

function LegendList({ items = [] }) {
  const total = items.reduce((s, x) => s + x.value, 0) || 1;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12, flex: 1, minWidth: 0 }}>
      {items.map((it, i) => (
        <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <span style={{ width: 9, height: 9, borderRadius: '50%', background: it.color, flex: 'none' }} />
          <span style={{ fontFamily: 'var(--font-sans)', fontSize: 13.5, color: 'var(--text-primary)', flex: 1, minWidth: 0, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{it.label}</span>
          <span style={{ fontFamily: 'var(--font-mono)', fontSize: 13, fontWeight: 600, color: 'var(--text-secondary)', fontVariantNumeric: 'tabular-nums' }}>{window.HisabakMock.money(it.value, { decimals: false })}</span>
          <span style={{ fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-tertiary)', width: 36, textAlign: 'right' }}>{Math.round((it.value / total) * 100)}%</span>
        </div>
      ))}
    </div>
  );
}

Object.assign(window, { AreaChart, Sparkline, GroupedBars, DonutChart, LegendList });
