# 01 — Protobuf and gRPC

## 1. Protobuf schema

```proto
syntax = "proto3";

package listings.v1;

message Listing {
  string id = 1;
  string title = 2;
  Money price = 3;
  ListingStatus status = 4;
}

message Money {
  string currency = 1;
  int64 amount_minor = 2; // cents
}

enum ListingStatus {
  LISTING_STATUS_UNSPECIFIED = 0;
  LISTING_STATUS_DRAFT = 1;
  LISTING_STATUS_ACTIVE = 2;
  LISTING_STATUS_SOLD = 3;
}

service ListingService {
  rpc GetListing(GetListingRequest) returns (Listing);
  rpc ListListings(ListListingsRequest) returns (stream Listing);
  rpc UploadListings(stream Listing) returns (UploadSummary);
  rpc Chat(stream ChatMessage) returns (stream ChatMessage);
}
```

Field numbers are the wire contract. Never reuse a removed number; reserve it:

```proto
reserved 5, 7;
reserved "old_field";
```

---

## 2. Call types

| Type | Request | Response | Test shape |
|---|---|---|---|
| **Unary** | 1 | 1 | Request/response, status |
| **Server streaming** | 1 | many | Consume until end, assert order/count |
| **Client streaming** | many | 1 | Send, close, assert summary |
| **Bidirectional** | many | many | Concurrent send/receive, cancel |

---

## 3. Status codes

| Code | Meaning | HTTP analogy |
|---|---|---|
| `OK` | Success | 200 |
| `INVALID_ARGUMENT` | Bad input | 400 |
| `UNAUTHENTICATED` | Missing/invalid auth | 401 |
| `PERMISSION_DENIED` | Not allowed | 403 |
| `NOT_FOUND` | Missing | 404 |
| `ALREADY_EXISTS` | Duplicate | 409 |
| `RESOURCE_EXHAUSTED` | Rate/quota | 429 |
| `FAILED_PRECONDITION` | Wrong state | 412/409 |
| `ABORTED` | Concurrency conflict | 409 |
| `OUT_OF_RANGE` | Invalid range | 400 |
| `UNIMPLEMENTED` | Not supported | 501 |
| `INTERNAL` | Server bug | 500 |
| `UNAVAILABLE` | Transient/down | 503 |
| `DEADLINE_EXCEEDED` | Timeout | 504 |

Assert both the code and the message/details.

---

## 4. Metadata

```java
Metadata headers = new Metadata();
headers.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + token);
headers.put(Metadata.Key.of("x-correlation-id", Metadata.ASCII_STRING_MARSHALLER), correlationId);

BlockingStub stub = ListingServiceGrpc.newBlockingStub(channel)
        .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers))
        .withDeadlineAfter(2, TimeUnit.SECONDS);
```

Test:

- Auth required.
- Token expiry/scope.
- Correlation ID propagation.
- Tenant metadata.
- Deadline propagation.

---

## 5. Deadlines

Every call should have a deadline. Test:

- Server responds before deadline → OK.
- Server exceeds deadline → `DEADLINE_EXCEEDED`.
- Deadline propagates to downstream calls.
- Cancellation stops server work.

---

## 6. Error details

```proto
import "google/rpc/status.proto";
import "google/rpc/error_details.proto";
```

Structured details can include field violations, retry info, and resource info. Assert the detail
type and fields, not just the message string.

---

## 7. Reflection

grpcurl can discover services if reflection is enabled:

```bash
grpcurl -plaintext localhost:50051 list
grpcurl -plaintext localhost:50051 listings.v1.ListingService/GetListing \
  -d '{"id":"123"}'
```

If reflection is disabled, pass the `.proto` files:

```bash
grpcurl -plaintext -proto listings.proto \
  -d '{"id":"123"}' localhost:50051 listings.v1.ListingService/GetListing
```

---

## 8. Compatibility

Breaking:

- Changing a field type.
- Reusing a field number.
- Removing a required field (proto3 has no required, but semantics matter).
- Changing an enum value number.
- Changing a service/method name.

Safe:

- Adding a field with a new number.
- Adding an enum value.
- Deprecating a field.
- Adding a new method.

Use a Protobuf schema registry/linter (Buf) in CI.
