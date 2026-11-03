# Java — Reading Test Data (Properties, JSON, Excel)

## Say it right
> "Config in a **`.properties`** file via `java.util.Properties`, env-specific (`config-qa.properties`) selected by `-Denv`. Test data in **JSON** with **Jackson**, mapped to POJOs. When we used Excel it was **Apache POI** with a TestNG `@DataProvider`. We migrated Excel → JSON because JSON is text, so it's VCS-friendly."

---

## 1. Properties
```java
Properties props = new Properties();
try (InputStream in = getClass().getClassLoader()
        .getResourceAsStream("config-qa.properties")) {
    props.load(in);
}
String url = props.getProperty("baseUrl");
```
- Env selection: `System.getProperty("env")` → `config-qa.properties` / `config-uat.properties`.
- `getResourceAsStream` (classpath) works from the packaged jar / CI — not an absolute path.

## 2. JSON — Jackson
```java
ObjectMapper mapper = new ObjectMapper();

User user = mapper.readValue(new File("src/test/resources/user.json"), User.class); // typed POJO
JsonNode node = mapper.readTree(new File(".../data.json"));                          // tree
String acct = node.get("accounts").get(0).get("number").asText();

String json = mapper.writeValueAsString(user);   // POJO → JSON
```
- `readValue` → typed POJO (type-safe); `readTree` → flexible `JsonNode`.
- Same Jackson mapper powers RestAssured serialization/deserialization.
- Gson is an alternative.

## 3. Excel — Apache POI
```java
Workbook wb = WorkbookFactory.create(new File("testdata.xlsx"));  // .xls + .xlsx
Sheet sheet = wb.getSheet("Login");
DataFormatter fmt = new DataFormatter();                          // any cell type → String

for (Row row : sheet) {
    String user = fmt.formatCellValue(row.getCell(0));
    String pass = fmt.formatCellValue(row.getCell(1));
}
wb.close();
```
- `WorkbookFactory` auto-detects; `HSSFWorkbook` = .xls, `XSSFWorkbook` = .xlsx.
- **`DataFormatter` is essential** — without it a numeric cell reads `1.0` and destroys account numbers / leading zeros.
- Feed TestNG via `@DataProvider(name="excelData")`.
- CSV instead → **OpenCSV** (lighter than POI).

---

## Depth ladder
1. **Why move Excel → JSON?** ← the killer answer:
   - Excel is **binary** → unreadable diffs, **merge conflicts** in git.
   - JSON is **text** → clean code review + versioning.
   - JSON supports **nested** structures; Excel is flat.
   - Drops the heavy **POI** dependency; parses easily in CI.
   - "Excel for business test-case sheets; JSON for developer-managed test data and API payloads."
2. **Secrets?** → env variables / encrypted values; never commit credentials.
3. **Where does reading live?** → a centralized `ConfigManager` / `TestDataReader` utility.
4. **How does data reach the test?** → `@DataProvider` reads the file and feeds the test / scenario.

---

## Memorize
1. **"Properties → `java.util.Properties`. JSON → Jackson `ObjectMapper`. Excel → Apache POI + `DataFormatter`."**
2. **"`getResourceAsStream` for classpath config; env chosen with `-Denv`."**
3. **"Excel → JSON because JSON is text: readable diffs, no merge conflicts, nested data, lighter dependency."**

## Likely follow-ups
- How do you avoid reading the same file repeatedly? → load once in a singleton util.
- How do you handle cell types in Excel? → `DataFormatter`.
- How does JSON tie to RestAssured? → same Jackson mapper for request/response POJOs.
