# 02 — Async, Promises, and Modules

## 1. Promises

```ts
const response = await fetch('/api/listings');
const listings = await response.json();
```

A Promise represents a future value. `await` pauses the async function until it settles.

```ts
async function getListing(id: string): Promise<Listing> {
  const response = await fetch(`/api/listings/${id}`);
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`);
  }
  return response.json() as Promise<Listing>;
}
```

---

## 2. async/await rules

1. **Await every promise you depend on.**
2. **Return promises from helpers**; don't fire-and-forget.
3. **Don't use `async` callbacks in `forEach`** — it does not wait.
4. **Use `Promise.all` for independent work**, `for...of` for sequential work.
5. **Handle rejections** with `try/catch` or `.catch`.
6. **Don't mix callbacks and promises** unless the API requires it.

```ts
// Bad: forEach does not await
items.forEach(async (item) => {
  await process(item);
});

// Good: sequential
for (const item of items) {
  await process(item);
}

// Good: parallel
await Promise.all(items.map(process));
```

---

## 3. Error handling

```ts
try {
  await loginPage.login(user, password);
} catch (error) {
  const message = error instanceof Error ? error.message : String(error);
  throw new Error(`Login failed for ${user.email}: ${message}`);
}
```

Rules:

- Preserve the original cause (`{ cause: error }`).
- Add context (test, user, endpoint).
- Don't swallow errors.
- Don't catch and pass silently.

---

## 4. Timeouts and retries

```ts
async function withTimeout<T>(promise: Promise<T>, ms: number): Promise<T> {
  const timeout = new Promise<never>((_, reject) =>
    setTimeout(() => reject(new Error(`Timed out after ${ms}ms`)), ms)
  );
  return Promise.race([promise, timeout]);
}
```

Playwright has its own timeouts/auto-waiting; use those for UI. Use explicit timeouts for API/network
helpers.

---

## 5. Modules

```ts
// pages/LoginPage.ts
export class LoginPage {
  constructor(private readonly page: Page) {}
}

// fixtures/test.ts
import { LoginPage } from '../pages/LoginPage';
export { LoginPage };
```

- `export`/`import` are file-scoped.
- Prefer named exports for discoverability.
- Avoid circular imports.
- Keep barrel files (`index.ts`) small; they can hurt startup and create cycles.

---

## 6. tsconfig for a Playwright framework

```json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "CommonJS",
    "moduleResolution": "Node",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "resolveJsonModule": true,
    "types": ["node"],
    "outDir": "dist"
  },
  "include": ["tests", "playwright.config.ts"]
}
```

Playwright runs TypeScript directly; `tsc --noEmit` is still useful for typechecking in CI.

---

## 7. Environment variables

```ts
const baseUrl = process.env.BASE_URL;
if (!baseUrl) {
  throw new Error('BASE_URL is required');
}
```

- Use `.env` locally, CI secrets/variables in the pipeline.
- Never commit `.env`.
- Fail fast on missing required config.

---

## 8. JSON and runtime validation

TypeScript types do not validate JSON. At the boundary:

```ts
import { z } from 'zod';

const ListingSchema = z.object({
  id: z.string(),
  title: z.string(),
  price: z.number(),
});

const listing = ListingSchema.parse(await response.json());
```

Or use a type guard. This is the difference between "typed" and "verified."

---

## 9. Async checklist

- [ ] Every promise is awaited or returned.
- [ ] No `async` callbacks in `forEach`.
- [ ] Independent work uses `Promise.all`.
- [ ] Errors preserve context.
- [ ] Timeouts on network/API helpers.
- [ ] Runtime validation at JSON boundaries.
- [ ] Config fails fast.
- [ ] `tsc --noEmit` passes in CI.
