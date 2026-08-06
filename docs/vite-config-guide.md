# VYRA Vite Configuration Guide

## Overview

The enhanced Vite configuration provides optimized builds, environment management, and VYRA-specific features.

## Key Improvements

### 1. Environment Variable Management

```typescript
// In your components, use the env utility
import { env, isDevelopment } from '@/lib/env';

// Type-safe access to environment variables
const apiUrl = env.API_URL;
const wsUrl = env.WS_URL;

// Environment checks
if (isDevelopment) {
  console.log('Development mode active');
}
```

### 2. Proxy Configuration

In development, API calls are automatically proxied:
- `/api/*` → `http://localhost:3000/api/*`
- `/ws` → `ws://localhost:3000/ws`

### 3. Optimized Chunking

The config automatically splits code into optimized chunks:
- `react-core` - React and React DOM
- `radix-*` - UI component libraries
- `forms` - Form handling libraries
- `crypto` - Encryption libraries (including jose)
- `payments` - Stripe integration
- `monitoring` - Sentry and analytics

### 4. Environment-Specific Builds

#### Development
- Source maps enabled
- No minification for faster builds
- Enhanced debugging

#### Production
- Aggressive minification with Terser
- Tree shaking enabled
- Console statements removed
- Optimized chunk splitting

## Environment Variables

### Required for Client (VITE_ prefix)
```env
VITE_API_URL=http://localhost:3000
VITE_WS_URL=ws://localhost:3000
VITE_APP_VERSION=1.0.0
```

### Optional for Enhanced Features
```env
VITE_STRIPE_PUBLISHABLE_KEY=pk_test_...
VITE_SENTRY_DSN=https://...
VITE_DEV_PORT=8080
```

## Build Commands

```bash
# Development server with hot reload
pnpm dev

# Production build with optimizations
pnpm build

# Preview production build locally
pnpm preview
```

## Performance Features

1. **Chunk Size Optimization**: Warns at 600KB chunks
2. **Asset Inlining**: Files < 4KB are inlined
3. **CSS Code Splitting**: Separate CSS files for better caching
4. **Pre-bundling**: Common dependencies are pre-bundled in dev

## Security Headers

Production preview includes security headers:
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY`
- `X-XSS-Protection: 1; mode=block`
- `Referrer-Policy: strict-origin-when-cross-origin`

## Troubleshooting

### Missing Environment Variables
The config validates required variables and provides helpful error messages.

### Build Failures
Check the console for specific chunk or dependency issues. The config includes error handling for common problems.

### Development Server Issues
Ensure your Express server is running on the correct port (default: 3000) for proxy to work.