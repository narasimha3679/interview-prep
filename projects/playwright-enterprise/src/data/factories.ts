/**
 * Factory functions, not fixture files.
 *
 * Interview answer: "Tests must be parallel-safe and order-independent, so every test builds its own
 * data with a unique marker. Shared rows in a shared database are how suites become flaky at 3am."
 *
 * No random numbers: a counter plus a timestamp is unique, and it is reproducible enough to grep for
 * in logs.
 */
import type { Booking } from '../api/schemas';
import type { CustomerDetails } from '../pages/checkout.page';

let counter = 0;

export function uniqueId(prefix: string): string {
  counter += 1;
  return `${prefix}-${Date.now()}-${counter}`;
}

/** Stable input data for a checkout. Not unique — the demo app has no accounts. */
export const customer: CustomerDetails = {
  firstName: 'Interview',
  lastName: 'Candidate',
  postalCode: '10001',
};

/**
 * Valid booking payload with a unique `additionalneeds` marker, so a test can find its own record
 * among anything else running against the shared demo API.
 */
export function bookingPayload(overrides: Partial<Booking> = {}): Booking {
  return {
    firstname: 'Interview',
    lastname: 'Candidate',
    totalprice: 120,
    depositpaid: true,
    bookingdates: { checkin: '2027-01-10', checkout: '2027-01-12' },
    additionalneeds: uniqueId('qa'),
    ...overrides,
  };
}
