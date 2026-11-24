/**
 * Authentication resource. One job: exchange credentials for a token.
 *
 * The worker-scoped fixture in `src/fixtures/test.ts` calls this once per worker, not once per test.
 */
import type { ApiClient } from './api-client';
import { TokenSchema } from './schemas';

export class AuthApi {
  constructor(private readonly client: ApiClient) {}

  async createToken(username: string, password: string): Promise<string> {
    const { token } = await this.client.post('/auth', TokenSchema, {
      data: { username, password },
    });
    return token;
  }
}
