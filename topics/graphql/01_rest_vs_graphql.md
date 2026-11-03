# 01 — REST vs GraphQL

## 1. The mental model

**REST** exposes resources. The server decides the response shape. The client may over-fetch (get
more than it needs) or under-fetch (need multiple calls).

**GraphQL** exposes a type system. The client sends a query document describing exactly the fields
it wants. One request can traverse relationships.

```graphql
query Listing($id: ID!) {
  listing(id: $id) {
    id
    title
    price { amount currency }
    seller { id displayName }
  }
}
```

The response mirrors the query shape:

```json
{
  "data": {
    "listing": {
      "id": "123",
      "title": "Vintage camera",
      "price": { "amount": 249.99, "currency": "USD" },
      "seller": { "id": "s1", "displayName": "Ana" }
    }
  }
}
```

---

## 2. When GraphQL is a good fit

- Multiple clients need different shapes of the same data.
- Mobile clients need to minimize payload size and round trips.
- The domain is graph-like (catalog, social, recommendations).
- The team can invest in schema governance and resolver performance.

## When REST is a better fit

- Simple resource CRUD.
- HTTP caching is important.
- File uploads/downloads dominate.
- The team wants straightforward status codes and endpoint-level rate limits.
- The API is public and needs broad tooling compatibility.

---

## 3. Testing implications

| Question | REST | GraphQL |
|---|---|---|
| What is the contract? | OpenAPI/schema per endpoint | GraphQL SDL |
| What is a "response"? | Endpoint-specific | Query-specific shape |
| How do you test errors? | Status code + error body | `errors[]` + `data` |
| How do you test auth? | Endpoint/method | Endpoint + field/resolver |
| How do you test performance? | Endpoint latency | Query complexity + resolver N+1 |
| How do you test caching? | HTTP headers/CDN | Persisted queries, field cache |
| How do you test pagination? | `page`/`limit` or cursor | Connection spec or offset args |
| How do you test versioning? | URL/header version | Schema evolution + deprecation |

---

## 4. The GraphQL request

All operations are usually `POST /graphql` with JSON:

```json
{
  "query": "query Listing($id: ID!) { listing(id: $id) { id title } }",
  "variables": { "id": "123" },
  "operationName": "Listing"
}
```

`operationName` matters when the document contains multiple operations.

Some servers support `GET` for queries, but mutations must be `POST`.

---

## 5. What GraphQL does not solve

- **Authorization** — you still need field/resolver-level checks.
- **N+1** — resolvers can still make one query per parent.
- **Rate limiting** — one query can be expensive; limit by complexity/depth.
- **Caching** — HTTP caching is weaker; persisted queries and CDN strategies help.
- **Versioning** — schema evolution needs discipline and deprecation.
- **File uploads** — separate multipart spec or REST endpoint.
- **Observability** — one endpoint means you need operation names and tracing.

---

## 6. Interview line

> "GraphQL moves complexity from the endpoint to the query. That means my tests move from 'did this
> endpoint return 200' to 'did this exact query shape return the right data and errors, within the
> cost and authorization limits we set.'"
