/**
 * Booking resource for restful-booker.
 *
 * Domain methods, not HTTP verbs: `create`, `getById`, `remove`. A spec that reads
 * `bookingApi.remove(id)` never has to know about the token cookie or the endpoint shape.
 *
 * `removeIfExists` exists for teardown: cleanup must not fail a test that already failed.
 */
import type { APIResponse } from '@playwright/test';
import type { ApiClient } from './api-client';
import {
  BookingIdListSchema,
  BookingSchema,
  CreatedBookingSchema,
  type Booking,
  type CreatedBooking,
} from './schemas';

const JSON_HEADERS = { Accept: 'application/json' } as const;

export class BookingApi {
  constructor(
    private readonly client: ApiClient,
    private readonly token: string,
  ) {}

  create(payload: Booking): Promise<CreatedBooking> {
    return this.client.post('/booking', CreatedBookingSchema, {
      data: payload,
      headers: { ...JSON_HEADERS, 'Content-Type': 'application/json' },
    });
  }

  getById(id: number): Promise<Booking> {
    return this.client.get(`/booking/${id}`, BookingSchema, { headers: { ...JSON_HEADERS } });
  }

  /** For status-code contract tests (404 after delete, 403 with a bad token). */
  getRaw(id: number): Promise<APIResponse> {
    return this.client.raw('GET', `/booking/${id}`, { headers: { ...JSON_HEADERS } });
  }

  getAll(): Promise<{ bookingid: number }[]> {
    return this.client.get('/booking', BookingIdListSchema, { headers: { ...JSON_HEADERS } });
  }

  update(id: number, payload: Booking): Promise<Booking> {
    return this.client.put(`/booking/${id}`, BookingSchema, {
      data: payload,
      headers: this.authorisedJson(),
    });
  }

  remove(id: number): Promise<APIResponse> {
    return this.client.delete(`/booking/${id}`, { headers: this.authorisedJson() });
  }

  removeWithToken(id: number, token: string): Promise<APIResponse> {
    return this.client.delete(`/booking/${id}`, {
      headers: { ...JSON_HEADERS, Cookie: `token=${token}` },
    });
  }

  /**
   * Teardown-safe delete: a missing booking is a success, not an error.
   *
   * restful-booker answers 405 — not the REST-correct 404 — when the id is already gone, so cleanup
   * accepts both. A teardown that throws turns a green test red for the wrong reason.
   */
  async removeIfExists(id: number): Promise<void> {
    const response = await this.remove(id);
    if (![200, 201, 404, 405].includes(response.status())) {
      throw new Error(`Cleanup failed for booking ${id}: ${response.status()}`);
    }
  }

  private authorisedJson(): Record<string, string> {
    return {
      ...JSON_HEADERS,
      'Content-Type': 'application/json',
      Cookie: `token=${this.token}`,
    };
  }
}
