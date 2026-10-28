# Selenium — Cookie / Session Reuse (skip UI login for speed)

## Say it right
> "I log in once through the UI, capture the cookies, and inject them into each subsequent session to skip the UI login. But `driver.manage().getCookies()` **doesn't return HttpOnly cookies** — so for enterprise apps I use **CDP `Network.getAllCookies` / `setCookies`**."

---

## Basic cookie injection
```java
// 1) Capture once, after a real UI login
Set<Cookie> cookies = driver.manage().getCookies();
cache.put("authCookies", cookies);

// 2) Reuse in a fresh session
driver.get("https://app.example.com");        // MUST be on the domain first
for (Cookie c : cookies) {
    driver.manage().addCookie(c);
}
driver.navigate().refresh();                   // server now sees the session
```

**Rules**
1. Navigate to the domain **before** adding cookies → else `InvalidCookieDomainException`.
2. **Refresh/navigate after** adding — cookies are sent on the *next* request.

---

## ⚠️ The gotcha — HttpOnly cookies
`driver.manage().getCookies()` **does NOT return HttpOnly cookies**, and most session cookies are HttpOnly. Use **CDP** (Selenium 4):
```java
DevTools devTools = ((ChromeDriver) driver).getDevTools();
devTools.createSession();
devTools.send(Network.getAllCookies());        // includes HttpOnly
devTools.send(Network.setCookies(cookies));    // inject
```

---

## Tokens in localStorage (SPA / JWT)
```java
JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("localStorage.setItem('access_token', arguments[0]);", token);
driver.navigate().refresh();
```

---

## Managing cookies
```java
driver.manage().getCookieNamed("JSESSIONID");
driver.manage().deleteCookieNamed("JSESSIONID");
driver.manage().deleteAllCookies();
```

---

## Caveats (shows maturity)
1. **You bypass the login UI** → keep **one** real UI login test for the login feature.
2. **Sessions expire** → re-authenticate on 401 / redirect-to-login.
3. Some apps bind the session to user-agent/IP → injection may fail; verify first.
4. In **parallel** runs, cookies must be **per-thread** (ThreadLocal rule).

---

## Memorize
1. **"Navigate to the domain → `addCookie` → refresh. `InvalidCookieDomainException` if you don't."**
2. **"`getCookies()` misses HttpOnly → use CDP `Network.getAllCookies` / `setCookies`."**
3. **"Token in `localStorage` → set it with JavaScript, then refresh."**

## Likely follow-ups
- Why can't Selenium always read the session cookie? → HttpOnly.
- How do you keep the login test honest? → one real end-to-end UI login test.
- Cookie expired mid-suite? → detect redirect to login / 401 and re-authenticate.
