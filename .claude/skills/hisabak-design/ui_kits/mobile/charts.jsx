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
          <Money value={it.value} size={13} weight={600} color="var(--text-secondary)" />
          <span style={{ fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--text-tertiary)', width: 36, textAlign: 'right' }}>{Math.round((it.value / total) * 100)}%</span>
        </div>
      ))}
    </div>
  );
}

/* Official UAE dirham symbol (traced from res/drawable/ic_dirham). The brand shows this glyph for
   money — never the text "AED". viewBox is wider than tall (1000×870). */
function Dirham({ size = 14, color = 'currentColor', style }) {
  return (
    <svg viewBox="0 0 1000 870" height={size} width={size * 1.15} aria-label="dirham"
      style={{ display: 'inline-block', flex: 'none', ...style }}>
      <path fill={color} d="m88.3 1c0.4 0.6 2.6 3.3 4.7 5.9 15.3 18.2 26.8 47.8 33 85.1 4.1 24.5 4.3 32.2 4.3 125.6v87h-41.8c-38.2 0-42.6-0.2-50.1-1.7-11.8-2.5-24-9.2-32.2-17.8-6.5-6.9-6.3-7.3-5.9 13.6 0.5 17.3 0.7 19.2 3.2 28.6 4 14.9 9.5 26 17.8 35.9 11.3 13.6 22.8 21.2 39.2 26.3 3.5 1 10.9 1.4 37.1 1.6l32.7 0.5v43.3 43.4l-46.1-0.3-46.3-0.3-8-3.2c-9.5-3.8-13.8-6.6-23.1-14.9l-6.8-6.1 0.4 19.1c0.5 17.7 0.6 19.7 3.1 28.7 8.7 31.8 29.7 54.5 57.4 61.9 6.9 1.9 9.6 2 38.5 2.4l30.9 0.4v89.6c0 54.1-0.3 94-0.8 100.8-0.5 6.2-2.1 17.8-3.5 25.9-6.5 37.3-18.2 65.4-35 83.6l-3.4 3.7h169.1c101.1 0 176.7-0.4 187.8-0.9 19.5-1 63-5.3 72.8-7.4 3.1-0.6 8.9-1.5 12.7-2.1 8.1-1.2 21.5-4 40.8-8.9 27.2-6.8 52-15.3 76.3-26.1 7.6-3.4 29.4-14.5 35.2-18 3.1-1.8 6.8-4 8.2-4.7 3.9-2.1 10.4-6.3 19.9-13.1 4.7-3.4 9.4-6.7 10.4-7.4 4.2-2.8 18.7-14.9 25.3-21 25.1-23.1 46.1-48.8 62.4-76.3 2.3-4 5.3-9 6.6-11.1 3.3-5.6 16.9-33.6 18.2-37.8 0.6-1.9 1.4-3.9 1.8-4.3 2.6-3.4 17.6-50.6 19.4-60.9 0.6-3.3 0.9-3.8 3.4-4.3 1.6-0.3 24.9-0.3 51.8-0.1 53.8 0.4 53.8 0.4 65.7 5.9 6.7 3.1 8.7 4.5 16.1 11.2 9.7 8.7 8.8 10.1 8.2-11.7-0.4-12.8-0.9-20.7-1.8-23.9-3.4-12.3-4.2-14.9-7.2-21.1-9.8-21.4-26.2-36.7-47.2-44l-8.2-3-33.4-0.4-33.3-0.5 0.4-11.7c0.4-15.4 0.4-45.9-0.1-61.6l-0.4-12.6 44.6-0.2c38.2-0.2 45.3 0 49.5 1.1 12.6 3.5 21.1 8.3 31.5 17.8l5.8 5.4v-14.8c0-17.6-0.9-25.4-4.5-37-7.1-23.5-21.1-41-41.1-51.8-13-7-13.8-7.2-58.5-7.5-26.2-0.2-39.9-0.6-40.6-1.2-0.6-0.6-1.1-1.6-1.1-2.4 0-0.8-1.5-7.1-3.5-13.9-23.4-82.7-67.1-148.4-131-197.1-8.7-6.7-30-20.8-38.6-25.6-3.3-1.9-6.9-3.9-7.8-4.5-4.2-2.3-28.3-14.1-34.3-16.6-3.6-1.6-8.3-3.6-10.4-4.4-35.3-15.3-94.5-29.8-139.7-34.3-7.4-0.7-17.2-1.8-21.7-2.2-20.4-2.3-48.7-2.6-209.4-2.6-135.8 0-169.9 0.3-169.4 1zm330.7 43.3c33.8 2 54.6 4.6 78.9 10.5 74.2 17.6 126.4 54.8 164.3 117 3.5 5.8 18.3 36 20.5 42.1 10.5 28.3 15.6 45.1 20.1 67.3 1.1 5.4 2.6 12.6 3.3 16 0.7 3.3 1 6.4 0.7 6.7-0.5 0.4-100.9 0.6-223.3 0.5l-222.5-0.2-0.3-128.5c-0.1-70.6 0-129.3 0.3-130.4l0.4-1.9h71.1c39 0 78 0.4 86.5 0.9zm297.5 350.3c0.7 4.3 0.7 77.3 0 80.9l-0.6 2.7-227.5-0.2-227.4-0.3-0.2-42.4c-0.2-23.3 0-42.7 0.2-43.1 0.3-0.5 97.2-0.8 227.7-0.8h227.2zm-10.2 171.7c0.5 1.5-1.9 13.8-6.8 33.8-5.6 22.5-13.2 45.2-20.9 62-3.8 8.6-13.3 27.2-15.6 30.7-1.1 1.6-4.3 6.7-7.1 11.2-18 28.2-43.7 53.9-73 72.9-10.7 6.8-32.7 18.4-38.6 20.2-1.2 0.3-2.5 0.9-3 1.3-0.7 0.6-9.8 4-20.4 7.8-19.5 6.9-56.6 14.4-86.4 17.5-19.3 1.9-22.4 2-96.7 2h-76.9v-129.7-129.8l220.9-0.4c121.5-0.2 221.6-0.5 222.4-0.7 0.9-0.1 1.8 0.5 2.1 1.2z" />
    </svg>
  );
}

