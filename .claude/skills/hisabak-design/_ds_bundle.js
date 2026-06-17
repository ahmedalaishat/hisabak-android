/* @ds-bundle: {"format":3,"namespace":"HisabakDesignSystem_aa2548","components":[{"name":"AmountText","sourcePath":"components/core/AmountText.jsx"},{"name":"Avatar","sourcePath":"components/core/Avatar.jsx"},{"name":"Badge","sourcePath":"components/core/Badge.jsx"},{"name":"Button","sourcePath":"components/core/Button.jsx"},{"name":"Chip","sourcePath":"components/core/Chip.jsx"},{"name":"IconButton","sourcePath":"components/core/IconButton.jsx"},{"name":"ProgressBar","sourcePath":"components/core/ProgressBar.jsx"},{"name":"StatusChip","sourcePath":"components/core/StatusChip.jsx"},{"name":"Card","sourcePath":"components/data/Card.jsx"},{"name":"CategoryIcon","sourcePath":"components/data/CategoryIcon.jsx"},{"name":"CategoryTile","sourcePath":"components/data/CategoryTile.jsx"},{"name":"EmptyState","sourcePath":"components/data/EmptyState.jsx"},{"name":"ListRow","sourcePath":"components/data/ListRow.jsx"},{"name":"StatCard","sourcePath":"components/data/StatCard.jsx"},{"name":"Input","sourcePath":"components/forms/Input.jsx"},{"name":"SearchBar","sourcePath":"components/forms/SearchBar.jsx"},{"name":"SegmentedControl","sourcePath":"components/forms/SegmentedControl.jsx"},{"name":"BottomNav","sourcePath":"components/navigation/BottomNav.jsx"},{"name":"TopAppBar","sourcePath":"components/navigation/TopAppBar.jsx"}],"sourceHashes":{"components/core/AmountText.jsx":"35552a55b866","components/core/Avatar.jsx":"cd9bbb07e58b","components/core/Badge.jsx":"320830de5d12","components/core/Button.jsx":"08e0f17beb84","components/core/Chip.jsx":"e5c45686af2a","components/core/IconButton.jsx":"d2e77e63b860","components/core/ProgressBar.jsx":"ebefbf5df022","components/core/StatusChip.jsx":"31c0011ad20c","components/data/Card.jsx":"60c9fb89543d","components/data/CategoryIcon.jsx":"581ee8192b4b","components/data/CategoryTile.jsx":"82a43c534ab4","components/data/EmptyState.jsx":"be990eb340d4","components/data/ListRow.jsx":"6bfbd4a20658","components/data/StatCard.jsx":"36d03c035123","components/forms/Input.jsx":"54e63eeb7dea","components/forms/SearchBar.jsx":"a2436cbfa408","components/forms/SegmentedControl.jsx":"441449fed4fb","components/navigation/BottomNav.jsx":"5fd22212b8b2","components/navigation/TopAppBar.jsx":"f61fa9a39b65","ui_kits/mobile/AddTransactionSheet.jsx":"37422c0c4df0","ui_kits/mobile/Categories.jsx":"6d090ae2a4a9","ui_kits/mobile/Dashboard.jsx":"989fd310d9b6","ui_kits/mobile/SmsInbox.jsx":"8e5123a139b6","ui_kits/mobile/Transactions.jsx":"06798c09b442","ui_kits/mobile/charts.jsx":"e3e12e8b77a4","ui_kits/mobile/mock.js":"5da649fc1337"},"inlinedExternals":[],"unexposedExports":[]} */

(() => {

const __ds_ns = (window.HisabakDesignSystem_aa2548 = window.HisabakDesignSystem_aa2548 || {});

const __ds_scope = {};

(__ds_ns.__errors = __ds_ns.__errors || []);

// components/core/AmountText.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Money display with tabular figures and signed coloring.
 * Renders "+AED 8,200.00" green for income, "−AED 342.75" coral for expense.
 * Pass `value` as a number (major units) or a preformatted string via `text`.
 */
function AmountText({
  value,
  text,
  currency = 'AED',
  sign = 'auto',
  // 'auto' | 'always' | 'never'
  tone = 'auto',
  // 'auto' | 'income' | 'expense' | 'savings' | 'investment' | 'neutral'
  size = 16,
  weight = 600,
  style,
  ...rest
}) {
  const n = typeof value === 'number' ? value : 0;
  const resolvedTone = tone === 'auto' ? n < 0 ? 'expense' : 'income' : tone;
  const colors = {
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)',
    neutral: 'var(--text-primary)'
  };
  const abs = Math.abs(n).toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
  let prefix = '';
  if (sign === 'always' || sign === 'auto' && tone !== 'neutral') prefix = n < 0 ? '−' : '+';
  const body = text != null ? text : `${prefix}${currency} ${abs}`;
  return /*#__PURE__*/React.createElement("span", _extends({
    style: {
      fontFamily: 'var(--font-mono)',
      fontVariantNumeric: 'tabular-nums',
      fontWeight: weight,
      fontSize: size,
      letterSpacing: '-0.02em',
      lineHeight: 1.1,
      color: colors[resolvedTone] || 'var(--text-primary)',
      whiteSpace: 'nowrap',
      ...style
    }
  }, rest), body);
}
Object.assign(__ds_scope, { AmountText });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/AmountText.jsx", error: String((e && e.message) || e) }); }

// components/core/Avatar.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * User avatar — initials on a tinted circle, or an image via `src`.
 */
function Avatar({
  name = '',
  src,
  size = 36,
  style,
  ...rest
}) {
  const initials = name.split(' ').filter(Boolean).slice(0, 2).map(w => w[0]).join('').toUpperCase();
  return /*#__PURE__*/React.createElement("div", _extends({
    style: {
      width: size,
      height: size,
      flex: 'none',
      borderRadius: '50%',
      overflow: 'hidden',
      display: 'grid',
      placeItems: 'center',
      background: 'var(--accent-soft)',
      color: 'var(--accent-hover)',
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: size * 0.4,
      lineHeight: 1,
      boxShadow: 'inset 0 0 0 1px var(--border)',
      ...style
    }
  }, rest), src ? /*#__PURE__*/React.createElement("img", {
    src: src,
    alt: name,
    style: {
      width: '100%',
      height: '100%',
      objectFit: 'cover'
    }
  }) : initials || '?');
}
Object.assign(__ds_scope, { Avatar });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Avatar.jsx", error: String((e && e.message) || e) }); }

// components/core/Badge.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Small status/category badge. Tones map to semantic + financial colors.
 * tone: neutral | income | expense | savings | investment | success | warning | danger | info
 */
function Badge({
  children,
  tone = 'neutral',
  dot = false,
  style,
  ...rest
}) {
  const map = {
    neutral: ['var(--surface-sunken)', 'var(--text-secondary)'],
    income: ['var(--income-soft)', 'var(--income)'],
    expense: ['var(--expense-soft)', 'var(--expense)'],
    savings: ['var(--savings-soft)', 'var(--savings)'],
    investment: ['var(--investment-soft)', 'var(--investment)'],
    success: ['var(--income-soft)', 'var(--success)'],
    warning: ['var(--warning-soft)', 'var(--warning)'],
    danger: ['var(--danger-soft)', 'var(--danger)'],
    info: ['var(--info-soft)', 'var(--info)']
  };
  const [bg, fg] = map[tone] || map.neutral;
  return /*#__PURE__*/React.createElement("span", _extends({
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 6,
      height: 24,
      padding: '0 10px',
      borderRadius: 'var(--r-pill)',
      background: bg,
      color: fg,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 12,
      lineHeight: 1,
      whiteSpace: 'nowrap',
      ...style
    }
  }, rest), dot && /*#__PURE__*/React.createElement("span", {
    style: {
      width: 6,
      height: 6,
      borderRadius: '50%',
      background: fg
    }
  }), children);
}
Object.assign(__ds_scope, { Badge });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Badge.jsx", error: String((e && e.message) || e) }); }

