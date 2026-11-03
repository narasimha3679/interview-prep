# 01 — TypeScript Fundamentals

## 1. Basic types

```ts
let name: string = 'Ana';
let count: number = 3;
let active: boolean = true;
let ids: number[] = [1, 2, 3];
let tuple: [string, number] = ['item', 1];
let anything: unknown = JSON.parse('{}');
```

Avoid `any`. Use `unknown` when the type is genuinely unknown, then narrow.

---

## 2. Interface vs type

```ts
interface User {
  id: string;
  name: string;
  email?: string; // optional
}

type Status = 'draft' | 'active' | 'sold';

type Listing = {
  id: string;
  title: string;
  status: Status;
  price: Money;
};
```

| Use | Interface | Type alias |
|---|---|---|
| Object shape | ✅ | ✅ |
| Extend/merge | ✅ | ✅ (intersection) |
| Union | ❌ | ✅ |
| Primitive alias | ❌ | ✅ |
| Declaration merging | ✅ | ❌ |

For page objects/API models, either is fine; be consistent.

---

## 3. Unions and narrowing

```ts
type ApiResult<T> =
  | { ok: true; data: T }
  | { ok: false; error: string };

function unwrap<T>(result: ApiResult<T>): T {
  if (result.ok) {
    return result.data; // narrowed
  }
  throw new Error(result.error);
}
```

Model states explicitly:

```ts
type LoadState =
  | { state: 'loading' }
  | { state: 'loaded'; items: Listing[] }
  | { state: 'error'; message: string };
```

This prevents "impossible" combinations like `loading` with items.

---

## 4. Generics

```ts
interface Page<T> {
  items: T[];
  total: number;
  page: number;
}

interface ApiResponse<T> {
  data: T;
  errors: string[];
}

function first<T>(items: T[]): T | undefined {
  return items[0];
}
```

Generics let helpers stay reusable without `any`.

---

## 5. Utility types

```ts
interface Listing {
  id: string;
  title: string;
  price: number;
  status: 'draft' | 'active' | 'sold';
}

type ListingPreview = Pick<Listing, 'id' | 'title'>;
type ListingUpdate = Partial<Omit<Listing, 'id'>>;
type ListingMap = Record<string, Listing>;
```

Common: `Partial`, `Pick`, `Omit`, `Record`, `Readonly`, `Required`, `ReturnType`.

---

## 6. Functions and overloads

```ts
function formatPrice(amount: number, currency: string): string {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency }).format(amount);
}

type Comparator<T> = (a: T, b: T) => number;

const byTitle: Comparator<Listing> = (a, b) => a.title.localeCompare(b.title);
```

---

## 7. Type guards

```ts
function isListing(value: unknown): value is Listing {
  return typeof value === 'object'
    && value !== null
    && 'id' in value
    && 'title' in value;
}
```

Use guards at boundaries (API responses, JSON files).

---

## 8. Enums vs union types

```ts
// Prefer union types for simple sets
type ListingStatus = 'DRAFT' | 'ACTIVE' | 'SOLD';

// Enums are useful when you need a runtime object
enum HttpMethod {
  GET = 'GET',
  POST = 'POST',
}
```

Union types are lighter and often easier to serialize.

---

## 9. Strict mode

`tsconfig.json`:

```json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "strict": true,
    "noUncheckedIndexedAccess": true,
    "noImplicitOverride": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "types": ["node"]
  }
}
```

Strict mode catches null/undefined issues that become flaky tests.

---

## 10. Common pitfalls

1. `any` hides errors; use `unknown`.
2. `!` non-null assertion bypasses checks; narrow instead.
3. `as` casts are not runtime validation; use guards/schema validation.
4. `Object.keys` returns `string[]`, not keyof T; type carefully.
5. Optional properties are not the same as `| undefined` in all contexts.
6. Arrays are mutable; `readonly T[]` documents intent.
7. `interface` merging can surprise you in large codebases.
8. `enum` runtime behavior differs from union types.

---

## 11. TypeScript checklist

- [ ] `strict: true`.
- [ ] No `any` in framework code.
- [ ] API responses validated at runtime.
- [ ] Page objects/API clients typed.
- [ ] Test data uses factories/builders.
- [ ] `await` every promise.
- [ ] Modules/config match the runtime.
- [ ] No non-null assertions without a guard.
