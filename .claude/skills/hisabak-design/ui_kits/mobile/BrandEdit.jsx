/* Brand edit — name + a category picker (color-dot chips, incl. "None"). Mirrors BrandEditScreen.kt. */
function BrandEdit() {
  const NS = window.HisabakDesignSystem_aa2548;
  const { Input, Button, Chip } = NS;
  const { FormSection } = window.HisabakExtras;
  const M = window.HisabakMock;
  const [name, setName] = React.useState('Carrefour');
  const [cat, setCat] = React.useState('groceries');

  return (
    <div style={{ padding: '20px 16px 28px', display: 'flex', flexDirection: 'column', gap: 20 }}>
      <Input label="Brand name" value={name} onChange={e => setName(e.target.value)} />

      <FormSection label="Category">
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
          <Chip selected={cat === null} onClick={() => setCat(null)}>None</Chip>
          {M.CATEGORIES.map(c => (
            <Chip key={c.id} selected={cat === c.id} color={c.color} onClick={() => setCat(c.id)}>{c.name}</Chip>
          ))}
        </div>
      </FormSection>

      <Button fullWidth>Save</Button>
    </div>
  );
}
window.HisabakBrandEdit = BrandEdit;
