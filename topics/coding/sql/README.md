# SQL Practice

Use the schema in [`../../sql/sql-mini-round.md`](../../sql/sql-mini-round.md)
or create:

```sql
CREATE TABLE users (
  id        BIGINT PRIMARY KEY,
  email     VARCHAR(255) NOT NULL UNIQUE,
  country   VARCHAR(2) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE orders (
  id         BIGINT PRIMARY KEY,
  user_id    BIGINT NOT NULL REFERENCES users(id),
  status     VARCHAR(20) NOT NULL,
  total      DECIMAL(12,2) NOT NULL,
  created_at TIMESTAMP NOT NULL
);
```

## Drills

1. Find users with no orders.
2. Find the top 3 users by total paid order value.
3. Find duplicate emails.
4. Find the latest order per user.
5. Find orders whose total is above the user's average.
6. Count orders per status per country.
7. Find users who ordered in both January and February.
8. Find the second-highest order total.
9. Find month-over-month revenue growth.
10. Find orders with a total that doesn't match the sum of line items.

## Test mindset

- Check cardinality: does the join multiply rows?
- Check NULL behavior.
- Check boundary dates/time zones.
- Check pagination stability.
- Check the query plan for critical queries.
- Use parameterized queries; never concatenate input.
