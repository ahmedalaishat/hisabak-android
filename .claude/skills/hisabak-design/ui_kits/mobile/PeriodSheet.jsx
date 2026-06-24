/* Auto-backup period picker — radio bottom sheet (mirrors PeriodSheet in BackupScreen.kt).
   Options: Never, Daily, Weekly, Monthly (default Never in the app; the kit defaults to Weekly). */
function PeriodSheet({ open, selected, onSelect, onClose }) {
  const { Sheet, RadioRow } = window.HisabakExtras;
  const options = ['Never', 'Daily', 'Weekly', 'Monthly'];
  return (
    <Sheet open={open} onClose={onClose}>
      <div style={{ fontFamily: 'var(--font-sans)', fontWeight: 600, fontSize: 18, color: 'var(--text-primary)', padding: '0 16px 8px' }}>Automatic backups</div>
      {options.map(o => (
        <RadioRow key={o} label={o} selected={selected === o} onClick={() => onSelect(o)} />
      ))}
    </Sheet>
  );
}
window.HisabakPeriodSheet = PeriodSheet;
