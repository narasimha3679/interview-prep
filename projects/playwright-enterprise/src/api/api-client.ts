/**
 * Typed HTTP client shared by every API resource class.
 *
 * Interview answer: "The client owns transport concerns — base URL, headers, status handling,
 * timing and schema validation. Resource classes own the domain. Response types are validated at
 * runtime with zod, because TypeScript types are erased and `as Booking` is a claim, not proof."
 */
import type { APIRequestContext, APIResponse } from '@playwright/test';
import type { z } from 'zod';

export class ApiError extends Error {
  constructor(
    readonly method: string,
    readonly path: string,
    readonly status: number,
    readonly body: string,
  ) {
    super(`${method} ${path} failed with ${status}: ${body.slice(0, 500)}`);
    this.name = 'ApiError';
  }
}

type RequestOptions = {
  headers?: Record<string, string>;
  data?: unknown;
  params?: Record<string, string | number | boolean>;
};

export class ApiClient {
  constructor(
    private readonly request: APIRequestContext,
    private readonly baseUrl: string,
  ) {}

  get<T>(path: string, schema: z.ZodType<T>, options: RequestOptions = {}): Promise<T> {
    return this.send('GET', path, schema, options);
  }

  post<T>(path: string, schema: z.ZodType<T>, options: RequestOptions = {}): Promise<T> {
    return this.send('POST', path, schema, options);
  }

  put<T>(path: string, schema: z.ZodType<T>, options: RequestOptions = {}): Promise<T> {
    return this.send('PUT', path, schema, options);
  }

  async delete(path: string, options: RequestOptions = {}): Promise<APIResponse> {
    return this.raw('DELETE', path, options);
  }

  /** Escape hatch for contract tests that assert on status codes rather than payloads. */
  raw(method: string, path: string, options: RequestOptions = {}): Promise<APIResponse> {
    return this.request.fetch(`${this.baseUrl}${path}`, {
      method,
      headers: options.headers,
      data: options.data as never,
      params: options.params,
      failOnStatusCode: false,
    });
  }

  private async send<T>(
    method: string,
    path: string,
    schema: z.ZodType<T>,
    options: RequestOptions,
  ): Promise<T> {
    const response = await this.raw(method, path, options);
    const body = await response.text();

    if (!response.ok()) {
      throw new ApiError(method, path, response.status(), body);
    }

    let json: unknown;
    try {
      json = JSON.parse(body);
    } catch {
      throw new ApiError(method, path, response.status(), `non-JSON body: ${body.slice(0, 200)}`);
    }

    const result = schema.safeParse(json);
    if (!result.success) {
      throw new ApiError(
        method,
        path,
        response.status(),
        `schema mismatch: ${result.error.issues.map((i) => i.path.join('.')).join(', ')}`,
      );
    }

    return result.data;
  }
}
