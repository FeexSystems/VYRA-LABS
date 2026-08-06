# VYRA Testing Documentation

This document provides comprehensive information about the testing framework and test suites implemented for the VYRA chat system.

## Test Architecture Overview

The VYRA project implements a four-tier testing strategy:

### 1. Unit Tests (`tests/unit/`)
- **Purpose**: Test individual components, services, and utilities in isolation
- **Scope**: Individual functions, classes, and methods
- **Coverage**: Services, utilities, components, database operations
- **Framework**: Vitest with comprehensive mocking

### 2. Integration Tests (`tests/integration/`)
- **Purpose**: Test interactions between multiple components
- **Scope**: WebSocket flows, service integrations, database interactions
- **Coverage**: Real-time messaging, media handling, AI integration
- **Framework**: Vitest with real service instances

### 3. End-to-End Tests (`tests/e2e/`)
- **Purpose**: Test complete user workflows and system behavior
- **Scope**: Full user journeys, security scenarios, performance validation
- **Coverage**: Authentication, messaging, media sharing, AI assistance
- **Framework**: Custom E2E runner with Vitest

### 4. Performance Tests (`tests/performance/`)
- **Purpose**: Test system performance, scalability, and resource utilization
- **Scope**: Concurrent connections, load testing, memory management
- **Coverage**: WebSocket performance, database optimization, stress testing
- **Framework**: Custom benchmarking suite with Vitest

## Quick Start

### Run All Tests
```bash
npm run test:all
```

### Run Specific Test Types
```bash
# Unit tests only
npm run test:unit

# Integration tests only
npm run test:integration

# End-to-end tests only
npm run test:e2e

# Performance tests only
npm run test:performance

# Performance tests with verbose output
npm run test:performance:verbose

# Concurrent connection performance tests
npm run test:performance:concurrent

# Performance benchmark suite
npm run test:performance:suite

# With verbose output
npm run test:e2e:verbose

# With coverage report
npm run test:e2e:coverage
```

### Watch Mode (Development)
```bash
npm run test:watch
```

### Coverage Reports
```bash
npm run test:coverage
```

## Test Suite Details

### Unit Tests

#### Service Tests
- **ChatService** (`tests/unit/services/chat.service.test.ts`)
  - Message sending and receiving
  - Conversation management
  - Pagination and search
  - Error handling and validation

- **MediaService** (`tests/unit/services/media.service.test.ts`)
  - File upload and validation
  - Image processing and compression
  - Thumbnail generation
  - Security scanning and sanitization

- **AIAssistant** (`tests/unit/services/aiAssistant.service.test.ts`)
  - Message analysis and sentiment detection
  - Response suggestion generation
  - Monetization opportunity identification
  - OpenAI API integration

#### Utility Tests
- **MessageCache** (`tests/unit/utils/messageCache.test.ts`)
  - Caching functionality and TTL management
  - Search capabilities
  - Import/export operations
  - Performance optimization

- **DatabaseOptimizer** (`tests/unit/database/optimizer.test.ts`)
  - Index creation and management
  - Query performance optimization
  - Health monitoring

### Integration Tests

#### WebSocket Integration (`tests/integration/websocket/message-flow.test.ts`)
- Connection management and authentication
- Real-time message broadcasting
- Typing indicators and presence updates
- Message delivery confirmation
- Rate limiting and error handling
- Concurrent connections and reconnection scenarios

#### Service Integration (`tests/integration/services/chat-integration.test.ts`)
- End-to-end message flow
- Media attachment integration
- AI analysis integration
- Database persistence
- Performance under load

### End-to-End Tests

#### Complete User Flow (`tests/e2e/chat-complete-flow.test.ts`)
- **User Journey Testing**
  - Creator-to-fan conversation flows
  - Message delivery and read receipts
  - Multi-user concurrent scenarios
  - Message persistence and caching

- **Media Sharing Workflow**
  - File upload via HTTP API
  - Media message delivery via WebSocket
  - Error handling for invalid uploads
  - Thumbnail generation and display

- **AI Assistant Integration**
  - Message analysis for sentiment and intent
  - Response suggestion generation
  - Monetization opportunity detection
  - Context-aware recommendations

#### Security and Authorization (`tests/e2e/chat-security.test.ts`)
- **Authentication Security**
  - Token validation and expiration
  - Malformed token rejection
  - Unauthorized access prevention

- **Authorization and Access Control**
  - Conversation access restrictions
  - Tier-based message limits
  - Fan-to-fan blocking

- **Input Validation**
  - XSS and SQL injection prevention
  - Content sanitization
  - Message length limits

- **Session Management**
  - Multiple session handling
  - Connection cleanup
  - Performance under concurrent load

### Performance Tests

#### Concurrent Connection Tests (`tests/performance/concurrent-connections.test.ts`)
- **Baseline Performance Testing**
  - Single connection establishment benchmarks
  - Message sending latency validation
  - Memory usage stability verification

- **Concurrent Connection Handling**
  - 50-100 simultaneous connection tests
  - Rapid connection/disconnection cycles
  - Connection cleanup efficiency

