/**
 * API contract tests.
 *
 * Why they live in the same framework as the UI tests: they are the fast, deterministic layer of the
 * same risk. In a bank you would seed state here and only click what the test is actually about.
 *
 * Every created booking is registered in `bookingCleanup`, so a failed assertion still tidies up.
 */
import { bookingPayload } from '../../src/data/factories';
import { expect, test } from '../../src/fixtures/test';

test.describe('Booking API contract', () => {
  test('creates, reads and deletes a booking @smoke', async ({ bookingApi, bookingCleanup }) => {
    const payload = bookingPayload();

    const created = await test.step('create a booking', async () => {
      const response = await bookingApi.create(payload);
      bookingCleanup.push(response.bookingid);
      return response;
    });

    await test.step('read it back exactly as written', async () => {
      const fetched = await bookingApi.getById(created.bookingid);
      expect(fetched).toEqual(payload);
    });

    await test.step('delete it', async () => {
      const response = await bookingApi.remove(created.bookingid);
      expect([200, 201]).toContain(response.status());
    });

    await test.step('confirm it is gone', async () => {
      const response = await bookingApi.getRaw(created.bookingid);
      expect(response.status()).toBe(404);
    });
  });

  test('a created booking appears in the collection @regression', async ({
    bookingApi,
    bookingCleanup,
  }) => {
    const created = await bookingApi.create(bookingPayload({ firstname: 'Collection' }));
    bookingCleanup.push(created.bookingid);

    const bookings = await bookingApi.getAll();

    expect(bookings.map((booking) => booking.bookingid)).toContain(created.bookingid);
  });

  test('an update with a valid token is persisted @regression', async ({
    bookingApi,
    bookingCleanup,
  }) => {
    const created = await bookingApi.create(bookingPayload());
    bookingCleanup.push(created.bookingid);

    const updated = await bookingApi.update(
      created.bookingid,
      bookingPayload({ firstname: 'Updated', additionalneeds: created.booking.additionalneeds }),
    );

    expect(updated.firstname).toBe('Updated');
    expect(await bookingApi.getById(created.bookingid)).toEqual(updated);
  });

  test('a delete with an invalid token is refused @regression', async ({
    bookingApi,
    bookingCleanup,
  }) => {
    const created = await bookingApi.create(bookingPayload());
    bookingCleanup.push(created.bookingid);

    const response = await bookingApi.removeWithToken(created.bookingid, 'invalid-token');

    expect(response.status()).toBe(403);
    expect((await bookingApi.getRaw(created.bookingid)).ok()).toBe(true);
  });

  test('reading an unknown booking returns 404 @regression', async ({ bookingApi }) => {
    const response = await bookingApi.getRaw(999_999_999);

    expect(response.status()).toBe(404);
  });
});