// components/core/Button.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Hisabak primary button. Pill-shaped, calm, confident.
 * Variants: primary (green CTA), secondary (outlined), ghost (text), danger.
 */
function Button({
  children,
  variant = 'primary',
  size = 'md',
  fullWidth = false,
  disabled = false,
  leadingIcon,
  trailingIcon,
  onClick,
  style,
  ...rest
}) {
  const sizes = {
    sm: {
      height: 36,
      padding: '0 14px',
      font: 14
    },
    md: {
      height: 48,
      padding: '0 20px',
      font: 15
    },
    lg: {
      height: 52,
      padding: '0 24px',
      font: 16
    }
  };
  const s = sizes[size] || sizes.md;
  const variants = {
    primary: {
      background: 'var(--accent)',
      color: 'var(--accent-on)',
      boxShadow: 'var(--shadow-accent)',
      border: '1px solid transparent'
    },
    secondary: {
      background: 'var(--surface)',
      color: 'var(--text-primary)',
      border: '1px solid var(--border-strong)'
    },
    ghost: {
      background: 'transparent',
      color: 'var(--accent-hover)',
      border: '1px solid transparent'
    },
    danger: {
      background: 'var(--danger)',
      color: '#fff',
      border: '1px solid transparent'
    }
  };
  return /*#__PURE__*/React.createElement("button", _extends({
    type: "button",
    disabled: disabled,
    onClick: onClick,
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      gap: 8,
      height: s.height,
      padding: s.padding,
      width: fullWidth ? '100%' : 'auto',
      borderRadius: 'var(--r-pill)',
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: s.font,
      lineHeight: 1,
      cursor: disabled ? 'not-allowed' : 'pointer',
      opacity: disabled ? 0.45 : 1,
      transition: 'transform var(--dur-fast) var(--ease-standard), background var(--dur-fast), filter var(--dur-fast)',
      WebkitTapHighlightColor: 'transparent',
      ...variants[variant],
      ...style
    },
    onMouseDown: e => {
      if (!disabled) e.currentTarget.style.transform = 'scale(var(--press-scale))';
    },
    onMouseUp: e => {
      e.currentTarget.style.transform = 'scale(1)';
    },
    onMouseLeave: e => {
      e.currentTarget.style.transform = 'scale(1)';
    }
  }, rest), leadingIcon && /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20
    }
  }, leadingIcon), children, trailingIcon && /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20
    }
  }, trailingIcon));
}
Object.assign(__ds_scope, { Button });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Button.jsx", error: String((e && e.message) || e) }); }

// components/core/Chip.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Filter / selection chip. Used for period filters, type filters, brand pickers.
 * Pass `color` (a category hex/var) to render a leading color dot.
 */
function Chip({
  children,
  selected = false,
  color,
  leadingIcon,
  onClick,
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("button", _extends({
    type: "button",
    onClick: onClick,
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 7,
      height: 36,
      padding: color || leadingIcon ? '0 14px 0 10px' : '0 14px',
      borderRadius: 'var(--r-pill)',
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 14,
      lineHeight: 1,
      cursor: 'pointer',
      whiteSpace: 'nowrap',
      transition: 'background var(--dur-fast), border-color var(--dur-fast), color var(--dur-fast)',
      WebkitTapHighlightColor: 'transparent',
      background: selected ? 'var(--accent)' : 'var(--surface)',
      color: selected ? 'var(--accent-on)' : 'var(--text-secondary)',
      border: selected ? '1px solid transparent' : '1px solid var(--border)',
      ...style
    }
  }, rest), color && /*#__PURE__*/React.createElement("span", {
    style: {
      width: 8,
      height: 8,
      borderRadius: '50%',
      background: color,
      flex: 'none'
    }
  }), leadingIcon && /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18
    }
  }, leadingIcon), children);
}
Object.assign(__ds_scope, { Chip });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/Chip.jsx", error: String((e && e.message) || e) }); }

// components/core/IconButton.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Circular icon-only button for app bars and rows. Material Symbols icon.
 */
function IconButton({
  icon,
  size = 'md',
  variant = 'plain',
  filled = false,
  ariaLabel,
  disabled = false,
  onClick,
  style,
  ...rest
}) {
  const dims = {
    sm: 32,
    md: 40,
    lg: 44
  };
  const iconSize = {
    sm: 18,
    md: 22,
    lg: 24
  };
  const d = dims[size] || dims.md;
  const variants = {
    plain: {
      background: 'transparent',
      color: 'var(--text-secondary)'
    },
    soft: {
      background: 'var(--surface-sunken)',
      color: 'var(--text-primary)'
    },
    accent: {
      background: 'var(--accent-soft)',
      color: 'var(--accent-hover)'
    }
  };
  return /*#__PURE__*/React.createElement("button", _extends({
    type: "button",
    "aria-label": ariaLabel,
    disabled: disabled,
    onClick: onClick,
    style: {
      display: 'inline-grid',
      placeItems: 'center',
      width: d,
      height: d,
      flex: 'none',
      border: 'none',
      borderRadius: 'var(--r-pill)',
      cursor: disabled ? 'not-allowed' : 'pointer',
      opacity: disabled ? 0.4 : 1,
      transition: 'background var(--dur-fast), transform var(--dur-fast)',
      WebkitTapHighlightColor: 'transparent',
      ...variants[variant],
      ...style
    },
    onMouseDown: e => {
      e.currentTarget.style.transform = 'scale(0.92)';
    },
    onMouseUp: e => {
      e.currentTarget.style.transform = 'scale(1)';
    },
    onMouseLeave: e => {
      e.currentTarget.style.transform = 'scale(1)';
    }
  }, rest), /*#__PURE__*/React.createElement("span", {
    className: 'material-symbols-rounded' + (filled ? ' is-filled' : ''),
    style: {
      fontSize: iconSize[size] || 22
    }
  }, icon));
}
Object.assign(__ds_scope, { IconButton });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/IconButton.jsx", error: String((e && e.message) || e) }); }

// components/core/ProgressBar.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Thin progress / ratio bar. Used under the balance hero (income ratio) and
 * on category summaries. `tone` colors the filled portion.
 */
