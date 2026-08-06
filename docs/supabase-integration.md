# Supabase Integration for Notes Feature

This document explains how to use the Supabase integration with the notes feature in the VYRA platform.

## Overview

The VYRA platform now supports both SQLite (for local development) and Supabase PostgreSQL (for production) databases. The notes feature automatically detects which database to use based on your environment configuration.

## Prerequisites

1. A Supabase account and project
2. Supabase project URL and API key
3. Environment variables properly configured

## Environment Configuration

To enable Supabase integration, you need to set the following environment variables in your `.env` file:

```env
# Supabase Configuration
SUPABASE_URL=your-supabase-project-url
SUPABASE_KEY=your-supabase-anon-key
```

If these variables are present, the application will automatically use Supabase for the notes feature. If they're missing, it will fall back to SQLite.

## How It Works

The notes feature uses a dual-database approach:

1. **Development**: Uses SQLite for local development
2. **Production**: Uses Supabase PostgreSQL for production deployments

The application automatically detects which database to use based on the presence of Supabase environment variables.

## Database Schema

The notes table schema is compatible with both SQLite and PostgreSQL:

```sql
CREATE TABLE notes (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  title TEXT NOT NULL,
  content TEXT,
  user_id UUID REFERENCES users(id),
  tags TEXT[], -- PostgreSQL array type
  is_private BOOLEAN DEFAULT true,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

For SQLite, some adaptations are made:
- `tags` is stored as a JSON string
- `is_private` is stored as an integer (0 or 1)
- `created_at` and `updated_at` are stored as ISO date strings

## Row Level Security (RLS)

The notes table includes Row Level Security policies to ensure data isolation:

```sql
-- Enable RLS
ALTER TABLE notes ENABLE ROW LEVEL SECURITY;

-- Users can only view their own notes
CREATE POLICY "Users can view their own notes" ON notes
  FOR SELECT USING (user_id = auth.uid());

-- Users can only insert their own notes
CREATE POLICY "Users can insert their own notes" ON notes
  FOR INSERT WITH CHECK (user_id = auth.uid());

-- Users can only update their own notes
CREATE POLICY "Users can update their own notes" ON notes
  FOR UPDATE USING (user_id = auth.uid());

-- Users can only delete their own notes
CREATE POLICY "Users can delete their own notes" ON notes
  FOR DELETE USING (user_id = auth.uid());
```

## API Endpoints

The notes feature provides the following REST API endpoints:

### Get Notes
```
GET /api/notes
```
Parameters:
- `limit` (optional): Number of notes to return (default: 50)
- `offset` (optional): Number of notes to skip (default: 0)
- `search` (optional): Search query for note titles and content
- `tags` (optional): Filter notes by tags

### Create Note
```
POST /api/notes
```
Body:
```json
{
  "title": "Note Title",
  "content": "Note content",
  "tags": ["tag1", "tag2"],
  "is_private": true
}
```

### Get Note by ID
```
GET /api/notes/:id
```

### Update Note
```
PUT /api/notes/:id
```
Body:
```json
{
  "title": "Updated Title",
  "content": "Updated content",
  "tags": ["tag1", "tag3"],
  "is_private": false
}
```

### Delete Note
```
DELETE /api/notes/:id
```

### Get Note Statistics
```
GET /api/notes/user/stats
```

## Testing

To test the Supabase integration:

1. Ensure your environment variables are set correctly
2. Run the test script:
   ```bash
   npm run test:supabase
   ```

3. Run the full test suite:
   ```bash
   npm test
   ```

## Troubleshooting

### Common Issues

1. **Missing Environment Variables**: Ensure `SUPABASE_URL` and `SUPABASE_KEY` are set in your `.env` file.

2. **Database Connection Errors**: Verify your Supabase project URL and API key are correct.

3. **RLS Policy Violations**: Ensure your Row Level Security policies are properly configured.

4. **Schema Mismatches**: Make sure your notes table schema matches the expected structure.

### Debugging Tips

1. Check the Supabase dashboard for any error logs
2. Verify that the notes table exists in your database
3. Test your RLS policies using the Supabase SQL editor
4. Use the Supabase API explorer to test endpoints directly

## Migration from SQLite to Supabase

To migrate existing data from SQLite to Supabase:

1. Export your SQLite notes data
2. Transform the data to match the Supabase schema
3. Import the data into your Supabase database
4. Update your environment variables to use Supabase

## Security Considerations

1. **API Keys**: Never commit your Supabase API keys to version control
2. **RLS Policies**: Always verify that your Row Level Security policies are correctly configured
3. **Data Encryption**: Consider encrypting sensitive note content
4. **Access Control**: Implement proper authentication and authorization

## Performance Optimization

1. **Indexing**: Ensure proper indexes are created on frequently queried columns
2. **Pagination**: Use limit and offset parameters to paginate large result sets
3. **Caching**: Implement caching for frequently accessed notes
4. **Connection Pooling**: Use connection pooling for high-traffic applications

## Support

For issues with the Supabase integration, please check:

1. The Supabase documentation: https://supabase.io/docs
2. The VYRA documentation: [docs/](./)
3. Open a GitHub issue if you encounter bugs