- **Message Throughput Testing**
  - High-volume message processing
  - Multi-user concurrent messaging
  - Broadcasting performance validation

- **Stress Testing**
  - Gradual load increase scenarios
  - System limit identification
  - Failure mode analysis

- **Resource Utilization Monitoring**
  - Memory leak detection
  - CPU usage optimization
  - Database performance under load

#### Performance Benchmarking Suite (`tests/performance/performance-suite.test.ts`)
- **WebSocket Connection Performance**
  - Connection establishment benchmarks
  - Concurrent connection stress tests
  - Authentication overhead measurement

- **Message Processing Benchmarks**
  - Message sending latency testing
  - Broadcasting efficiency validation
  - Real-time delivery performance

- **Database Performance Testing**
  - Query optimization validation
  - Pagination performance benchmarks
  - Concurrent operation handling

- **Memory and Resource Management**
  - Memory stability over time
  - Connection cleanup efficiency
  - Resource leak detection

#### Benchmarking Framework (`tests/performance/benchmark.ts`)
- **Performance Measurement Tools**
  - Latency and throughput benchmarking
  - Memory usage monitoring
  - Error rate tracking
  - Percentile calculations (50th, 95th, 99th)

- **Load Testing Profiles**
  - Standard load scenarios
  - Stress testing configurations
  - Gradual ramp-up patterns
  - Peak load simulation

- **Reporting and Analysis**
  - Performance threshold validation
  - Baseline comparison tools
  - Detailed metric collection
  - Performance regression detection

## Test Configuration

### Environment Variables
```bash
NODE_ENV=test
JWT_SECRET=test-jwt-secret
OPENAI_API_KEY=test-openai-key
DATABASE_URL=:memory:
LOG_LEVEL=error
UPLOAD_DIR=./test-uploads
```

### Vitest Configuration (`vitest.config.ts`)
- Global test setup and teardown
- Coverage thresholds (80% minimum)
- Custom matchers and utilities
- Timeout configurations
- Parallel execution settings

### Test Setup (`tests/setup.ts`)
- Global mocks and utilities
- Custom assertions
- Browser API mocking
- Performance monitoring tools

## Writing Tests

### Unit Test Example
```typescript
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { ChatService } from '../../../server/services/chat.js';

describe('ChatService', () => {
  let chatService: ChatService;
  let mockDatabase: any;

  beforeEach(() => {
    mockDatabase = { prepare: vi.fn() };
    chatService = new ChatService(mockDatabase);
  });

  it('should send message successfully', async () => {
    // Test implementation
    const result = await chatService.sendMessage('conv_123', 'user_123', 'Hello');
    expect(result).toBeDefined();
  });
});
```

### Integration Test Example
```typescript
import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import WebSocket from 'ws';

describe('WebSocket Integration', () => {
  let server: any;
  let client: WebSocket;

  beforeAll(async () => {
    // Setup test server
  });

  afterAll(async () => {
    // Cleanup
  });

  it('should handle real-time messaging', async () => {
    // Test WebSocket communication
  });
});
```

### E2E Test Example
```typescript
import { describe, it, expect } from 'vitest';
import { TestUtils, TestDataFactory } from './helpers.js';

describe('Complete User Journey', () => {
  it('should complete full conversation flow', async () => {
    const creator = TestDataFactory.createTestUser({ role: 'creator' });
    const fan = TestDataFactory.createTestUser({ role: 'fan' });
    
    // Test complete workflow
  });
});
```

## Test Data Management

### Test Data Factory
The `TestDataFactory` class provides standardized test data creation:

```typescript
// Create test users
const creator = TestDataFactory.createTestUser({
  role: 'creator',
  tier: 'premium'
});

// Create test conversations
const conversation = TestDataFactory.createTestConversation(
  creator.id, 
  fan.id
);

// Create test messages
const message = TestDataFactory.createTestMessage(
  conversation.id,
  creator.id,
  { content: 'Test message' }
);

// Create test media files
const mediaFile = TestDataFactory.createTestMediaFile({
  mimetype: 'image/jpeg',
  size: 1024 * 100
});
```

### Database Setup
Tests use in-memory SQLite databases for isolation:
- Each test suite gets a fresh database
- Automatic schema creation and cleanup
- Realistic data relationships
- Performance optimized for testing

## Coverage Reports

### Coverage Thresholds
- **Branches**: 80% minimum
- **Functions**: 80% minimum
- **Lines**: 80% minimum
- **Statements**: 80% minimum

### Generating Reports
```bash
# Text coverage report
npm run test:coverage

# HTML coverage report
npm run test:coverage -- --reporter=html

# JSON coverage report
npm run test:coverage -- --reporter=json
```

### Coverage Exclusions
- `node_modules/`
- `tests/` directory
- `dist/` build output
- Type definition files (`*.d.ts`)
- Configuration files

## Performance Testing

### Metrics Monitored
- **Latency Metrics**
  - WebSocket connection establishment time
  - Message delivery latency
  - Database query response time
  - API endpoint response time

- **Throughput Metrics**
  - Messages per second
  - Concurrent connections supported
  - Database operations per second
  - File upload bandwidth

