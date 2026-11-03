# 02 — Testing gRPC

## 1. Test levels

| Level | What |
|---|---|
| **Unit** | Message mapping, validation, status mapping |
| **Component** | Service + real DB/broker, neighbors stubbed |
| **Contract** | Protobuf compatibility, consumer/provider |
| **Integration** | Multiple services over gRPC |
| **E2E** | Critical journey through the gateway |

---

## 2. Unary test

```java
@Test
void getListingReturnsNotFound() {
    GetListingRequest request = GetListingRequest.newBuilder()
            .setId("missing")
            .build();

    StatusRuntimeException ex = assertThrows(StatusRuntimeException.class,
            () -> stub.getListing(request));

    assertThat(ex.getStatus().getCode()).isEqualTo(Status.Code.NOT_FOUND);
}
```

Test:

- `OK` + payload.
- `NOT_FOUND`.
- `INVALID_ARGUMENT`.
- `UNAUTHENTICATED` / `PERMISSION_DENIED`.
- `DEADLINE_EXCEEDED`.
- `UNAVAILABLE` (server down).

---

## 3. Server streaming

```java
Iterator<Listing> stream = stub.listListings(request);

List<Listing> received = new ArrayList<>();
while (stream.hasNext()) {
    received.add(stream.next());
}

assertThat(received).hasSize(3);
assertThat(received).extracting(Listing::getId).containsExactly("1", "2", "3");
```

Test:

- All items received.
- Ordering.
- Empty stream.
- Error mid-stream.
- Client cancellation.
- Backpressure/slow consumer.

---

## 4. Client streaming

```java
StreamObserver<UploadSummary> responseObserver = new StreamObserver<>() {
    @Override public void onNext(UploadSummary value) { summary.set(value); }
    @Override public void onError(Throwable t) { error.set(t); }
    @Override public void onCompleted() { done.countDown(); }
};

StreamObserver<Listing> requestObserver = stub.uploadListings(responseObserver);
requestObserver.onNext(listing1);
requestObserver.onNext(listing2);
requestObserver.onCompleted();
done.await(2, TimeUnit.SECONDS);
```

Test:

- Summary counts.
- Partial failure.
- Client closes early.
- Server error.

---

## 5. Bidirectional streaming

- Send and receive concurrently.
- Assert ordering per direction.
- Test cancel from client/server.
- Test half-close.
- Test slow consumer/backpressure.

Use a `CountDownLatch`/`CompletableFuture` and a timeout. Never block forever.

---

## 6. Auth and metadata

```java
Metadata headers = new Metadata();
headers.put(AuthInterceptor.AUTHORIZATION,
        "Bearer " + expiredToken);
stub = stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers));

StatusRuntimeException ex = assertThrows(StatusRuntimeException.class,
        () -> stub.getListing(request));
assertThat(ex.getStatus().getCode()).isEqualTo(Status.Code.UNAUTHENTICATED);
```

Test the same matrix as REST: anonymous, owner, non-owner, admin, tenant, expired, wrong scope.

---

## 7. Contract testing

- Lint the `.proto` (Buf).
- Diff against the previous version.
- Fail on field-number reuse/type change.
- Generate clients from the same proto.
- Test provider against consumer expectations.
- Use a schema registry for events.

---

## 8. Tooling

| Tool | Use |
|---|---|
| **grpcurl** | Manual/CI calls |
| **Postman** | gRPC requests |
| **Karate** | gRPC feature tests |
| **BloomRPC** | GUI client |
| **Buf** | Lint/breaking-change detection |
| **ghz** | Load testing |
| **Generated stubs** | Java/Go/Node tests |

---

## 9. gRPC checklist

- [ ] `.proto` lint/diff in CI.
- [ ] Unary success + error codes.
- [ ] Streaming consume/close/cancel.
- [ ] Deadlines.
- [ ] Metadata/auth matrix.
- [ ] Error details.
- [ ] Contract compatibility.
- [ ] Load test for critical methods.
- [ ] Correlation ID propagation.
- [ ] Reflection policy documented.
