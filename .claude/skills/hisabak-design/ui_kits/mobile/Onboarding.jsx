/* Onboarding — 6-page intro pager with skip, animated dots, and a primary CTA (mirrors
   OnboardingScreen.kt). The last page's CTA finishes (and, in the SMS build, primes capture). */
function Onboarding({ onFinish }) {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Button } = NS;
  const { HeroDisc } = window.HisabakExtras;
  const [page, setPage] = React.useState(0);

  const pages = [
    { icon: 'savings', tint: 'var(--accent-soft)', fg: 'var(--accent)', title: 'Welcome to Hisabak', body: 'Your calm companion for understanding where your money goes.' },
    { icon: 'sms', tint: 'var(--savings-soft)', fg: 'var(--savings)', title: 'Capture from SMS', body: 'Hisabak reads your bank alerts and logs transactions automatically — no typing.' },
    { icon: 'lock', tint: 'var(--accent-soft)', fg: 'var(--accent)', title: 'Private by design', body: 'Your data is encrypted on your device and never leaves without your say.' },
    { icon: 'donut_small', tint: 'var(--investment-soft)', fg: 'var(--investment)', title: 'Set budgets', body: 'Give each category a limit and get a nudge before you overspend.' },
    { icon: 'insights', tint: 'var(--expense-soft)', fg: 'var(--expense)', title: 'See the trends', body: 'Clean charts show income, spending, and net worth over time.' },
    { icon: 'check_circle', tint: 'var(--accent-soft)', fg: 'var(--accent)', title: "You're all set", body: 'Add your first transaction or let an SMS do it for you.' },
  ];
  const isLast = page === pages.length - 1;
  const p = pages[page];

  return (
    <div style={{ position: 'absolute', inset: 0, background: 'var(--bg)', display: 'flex', flexDirection: 'column', zIndex: 30 }}>
      <div style={{ height: 48, display: 'flex', alignItems: 'center', justifyContent: 'flex-end', padding: '0 16px' }}>
        {!isLast && <span onClick={() => setPage(pages.length - 1)} style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 14, color: 'var(--text-secondary)', cursor: 'pointer', padding: 8 }}>Skip</span>}
      </div>

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', textAlign: 'center', padding: '0 36px', gap: 28 }}>
        <HeroDisc icon={p.icon} size={132} iconSize={60} tint={p.tint} fg={p.fg} />
        <div>
          <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 700, fontSize: 26, color: 'var(--text-primary)', letterSpacing: '-0.02em' }}>{p.title}</div>
          <div style={{ fontFamily: 'var(--font-sans)', fontSize: 15.5, color: 'var(--text-secondary)', marginTop: 12, lineHeight: 1.5 }}>{p.body}</div>
        </div>
      </div>

      <div style={{ padding: '0 24px 28px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 20 }}>
        <div style={{ display: 'flex', gap: 7 }}>
          {pages.map((_, i) => (
            <span key={i} style={{
              height: 7, width: i === page ? 22 : 7, borderRadius: 999,
              background: i === page ? 'var(--accent)' : 'var(--border-strong)', transition: 'width var(--dur-base), background var(--dur-base)',
            }} />
          ))}
        </div>
        <Button fullWidth onClick={() => (isLast ? onFinish() : setPage(page + 1))}>{isLast ? 'Get started' : 'Next'}</Button>
      </div>
    </div>
  );
}
window.HisabakOnboarding = Onboarding;
