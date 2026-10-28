# RestAssured — File Upload (multipart) & Binary Validation

## Say it right
> "For a file upload I use `multiPart` — and I **never set `Content-Type: multipart/form-data` manually**, because RestAssured has to generate the **boundary**. Then I don't just check the 200: I **download the file back and compare a checksum** to prove it wasn't corrupted end to end."

---

## 1. Send an image — `multipart/form-data`
```java
given()
    .multiPart("file", new File("src/test/resources/logo.png"))            // field name + file
    .multiPart("file", new File("logo.png"), "image/png")                  // with content type
    .multiPart("meta", "{\"name\":\"logo\"}", "application/json")          // extra form fields
    .auth().oauth2(token)
.when()
    .post("/documents/upload")
.then()
    .statusCode(200)
    .body("fileId", notNullValue());
```

**Gotcha:** do **NOT** set `Content-Type: multipart/form-data` manually — RestAssured generates the **boundary**; setting it by hand breaks the upload.

**Other binary sends**
```java
.body(byteArray).contentType("image/png")            // raw binary body
.multiPart("file", file, "application/octet-stream")
```

---

## 2. What "test it" means
1. **Status code** → 200/201
2. **Response body** → `fileId`, URL, size, content type
3. **Response content type**
4. **Schema** → `matchesJsonSchemaInClasspath`
5. **Metadata** → returned `size` equals the actual file size

---

## 3. Killer answer — verify the ROUND TRIP
> "Uploading isn't enough. I **download the file back and compare a checksum** (MD5/SHA-256) against the original. That proves the bytes survived the entire pipeline — not just that the API returned 200."

```java
byte[] downloaded = given().spec(reqSpec).when().get("/documents/" + id)
        .then().statusCode(200).extract().asByteArray();

String originalHash   = DigestUtils.md5Hex(Files.readAllBytes(originalPath));
String downloadedHash = DigestUtils.md5Hex(downloaded);
assertEquals(originalHash, downloadedHash);
```

---

## 4. Negative cases
| Case | Expected |
|---|---|
| Unsupported format (`.exe`) | `400` / `415` |
| File too large | `413` |
| No file / wrong field name | `400` |
| Zero-byte / corrupted file | `400` |
| No / invalid token | `401` |

---

## 5. Automate — data-driven
```java
@DataProvider(name = "imageFiles")
public Object[][] files() {
    return new Object[][] {
        {"src/test/resources/logo.png",  200},
        {"src/test/resources/photo.jpg", 200},
        {"src/test/resources/notes.txt", 415},
        {"src/test/resources/huge.png",  413}
    };
}
```
Test files live in `src/test/resources`. Assert status per file + error code/message per negative case.

---

## Depth ladder
1. **Formats?** → `multiPart` (files), raw `body(byte[])`, JSON body, form params.
2. **Not corrupted?** → checksum on download (round trip).
3. **Why not set Content-Type manually?** → lose the multipart boundary.
4. **Boundary cases?** → size limit, wrong mime, zero bytes, filename special chars, concurrent uploads.
5. **Where in the framework?** → a file-upload API client class (same pattern as POM).

---

## Memorize
1. **"File upload = `.multiPart(\"file\", new File(...))`; never set `Content-Type: multipart/form-data` manually — RestAssured adds the boundary."**
2. **"Validate with a round trip: download it and compare the checksum (MD5/SHA-256)."**
3. **"Negative: wrong format → 415, too large → 413, missing file → 400, no auth → 401."**

## Likely follow-ups
- How do you validate binary download? → `extract().asByteArray()` + hash compare.
- How do you test a large file limit? → send an oversized file, expect 413.
- How do you pass form fields vs files? → `multiPart` for both; JSON fields as their own `multiPart`.