function ProgressBar({
  value = 0,
  tone = 'income',
  height = 6,
  track,
  style,
  ...rest
}) {
  const colors = {
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)',
    accent: 'var(--accent)'
  };
  const pct = Math.max(0, Math.min(100, value));
  return /*#__PURE__*/React.createElement("div", _extends({
    style: {
      width: '100%',
      height,
      borderRadius: 'var(--r-pill)',
      background: track || 'var(--surface-sunken)',
      overflow: 'hidden',
      ...style
    }
  }, rest), /*#__PURE__*/React.createElement("div", {
    style: {
      width: pct + '%',
      height: '100%',
      borderRadius: 'var(--r-pill)',
      background: colors[tone] || colors.income,
      transition: 'width var(--dur-slow) var(--ease-standard)'
    }
  }));
}
Object.assign(__ds_scope, { ProgressBar });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/ProgressBar.jsx", error: String((e && e.message) || e) }); }

// components/core/StatusChip.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * SMS parse-status chip. Three states with distinct color + icon so they are
 * unmistakable at a glance.
 *   linked   → green, already imported
 *   parsed   → blue, ready to import
 *   unparsed → gray, no data extracted
 */
function StatusChip({
  status = 'parsed',
  style,
  ...rest
}) {
  const map = {
    linked: ['Linked', 'var(--income-soft)', 'var(--income)', 'link'],
    parsed: ['Parsed', 'var(--info-soft)', 'var(--info)', 'bolt'],
    unparsed: ['Unparsed', 'var(--surface-sunken)', 'var(--text-tertiary)', 'help']
  };
  const [label, bg, fg, icon] = map[status] || map.parsed;
  return /*#__PURE__*/React.createElement("span", _extends({
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 5,
      height: 24,
      padding: '0 10px 0 8px',
      borderRadius: 'var(--r-pill)',
      background: bg,
      color: fg,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 12,
      lineHeight: 1,
      ...style
    }
  }, rest), /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 14
    }
  }, icon), label);
}
Object.assign(__ds_scope, { StatusChip });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/core/StatusChip.jsx", error: String((e && e.message) || e) }); }

// components/data/Card.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Surface container. The base card for the app.
 * variant: default (white surface + hairline) | hero (larger radius, soft shadow)
 *          | flat (no shadow) | tinted (pass `tint` color for soft background).
 */
function Card({
  children,
  variant = 'default',
  tint,
  padding = 16,
  onClick,
  style,
  ...rest
}) {
  const base = {
    borderRadius: variant === 'hero' ? 'var(--r-lg)' : 'var(--r-md)',
    padding,
    background: 'var(--surface)',
    boxShadow: 'var(--ring-card)'
  };
  const variants = {
    default: {},
    hero: {
      boxShadow: 'var(--shadow-card)',
      borderRadius: 'var(--r-lg)'
    },
    flat: {
      boxShadow: 'none',
      border: '1px solid var(--border)'
    },
    tinted: {
      background: tint || 'var(--accent-soft)',
      boxShadow: 'none'
    }
  };
  return /*#__PURE__*/React.createElement("div", _extends({
    onClick: onClick,
    style: {
      ...base,
      ...variants[variant],
      cursor: onClick ? 'pointer' : 'default',
      transition: 'box-shadow var(--dur-base), transform var(--dur-fast)',
      ...style
    }
  }, rest), children);
}
Object.assign(__ds_scope, { Card });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/data/Card.jsx", error: String((e && e.message) || e) }); }

// components/data/CategoryIcon.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Category icon tile — a Material icon on a tinted rounded square in the
 * category's color. A signature Hisabak pattern; reused in tiles, rows, pickers.
 */
function CategoryIcon({
  icon = 'category',
  color = 'var(--cat-gray)',
  size = 44,
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("span", _extends({
    style: {
      display: 'grid',
      placeItems: 'center',
      flex: 'none',
      width: size,
      height: size,
      borderRadius: 'var(--r-tile)',
      background: `color-mix(in srgb, ${color} 16%, transparent)`,
      color,
      ...style
    }
  }, rest), /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: size * 0.5
    }
  }, icon));
}
Object.assign(__ds_scope, { CategoryIcon });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/data/CategoryIcon.jsx", error: String((e && e.message) || e) }); }

// components/data/CategoryTile.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Category grid tile (2-column grid on the Categories screen).
 * Colored icon, name, type badge, monthly total, and a delete affordance.
 * Pass `addNew` to render the dashed "Add New" placeholder tile.
 */
function CategoryTile({
  name,
  icon = 'category',
  color = 'var(--cat-gray)',
  type = 'expense',
  // income|expense|savings|investment
  total,
  // formatted string e.g. "AED 1,240"
  onDelete,
  onClick,
  addNew = false,
  style,
  ...rest
}) {
  if (addNew) {
    return /*#__PURE__*/React.createElement("button", _extends({
      type: "button",
      onClick: onClick,
      style: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 8,
        minHeight: 132,
        padding: 16,
        cursor: 'pointer',
        border: '1.5px dashed var(--border-strong)',
        borderRadius: 'var(--r-md)',
        background: 'transparent',
        color: 'var(--text-secondary)',
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 14,
        ...style
      }
    }, rest), /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 26
      }
    }, "add"), "Add New");
  }
  const typeTone = {
    income: 'income',
    expense: 'expense',
    savings: 'savings',
    investment: 'investment'
  }[type] || 'neutral';
  return /*#__PURE__*/React.createElement("div", _extends({
    onClick: onClick,
    style: {
      position: 'relative',
      display: 'flex',
      flexDirection: 'column',
      gap: 10,
      padding: 14,
      minHeight: 132,
      background: 'var(--surface)',
      borderRadius: 'var(--r-md)',
      boxShadow: 'var(--ring-card)',
      cursor: onClick ? 'pointer' : 'default',
      ...style
    }
  }, rest), onDelete && /*#__PURE__*/React.createElement("button", {
    type: "button",
    "aria-label": "Delete",
    onClick: e => {
      e.stopPropagation();
      onDelete();
    },
    style: {
      position: 'absolute',
      top: 8,
      right: 8,
      display: 'grid',
      placeItems: 'center',
      width: 24,
      height: 24,
      border: 'none',
      borderRadius: '50%',
      background: 'transparent',
      color: 'var(--text-tertiary)',
      cursor: 'pointer'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18
    }
  }, "close")), /*#__PURE__*/React.createElement(__ds_scope.CategoryIcon, {
    icon: icon,
    color: color,
    size: 40
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, name), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginTop: 'auto'
    }
  }, /*#__PURE__*/React.createElement(__ds_scope.Badge, {
    tone: typeTone,
    style: {
      height: 20,
      fontSize: 11
    }
  }, type[0].toUpperCase() + type.slice(1)), total && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-mono)',
      fontWeight: 600,
      fontSize: 13,
      color: 'var(--text-secondary)',
      fontVariantNumeric: 'tabular-nums'
    }
  }, total)));
}
Object.assign(__ds_scope, { CategoryTile });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/data/CategoryTile.jsx", error: String((e && e.message) || e) }); }

