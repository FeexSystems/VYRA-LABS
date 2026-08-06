---
inclusion: always
---

# VYRA Project Structure & File Organization

## Directory Structure Rules

```
├── client/           # React frontend - all UI components and pages
├── server/           # Node.js backend - API, WebSocket, database
├── shared/           # Shared TypeScript types and utilities
├── database/         # Schema files and migration scripts
├── public/           # Static assets served by Express
├── .kiro/           # AI assistant configuration and specs
└── netlify/         # Deployment functions for Netlify
```

## Client Directory (`client/`)

```
client/
├── components/ui/    # shadcn/ui component library (DO NOT MODIFY)
├── components/       # Custom React components
├── pages/           # Route components (Index, Dashboard, NotFound)
├── hooks/           # Custom React hooks for state management
├── lib/             # Client utilities, API calls, helpers
├── contexts/        # React context providers
├── services/        # Client-side service layer
├── App.tsx          # Main app component with routing
└── global.css       # Tailwind + cyberpunk theme styles
```

### Client File Naming Rules
- **Components**: PascalCase (e.g., `ChatInterface.tsx`, `UserProfile.tsx`)
- **Pages**: PascalCase (e.g., `Dashboard.tsx`, `NotFound.tsx`)
- **Hooks**: camelCase with `use` prefix (e.g., `useChat.ts`, `useAuth.ts`)
- **Utilities**: camelCase (e.g., `apiClient.ts`, `formatters.ts`)
- **Types**: PascalCase with `.types.ts` suffix (e.g., `chat.types.ts`)

## Server Directory (`server/`)

```
server/
├── database/        # Database connection, schema, migrations
├── services/        # Business logic services (REQUIRED PATTERN)
├── websocket/       # WebSocket server and connection management
├── routes/          # Express route handlers
├── middleware/      # Express middleware functions
├── utils/           # Server utilities and helpers
├── config/          # Configuration management
├── index.ts         # Main server entry point
└── node-build.ts    # Production build configuration
```

### Server File Naming Rules
- **Services**: PascalCase with `Service` suffix (e.g., `ChatService.ts`)
- **Routes**: kebab-case (e.g., `auth-routes.ts`, `chat-routes.ts`)
- **Middleware**: camelCase (e.g., `authMiddleware.ts`, `rateLimiting.ts`)
- **Utilities**: camelCase (e.g., `encryption.ts`, `validation.ts`)

### Required Service Layer Pattern
- **ChatService**: Message and conversation management
- **AuthService**: JWT authentication and user management
- **EncryptionService**: AES-256 message encryption
- **KeyStorageService**: Secure key management
- **MediaService**: File upload and processing

## Database Schema Rules

### Table Naming (snake_case)
- `users` - Creator and fan profiles with tier system
- `conversations` - Encrypted chat sessions between creator/fan pairs
- `messages` - Encrypted message content with delivery status
- `media_attachments` - Rich media files with metadata
- `ai_suggestions` - AI-generated response suggestions

### Column Naming (snake_case)
- Primary keys: `id` (integer, auto-increment)
- Foreign keys: `{table}_id` (e.g., `user_id`, `conversation_id`)
- Timestamps: `created_at`, `updated_at` (ISO 8601 format)
- Boolean flags: `is_{property}` (e.g., `is_encrypted`, `is_read`)

### Required Relationships
- One-to-many: User → Messages
- Many-to-many: Users ↔ Conversations (creator/fan pairs)
- One-to-many: Conversation → Messages
- One-to-one: Message → MediaAttachment (optional)

## Shared Code (`shared/`)

```
shared/
├── api.ts           # API types and interfaces
├── types.ts         # Common TypeScript types
└── constants.ts     # Shared constants and enums
```

### Type Safety Rules
- Export all database types from `server/database/types.ts`
- Use Zod schemas for runtime validation
- Maintain strict type safety between client and server
- Never use `any` type - use `unknown` or proper typing

## Configuration File Locations

### Build Configuration
- `vite.config.ts` - Client build with Express integration
- `vite.config.server.ts` - Server build configuration
- `tsconfig.json` - TypeScript with path aliases

### Environment Configuration
- `.env` - Development environment variables
- `.env.example` - Template for required variables
- `server/config/environment.ts` - Environment validation

### Required Environment Variables
```
JWT_SECRET=your-secret-key
PING_MESSAGE=pong
ALLOWED_ORIGINS=http://localhost:3000
DATABASE_PATH=./data/vyra.db
```

## Path Alias Rules

Use these exact path aliases in imports:
- `@/*` - Points to `client/` directory
- `@shared/*` - Points to `shared/` directory
- `@server/*` - Points to `server/` directory (server-side only)

## File Creation Guidelines

When creating new files:
1. Follow the naming conventions above
2. Include proper TypeScript types
3. Add JSDoc comments for functions
4. Use existing patterns from similar files
5. Import shared types from appropriate locations
6. Follow the service layer pattern for business logic