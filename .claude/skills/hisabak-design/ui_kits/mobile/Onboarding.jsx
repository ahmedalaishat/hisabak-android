/* Onboarding — mirrors OnboardingScreen.kt / OnboardingPages.kt: a 6-page pager where each page is a
   hero illustration (top) over a left-aligned overline / title / subtitle block, with animated dots
   + a primary CTA ("Next" → "Get started"). Copy is the app's real onboarding strings. */
function Onboarding({ onFinish }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Button } = NS;
  const [page, setPage] = React.useState(0);

  const Preview = ({ children, width = 300, style }) => (
    <div style={{ width, maxWidth: '100%', background: 'var(--surface)', border: '1px solid var(--border)', borderRadius: 18, padding: 18, ...style }}>{children}</div>
  );
  const Tile = ({ icon, color }) => (
    <span style={{ width: 44, height: 44, flex: 'none', borderRadius: 13, background: `color-mix(in srgb, ${color} 15%, transparent)`, display: 'grid', placeItems: 'center' }}>
      <span className="material-symbols-rounded" style={{ fontSize: 22, color }}>{icon}</span>
    </span>
  );
  const Bars = () => (
    <div style={{ display: 'flex', alignItems: 'flex-end', gap: 6, height: 34, marginTop: 12 }}>
      {[14, 20, 13, 26, 18, 30, 34].map((h, i) => (
        <div key={i} style={{ width: 9, height: h, borderRadius: 4, background: [3, 5, 6].includes(i) ? 'var(--accent)' : 'var(--accent-soft)' }} />
      ))}
    </div>
  );
  const Guarantee = ({ text }) => (
    <div style={{ display: 'flex', alignItems: 'center', gap: 10, padding: '7px 0' }}>
      <span className="material-symbols-rounded" style={{ fontSize: 20, color: 'var(--accent)' }}>check_circle</span>
      <span style={{ fontFamily: 'var(--font-sans)', fontSize: 14, color: 'var(--text-primary)' }}>{text}</span>
    </div>
  );
  const Recap = ({ icon, color, title, sub }) => (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
      <Tile icon={icon} color={color} />
      <div>
        <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14.5, color: 'var(--text-primary)' }}>{title}</div>
        <div style={{ fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}>{sub}</div>
      </div>
    </div>
  );

  const pages = [
    {
      overline: 'Welcome to Hisabak', title: 'All your money, in one calm place.',
      subtitle: 'Track spending, set budgets, and see where every dirham goes — without the busywork.',
      hero: (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ width: 84, height: 84, borderRadius: 22, background: 'var(--accent)', display: 'grid', placeItems: 'center' }}>
            <span className="material-symbols-rounded" style={{ fontSize: 40, color: '#fff' }}>trending_up</span>
          </div>
          <Preview style={{ marginTop: 28, textAlign: 'center' }}>
            <div style={{ fontFamily: 'var(--font-sans)', fontSize: 11, letterSpacing: '0.06em', color: 'var(--text-secondary)' }}>NET WORTH</div>
            <div style={{ marginTop: 8, display: 'flex', justifyContent: 'center' }}><Money value={842500} tone="neutral" size={32} weight={700} /></div>
            <div style={{ display: 'flex', justifyContent: 'center' }}><Bars /></div>
          </Preview>
        </div>
      ),
    },
    {
      overline: 'SMS auto-capture', title: 'Your bank texts become transactions.',
      subtitle: 'Hisabak reads the alert, pulls out the amount and merchant, and files it — automatically.',
      hero: (
        <div style={{ width: 300, maxWidth: '100%', display: 'flex', flexDirection: 'column', gap: 12 }}>
          <div style={{ alignSelf: 'flex-start', background: 'var(--surface-sunken)', borderRadius: '16px 16px 16px 4px', padding: '12px 16px', fontFamily: 'var(--font-mono)', fontSize: 12.5, color: 'var(--text-primary)', lineHeight: 1.4 }}>
            Purchase of AED 1,250.00 with card 1234 at Lulu, Abu Dhabi.
          </div>
          <Preview>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
              <Tile icon="shopping_cart" color="var(--cat-orange)" />
              <div style={{ flex: 1 }}>
                <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)' }}>Lulu</div>
                <div style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--accent)' }}>
                  <span className="material-symbols-rounded" style={{ fontSize: 14 }}>auto_awesome</span>parsed automatically
                </div>
              </div>
              <Money value={1250} tone="expense" size={16} weight={700} />
            </div>
          </Preview>
        </div>
      ),
    },
    {
      overline: 'Private by design', title: 'Your data never leaves your device.',
      subtitle: 'Everything is stored locally — no account, no cloud, no sync. Your finances are yours alone.',
      hero: (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ width: 88, height: 88, borderRadius: 999, background: 'var(--accent-soft)', display: 'grid', placeItems: 'center' }}>
            <span className="material-symbols-rounded is-filled" style={{ fontSize: 44, color: 'var(--accent)' }}>lock</span>
          </div>
          <Preview style={{ marginTop: 24 }}>
            <Guarantee text="Stored on this device" />
            <Guarantee text="Never synced to a server" />
            <Guarantee text="No account, no tracking" />
          </Preview>
        </div>
      ),
    },
    {
      overline: 'Budgets & alerts', title: 'Know before you overspend.',
      subtitle: 'Set a monthly limit per category. We nudge you at 80%, 90%, and 100% — in-app and on your phone.',
      hero: (
        <Preview>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 11, letterSpacing: '0.06em', color: 'var(--text-secondary)' }}>JUNE BUDGET</div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 12, margin: '12px 0' }}>
            <Tile icon="restaurant" color="var(--cat-red)" />
            <div style={{ flex: 1, fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 15, color: 'var(--text-primary)' }}>Dining</div>
            <Money value={510} tone="neutral" size={14} weight={600} />
          </div>
          <div style={{ height: 8, borderRadius: 999, background: 'var(--surface-sunken)', overflow: 'hidden' }}>
            <div style={{ width: '85%', height: '100%', background: 'var(--warning)' }} />
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 8, fontFamily: 'var(--font-sans)', fontSize: 12, color: 'var(--warning)' }}>
            <span>85% of budget</span><span style={{ color: 'var(--text-secondary)', display: 'inline-flex', alignItems: 'center', gap: 4 }}><Money value={90} size={12} weight={600} color="var(--text-secondary)" /> left</span>
          </div>
        </Preview>
      ),
    },
    {
      overline: 'Insights', title: 'See exactly where it goes.',
      subtitle: 'Net-worth trends, income vs spending, and a clean breakdown by category and brand.',
      hero: (
        <Preview>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 11, letterSpacing: '0.06em', color: 'var(--text-secondary)' }}>NET WORTH · 6 MONTHS</div>
          <div style={{ margin: '8px -4px 12px' }}><AreaChart data={[9100, 9600, 9400, 10200, 11100, 12450]} color="var(--accent)" height={84} /></div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
            <DonutChart segments={[{ value: 32, color: 'var(--cat-gray)' }, { value: 22, color: 'var(--cat-orange)' }, { value: 12, color: 'var(--cat-red)' }, { value: 8, color: 'var(--cat-teal)' }]} size={72} thickness={12} />
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              {[['Rent', 'var(--cat-gray)'], ['Groceries', 'var(--cat-orange)'], ['Dining', 'var(--cat-red)']].map(([l, c]) => (
                <span key={l} style={{ display: 'inline-flex', alignItems: 'center', gap: 7, fontFamily: 'var(--font-sans)', fontSize: 12.5, color: 'var(--text-secondary)' }}><span style={{ width: 8, height: 8, borderRadius: 999, background: c }} />{l}</span>
              ))}
            </div>
          </div>
        </Preview>
      ),
    },
    {
      overline: "You're all set", title: 'Ready when you are.',
      subtitle: 'Turn on SMS auto-capture to log transactions the moment they happen — or add them by hand anytime.',
      hero: (
        <div style={{ width: 300, maxWidth: '100%', display: 'flex', flexDirection: 'column', gap: 16 }}>
          <Recap icon="bolt" color="var(--accent)" title="Automatic capture" sub="from your bank SMS" />
          <Recap icon="lock" color="var(--savings)" title="Private by design" sub="your data stays on this device" />
          <Recap icon="account_balance_wallet" color="var(--cat-orange)" title="Smart budgets" sub="alerts before you overshoot" />
          <Recap icon="insights" color="var(--investment)" title="Clear insights" sub="trends, categories, brands" />
        </div>
      ),
    },
  ];

  const isLast = page === pages.length - 1;
  const p = pages[page];

  return (
    <div style={{ position: 'absolute', inset: 0, background: 'var(--bg)', display: 'flex', flexDirection: 'column', zIndex: 30 }}>
      <div style={{ height: 48, display: 'flex', alignItems: 'center', justifyContent: 'flex-end', padding: '0 16px' }}>
        {!isLast && <span onClick={() => setPage(pages.length - 1)} style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14, color: 'var(--text-secondary)', cursor: 'pointer', padding: 8 }}>Skip</span>}
      </div>

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', padding: '0 28px', minHeight: 0 }}>
        <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>{p.hero}</div>
        <div style={{ paddingBottom: 8 }}>
          <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 11.5, letterSpacing: '0.06em', textTransform: 'uppercase', color: 'var(--accent)' }}>{p.overline}</div>
          <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 25, color: 'var(--text-primary)', marginTop: 12, letterSpacing: '-0.02em', lineHeight: 1.2 }}>{p.title}</div>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-secondary)', marginTop: 12, lineHeight: 1.5 }}>{p.subtitle}</div>
        </div>
      </div>

      <div style={{ padding: '12px 24px 28px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 20 }}>
        <div style={{ display: 'flex', gap: 7 }}>
          {pages.map((_, i) => (
            <span key={i} style={{ height: 7, width: i === page ? 22 : 7, borderRadius: 999, background: i === page ? 'var(--accent)' : 'var(--border-strong)', transition: 'width var(--dur-base), background var(--dur-base)' }} />
          ))}
        </div>
        <Button fullWidth onClick={() => (isLast ? onFinish() : setPage(page + 1))}>{isLast ? 'Get started' : 'Next'}</Button>
      </div>
    </div>
  );
}
window.HisabakOnboarding = Onboarding;