// components/data/EmptyState.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Empty state — big tinted icon, title, guidance, and a CTA. Every list screen
 * gets one. Keep copy short and action-oriented.
 */
function EmptyState({
  icon = 'inbox',
  title = 'Nothing here yet',
  description,
  actionLabel,
  onAction,
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("div", _extends({
    style: {
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      textAlign: 'center',
      gap: 12,
      padding: '40px 24px',
      ...style
    }
  }, rest), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'grid',
      placeItems: 'center',
      width: 72,
      height: 72,
      borderRadius: 'var(--r-lg)',
      background: 'var(--accent-soft)',
      color: 'var(--accent)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 36
    }
  }, icon)), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 17,
      color: 'var(--text-primary)'
    }
  }, title), description && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 14,
      lineHeight: 1.5,
      color: 'var(--text-secondary)',
      maxWidth: 260
    }
  }, description), actionLabel && /*#__PURE__*/React.createElement(__ds_scope.Button, {
    leadingIcon: "add",
    onClick: onAction,
    style: {
      marginTop: 6
    }
  }, actionLabel));
}
Object.assign(__ds_scope, { EmptyState });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/data/EmptyState.jsx", error: String((e && e.message) || e) }); }

// components/data/ListRow.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * List row — transactions, brands, SMS. A leading colored avatar (initial or
 * Material icon), a title + subtitle, and a right slot (amount + meta, or custom).
 */
function ListRow({
  title,
  subtitle,
  leadingText,
  // initial(s) inside the colored circle
  leadingIcon,
  // Material Symbols ligature inside the circle
  color = 'var(--cat-gray)',
  amount,
  // number → rendered via AmountText
  amountTone = 'auto',
  meta,
  // small text under the amount (e.g. date)
  trailing,
  // custom right-side node (overrides amount/meta)
  onClick,
  divider = true,
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("div", _extends({
    onClick: onClick,
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 12,
      padding: '12px 4px',
      borderBottom: divider ? '1px solid var(--divider)' : 'none',
      cursor: onClick ? 'pointer' : 'default',
      ...style
    }
  }, rest), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'grid',
      placeItems: 'center',
      flex: 'none',
      width: 40,
      height: 40,
      borderRadius: '50%',
      background: `color-mix(in srgb, ${color} 14%, transparent)`,
      color,
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 15
    }
  }, leadingIcon ? /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20
    }
  }, leadingIcon) : leadingText || '?'), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1,
      minWidth: 0
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 15,
      color: 'var(--text-primary)',
      whiteSpace: 'nowrap',
      overflow: 'hidden',
      textOverflow: 'ellipsis'
    }
  }, title), subtitle && /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 13,
      color: 'var(--text-secondary)',
      whiteSpace: 'nowrap',
      overflow: 'hidden',
      textOverflow: 'ellipsis',
      marginTop: 1
    }
  }, subtitle)), trailing != null ? trailing : amount != null && /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'flex-end',
      gap: 2,
      flex: 'none'
    }
  }, /*#__PURE__*/React.createElement(__ds_scope.AmountText, {
    value: amount,
    tone: amountTone
  }), meta && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12,
      color: 'var(--text-tertiary)'
    }
  }, meta)));
}
Object.assign(__ds_scope, { ListRow });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/data/ListRow.jsx", error: String((e && e.message) || e) }); }

// components/data/StatCard.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Compact stat pill / card: icon + label + value, with an optional delta.
 * Use for Total Cash / Savings / Investment and the Income / Expense summaries.
 */
function StatCard({
  label,
  value,
  currency = 'AED',
  icon,
  tone = 'neutral',
  // colors the icon tile + value
  delta,
  // e.g. +12.4  (percent vs last period)
  emphasis = false,
  // larger amount for income/expense hero cards
  style,
  ...rest
}) {
  const toneColor = {
    neutral: 'var(--text-primary)',
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)'
  };
  const toneSoft = {
    neutral: 'var(--surface-sunken)',
    income: 'var(--income-soft)',
    expense: 'var(--expense-soft)',
    savings: 'var(--savings-soft)',
    investment: 'var(--investment-soft)'
  };
  const deltaUp = (delta || 0) >= 0;
  return /*#__PURE__*/React.createElement("div", _extends({
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 10,
      padding: 14,
      borderRadius: 'var(--r-md)',
      background: 'var(--surface)',
      boxShadow: 'var(--ring-card)',
      ...style
    }
  }, rest), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between'
    }
  }, icon && /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'grid',
      placeItems: 'center',
      width: 32,
      height: 32,
      borderRadius: 'var(--r-sm)',
      background: toneSoft[tone],
      color: toneColor[tone]
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18
    }
  }, icon)), delta != null && /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 2,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 12.5,
      color: deltaUp ? 'var(--income)' : 'var(--expense)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 16
    }
  }, deltaUp ? 'arrow_upward' : 'arrow_downward'), Math.abs(delta), "%")), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 13,
      color: 'var(--text-secondary)'
    }
  }, label), /*#__PURE__*/React.createElement(__ds_scope.AmountText, {
    value: value,
    currency: currency,
    sign: "never",
    tone: tone === 'neutral' ? 'neutral' : tone,
    size: emphasis ? 26 : 18,
    weight: 700
  }));
}
Object.assign(__ds_scope, { StatCard });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/data/StatCard.jsx", error: String((e && e.message) || e) }); }

// components/forms/Input.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Text input with optional label, leading icon, helper / error text.
 */
function Input({
  label,
  value,
  placeholder,
  leadingIcon,
  helper,
  error,
  type = 'text',
  multiline = false,
  rows = 3,
  onChange,
  style,
  ...rest
}) {
  const hasError = Boolean(error);
  const field = {
    width: '100%',
    minHeight: multiline ? undefined : 48,
    padding: leadingIcon ? '0 14px 0 42px' : '13px 14px',
    border: `1px solid ${hasError ? 'var(--danger)' : 'var(--border)'}`,
    borderRadius: 'var(--r-md)',
    background: 'var(--surface)',
    color: 'var(--text-primary)',
    fontFamily: 'var(--font-sans)',
    fontSize: 15,
    lineHeight: 1.4,
    outline: 'none',
    transition: 'border-color var(--dur-fast), box-shadow var(--dur-fast)',
    resize: multiline ? 'vertical' : undefined,
    boxSizing: 'border-box'
  };
  const onFocus = e => {
    e.target.style.borderColor = hasError ? 'var(--danger)' : 'var(--accent)';
    e.target.style.boxShadow = `0 0 0 3px ${hasError ? 'var(--danger-soft)' : 'var(--focus-ring)'}`;
  };
  const onBlur = e => {
    e.target.style.borderColor = hasError ? 'var(--danger)' : 'var(--border)';
    e.target.style.boxShadow = 'none';
  };
  return /*#__PURE__*/React.createElement("label", {
    style: {
      display: 'flex',
      flexDirection: 'column',
      gap: 6,
      ...style
    }
  }, label && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 14,
      color: 'var(--text-secondary)'
    }
  }, label), /*#__PURE__*/React.createElement("span", {
    style: {
      position: 'relative',
      display: 'flex',
      alignItems: 'center'
    }
  }, leadingIcon && /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      position: 'absolute',
      left: 12,
      fontSize: 20,
      color: 'var(--text-tertiary)',
      pointerEvents: 'none'
    }
  }, leadingIcon), multiline ? /*#__PURE__*/React.createElement("textarea", _extends({
    rows: rows,
    value: value,
    placeholder: placeholder,
    onChange: onChange,
    onFocus: onFocus,
    onBlur: onBlur,
    style: {
      ...field,
      paddingTop: 12,
      paddingBottom: 12
    }
  }, rest)) : /*#__PURE__*/React.createElement("input", _extends({
    type: type,
    value: value,
    placeholder: placeholder,
    onChange: onChange,
    onFocus: onFocus,
    onBlur: onBlur,
    style: field
  }, rest))), (helper || error) && /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: hasError ? 'var(--danger)' : 'var(--text-tertiary)'
    }
  }, error || helper));
}
Object.assign(__ds_scope, { Input });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/forms/Input.jsx", error: String((e && e.message) || e) }); }

