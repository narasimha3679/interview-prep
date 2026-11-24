import js from '@eslint/js';
import tseslint from 'typescript-eslint';

/**
 * Lint policy. Type-aware rules are on because the two bugs that actually cost a Playwright suite
 * time are un-awaited async calls and `any` spreading through fixtures. `tsc` cannot catch the first.
 */
export default tseslint.config(
  {
    ignores: [
      'node_modules/**',
      'eslint.config.mjs',
      'playwright-report/**',
      'test-results/**',
      'blob-report/**',
      'reports/**',
      'auth/**',
    ],
  },
  js.configs.recommended,
  ...tseslint.configs.recommendedTypeChecked,
  {
    languageOptions: {
      parserOptions: {
        projectService: true,
        tsconfigRootDir: import.meta.dirname,
      },
    },
    rules: {
      '@typescript-eslint/consistent-type-imports': 'error',
      '@typescript-eslint/no-explicit-any': 'error',
      '@typescript-eslint/no-floating-promises': 'error',
      '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
    },
  },
);
