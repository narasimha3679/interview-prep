# Accessibility Testing — Study Pack

WCAG basics and how to add accessibility checks to a web/mobile test suite without pretending
automation proves accessibility.

**Last verified:** November 2026.

---

## 60-second cram

1. **WCAG principles:** Perceivable, Operable, Understandable, Robust (POUR).
2. **Conformance levels:** A (minimum), AA (common legal target), AAA (enhanced).
3. **Automation catches maybe 30–50% of issues.** Keyboard, screen reader, and cognitive testing need
   humans.
4. **The highest-value checks:** keyboard-only navigation, focus order/visibility, labels, alt text,
   contrast, touch target size, form errors.
5. **Semantic HTML is the foundation.** Buttons, links, labels, headings, landmarks.
6. **ARIA is a patch, not a replacement.** Bad ARIA is worse than no ARIA.
7. **Test with the keyboard and a screen reader**, not just axe.
8. **Accessibility is a requirement, not a nice-to-have.** It overlaps with SEO, mobile usability, and
   legal compliance.

**The one line that shows depth:**

> "Automated tools catch the mechanical issues; keyboard and screen-reader testing catch whether a
> person can actually complete the task."

---

## File map

| File | What it gives you |
|---|---|
| `01_wcag.md` | Principles, levels, common failures |
| `02_tools.md` | axe, Playwright, Lighthouse, manual checks |
| `03_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **WCAG** | Web Content Accessibility Guidelines | Conformance target |
| **POUR** | Perceivable, Operable, Understandable, Robust | Test lens |
| **A/AA/AAA** | Conformance levels | AA is the common target |
| **ARIA** | Accessible Rich Internet Applications | Roles/states, use sparingly |
| **Focus order** | Tab sequence | Must match visual/logical order |
| **Focus visible** | Visible focus indicator | Keyboard users need it |
| **Alt text** | Image description | Meaningful or empty for decorative |
| **Contrast** | Text/background ratio | 4.5:1 normal, 3:1 large |
| **Touch target** | Tappable area | ~44×44 CSS px |
| **Screen reader** | Assistive tech | NVDA/JAWS/VoiceOver/TalkBack |

---

## Traps

1. **Running axe and calling it done.** It misses most interaction issues.
2. **`div` with `onClick` instead of `button`.** No keyboard/semantics.
3. **Removing focus outlines.** Keyboard users lose their place.
4. **Placeholder as label.** It disappears and is not a label.
5. **Color-only status.** "Red = error" fails color-blind users.
6. **Auto-playing motion.** No pause/stop.
7. **ARIA misuse.** Wrong roles/states confuse screen readers.
8. **Modal focus trap missing.** Focus escapes behind the dialog.
9. **Error messages not associated with fields.** Screen readers miss them.
10. **Testing only desktop.** Mobile accessibility matters too.

---

## How to study this pack

- **10 minutes:** README + cram.
- **20 minutes:** `01`, map WCAG to a form.
- **30 minutes:** `02`, run axe + keyboard test.
- **20 minutes:** `03`, answer out loud.
