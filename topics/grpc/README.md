# gRPC Testing — Study Pack

Protobuf, unary and streaming RPCs, and how to test gRPC services without treating them like REST.

**Last verified:** November 2026.

---

## 60-second cram

1. **gRPC is RPC over HTTP/2** with Protobuf messages. The `.proto` file is the contract.
2. **Four call types:** unary, server streaming, client streaming, bidirectional streaming.
3. **Status codes are gRPC codes**, not HTTP statuses: `OK`, `INVALID_ARGUMENT`, `NOT_FOUND`,
   `PERMISSION_DENIED`, `UNAVAILABLE`, etc.
4. **Deadlines matter.** Every call should set one; test the timeout path.
5. **Metadata is the header equivalent** — auth tokens, trace IDs, tenant IDs.
6. **Streaming changes the test shape.** You must consume/close the stream and test partial failure,
   cancellation, and backpressure.
7. **Tools:** grpcurl, Postman gRPC, Karate gRPC, BloomRPC, generated clients.
8. **Contract testing:** Protobuf field numbers and types are the compatibility contract. Never reuse
   a field number.

**The one line that shows depth:**

> "gRPC testing is contract-first: the `.proto` defines the schema and field numbers, and the hard
> part is streaming behavior, deadlines, and status codes — not JSON shape."

---

## File map

| File | What it gives you |
|---|---|
| `01_protobuf_and_rpc.md` | Protobuf, services, streaming, status codes |
| `02_testing_grpc.md` | Test strategy and examples |
| `03_qa.md` | Questions with model answers |

---

## Key terms

| Term | Meaning | Testing implication |
|---|---|---|
| **Protobuf** | Binary schema-driven serialization | Contract is `.proto` |
| **Field number** | Stable wire identifier | Never reuse/change |
| **Service** | Set of RPC methods | Test each method |
| **Unary** | One request, one response | Like REST |
| **Server streaming** | One request, many responses | Consume/close stream |
| **Client streaming** | Many requests, one response | Send/close stream |
| **Bidirectional** | Many both ways | Concurrency/cancel |
| **Deadline** | Call timeout | Test timeout path |
| **Metadata** | Key/value headers | Auth/trace/tenant |
| **Status code** | gRPC result code | Assert code + message |

---

## Traps

1. **Asserting HTTP 200 instead of gRPC status.** gRPC uses its own codes.
2. **No deadline.** A hung call blocks the test.
3. **Not closing streams.** Resource leaks/hangs.
4. **Changing a field number.** Breaks wire compatibility.
5. **Ignoring metadata/auth.** Auth is per-call.
6. **Testing only unary calls.** Streaming is where the bugs are.
7. **Assuming JSON.** Protobuf is binary; use the generated client or grpcurl.
8. **Not testing cancellation.** Client cancel should stop server work.
9. **No reflection.** grpcurl needs reflection or the proto files.
10. **Ignoring error details.** `google.rpc.Status` details carry structured errors.

---

## How to study this pack

- **10 minutes:** README + cram.
- **30 minutes:** `01`, write a proto with unary + streaming.
- **30 minutes:** `02`, test unary and streaming.
- **20 minutes:** `03`, answer out loud.