- **Resource Utilization**
  - Memory usage patterns
  - CPU utilization
  - Database connection pooling
  - Network bandwidth consumption

- **Reliability Metrics**
  - Connection success rate
  - Message delivery success rate
  - Error rates and failure modes
  - System recovery time

### Performance Thresholds
- **Connection Performance**
  - WebSocket connection: < 100ms average, < 500ms max
  - Concurrent connections: > 100 simultaneous
  - Connection success rate: > 95%

- **Message Performance**
  - Message delivery: < 50ms average, < 200ms 95th percentile
  - Message throughput: > 100 messages/second
  - Broadcasting latency: < 100ms to all recipients

- **Database Performance**
  - Query response: < 10ms average, < 100ms max
  - Pagination queries: < 25ms average
  - Database operations: > 500 ops/second

- **Memory and Resources**
  - Memory growth: < 10MB per 100 connections
  - Memory stability: No significant leaks over time
  - CPU usage: < 80% under normal load

### Running Performance Tests
```bash
# Run all performance tests
npm run test:performance

# Run with detailed output
npm run test:performance:verbose

# Run specific performance test suites
npm run test:performance:concurrent
npm run test:performance:suite

# Run with custom thresholds
PERFORMANCE_TIMEOUT=60000 npm run test:performance

# Generate performance reports
npm run test:performance > performance-results.log
```

### Performance Test Configuration
```bash
# Environment variables for performance testing
NODE_ENV=test
PERFORMANCE_TEST=true
MAX_CONCURRENT_CONNECTIONS=200
MESSAGE_THROUGHPUT_TARGET=150
MEMORY_LIMIT_MB=500
TEST_DURATION_MS=60000
```

### Performance Monitoring in CI/CD
```yaml
# Example GitHub Actions performance testing
name: Performance Tests
on: [push, pull_request]

jobs:
  performance:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run test:performance
      - name: Upload performance report
        uses: actions/upload-artifact@v3
        with:
          name: performance-report
          path: performance-test-report.json
```

## Debugging Tests

### Debug Mode
```bash
# Run with debug output
NODE_ENV=test DEBUG=* npm run test:e2e

# Run specific test file
npx vitest run tests/unit/services/chat.service.test.ts

# Run in watch mode with UI
npx vitest --ui
```

### Common Issues

#### Database Connection Issues
```bash
# Ensure database is properly initialized
rm -rf ./test-uploads ./test-logs
npm run test:unit
```

#### WebSocket Connection Timeouts
```bash
# Increase timeout for slow environments
TEST_TIMEOUT=10000 npm run test:e2e
```

#### Memory Issues
```bash
# Run tests with more memory
NODE_OPTIONS="--max-old-space-size=4096" npm run test:all
```

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Test Suite
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run test:all
      - run: npm run test:coverage
      - uses: codecov/codecov-action@v3
```

### Test Reports
- JSON test results: `test-results-e2e.json`
- Coverage reports: `coverage/` directory
- Performance metrics: Console output
- Error logs: `test-logs/` directory

## Best Practices

### Writing Maintainable Tests
1. **Use descriptive test names** that explain what is being tested
2. **Follow AAA pattern**: Arrange, Act, Assert
3. **Mock external dependencies** to ensure test isolation
4. **Use factories** for consistent test data generation
5. **Clean up after tests** to prevent interference

### Test Organization
1. **Group related tests** using nested `describe` blocks
2. **Use consistent naming** for test files and functions
3. **Document complex test scenarios** with comments
4. **Keep tests focused** on single functionality
5. **Avoid test interdependencies**

### Performance Considerations
1. **Use parallel execution** where possible
2. **Mock expensive operations** in unit tests
3. **Optimize database queries** in integration tests
4. **Monitor test execution time** and optimize slow tests
5. **Use memory-efficient test data**

## Troubleshooting

### Common Test Failures

#### Timeout Errors
- Increase timeout values in test configuration
- Check for unresolved promises in async tests
- Verify WebSocket connections are properly closed

#### Database Errors
- Ensure database schema is properly initialized
- Check for conflicting test data
- Verify database connections are cleaned up

#### Memory Leaks
- Check for unclosed file handles
- Verify WebSocket connections are terminated
- Monitor test database cleanup

#### Flaky Tests
- Add proper wait conditions for async operations
- Use deterministic test data
- Avoid time-dependent assertions

### Getting Help
- Check test logs in `test-logs/` directory
- Review coverage reports for missing test areas
- Run tests with verbose output for detailed information
- Consult the development team for complex scenarios

## Contributing

### Adding New Tests
1. Follow existing test structure and patterns
2. Add appropriate mocks and setup
3. Ensure tests pass in isolation
4. Update coverage expectations if needed
5. Document complex test scenarios

### Test Review Checklist
- [ ] Tests cover all new functionality
- [ ] Error scenarios are tested
- [ ] Mocks are properly configured
- [ ] Tests are deterministic and reliable
- [ ] Performance implications are considered
- [ ] Documentation is updated

---

For more information about specific test implementations, refer to the individual test files and their inline documentation.
