/* Hisabak mobile UI kit — shared mock data + format helpers (plain global script). */
(function () {
  const CATEGORIES = [
    { id: 'salary',    name: 'Salary',        type: 'income',     color: 'var(--cat-green)',  icon: 'work',            total: 8200 },
    { id: 'groceries', name: 'Groceries',     type: 'expense',    color: 'var(--cat-orange)', icon: 'shopping_cart',   total: 1240 },
    { id: 'dining',    name: 'Dining',        type: 'expense',    color: 'var(--cat-red)',    icon: 'restaurant',      total: 642 },
    { id: 'transport', name: 'Transport',     type: 'expense',    color: 'var(--cat-teal)',   icon: 'directions_car',  total: 388 },
    { id: 'fun',       name: 'Entertainment', type: 'expense',    color: 'var(--cat-purple)', icon: 'movie',           total: 256 },
    { id: 'rent',      name: 'Rent',          type: 'expense',    color: 'var(--cat-gray)',   icon: 'home',            total: 3200 },
    { id: 'savings',   name: 'Savings',       type: 'savings',    color: 'var(--cat-blue)',   icon: 'savings',         total: 2000 },
    { id: 'invest',    name: 'Investment',    type: 'investment', color: 'var(--cat-purple)', icon: 'trending_up',     total: 1500 },
  ];

  const BRANDS = [
    { id: 'acme',     name: 'Acme Corp',  category: 'salary',    initial: 'A' },
    { id: 'carrefour',name: 'Carrefour',  category: 'groceries', initial: 'C' },
    { id: 'starbucks',name: 'Starbucks',  category: 'dining',    initial: 'S' },
    { id: 'talabat',  name: 'Talabat',    category: 'dining',    initial: 'T' },
    { id: 'uber',     name: 'Uber',       category: 'transport', initial: 'U' },
    { id: 'netflix',  name: 'Netflix',    category: 'fun',       initial: 'N' },
    { id: 'stc',      name: 'STC',        category: 'rent',      initial: 'S' },
    { id: 'amazon',   name: 'Amazon',     category: 'groceries', initial: 'A' },
  ];

  const TX = [
    { id: 't1', brand: 'Acme Corp', cat: 'salary',    note: 'Monthly salary',   amount: 8200,    date: '1 Jun',  day: 'Today' },
    { id: 't2', brand: 'Carrefour', cat: 'groceries', note: 'Weekly groceries', amount: -342.75, date: '1 Jun',  day: 'Today' },
    { id: 't3', brand: 'Starbucks', cat: 'dining',    note: 'Flat white',       amount: -28.00,  date: '1 Jun',  day: 'Today' },
    { id: 't4', brand: 'Uber',      cat: 'transport', note: 'Airport ride',     amount: -45.50,  date: '31 May', day: 'Yesterday' },
    { id: 't5', brand: 'Talabat',   cat: 'dining',    note: 'Dinner delivery',  amount: -89.00,  date: '31 May', day: 'Yesterday' },
    { id: 't6', brand: 'Netflix',   cat: 'fun',       note: 'Subscription',     amount: -56.00,  date: '28 May', day: 'This week' },
    { id: 't7', brand: 'Savings',   cat: 'savings',   note: 'Auto transfer',    amount: -2000,   date: '25 May', day: 'This week' },
    { id: 't8', brand: 'STC',       cat: 'rent',      note: 'Internet bill',    amount: -120,    date: '24 May', day: 'This week' },
  ];

  const SMS = [
    { id: 's1', body: 'Purchase of AED 342.75 at CARREFOUR HYPERMARKET on 01/06. Available balance AED 12,107.', time: 'Today · 14:32', status: 'parsed', brand: 'Carrefour', amount: 342.75 },
    { id: 's2', body: 'Salary of AED 8,200.00 has been credited to your account ****4471 from ACME CORP.', time: 'Today · 09:01', status: 'linked', brand: 'Acme Corp', amount: 8200 },
    { id: 's3', body: 'Your verification code is 449201. Do not share this code with anyone.', time: 'Yesterday · 20:14', status: 'unparsed' },
    { id: 's4', body: 'Payment of AED 56.00 to NETFLIX.COM was successful. Card ****4471.', time: 'Yesterday · 03:10', status: 'parsed', brand: 'Netflix', amount: 56.00 },
    { id: 's5', body: 'Withdrawal of AED 500.00 at ATM RIYADH-OLAYA. Balance AED 11,607.', time: '30 May · 18:45', status: 'parsed', brand: 'Cash', amount: 500.00 },
  ];

  function money(n, opts) {
    opts = opts || {};
    const abs = Math.abs(n).toLocaleString('en-US', {
      minimumFractionDigits: opts.decimals === false ? 0 : 2,
      maximumFractionDigits: opts.decimals === false ? 0 : 2,
    });
    return (opts.currency === false ? '' : 'AED ') + abs;
  }

  window.HisabakMock = { CATEGORIES, BRANDS, TX, SMS, money };
})();