// components/forms/SearchBar.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Full-width search bar — persistent on list screens.
 */
function SearchBar({
  value,
  placeholder = 'Search',
  onChange,
  onClear,
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8,
      height: 44,
      padding: '0 12px',
      background: 'var(--surface-sunken)',
      border: '1px solid transparent',
      borderRadius: 'var(--r-pill)',
      ...style
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 20,
      color: 'var(--text-tertiary)'
    }
  }, "search"), /*#__PURE__*/React.createElement("input", _extends({
    value: value,
    placeholder: placeholder,
    onChange: onChange,
    style: {
      flex: 1,
      minWidth: 0,
      border: 'none',
      outline: 'none',
      background: 'transparent',
      fontFamily: 'var(--font-sans)',
      fontSize: 15,
      color: 'var(--text-primary)'
    }
  }, rest)), value ? /*#__PURE__*/React.createElement("button", {
    type: "button",
    onClick: onClear,
    "aria-label": "Clear",
    style: {
      display: 'grid',
      placeItems: 'center',
      width: 24,
      height: 24,
      border: 'none',
      background: 'transparent',
      cursor: 'pointer',
      color: 'var(--text-tertiary)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18
    }
  }, "close")) : null);
}
Object.assign(__ds_scope, { SearchBar });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/forms/SearchBar.jsx", error: String((e && e.message) || e) }); }

// components/forms/SegmentedControl.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Segmented control — for the Expense/Income/Savings/Investment type toggle and
 * period selectors. Each option may carry its own active color via `tones`.
 * options: [{ value, label, tone? }]  tone ∈ income|expense|savings|investment|accent
 */
function SegmentedControl({
  options = [],
  value,
  onChange,
  style,
  ...rest
}) {
  const toneColor = {
    income: 'var(--income)',
    expense: 'var(--expense)',
    savings: 'var(--savings)',
    investment: 'var(--investment)',
    accent: 'var(--accent)'
  };
  return /*#__PURE__*/React.createElement("div", _extends({
    role: "tablist",
    style: {
      display: 'grid',
      gridTemplateColumns: `repeat(${options.length}, 1fr)`,
      gap: 4,
      padding: 4,
      background: 'var(--surface-sunken)',
      borderRadius: 'var(--r-md)',
      ...style
    }
  }, rest), options.map(o => {
    const active = o.value === value;
    const accent = toneColor[o.tone] || 'var(--accent)';
    return /*#__PURE__*/React.createElement("button", {
      key: o.value,
      type: "button",
      role: "tab",
      "aria-selected": active,
      onClick: () => onChange && onChange(o.value),
      style: {
        display: 'inline-flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 6,
        height: 38,
        border: 'none',
        borderRadius: 'var(--r-sm)',
        cursor: 'pointer',
        fontFamily: 'var(--font-sans)',
        fontWeight: 600,
        fontSize: 14,
        transition: 'all var(--dur-fast) var(--ease-standard)',
        background: active ? 'var(--surface)' : 'transparent',
        color: active ? accent : 'var(--text-secondary)',
        boxShadow: active ? 'var(--shadow-xs)' : 'none'
      }
    }, o.icon && /*#__PURE__*/React.createElement("span", {
      className: "material-symbols-rounded",
      style: {
        fontSize: 18
      }
    }, o.icon), o.label);
  }));
}
Object.assign(__ds_scope, { SegmentedControl });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/forms/SegmentedControl.jsx", error: String((e && e.message) || e) }); }

// components/navigation/BottomNav.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
const DEFAULT_TABS = [{
  value: 'dashboard',
  label: 'Dashboard',
  icon: 'donut_small'
}, {
  value: 'transactions',
  label: 'Transactions',
  icon: 'receipt_long'
}, {
  value: 'sms',
  label: 'SMS',
  icon: 'sms'
}, {
  value: 'brands',
  label: 'Brands',
  icon: 'sell'
}, {
  value: 'categories',
  label: 'Categories',
  icon: 'category'
}];

/**
 * Bottom navigation — 5 tabs. Inactive tabs use the outlined icon + muted label;
 * the active tab fills the icon and switches to the accent green.
 */
function BottomNav({
  tabs = DEFAULT_TABS,
  value = 'dashboard',
  onChange,
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("nav", _extends({
    style: {
      display: 'grid',
      gridTemplateColumns: `repeat(${tabs.length}, 1fr)`,
      alignItems: 'stretch',
      height: 64,
      background: 'var(--nav-bg)',
      borderTop: '1px solid var(--divider)',
      ...style
    }
  }, rest), tabs.map(t => {
    const active = t.value === value;
    return /*#__PURE__*/React.createElement("button", {
      key: t.value,
      type: "button",
      "aria-current": active ? 'page' : undefined,
      onClick: () => onChange && onChange(t.value),
      style: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 3,
        border: 'none',
        background: 'transparent',
        cursor: 'pointer',
        color: active ? 'var(--nav-icon-active)' : 'var(--nav-icon)',
        WebkitTapHighlightColor: 'transparent',
        transition: 'color var(--dur-fast)'
      }
    }, /*#__PURE__*/React.createElement("span", {
      className: 'material-symbols-rounded' + (active ? ' is-filled' : ''),
      style: {
        fontSize: 24
      }
    }, t.icon), /*#__PURE__*/React.createElement("span", {
      style: {
        fontFamily: 'var(--font-sans)',
        fontWeight: active ? 600 : 500,
        fontSize: 11,
        letterSpacing: '0.01em',
        color: active ? 'var(--nav-label-active)' : 'var(--nav-label)'
      }
    }, t.label));
  }));
}
Object.assign(__ds_scope, { BottomNav });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/navigation/BottomNav.jsx", error: String((e && e.message) || e) }); }