/* Money — dirham glyph + compact tabular figure (e.g. 6,450 → 6.45K). Reads LTR. */
function Money({ value = 0, tone = 'neutral', sign = 'never', size = 15, weight = 700, color }) {
  const toneKey = tone === 'auto' ? (value < 0 ? 'expense' : 'income') : tone;
  const c = color || ({ income: 'var(--income)', expense: 'var(--expense)', savings: 'var(--savings)', investment: 'var(--investment)', neutral: 'var(--text-primary)' }[toneKey] || 'var(--text-primary)');
  const a = Math.abs(value);
  const num = a >= 1e6 ? (a / 1e6).toFixed(2) + 'M' : a >= 1000 ? (a / 1000).toFixed(2) + 'K' : a.toFixed(2);
  const prefix = sign === 'always' || (sign === 'auto' && value !== 0) ? (value < 0 ? '−' : '+') : '';
  return (
    <span style={{ display: 'inline-flex', alignItems: 'center', gap: 3, color: c, direction: 'ltr' }}>
      {prefix && <span style={{ fontFamily: 'var(--font-sans)', fontWeight: weight, fontSize: size }}>{prefix}</span>}
      <Dirham size={size * 0.82} color={c} />
      <span style={{ fontFamily: 'var(--font-mono)', fontWeight: weight, fontSize: size, fontVariantNumeric: 'tabular-nums' }}>{num}</span>
    </span>
  );
}

Object.assign(window, { AreaChart, Sparkline, GroupedBars, DonutChart, LegendList, Dirham, Money });
