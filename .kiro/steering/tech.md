---
inclusion: always
---

# VYRA Technical Guidelines

## Critical Rules

### Package Management
- **ALWAYS use pnpm** - never npm or yarn
- Commands: `pnpm dev`, `pnpm build`, `pnpm test`, `pnpm format.fix`

### Technology Stack
- **Frontend**: React 18.3.1 + TypeScript + Vite + Tailwind CSS (ES Modules)
- **Backend**: Node.js + Express 5+ + SQLite 3 (ES Modules)
- **UI**: shadcn/ui components with cyberpunk theme
- **State**: React hooks + TanStack Query (no Redux)
- **WebSockets**: Native WebSocket (no Socket.io)
- **Module System**: ES Modules (`"type": "module"`) for modern JavaScript standards

## Code Patterns

### File Structure
- `client/` - React frontend with path alias `@/*`
- `server/` - Node.js backend with path alias `@server/*`
- `shared/` - Common types with path alias `@shared/*`

### Required Patterns
- **Services**: Business logic in `server/services/` with `Service` suffix
- **Middleware**: Express middleware in `server/middleware/`
- **Types**: Shared TypeScript interfaces in `shared/`
- **Components**: Functional React components only

### Error Handling
```typescript
// Always wrap async operations
try {
  const result = await someAsyncOperation();
  return result;
} catch (error) {
  logger.error('Operation failed', { error });
  throw error;
}
```

### Database Queries
```typescript
// Use parameterized queries to prevent SQL injection
const stmt = db.prepare('SELECT * FROM users WHERE id = ?');
const user = stmt.get(userId);
```

## Security Requirements

### Input Validation
- **All inputs** must use Zod schemas for validation
- **Never trust** client-side data without server validation

### Encryption & Auth
- **AES-256** for message encryption at rest
- **JWT tokens** for stateless authentication
- **Rate limiting** on all API endpoints

### Database Security
- Use prepared statements for all queries
- Encrypt sensitive data before storage
- Implement proper access controls

## Development Standards

### TypeScript with ES Modules
- Use strict typing where possible
- Prefer `unknown` over `any`
- Export types from appropriate modules
- **File extensions**: Use `.js` extensions for relative imports (TypeScript compiles to JS)
- **Import types**: Use `import type` for type-only imports to optimize bundles
- **Named exports**: Prefer named exports over default exports for better tree-shaking

### Imports & Exports (ES Modules)
```typescript
// Use path aliases with ES module syntax
import { Component } from '@/components/Component';
import { ApiResponse } from '@shared/types';
import { UserService } from '@server/services/UserService';

// File extensions required for relative imports in ES modules
import { helper } from './utils/helper.js'; // .js extension for compiled TypeScript
import type { UserType } from './types/user.js'; // Type-only imports

// Named exports preferred over default exports
export { UserService, AuthService };
export type { User, Session };
```

### Logging
```typescript
// Use structured logging
logger.info('User action', { 
  userId, 
  action: 'login', 
  timestamp: new Date().toISOString() 
});
```

## Performance Guidelines

### Database
- Use connection pooling for SQLite
- Implement proper indexing strategies
- Cache frequently accessed data

### Frontend
- Lazy load components and routes
- Optimize bundle size with Vite
- Use React.memo for expensive components

### WebSocket
- Implement connection management
- Handle reconnection logic
- Rate limit message frequency