/**
 * Single source of truth for configuration.
 *
 * Interview answer: "Config is code. Every value is validated at startup and the run fails with a
 * readable message if something is missing. Silent fallbacks are how a suite ends up testing the
 * wrong environment and reporting green."
 *
 * Why zod: `process.env` is `string | undefined`. Parsing once here means the rest of the framework
 * never does `process.env.X!` or `?? 'some-default'`.
 */
import 'dotenv/config';
import path from 'node:path';
import { z } from 'zod';

const EnvSchema = z.object({
  APP_ENV: z.enum(['local', 'ci', 'staging']).default('local'),

  WEB_BASE_URL: z.url(),
  API_BASE_URL: z.url(),

  SAUCEDEMO_USERNAME: z.string().min(1),
  SAUCEDEMO_PASSWORD: z.string().min(1),

  API_USERNAME: z.string().min(1),
  API_PASSWORD: z.string().min(1),

  TEST_TIMEOUT_MS: z.coerce.number().int().positive().default(30_000),
  EXPECT_TIMEOUT_MS: z.coerce.number().int().positive().default(5_000),
  ACTION_TIMEOUT_MS: z.coerce.number().int().positive().default(10_000),
  NAVIGATION_TIMEOUT_MS: z.coerce.number().int().positive().default(15_000),
});

const parsed = EnvSchema.safeParse(process.env);

if (!parsed.success) {
  const details = parsed.error.issues
    .map((issue) => `  - ${issue.path.join('.') || '(root)'}: ${issue.message}`)
    .join('\n');

  throw new Error(
    `Invalid environment configuration:\n${details}\n\n` +
      `Copy .env.example to .env and fill in every required value.`,
  );
}

const env = Object.freeze(parsed.data);

/** Repository root, resolved from this file — never from the shell's current directory. */
const ROOT = path.resolve(__dirname, '..', '..');

export const config = Object.freeze({
  appEnv: env.APP_ENV,
  isCI: Boolean(process.env.CI),

  web: {
    baseUrl: env.WEB_BASE_URL,
  },

  api: {
    baseUrl: env.API_BASE_URL,
  },

  credentials: Object.freeze({
    web: Object.freeze({
      username: env.SAUCEDEMO_USERNAME,
      password: env.SAUCEDEMO_PASSWORD,
    }),
    api: Object.freeze({
      username: env.API_USERNAME,
      password: env.API_PASSWORD,
    }),
  }),

  auth: Object.freeze({
    dir: path.join(ROOT, 'auth'),
    /** Session files are generated artifacts, never committed. */
    statePath: (role: string): string => path.join(ROOT, 'auth', `${role}.json`),
  }),

  timeouts: Object.freeze({
    test: env.TEST_TIMEOUT_MS,
    expect: env.EXPECT_TIMEOUT_MS,
    action: env.ACTION_TIMEOUT_MS,
    navigation: env.NAVIGATION_TIMEOUT_MS,
  }),
});

export type AppConfig = typeof config;
