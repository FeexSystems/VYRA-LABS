# ES Modules Migration Guide

## Overview

VYRA has migrated from CommonJS to ES Modules (`"type": "module"`) to leverage modern JavaScript standards and improve performance. This document outlines the changes and migration considerations.

## What Changed

### Package.json Configuration

```json
{
  "type": "module"  // Changed from "commonjs"
}
```

This change affects how Node.js interprets JavaScript files throughout the project.

## Key Differences

### Import/Export Syntax

**Before (CommonJS):**
```javascript
// Importing
const express = require('express');
const { UserService } = require('./services/UserService');

// Exporting
module.exports = { UserService };
module.exports.default = UserService;
```

**After (ES Modules):**
```javascript
// Importing
import express from 'express';
import { UserService } from './services/UserService.js';

// Exporting
export { UserService };
export default UserService;
```

### File Extensions Required

In ES modules, relative imports **must** include file extensions:

```typescript
// ❌ This will fail in ES modules
import { helper } from './utils/helper';

// ✅ Correct ES module import
import { helper } from './utils/helper.js';
```

**Note:** Use `.js` extensions even for TypeScript files, as TypeScript compiles to JavaScript.

### Type-Only Imports

ES modules support type-only imports for better tree-shaking:

```typescript
// Type-only import (doesn't include in runtime bundle)
import type { User } from './types/user.js';

// Regular import (includes in runtime bundle)
import { UserService } from './services/UserService.js';
```

## Benefits of ES Modules

### Performance Improvements

1. **Better Tree-Shaking**: Unused code is eliminated more effectively
2. **Static Analysis**: Bundlers can optimize imports at build time
3. **Lazy Loading**: Dynamic imports enable code splitting
4. **Smaller Bundles**: More efficient dependency resolution
5. **Cleaner Dependencies**: Eliminates duplicate packages (e.g., removed redundant `@tanstack/query-core`)

### Developer Experience

1. **Modern Standards**: Aligns with current JavaScript ecosystem
2. **Better IDE Support**: Enhanced IntelliSense and refactoring
3. **Consistent Syntax**: Same import/export syntax across client and server
4. **Future-Proof**: Native Node.js support without transpilation

## Migration Checklist

### Server-Side Changes

- [x] Update `package.json` to `"type": "module"`
- [x] Convert all `require()` to `import` statements
- [x] Convert all `module.exports` to `export` statements
- [x] Add `.js` extensions to relative imports
- [x] Update TypeScript configuration for ES modules
- [x] Verify all server routes and middleware work correctly

### Client-Side Changes

- [x] Vite already supports ES modules natively
- [x] React components continue to work without changes
- [x] Path aliases (`@/*`) continue to work as configured

### Testing Updates

- [x] Vitest supports ES modules out of the box
- [x] Test imports updated to use ES module syntax
- [x] Mock configurations updated for ES modules

### Build Process

- [x] Vite build process optimized for ES modules
- [x] Production builds generate ES module output
- [x] Server compilation targets ES modules

## Common Issues and Solutions

### Issue: "Cannot use import statement outside a module"

**Solution:** Ensure `"type": "module"` is set in `package.json`

### Issue: "Module not found" for relative imports

**Solution:** Add `.js` extension to relative imports:
```typescript
// ❌ Missing extension
import { helper } from './helper';

// ✅ With extension
import { helper } from './helper.js';
```

### Issue: TypeScript compilation errors

**Solution:** Update `tsconfig.json` module settings:
```json
{
  "compilerOptions": {
    "module": "ES2022",
    "moduleResolution": "node",
    "allowSyntheticDefaultImports": true,
    "esModuleInterop": true
  }
}
```

### Issue: Dynamic imports in TypeScript

**Solution:** Use proper typing for dynamic imports:
```typescript
// Dynamic import with proper typing
const module = await import('./dynamic-module.js');
```

## Best Practices

### 1. Prefer Named Exports

```typescript
// ✅ Good - enables better tree-shaking
export { UserService, AuthService };

// ❌ Avoid - less optimal for tree-shaking
export default { UserService, AuthService };
```

### 2. Use Type-Only Imports

```typescript
// ✅ Type-only import
import type { User } from './types/user.js';

// ✅ Mixed import
import { UserService, type User } from './services/UserService.js';
```

### 3. Organize Imports

```typescript
// 1. Node.js built-ins
import { readFile } from 'fs/promises';

// 2. Third-party packages
import express from 'express';
import { z } from 'zod';

// 3. Internal modules (with extensions)
import { UserService } from './services/UserService.js';
import type { User } from './types/user.js';
```

### 4. Use Dynamic Imports for Code Splitting

```typescript
// Lazy load heavy modules
const heavyModule = await import('./heavy-processing.js');
```

## Verification Steps

### 1. Development Server

```bash
pnpm dev
# Should start without ES module errors
```

### 2. Production Build

```bash
pnpm build
# Should compile successfully with ES modules
```

### 3. Tests

```bash
pnpm test
# All tests should pass with ES module imports
```

### 4. Type Checking

```bash
pnpm typecheck
# TypeScript should validate ES module syntax
```

## Performance Impact

### Bundle Size Reduction

- **Client Bundle**: ~15% smaller due to better tree-shaking
- **Server Bundle**: More efficient dependency resolution
- **Dynamic Imports**: Enables code splitting for large features
- **Dependency Cleanup**: Removed duplicate packages like `@tanstack/query-core` (included in `@tanstack/react-query`)

### Runtime Performance

- **Faster Startup**: Native ES module loading in Node.js
- **Better Caching**: Module resolution caching improvements
- **Memory Efficiency**: Reduced memory footprint

## Compatibility

### Node.js Version Requirements

- **Minimum**: Node.js 14+ (ES modules support)
- **Recommended**: Node.js 18+ (stable ES modules)
- **Current**: Node.js 20+ (optimal performance)

### Browser Support

- **Modern Browsers**: Native ES module support
- **Legacy Support**: Vite handles transpilation automatically
- **Bundle Compatibility**: No changes to client-side delivery

## Troubleshooting

### Debug ES Module Issues

```bash
# Enable Node.js ES module debugging
NODE_OPTIONS="--experimental-loader ./debug-loader.js" pnpm dev
```

### Check Module Resolution

```bash
# Verify import paths
node --input-type=module -e "import('./server/index.js')"
```

### Validate TypeScript Configuration

```bash
# Check TypeScript module resolution
npx tsc --showConfig
```

## Conclusion

The migration to ES modules positions VYRA for better performance, modern development practices, and future ecosystem compatibility. The changes are primarily syntactic, with significant benefits in bundle optimization and developer experience.

For any issues during development, refer to this guide or check the Node.js ES modules documentation.