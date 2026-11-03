# 01 — WCAG

## 1. POUR

| Principle | Meaning | Example failures |
|---|---|---|
| **Perceivable** | Information must be presentable | Missing alt text, low contrast, video without captions |
| **Operable** | UI must be usable | Keyboard traps, no focus indicator, tiny targets |
| **Understandable** | Content/behavior must be clear | Unclear errors, inconsistent navigation |
| **Robust** | Works with assistive tech | Invalid HTML, broken ARIA |

---

## 2. Levels

| Level | Meaning | Typical use |
|---|---|---|
| **A** | Minimum | Baseline |
| **AA** | Common target | Most legal/policy requirements |
| **AAA** | Enhanced | Specialized/high-accessibility contexts |

Most organizations target **AA**.

---

## 3. Common failures and tests

### Text alternatives

- Images have meaningful `alt` or empty `alt=""` if decorative.
- Icons/buttons have accessible names.
- Charts have text summaries.

**Test:** screen reader / accessibility tree; axe.

### Keyboard

- Every interactive element is reachable by Tab.
- Focus order matches visual/logical order.
- Focus is visible.
- No keyboard trap.
- Escape closes modals.
- Enter/Space activate buttons.

**Test:** keyboard-only pass.

### Forms

- Every input has a label.
- Required fields are indicated programmatically.
- Errors are associated with the field.
- Error messages are specific.
- Autocomplete where appropriate.

**Test:** screen reader + keyboard + axe.

### Contrast

- Normal text: 4.5:1.
- Large text: 3:1.
- UI components/graphics: 3:1.

**Test:** contrast checker; axe.

### Motion

- No auto-playing motion over 5 seconds without pause/stop.
- Respect `prefers-reduced-motion`.

**Test:** manual + CSS media query.

### Touch targets

- At least ~44×44 CSS px (platform guidance varies).
- Spacing between targets.

**Test:** device/viewport.

### Semantics

- Headings in order (`h1` → `h2`).
- Landmarks (`header`, `nav`, `main`, `footer`).
- Lists/tables used correctly.
- Buttons vs links.

**Test:** accessibility tree/axe.

### Modals

- Focus moves into the dialog.
- Focus is trapped.
- Escape closes.
- Focus returns to the trigger.
- `aria-modal`/role correct.

**Test:** keyboard + screen reader.

### Dynamic content

- Live regions announce status/errors.
- Loading states are announced.
- Toasts are not missed.

**Test:** screen reader.

---

## 4. ARIA rules

1. Prefer native HTML semantics.
2. Do not add ARIA if native semantics already work.
3. Never change native semantics unnecessarily.
4. All ARIA roles/states/properties must be valid.
5. Interactive ARIA controls must be keyboard operable.
6. Do not hide focusable elements with `aria-hidden`.
7. Every form control needs an accessible name.

**Bad ARIA is worse than no ARIA.**

---

## 5. Accessibility test plan

| Area | Automated | Manual |
|---|---|---|
| Alt text | axe | Meaningfulness |
| Contrast | axe | Visual check |
| Labels | axe | Screen reader |
| Keyboard | Partial | Full keyboard pass |
| Focus order | Partial | Full pass |
| Modals | Partial | Keyboard + screen reader |
| Dynamic content | Partial | Screen reader |
| Motion | Partial | Visual/preference |
| Touch targets | Partial | Device |
| Cognitive load | No | Human review |

---

## 6. Checklist

- [ ] WCAG target level defined (usually AA).
- [ ] Automated scan on critical pages.
- [ ] Keyboard-only pass on critical journeys.
- [ ] Screen-reader pass on critical journeys.
- [ ] Forms have labels/errors.
- [ ] Contrast passes.
- [ ] Focus visible/ordered.
- [ ] Modals trap/return focus.
- [ ] Reduced motion respected.
- [ ] Touch targets adequate.
- [ ] Accessibility issues have owners.
