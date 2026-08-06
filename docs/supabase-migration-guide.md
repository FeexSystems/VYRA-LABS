# VYRA Supabase Migration Guide

## Overview

This guide documents the migration of VYRA's database infrastructure from Neon PostgreSQL to Supabase PostgreSQL, completed on January 26, 2026.

## Migration Details

### Previous Setup (Neon)
- **Provider**: Neon PostgreSQL
- **Connection**: `postgresql://neondb_owner:npg_reVsB01xwUpG@ep-lively-leaf-aexsawiu-pooler.c-2.us-east-2.aws.neon.tech/neondb`
- **Features**: Serverless PostgreSQL with branching

### New Setup (Supabase)
- **Provider**: Supabase PostgreSQL
- **Connection**: `postgresql://postgres:[password]@db.jenzfdxnlcstvuqjchsy.supabase.co:5432/postgres`
- **Project ID**: `jenzfdxnlcstvuqjchsy`
- **Features**: PostgreSQL + Real-time + Auth + Storage + Edge Functions

## Migration Benefits

### Enhanced Real-time Capabilities
- **Built-in WebSocket Support**: Native real-time subscriptions for live chat
- **Row Level Security (RLS)**: Advanced security policies for data access
- **Real-time Filters**: Subscribe to specific data changes with filters
- **Presence System**: Track online users and typing indicators

### Additional Services
- **Supabase Auth**: OAuth integrations (Google, GitHub, Discord, etc.)
- **Supabase Storage**: Secure file storage for media uploads
- **Edge Functions**: Serverless functions for AI processing
- **Dashboard**: Visual database management and monitoring

### Performance Improvements
- **Connection Pooling**: Better handling of concurrent connections
- **Global CDN**: Faster response times worldwide
- **Automatic Backups**: Point-in-time recovery capabilities
- **Performance Insights**: Query performance monitoring and optimization

## Configuration Changes

### Environment Variables
```env
# Old (Neon)
DATABASE_URL=postgresql://neondb_owner:npg_reVsB01xwUpG@ep-lively-leaf-aexsawiu-pooler.c-2.us-east-2.aws.neon.tech/neondb?sslmode=require&channel_binding=require

# New (Supabase) - Production Configuration
DATABASE_URL=postgresql://postgres:Bushfexerthunder@db.jenzfdxnlcstvuqjchsy.supabase.co:5432/postgres

# Supabase Configuration - Production Ready
SUPABASE_URL=https://jenzfdxnlcstvuqjchsy.supabase.co
SUPABASE_JWT_SECRET=QBMdP1gaSIa+XYOIB/Z1d3qU6i5XZDnhDtYOy864Nqt6DEzq2vwKCuGFYOeMEcoGbBiDoStegAMIrDQjG2TI/Q==
SUPABASE_SERVICE_ROLE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImplbnpmZHhubGNzdHZ1cWpjaHN5Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc2OTM5NzUwMiwiZXhwIjoyMDg0OTczNTAyfQ.CrsXq1qInDhqBB65r54mI1Azgxd8Pt4mTDG4cdPw1i0
VITE_SUPABASE_URL=https://jenzfdxnlcstvuqjchsy.supabase.co
VITE_SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImplbnpmZHhubGNzdHZ1cWpjaHN5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjkzOTc1MDIsImV4cCI6MjA4NDk3MzUwMn0.VFj1PEnjfJIe8mMo3jW5orXGoXc41Tzkar29d59prPY
```

### Database Connection Updates
The existing `DatabasePool` class in `server/database/connection-fixed.ts` is compatible with Supabase PostgreSQL:

```typescript
// SSL configuration for Supabase
ssl: this.config.ssl !== false ? { rejectUnauthorized: false } : false
```

## Schema Compatibility

### Existing Schema
The current VYRA schema (`server/database/schema.sql`) is fully compatible with Supabase PostgreSQL:

- ✅ UUID support with `uuid_generate_v4()`
- ✅ JSONB columns for AI suggestions
- ✅ Timestamp with timezone support
- ✅ Triggers and functions
- ✅ Indexes and constraints

### Supabase-Specific Enhancements
```sql
-- Enable Row Level Security (RLS)
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE conversations ENABLE ROW LEVEL SECURITY;
ALTER TABLE messages ENABLE ROW LEVEL SECURITY;

-- Example RLS Policy for user data
CREATE POLICY "Users can view own data" ON users
  FOR SELECT USING (auth.uid() = id);

-- Real-time subscriptions
ALTER PUBLICATION supabase_realtime ADD TABLE messages;
ALTER PUBLICATION supabase_realtime ADD TABLE conversations;
```

