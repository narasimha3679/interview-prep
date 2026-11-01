# Interview Prep — Theory Notes

QE / SDET interview prep: **Selenium · Java · TestNG · RestAssured · Cucumber · SQL**

---

## 🔥 If you only have 10 minutes — these 15 lines

1. **Waits** — "Implicit = global, presence only, runs on the driver. Explicit = targeted, any condition, runs in my code, FluentWait polling every 500 ms. Never mix them."
2. **Locators** — "Priority id → name → CSS → XPath. XPath does text matching, ancestors, functions — CSS can't. Dynamic id → match the stable part."
3. **POM** — "One class per page; locators + actions inside; tests hold only test logic. Methods return the next page (page chaining)."
4. **UI elements** — "Dropdown → `Select` (native only). Alert → `switchTo().alert()`. Windows → `getWindowHandles()`. Frame → `switchTo().frame()`. Hover → `Actions` + `perform()`."
5. **Exceptions** — "NoSuchElement = not found. Stale = found then re-rendered → re-locate. NotInteractable = can't interact. ClickIntercepted = something covering it."
6. **Framework design** — "Layers: build/config → driver → pages → tests → data → reporting → CI + integrations. Driver in ThreadLocal for parallel."
7. **Framework exception handling** — "Catch where there's something to do: retry in the wait wrapper, context in the page layer, capture in the listener. Never swallow."
8. **TestNG order** — "Suite → Test → Class → Groups → Method. Driver setup in `@BeforeMethod`, `quit()` in `@AfterMethod`."
9. **Parallel** — "`ThreadLocal<WebDriver>` + `driver.remove()` + no shared mutable state + unique test data. `parallel=classes` is safest."
10. **Assertions** — "Hard assert stops the test; soft assert reports at `assertAll()` — never forget it."
11. **Reporting** — "Listeners = event hooks. `ITestListener` for pass/fail/screenshots, `ISuiteListener` for init + `flush()`. Forgetting `flush()` = empty report."
12. **Java OOP** — "Encapsulation (page object), inheritance (BasePage), polymorphism (`WebDriver d = new ChromeDriver()`), abstraction (`WebDriver` interface). Abstract = code + state; interface = contract."
13. **`==` vs `equals`** — "`==` = reference, `.equals()` = content (only if overridden). String is immutable for: pool, security, thread safety, cached hashCode."
14. **Collections** — "Map is NOT a Collection. HashMap unordered, LinkedHashMap insertion order, TreeMap sorted. ConcurrentHashMap = per-bucket locking."
15. **RestAssured** — "`given()` = request setup incl. **body**. `when()` = method + endpoint. `then()` = status/body/header/time. Specs = POM of API testing."

---

## 📁 Structure

### Selenium
| File | Topic |
|---|---|
| `selenium/waits.md` | implicit / explicit / fluent, mixing traps |
| `selenium/locators.md` | XPath vs CSS, dynamic elements |
| `selenium/page-object-model.md` | POM, PageFactory, page chaining |
| `selenium/ui-elements.md` | dropdown, alert, windows, frames, Actions |
| `selenium/exceptions-and-flakiness.md` | exception wallet, stale element, flakiness |
| `selenium/framework-design.md` | 7-layer framework anatomy |
| `selenium/exception-handling-strategy.md` | where to catch in a framework |

### TestNG
| File | Topic |
|---|---|
| `testng/annotations-and-lifecycle.md` | annotations, order, DataProvider vs Parameters |
| `testng/testng-xml-and-parallel.md` | testng.xml, parallel modes, ThreadLocal |
| `testng/assertions.md` | hard vs soft assert, `assertAll()` |
| `testng/retry-and-listeners.md` | IRetryAnalyzer, Cucumber rerun, ITestResult |
| `testng/listeners-and-reporting.md` | ITestListener/ISuiteListener, ExtentReports, flush() |
| `testng/testng-vs-junit.md` | comparison + other @Test attributes |

### Java
| File | Topic |
|---|---|
| `java/oop-pillars-abstract-vs-interface.md` | 4 pillars, abstract vs interface |
| `java/equals-and-string-immutability.md` | `==` vs `equals`, String pool, StringBuilder |
| `java/collections.md` | List/Set/Map, ArrayList vs LinkedList, HashMap internals |
| `java/exceptions.md` | checked vs unchecked, try-with-resources, finally |
| `java/lambdas-and-streams.md` | lambdas, Stream API, io vs util streams |
| `java/reading-test-data.md` | properties, JSON/Jackson, Excel/POI |
| `java/jdbc-db-validation.md` | JDBC, PreparedStatement, eventual consistency |

### RestAssured
| File | Topic |
|---|---|
| `restassured/given-when-then.md` | structure, key methods, specs |
| `restassured/jsonpath-and-pojos.md` | GPath, POJO (de)serialization, JSON schema |
| `restassured/auth-and-specs.md` | basic/preemptive/Bearer, RequestSpecification |
| `restassured/file-upload.md` | multipart, checksum round-trip, negative cases |
| `restassured/params-status-negative.md` | params, status codes, negative, CRUD + JDBC |

### Cucumber
| File | Topic |
|---|---|
| `cucumber/data-in-bdd.md` | Examples vs DataTable vs @DataProvider |

### Database
| File | Topic |
|---|---|
| `database/sql-mini-round.md` | JOINs, HAVING, duplicates, nth highest, ACID |

### AI / LLM
| File | Topic |
|---|---|
| `../ai-testing-prep/README.md` | LLM, RAG, chatbot testing, safety, TD banking lens |

---

## 🎯 Delivery tips (the thing that actually loses marks)
- **Answer with structure**: *what → why → benefit*. Never a one-liner.
- **Name the tool and the method**: "I use `ExpectedConditions.elementToBeClickable`" beats "I wait for it".
- **Give a project example** — you built this at TD, say so.
- **Say the trap**: "…but if you mix implicit and explicit, the timeouts compound."
- **If you don't know**: "I haven't used that; I'd assume X — how does your team do it?" Honest + curious beats bluffing.
