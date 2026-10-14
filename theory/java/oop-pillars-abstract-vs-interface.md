# Java — OOP: 4 Pillars, Abstract Class vs Interface

## Say it right
> "The four pillars are **encapsulation, inheritance, polymorphism, abstraction**. I remember them through my framework: encapsulation = a page object hiding locators; inheritance = `BasePage`/`BaseTest`; polymorphism = `WebDriver driver = new ChromeDriver()`; abstraction = the `WebDriver` interface itself."

---

## The 4 pillars (with testing examples)

**1. Encapsulation** — bundle data + behaviour, hide internals.
```java
public class LoginPage {
    private WebDriver driver;                            // hidden
    public HomePage login(String u, String p) { ... }    // exposed
}
```

**2. Inheritance** — child reuses parent (`extends`).
Every page extends `BasePage`; every test extends `BaseTest` (driver setup/teardown there).

**3. Polymorphism** — same call, different behaviour.
- **Compile-time** → overloading (same name, different params)
- **Runtime** → overriding
```java
WebDriver driver = new ChromeDriver();   // cross-browser: same code after this line
```

**4. Abstraction** — hide *how*, expose *what*.
```java
public abstract class BasePage { public abstract boolean isLoaded(); }
```

---

## Abstract class vs Interface

| | **Abstract class** | **Interface** |
|---|---|---|
| Keyword | `abstract class` | `interface` |
| Methods | abstract **+ concrete** | abstract, `default`, `static` (Java 8+), `private` (9+) |
| Variables | any kind | only `public static final` (constants) |
| Constructor | ✅ yes | ❌ no |
| Inheritance | `extends` **one** | `implements` **many** |
| Purpose | **shared code + state**, related classes ("is-a") | **contract / capability**, unrelated classes ("can-do") |

```java
public abstract class BasePage {
    protected WebDriver driver;                 // shared state
    protected WebDriverWait wait;
    public abstract boolean isLoaded();         // each page must define
}

public interface Verifiable { boolean isLoaded(); }   // pure contract
```

Rule: **abstract class = shared code and state ("is-a"). Interface = contract ("can-do").**

---

## Depth ladder
1. **Best Selenium example?** → `WebDriver` is an **interface** implemented by `ChromeDriver`/`FirefoxDriver` (abstraction + polymorphism); `RemoteWebDriver` is an **abstract class** with shared implementation.
2. **Why did Java 8 add `default` methods?** → **backward compatibility** — add an interface method without breaking existing implementations (e.g. `Collection.stream()`).
3. **Can an interface have a constructor?** → No. An abstract class can (called via `super()`).
4. **Abstract class with zero abstract methods?** → allowed; it just prevents instantiation.
5. **Can you instantiate either?** → No; abstract = incomplete blueprint.

---

## Memorize
1. **"4 pillars: encapsulation (page object), inheritance (BasePage), polymorphism (`WebDriver d = new ChromeDriver()`), abstraction (`WebDriver` interface)."**
2. **"Abstract class = shared code + state, single inheritance, has constructors. Interface = pure contract, multiple inheritance, no constructor."**
3. **"`WebDriver` is an interface; `RemoteWebDriver` is an abstract class."**

## Likely follow-ups
- Overloading vs overriding? → compile-time (same class) vs runtime (subclass).
- Can you override a static method? → No, it's hidden, not overridden.
- Can an interface extend another interface? → Yes, multiple.
- Why interface over abstract class for `WebDriver`? → many unrelated implementations; contract-only.
