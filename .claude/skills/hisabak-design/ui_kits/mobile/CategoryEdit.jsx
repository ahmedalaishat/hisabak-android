/* Category edit — name, type (segmented, semantic tones), monthly limit (expense only), color
   swatches, icon chips, live preview, save. Mirrors CategoryEditScreen.kt. */
function CategoryEdit() {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Input, SegmentedControl, Button, CategoryIcon } = NS;
  const { FormSection } = window.HisabakExtras;
  const [name, setName] = React.useState('Groceries');
  const [type, setType] = React.useState('expense');
  const [color, setColor] = React.useState('var(--cat-orange)');
  const [icon, setIcon] = React.useState('shopping_cart');

  const palette = ['var(--cat-green)', 'var(--cat-blue)', 'var(--cat-orange)', 'var(--cat-red)', 'var(--cat-teal)', 'var(--cat-purple)', 'var(--cat-pink)', 'var(--cat-gray)'];
  const icons = ['account_balance_wallet', 'shopping_cart', 'work', 'directions_car', 'restaurant', 'savings', 'home', 'movie', 'menu_book', 'favorite', 'card_giftcard', 'flight'];

  return (
    <div style={{ padding: '20px 16px 28px', display: 'flex', flexDirection: 'column', gap: 20 }}>
      <Input label="Name" value={name} onChange={e => setName(e.target.value)} />

      <FormSection label="Type">
        <SegmentedControl
          options={[
            { value: 'income', label: 'Income', tone: 'income' },
            { value: 'expense', label: 'Expenses', tone: 'expense' },
            { value: 'savings', label: 'Savings', tone: 'savings' },
            { value: 'investment', label: 'Invest', tone: 'investment' },
          ]}
          value={type}
          onChange={setType}
        />
      </FormSection>

      {type === 'expense' && (
        <Input label="Monthly limit" value="2,000" leadingIcon="payments" helper="Get an alert as you approach it" onChange={() => {}} />
      )}

      <FormSection label="Color">
        <div style={{ display: 'flex', gap: 10, overflowX: 'auto', paddingBottom: 2 }}>
          {palette.map(c => (
            <button key={c} onClick={() => setColor(c)} style={{
              width: 36, height: 36, flex: 'none', borderRadius: 10, background: c, cursor: 'pointer',
              border: c === color ? '2px solid var(--text-primary)' : '2px solid transparent', display: 'grid', placeItems: 'center',
            }}>
              {c === color && <span className="material-symbols-rounded" style={{ fontSize: 18, color: '#fff' }}>check</span>}
            </button>
          ))}
        </div>
      </FormSection>

      <FormSection label="Icon">
        <div style={{ display: 'flex', gap: 10, overflowX: 'auto', paddingBottom: 2 }}>
          {icons.map(ic => (
            <button key={ic} onClick={() => setIcon(ic)} style={{
              width: 44, height: 44, flex: 'none', borderRadius: 12, cursor: 'pointer',
              background: `color-mix(in srgb, ${color} 16%, transparent)`,
              border: ic === icon ? '1px solid var(--accent)' : '1px solid var(--border)', display: 'grid', placeItems: 'center',
            }}>
              <span className="material-symbols-rounded" style={{ fontSize: 20, color }}>{ic}</span>
            </button>
          ))}
        </div>
      </FormSection>

      <FormSection label="Preview">
        <div style={{ display: 'flex', alignItems: 'center', gap: 14, background: 'var(--surface)', border: '1px solid var(--border)', borderRadius: 12, padding: 16 }}>
          <CategoryIcon icon={icon} color={color} size={44} />
          <span style={{ fontFamily: 'var(--font-sans)', fontSize: 15, color: 'var(--text-primary)' }}>{name || 'Category name'}</span>
        </div>
      </FormSection>

      <Button fullWidth>Save</Button>
    </div>
  );
}
window.HisabakCategoryEdit = CategoryEdit;
