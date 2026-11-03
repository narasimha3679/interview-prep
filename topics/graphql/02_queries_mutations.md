# 02 — Queries, Mutations, and Schema

## 1. Query

```graphql
query SearchListings($term: String!, $first: Int = 10) {
  search(term: $term, first: $first) {
    edges {
      node {
        id
        title
        price { amount currency }
      }
    }
    pageInfo { hasNextPage endCursor }
  }
}
```

Variables:

```json
{ "term": "camera", "first": 10 }
```

Test:

- Correct number of results.
- Fields present and correctly typed.
- Pagination metadata consistent.
- Empty result behaves correctly.
- Invalid `first` (negative, huge) is rejected or clamped.

---

## 2. Mutation

```graphql
mutation CreateListing($input: CreateListingInput!) {
  createListing(input: $input) {
    listing { id title status }
    errors { field message code }
  }
}
```

Variables:

```json
{
  "input": {
    "title": "Vintage camera",
    "price": { "amount": 249.99, "currency": "USD" },
    "sellerId": "s1"
  }
}
```

Test:

- Side effect happened (read it back).
- Returned payload matches.
- Validation errors are structured.
- Duplicate submission is idempotent if the API promises it.
- Unauthorized user cannot mutate.
- Partial failure does not leave inconsistent state.

---

## 3. Fragments

```graphql
fragment ListingCard on Listing {
  id
  title
  price { amount currency }
}

query {
  featured: listings(featured: true) { ...ListingCard }
  recent: listings(recent: true) { ...ListingCard }
}
```

Test that fragments resolve consistently and that aliases (`featured`, `recent`) return separate
data sets.

---

## 4. Aliases

```graphql
query {
  cheap: listings(maxPrice: 50) { id title }
  expensive: listings(minPrice: 500) { id title }
}
```

Aliases let one request ask for the same field with different arguments. Test that the server does
not mix the results.

---

## 5. Directives

```graphql
query Listing($withSeller: Boolean!) {
  listing(id: "123") {
    id
    title
    seller @include(if: $withSeller) { id displayName }
  }
}
```

Test both branches: `true` includes the field, `false` omits it.

---

## 6. Introspection

```graphql
query {
  __schema {
    types { name kind }
    queryType { name }
    mutationType { name }
  }
}
```

Use introspection to:

- Validate the schema in a test environment.
- Generate clients/types.
- Detect schema drift.

Do not assume it is enabled in production. Many teams disable it.

---

## 7. Pagination

### Offset pagination

```graphql
query($page: Int!, $size: Int!) {
  listings(page: $page, size: $size) {
    items { id title }
    total
  }
}
```

Test: first page, middle page, last page, page beyond end, size 0/negative, total consistency.

### Cursor pagination (Relay-style)

```graphql
query($after: String) {
  listings(first: 10, after: $after) {
    edges { cursor node { id title } }
    pageInfo { hasNextPage hasPreviousPage startCursor endCursor }
  }
}
```

Test: first page, next page, last page, invalid cursor, duplicate/missing items across pages,
stable ordering.

---

## 8. Batching and N+1

A nested query can cause one resolver call per parent:

```graphql
query {
  listings(first: 50) {
    edges {
      node {
        id
        seller { displayName }   # one lookup per listing?
      }
    }
  }
}
```

If the resolver does a DB/API call per listing, that is 50+ calls. **DataLoader** batches them.

Test:

- Query latency stays stable as the number of parents increases.
- The number of downstream calls is bounded (instrument with logs/metrics).
- Batching does not change authorization or result correctness.
- Cache key includes tenant/user where required.

---

## 9. Subscriptions

```graphql
subscription {
  listingPriceChanged(listingId: "123") {
    listingId
    newPrice { amount currency }
  }
}
```

Test:

- Connection establishes with auth.
- Events arrive for the right subscription/filter.
- No events for other users/tenants.
- Reconnect and missed-event behavior.
- Backpressure and slow consumer behavior.

Transport is usually WebSocket or server-sent events; test tooling differs from HTTP.

---

## 10. Schema definition (SDL)

```graphql
type Listing {
  id: ID!
  title: String!
  description: String
  price: Money!
  status: ListingStatus!
  seller: Seller!
}

type Money {
  amount: Decimal!
  currency: Currency!
}

enum ListingStatus { DRAFT ACTIVE SOLD REMOVED }

type Query {
  listing(id: ID!): Listing
  listings(first: Int, after: String): ListingConnection!
}

type Mutation {
  createListing(input: CreateListingInput!): CreateListingPayload!
}
```

Read the SDL as a contract: nullability, enums, input types, and required arguments are all
assertions waiting to happen.

---

## 11. Query checklist

- [ ] Valid query returns the requested shape.
- [ ] Variables are validated (missing, wrong type, null).
- [ ] Fragments compose correctly.
- [ ] Aliases return independent results.
- [ ] Directives include/skip correctly.
- [ ] Nullability behaves as declared.
- [ ] Pagination boundaries are correct.
- [ ] Unauthorized fields are rejected or nulled per policy.
- [ ] Nested queries do not N+1.
- [ ] Error responses include `errors[]` with useful `extensions`.