// components/navigation/TopAppBar.jsx
try { (() => {
function _extends() { return _extends = Object.assign ? Object.assign.bind() : function (n) { for (var e = 1; e < arguments.length; e++) { var t = arguments[e]; for (var r in t) ({}).hasOwnProperty.call(t, r) && (n[r] = t[r]); } return n; }, _extends.apply(null, arguments); }
/**
 * Persistent top app bar. Either a wordmark (left) + avatar & bell (right) for
 * the main shell, or a back arrow + centered title for detail screens.
 */
function TopAppBar({
  title,
  brand = false,
  // show the Hisabak wordmark instead of a title
  onBack,
  // show a back arrow
  showAvatar = false,
  avatarName = '',
  showBell = false,
  onBell,
  onAvatar,
  actions,
  // custom right-side nodes
  style,
  ...rest
}) {
  return /*#__PURE__*/React.createElement("header", _extends({
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 8,
      height: 56,
      padding: '0 8px 0 8px',
      background: 'var(--nav-bg)',
      borderBottom: '1px solid var(--divider)',
      ...style
    }
  }, rest), onBack && /*#__PURE__*/React.createElement(__ds_scope.IconButton, {
    icon: "arrow_back",
    ariaLabel: "Back",
    onClick: onBack
  }), brand ? /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 9,
      paddingLeft: onBack ? 0 : 8
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'grid',
      placeItems: 'center',
      width: 28,
      height: 28,
      borderRadius: 8,
      background: 'var(--accent)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 18,
      color: '#fff'
    }
  }, "show_chart")), /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 19,
      letterSpacing: '-0.02em',
      color: 'var(--text-primary)'
    }
  }, "Hisabak")) : /*#__PURE__*/React.createElement("h1", {
    style: {
      flex: 1,
      margin: 0,
      paddingLeft: onBack ? 0 : 8,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 18,
      color: 'var(--text-primary)',
      textAlign: onBack ? 'center' : 'left'
    }
  }, title), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1
    }
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 4
    }
  }, actions, showBell && /*#__PURE__*/React.createElement(__ds_scope.IconButton, {
    icon: "notifications",
    ariaLabel: "Notifications",
    onClick: onBell
  }), showAvatar && /*#__PURE__*/React.createElement("button", {
    type: "button",
    onClick: onAvatar,
    "aria-label": "Account",
    style: {
      border: 'none',
      background: 'transparent',
      padding: 2,
      cursor: 'pointer'
    }
  }, /*#__PURE__*/React.createElement(__ds_scope.Avatar, {
    name: avatarName,
    size: 34
  }))));
}
Object.assign(__ds_scope, { TopAppBar });
})(); } catch (e) { __ds_ns.__errors.push({ path: "components/navigation/TopAppBar.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/AddTransactionSheet.jsx
try { (() => {
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
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 18,
      color: 'var(--text-tertiary)'
    }
  }, "AED"), /*#__PURE__*/React.createElement("span", {
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
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/AddTransactionSheet.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/Categories.jsx
try { (() => {
/* Categories — most-used highlight, summary, type filter, 2-col grid. */
function Categories({
  onAdd
}) {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Button,
    SearchBar,
    Chip,
    CategoryTile,
    CategoryIcon,
    AmountText,
    ProgressBar,
    Badge
  } = NS;
  const M = window.HisabakMock;
  const [q, setQ] = React.useState('');
  const [filter, setFilter] = React.useState('all');
  const cats = M.CATEGORIES.filter(c => (filter === 'all' || c.type === filter) && c.name.toLowerCase().includes(q.toLowerCase()));
  return /*#__PURE__*/React.createElement("div", {
    style: {
      padding: '8px 16px 24px'
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement("h1", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 22,
      letterSpacing: '-0.02em',
      color: 'var(--text-primary)'
    }
  }, "Categories"), /*#__PURE__*/React.createElement(Button, {
    size: "sm",
    leadingIcon: "add",
    onClick: onAdd
  }, "New")), /*#__PURE__*/React.createElement(Card, {
    variant: "tinted",
    tint: "var(--accent-soft)",
    padding: 16,
    style: {
      marginTop: 14,
      display: 'flex',
      alignItems: 'center',
      gap: 14
    }
  }, /*#__PURE__*/React.createElement(CategoryIcon, {
    icon: "shopping_cart",
    color: "var(--cat-orange)",
    size: 48
  }), /*#__PURE__*/React.createElement("div", {
    style: {
      flex: 1
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 4,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 11.5,
      letterSpacing: '0.04em',
      textTransform: 'uppercase',
      color: 'var(--accent-hover)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded is-filled",
    style: {
      fontSize: 14
    }
  }, "star"), "Most used"), /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 700,
      fontSize: 18,
      color: 'var(--text-primary)',
      marginTop: 2
    }
  }, "Groceries")), /*#__PURE__*/React.createElement(AmountText, {
    value: 1240,
    tone: "neutral",
    sign: "never",
    size: 18,
    weight: 700
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: 12,
      marginTop: 12
    }
  }, /*#__PURE__*/React.createElement(Card, {
    padding: 14
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "Income"), /*#__PURE__*/React.createElement("div", {
    style: {
      margin: '4px 0 10px'
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: 9420,
    tone: "income",
    sign: "never",
    size: 18,
    weight: 700
  })), /*#__PURE__*/React.createElement(ProgressBar, {
    value: 60,
    tone: "income"
  })), /*#__PURE__*/React.createElement(Card, {
    padding: 14
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontSize: 12.5,
      color: 'var(--text-secondary)'
    }
  }, "Expenses"), /*#__PURE__*/React.createElement("div", {
    style: {
      margin: '4px 0 10px'
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: 6180,
    tone: "expense",
    sign: "never",
    size: 18,
    weight: 700
  })), /*#__PURE__*/React.createElement(ProgressBar, {
    value: 40,
    tone: "expense"
  }))), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 14
    }
  }, /*#__PURE__*/React.createElement(SearchBar, {
    value: q,
    placeholder: "Search categories",
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
  }, [['all', 'All'], ['expense', 'Expenses'], ['income', 'Income'], ['savings', 'Savings'], ['investment', 'Invest']].map(([v, l]) => /*#__PURE__*/React.createElement(Chip, {
    key: v,
    selected: filter === v,
    onClick: () => setFilter(v)
  }, l))), /*#__PURE__*/React.createElement("div", {
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
    onDelete: () => {}
  })), /*#__PURE__*/React.createElement(CategoryTile, {
    addNew: true,
    onClick: onAdd
  })));
}
window.HisabakCategories = Categories;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/Categories.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/Dashboard.jsx
try { (() => {
/* Dashboard — financial snapshot. Returns scroll content (shell provides app bar + nav). */
function Dashboard() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Chip,
    AmountText,
    StatCard
  } = NS;
  const M = window.HisabakMock;
  const [period, setPeriod] = React.useState('month');
  const netSeries = [9100, 9400, 9250, 9900, 10400, 10200, 11100, 11600, 11400, 12050, 12200, 12450];
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
  const expenseCats = M.CATEGORIES.filter(c => c.type === 'expense').slice(0, 5).map(c => ({
    label: c.name,
    value: c.total,
    color: c.color
  }));
  const topBrands = [{
    label: 'Carrefour',
    value: 1240,
    color: 'var(--cat-orange)'
  }, {
    label: 'STC',
    value: 820,
    color: 'var(--cat-gray)'
  }, {
    label: 'Talabat',
    value: 540,
    color: 'var(--cat-red)'
  }, {
    label: 'Uber',
    value: 388,
    color: 'var(--cat-teal)'
  }, {
    label: 'Netflix',
    value: 256,
    color: 'var(--cat-purple)'
  }];
  const Section = ({
    title,
    action,
    children
  }) => /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 22
    }
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      marginBottom: 12
    }
  }, /*#__PURE__*/React.createElement("h2", {
    style: {
      margin: 0,
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 17,
      color: 'var(--text-primary)'
    }
  }, title), action), children);
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
  }, "Net Worth"), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'baseline',
      gap: 10,
      marginTop: 4
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: 12450,
    tone: "neutral",
    sign: "never",
    size: 36,
    weight: 700
  }), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      gap: 2,
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
  }, "arrow_upward"), "8.2%")), /*#__PURE__*/React.createElement("div", {
    style: {
      margin: '14px -4px 4px'
    }
  }, /*#__PURE__*/React.createElement(AreaChart, {
    data: netSeries,
    color: "var(--accent)",
    height: 104
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      gap: 8,
      marginTop: 8
    }
  }, ['week', 'month', 'year', 'all'].map(p => /*#__PURE__*/React.createElement(Chip, {
    key: p,
    selected: period === p,
    onClick: () => setPeriod(p),
    style: {
      height: 30,
      fontSize: 13,
      padding: '0 12px'
    }
  }, p[0].toUpperCase() + p.slice(1))))), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: 'repeat(3,1fr)',
      gap: 10,
      marginTop: 12
    }
  }, /*#__PURE__*/React.createElement(StatCard, {
    label: "Cash",
    value: 6450,
    icon: "account_balance_wallet",
    tone: "neutral"
  }), /*#__PURE__*/React.createElement(StatCard, {
    label: "Savings",
    value: 4000,
    icon: "savings",
    tone: "savings"
  }), /*#__PURE__*/React.createElement(StatCard, {
    label: "Invest",
    value: 2000,
    icon: "trending_up",
    tone: "investment"
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: 12,
      marginTop: 12
    }
  }, /*#__PURE__*/React.createElement(Card, {
    padding: 14
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 13,
      color: 'var(--text-secondary)'
    }
  }, "Income"), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 12,
      color: 'var(--income)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 15
    }
  }, "arrow_upward"), "12%")), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: 9420,
    tone: "income",
    sign: "never",
    size: 22,
    weight: 700
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 10
    }
  }, /*#__PURE__*/React.createElement(Sparkline, {
    data: [5, 6, 5, 7, 6, 8, 7, 9],
    color: "var(--income)"
  }))), /*#__PURE__*/React.createElement(Card, {
    padding: 14
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }
  }, /*#__PURE__*/React.createElement("span", {
    style: {
      fontFamily: 'var(--font-sans)',
      fontWeight: 500,
      fontSize: 13,
      color: 'var(--text-secondary)'
    }
  }, "Expenses"), /*#__PURE__*/React.createElement("span", {
    style: {
      display: 'inline-flex',
      alignItems: 'center',
      fontFamily: 'var(--font-sans)',
      fontWeight: 600,
      fontSize: 12,
      color: 'var(--income)'
    }
  }, /*#__PURE__*/React.createElement("span", {
    className: "material-symbols-rounded",
    style: {
      fontSize: 15
    }
  }, "arrow_downward"), "4%")), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 8
    }
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: 6180,
    tone: "expense",
    sign: "never",
    size: 22,
    weight: 700
  })), /*#__PURE__*/React.createElement("div", {
    style: {
      marginTop: 10
    }
  }, /*#__PURE__*/React.createElement(Sparkline, {
    data: [7, 6, 8, 5, 6, 7, 5, 6],
    color: "var(--expense)"
  })))), /*#__PURE__*/React.createElement(Section, {
    title: "Income & spending"
  }, /*#__PURE__*/React.createElement(Card, {
    padding: 16
  }, /*#__PURE__*/React.createElement("div", {
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
  }))), /*#__PURE__*/React.createElement(Section, {
    title: "Expenses by category"
  }, /*#__PURE__*/React.createElement(Card, {
    padding: 16
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 18
    }
  }, /*#__PURE__*/React.createElement(DonutChart, {
    segments: expenseCats,
    centerLabel: M.money(2546 + 388, {
      decimals: false
    }).replace('AED ', ''),
    centerSub: "Total"
  }), /*#__PURE__*/React.createElement(LegendList, {
    items: expenseCats
  })))), /*#__PURE__*/React.createElement(Section, {
    title: "Top brands"
  }, /*#__PURE__*/React.createElement(Card, {
    padding: 16
  }, /*#__PURE__*/React.createElement("div", {
    style: {
      display: 'flex',
      alignItems: 'center',
      gap: 18
    }
  }, /*#__PURE__*/React.createElement(DonutChart, {
    segments: topBrands,
    centerLabel: "5",
    centerSub: "brands",
    thickness: 16,
    size: 120
  }), /*#__PURE__*/React.createElement(LegendList, {
    items: topBrands
  })))));
}
window.HisabakDashboard = Dashboard;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/Dashboard.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/SmsInbox.jsx
try { (() => {
/* SMS Inbox — auto-import status, paste & parse, message list. */
function SmsInbox() {
  const NS = window.HisabakDesignSystem_aa2548;
  const {
    Card,
    Button,
    SearchBar,
    StatusChip,
    AmountText,
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
  }, "Talabat"), /*#__PURE__*/React.createElement(AmountText, {
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
  }, s.brand), /*#__PURE__*/React.createElement(AmountText, {
    value: -s.amount,
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
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/SmsInbox.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/Transactions.jsx
try { (() => {
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
    AmountText,
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
  }, /*#__PURE__*/React.createElement(AmountText, {
    value: 12450,
    tone: "neutral",
    sign: "never",
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
      fontFamily: 'var(--font-sans)',
      fontSize: 12,
      color: 'var(--text-tertiary)'
    }
  }, /*#__PURE__*/React.createElement("span", null, "60% income ratio"), /*#__PURE__*/React.createElement("span", null, "AED 9,420 in \xB7 AED 6,180 out")), /*#__PURE__*/React.createElement(Button, {
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
  }, "Income"), /*#__PURE__*/React.createElement(AmountText, {
    value: 9420,
    tone: "income",
    sign: "never",
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
  }, "Expenses"), /*#__PURE__*/React.createElement(AmountText, {
    value: 6180,
    tone: "expense",
    sign: "never",
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
    amount: t.amount,
    meta: t.date,
    divider: i < items.length - 1
  }))))));
}
window.HisabakTransactions = Transactions;
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/Transactions.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/charts.jsx
try { (() => {
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
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/charts.jsx", error: String((e && e.message) || e) }); }

// ui_kits/mobile/mock.js
try { (() => {
/* Hisabak mobile UI kit — shared mock data + format helpers (plain global script). */
(function () {
  const CATEGORIES = [{
    id: 'salary',
    name: 'Salary',
    type: 'income',
    color: 'var(--cat-green)',
    icon: 'work',
    total: 8200
  }, {
    id: 'groceries',
    name: 'Groceries',
    type: 'expense',
    color: 'var(--cat-orange)',
    icon: 'shopping_cart',
    total: 1240
  }, {
    id: 'dining',
    name: 'Dining',
    type: 'expense',
    color: 'var(--cat-red)',
    icon: 'restaurant',
    total: 642
  }, {
    id: 'transport',
    name: 'Transport',
    type: 'expense',
    color: 'var(--cat-teal)',
    icon: 'directions_car',
    total: 388
  }, {
    id: 'fun',
    name: 'Entertainment',
    type: 'expense',
    color: 'var(--cat-purple)',
    icon: 'movie',
    total: 256
  }, {
    id: 'rent',
    name: 'Rent',
    type: 'expense',
    color: 'var(--cat-gray)',
    icon: 'home',
    total: 3200
  }, {
    id: 'savings',
    name: 'Savings',
    type: 'savings',
    color: 'var(--cat-blue)',
    icon: 'savings',
    total: 2000
  }, {
    id: 'invest',
    name: 'Investment',
    type: 'investment',
    color: 'var(--cat-purple)',
    icon: 'trending_up',
    total: 1500
  }];
  const BRANDS = [{
    id: 'acme',
    name: 'Acme Corp',
    category: 'salary',
    initial: 'A'
  }, {
    id: 'carrefour',
    name: 'Carrefour',
    category: 'groceries',
    initial: 'C'
  }, {
    id: 'starbucks',
    name: 'Starbucks',
    category: 'dining',
    initial: 'S'
  }, {
    id: 'talabat',
    name: 'Talabat',
    category: 'dining',
    initial: 'T'
  }, {
    id: 'uber',
    name: 'Uber',
    category: 'transport',
    initial: 'U'
  }, {
    id: 'netflix',
    name: 'Netflix',
    category: 'fun',
    initial: 'N'
  }, {
    id: 'stc',
    name: 'STC',
    category: 'rent',
    initial: 'S'
  }, {
    id: 'amazon',
    name: 'Amazon',
    category: 'groceries',
    initial: 'A'
  }];
  const TX = [{
    id: 't1',
    brand: 'Acme Corp',
    cat: 'salary',
    note: 'Monthly salary',
    amount: 8200,
    date: '1 Jun',
    day: 'Today'
  }, {
    id: 't2',
    brand: 'Carrefour',
    cat: 'groceries',
    note: 'Weekly groceries',
    amount: -342.75,
    date: '1 Jun',
    day: 'Today'
  }, {
    id: 't3',
    brand: 'Starbucks',
    cat: 'dining',
    note: 'Flat white',
    amount: -28.00,
    date: '1 Jun',
    day: 'Today'
  }, {
    id: 't4',
    brand: 'Uber',
    cat: 'transport',
    note: 'Airport ride',
    amount: -45.50,
    date: '31 May',
    day: 'Yesterday'
  }, {
    id: 't5',
    brand: 'Talabat',
    cat: 'dining',
    note: 'Dinner delivery',
    amount: -89.00,
    date: '31 May',
    day: 'Yesterday'
  }, {
    id: 't6',
    brand: 'Netflix',
    cat: 'fun',
    note: 'Subscription',
    amount: -56.00,
    date: '28 May',
    day: 'This week'
  }, {
    id: 't7',
    brand: 'Savings',
    cat: 'savings',
    note: 'Auto transfer',
    amount: -2000,
    date: '25 May',
    day: 'This week'
  }, {
    id: 't8',
    brand: 'STC',
    cat: 'rent',
    note: 'Internet bill',
    amount: -120,
    date: '24 May',
    day: 'This week'
  }];
  const SMS = [{
    id: 's1',
    body: 'Purchase of AED 342.75 at CARREFOUR HYPERMARKET on 01/06. Available balance AED 12,107.',
    time: 'Today · 14:32',
    status: 'parsed',
    brand: 'Carrefour',
    amount: 342.75
  }, {
    id: 's2',
    body: 'Salary of AED 8,200.00 has been credited to your account ****4471 from ACME CORP.',
    time: 'Today · 09:01',
    status: 'linked',
    brand: 'Acme Corp',
    amount: 8200
  }, {
    id: 's3',
    body: 'Your verification code is 449201. Do not share this code with anyone.',
    time: 'Yesterday · 20:14',
    status: 'unparsed'
  }, {
    id: 's4',
    body: 'Payment of AED 56.00 to NETFLIX.COM was successful. Card ****4471.',
    time: 'Yesterday · 03:10',
    status: 'parsed',
    brand: 'Netflix',
    amount: 56.00
  }, {
    id: 's5',
    body: 'Withdrawal of AED 500.00 at ATM RIYADH-OLAYA. Balance AED 11,607.',
    time: '30 May · 18:45',
    status: 'parsed',
    brand: 'Cash',
    amount: 500.00
  }];
  function money(n, opts) {
    opts = opts || {};
    const abs = Math.abs(n).toLocaleString('en-US', {
      minimumFractionDigits: opts.decimals === false ? 0 : 2,
      maximumFractionDigits: opts.decimals === false ? 0 : 2
    });
    return (opts.currency === false ? '' : 'AED ') + abs;
  }
  window.HisabakMock = {
    CATEGORIES,
    BRANDS,
    TX,
    SMS,
    money
  };
})();
})(); } catch (e) { __ds_ns.__errors.push({ path: "ui_kits/mobile/mock.js", error: String((e && e.message) || e) }); }

__ds_ns.AmountText = __ds_scope.AmountText;

__ds_ns.Avatar = __ds_scope.Avatar;

__ds_ns.Badge = __ds_scope.Badge;

__ds_ns.Button = __ds_scope.Button;

__ds_ns.Chip = __ds_scope.Chip;

__ds_ns.IconButton = __ds_scope.IconButton;

__ds_ns.ProgressBar = __ds_scope.ProgressBar;

__ds_ns.StatusChip = __ds_scope.StatusChip;

__ds_ns.Card = __ds_scope.Card;

__ds_ns.CategoryIcon = __ds_scope.CategoryIcon;

__ds_ns.CategoryTile = __ds_scope.CategoryTile;

__ds_ns.EmptyState = __ds_scope.EmptyState;

__ds_ns.ListRow = __ds_scope.ListRow;

__ds_ns.StatCard = __ds_scope.StatCard;

__ds_ns.Input = __ds_scope.Input;

__ds_ns.SearchBar = __ds_scope.SearchBar;

__ds_ns.SegmentedControl = __ds_scope.SegmentedControl;

__ds_ns.BottomNav = __ds_scope.BottomNav;

__ds_ns.TopAppBar = __ds_scope.TopAppBar;

})();
