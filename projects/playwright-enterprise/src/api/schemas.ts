/**
 * Runtime contracts for the API under test.
 *
 * These are not "types for autocomplete" — they are assertions. A field rename or a null where a
 * string is promised fails here, at the boundary, instead of surfacing as a confusing UI failure.
 */
import { z } from 'zod';

export const TokenSchema = z.object({
  token: z.string().min(1),
});

export const BookingSchema = z.object({
  firstname: z.string(),
  lastname: z.string(),
  totalprice: z.number().nonnegative(),
  depositpaid: z.boolean(),
  bookingdates: z.object({
    checkin: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
    checkout: z.string().regex(/^\d{4}-\d{2}-\d{2}$/),
  }),
  additionalneeds: z.string().optional(),
});

export const BookingIdSchema = z.object({
  bookingid: z.number().int().positive(),
});

export const CreatedBookingSchema = BookingIdSchema.extend({
  booking: BookingSchema,
});

export const BookingIdListSchema = z.array(BookingIdSchema);

export type Token = z.infer<typeof TokenSchema>;
export type Booking = z.infer<typeof BookingSchema>;
export type CreatedBooking = z.infer<typeof CreatedBookingSchema>;
