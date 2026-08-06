---
inclusion: always
---

# VYRA Product Rules & Business Logic

VYRA is an AI-powered creator platform for encrypted real-time messaging between creators and fans, with monetization tools and cyberpunk aesthetics.

## Core Business Rules

### User Roles & Access Control
```typescript
type UserRole = 'creator' | 'fan';
type FanTier = 'standard' | 'premium' | 'vip';

// Tier-based permissions (enforce server-side)
const tierLimits = {
  standard: { messagesPerDay: 10, mediaAccess: false },
  premium: { messagesPerDay: 50, mediaAccess: true },
  vip: { messagesPerDay: -1, mediaAccess: true, prioritySupport: true }
};
```

### Authentication & Security
- **JWT**: 24-hour expiration with refresh token rotation
- **Rate limiting**: 5 failed auth attempts = 15-minute lockout
- **Encryption**: AES-256-GCM for all message content (REQUIRED)
- **Key management**: Separate keys per conversation, rotate every 30 days
- **Input validation**: All inputs must use Zod schemas server-side

## Message System Constraints

### Message Validation Rules
- Max length: 5,000 characters
- Allowed types: text, image, video, audio
- Rate limit: 60 messages/minute per user
- All content MUST be encrypted before database storage

### WebSocket Requirements
- Max 3 concurrent connections per user
- Heartbeat every 30 seconds with auto-reconnect
- Message queue during disconnection (max 100 messages)
- Connection cleanup after 5 minutes idle

## Monetization Rules

### Payment Processing (Stripe Only)
- Minimum tip: $1.00 USD
- Maximum transaction: $500.00 USD
- Platform fee: 15% of all transactions
- Creator payout threshold: $50.00 USD
- All transactions must be logged and auditable

### Financial Data Handling
- Never store raw payment data
- Log all financial transactions with audit trail
- Implement proper error handling for payment failures
- Validate all amounts server-side before processing

## AI Feature Constraints

### Privacy & Data Protection
- **NEVER** send raw message content to external APIs
- Use local sentiment analysis models only
- Anonymize all data for AI training (remove PII)
- Allow per-conversation opt-out of AI features
- Cache AI suggestions for max 5 minutes

### AI Response Generation
- Generate exactly 3 contextual response options
- Track suggestion acceptance rate for optimization
- Allow creators to customize AI personality settings
- Provide sentiment analysis and engagement suggestions

## UI/UX Standards

### Cyberpunk Theme (Required Colors)
```css
:root {
  --neon-blue: #00f5ff;
  --neon-purple: #bf00ff;
  --neon-green: #39ff14;
  --error-red: #ff073a;
  --dark-bg: #0a0a0a;
  --card-bg: #1a1a1a;
}
```

### Component Requirements
- All interactive elements need hover states with glow effects
- Loading states use animated neon borders
- Mobile-first responsive design (breakpoints: 768px, 1024px)
- Minimum contrast ratio: 4.5:1 for accessibility
- Keyboard navigation support for all features

## Rate Limiting (Enforce Server-Side)
- API endpoints: 100 requests/minute per IP
- Message sending: 60 messages/minute per user
- File uploads: 10 uploads/hour per user
- Authentication: 5 attempts/15 minutes per IP

## Database & Performance Rules

### Query Standards
- Use prepared statements for all queries (prevent SQL injection)
- Index all foreign key columns
- Implement pagination (max 50 items per request)
- Cache frequently accessed data (15-minute TTL)

### WebSocket Performance
- Support max 10,000 concurrent connections
- Message delivery SLA: <100ms
- Implement connection pooling
- Auto-cleanup idle connections

## Security Implementation Patterns

### Required Security Checks
```typescript
// Always validate inputs server-side
const validateInput = (input: unknown, schema: ZodSchema) => {
  const result = schema.safeParse(input);
  if (!result.success) throw new ValidationError(result.error);
  return result.data;
};

// Encrypt sensitive data before storage
const encryptSensitiveData = async (data: string, userId: string) => {
  const key = await getOrCreateUserKey(userId);
  return encrypt(data, key);
};
```

### Critical Security Rules
- Check user permissions server-side for all operations
- Never trust client-side data without validation
- Encrypt sensitive data before database storage
- Don't leak sensitive information in error messages
- Implement proper session management and cleanup

## Development Guidelines

### Feature Implementation Priority
1. Core messaging with encryption
2. User authentication with JWT
3. Payment integration via Stripe
4. AI suggestions with local models
5. Media sharing with encryption

### Code Quality Requirements
- Unit tests with >80% coverage for all features
- Integration tests for payment and auth flows
- Security audits for encryption implementations
- Performance testing for WebSocket connections
- Accessibility testing for UI components