## Future Integration Opportunities

### Real-time Chat Features
```typescript
// Subscribe to new messages in a conversation
const subscription = supabase
  .channel('messages')
  .on('postgres_changes', {
    event: 'INSERT',
    schema: 'public',
    table: 'messages',
    filter: `conversation_id=eq.${conversationId}`
  }, (payload) => {
    // Handle new message
    handleNewMessage(payload.new);
  })
  .subscribe();
```

### Authentication Integration
```typescript
// Complement JWT with Supabase Auth for OAuth
const { data, error } = await supabase.auth.signInWithOAuth({
  provider: 'google',
  options: {
    redirectTo: 'https://vyra.app/auth/callback'
  }
});
```

### File Storage Integration
```typescript
// Upload media files to Supabase Storage
const { data, error } = await supabase.storage
  .from('media')
  .upload(`${userId}/${fileName}`, file, {
    cacheControl: '3600',
    upsert: false
  });
```

## Migration Verification

### Connection Test
```bash
# Test the new database connection
pnpm dev

# Check logs for successful connection
# Expected output: "✅ Database connection established successfully"
```

### Schema Verification
```sql
-- Verify tables exist
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' AND table_type = 'BASE TABLE';

-- Check user table structure
\d users;

-- Verify indexes
SELECT indexname, tablename FROM pg_indexes 
WHERE schemaname = 'public';
```

### Performance Testing
```typescript
// Test query performance
const startTime = Date.now();
const users = await db.query('SELECT COUNT(*) FROM users');
const duration = Date.now() - startTime;
console.log(`Query executed in ${duration}ms`);
```

## Rollback Plan

### Emergency Rollback
If issues arise, revert to Neon temporarily:

```env
# Restore Neon connection
DATABASE_URL=postgresql://neondb_owner:npg_reVsB01xwUpG@ep-lively-leaf-aexsawiu-pooler.c-2.us-east-2.aws.neon.tech/neondb?sslmode=require&channel_binding=require
```

### Data Migration
For permanent rollback, export data from Supabase:

```bash
# Export from Supabase
pg_dump "postgresql://postgres:[password]@db.jenzfdxnlcstvuqjchsy.supabase.co:5432/postgres" > vyra_backup.sql

# Import to Neon
psql "postgresql://neondb_owner:npg_reVsB01xwUpG@ep-lively-leaf-aexsawiu-pooler.c-2.us-east-2.aws.neon.tech/neondb" < vyra_backup.sql
```

## Security Considerations

### Connection Security
- ✅ SSL/TLS encryption enabled by default
- ✅ Connection string uses secure password
- ✅ Database accessible only from authorized IPs (configurable)

### Row Level Security (RLS)
- Implement RLS policies for multi-tenant security
- Ensure users can only access their own data
- Use Supabase Auth context for policy enforcement

### API Keys Management
- Store Supabase keys in environment variables
- Use service role key only for server-side operations
- Rotate keys regularly for security

## Monitoring and Maintenance

### Supabase Dashboard
- **URL**: https://supabase.com/dashboard/project/jenzfdxnlcstvuqjchsy
- **Features**: Query editor, table editor, logs, metrics
- **Monitoring**: Real-time performance metrics and alerts

### Performance Monitoring
```typescript
// Monitor connection pool stats
const stats = dbPool.getPoolStats();
console.log('Database utilization:', stats.utilization + '%');

// Track query performance
const metrics = dbPool.getAverageQueryPerformance();
console.log('Average query time:', metrics.averageDuration + 'ms');
```

### Backup Strategy
- **Automatic Backups**: Supabase provides automatic daily backups
- **Point-in-time Recovery**: Available for the last 7 days
- **Manual Backups**: Export critical data before major changes

## Next Steps

### Immediate Actions
1. ✅ Update environment variables
2. ✅ Test database connectivity
3. ⏳ Replace connection.ts with connection-fixed.ts
4. ⏳ Verify all authentication endpoints work
5. ⏳ Run integration tests

### Future Enhancements
1. **Real-time Chat**: Implement Supabase real-time subscriptions
2. **OAuth Integration**: Add social login options
3. **File Storage**: Migrate media uploads to Supabase Storage
4. **Edge Functions**: Move AI processing to Supabase Edge Functions
5. **RLS Policies**: Implement comprehensive row-level security

## Conclusion

The migration to Supabase PostgreSQL provides VYRA with enhanced real-time capabilities, better performance, and access to additional services that will accelerate development of advanced features like live chat, OAuth authentication, and media storage.

The migration maintains full compatibility with existing code while opening up new possibilities for platform enhancement.