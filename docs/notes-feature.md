# Notes Feature Documentation

## Overview

The Notes feature provides users with a personal note-taking system integrated into the VYRA dashboard. Users can create, manage, and organize their notes with features like tagging, privacy controls, and search functionality.

## Features

### 1. Note Creation and Management
- Create notes with titles and content
- Add tags for organization and categorization
- Set privacy status (private or public)
- Edit and delete existing notes

### 2. Organization
- Tag-based organization system
- Automatic timestamp tracking (created/updated)
- User-specific note isolation

### 3. Search and Filter
- Full-text search across note titles and content
- Tag-based filtering
- Private/public note visibility controls

### 4. Dashboard Integration
- Dedicated "Notes" tab in the creator dashboard
- Responsive UI with modern design
- Intuitive form-based note creation

## Technical Architecture

### Backend
- **Database Table**: `notes` table with columns for id, title, content, user_id, tags, is_private, created_at, updated_at
- **Service Layer**: [NotesService](file://c:\Users\ENGR%20BILLI\VYRA-20\server\services\notes.ts#L6-L287) class providing CRUD operations and business logic
- **API Routes**: RESTful endpoints under `/api/notes` for frontend integration
- **Database Migrations**: SQL migration file to create and manage the notes table

### Frontend
- **Dashboard Component**: [NotesDashboard](file://c:\Users\ENGR%20BILLI\VYRA-20\client\components\dashboard\notes-dashboard.tsx#L11-L360) component integrated into the main dashboard
- **UI Components**: Custom form and table components for note management
- **State Management**: Direct API calls with local state management

## Database Compatibility

The notes feature is designed to work with both SQLite (for local development) and PostgreSQL (for production with Supabase).

### SQLite Schema
```sql
CREATE TABLE notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT,
    user_id TEXT REFERENCES users(id) ON DELETE CASCADE,
    tags TEXT,  -- JSON string representation of array
    is_private INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### PostgreSQL Schema
```sql
CREATE TABLE notes (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title TEXT NOT NULL,
    content TEXT,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    tags TEXT[],  -- Native array type
    is_private BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

The service layer automatically detects the database type and uses appropriate data conversion functions.

## API Endpoints

### GET `/api/notes`
Retrieve notes with optional filtering and search.

**Query Parameters:**
- `limit` (number): Maximum number of notes to return (default: 50)
- `offset` (number): Offset for pagination (default: 0)
- `search` (string): Search term for full-text search
- `tags` (string): Comma-separated list of tags to filter by

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Note Title",
      "content": "Note content",
      "tags": ["tag1", "tag2"],
      "is_private": true,
      "created_at": "2023-01-01T00:00:00Z",
      "updated_at": "2023-01-01T00:00:00Z"
    }
  ]
}
```

### POST `/api/notes`
Create a new note.

**Request Body:**
```json
{
  "title": "Note Title",
  "content": "Note content",
  "tags": ["tag1", "tag2"],
  "is_private": true
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Note Title",
    "content": "Note content",
    "tags": ["tag1", "tag2"],
    "is_private": true,
    "created_at": "2023-01-01T00:00:00Z",
    "updated_at": "2023-01-01T00:00:00Z"
  }
}
```

### GET `/api/notes/:id`
Retrieve a specific note by ID.

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Note Title",
    "content": "Note content",
    "tags": ["tag1", "tag2"],
    "is_private": true,
    "created_at": "2023-01-01T00:00:00Z",
    "updated_at": "2023-01-01T00:00:00Z"
  }
}
```

### PUT `/api/notes/:id`
Update an existing note.

**Request Body:**
```json
{
  "title": "Updated Title",
  "content": "Updated content",
  "tags": ["newtag1", "newtag2"],
  "is_private": false
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Updated Title",
    "content": "Updated content",
    "tags": ["newtag1", "newtag2"],
    "is_private": false,
    "created_at": "2023-01-01T00:00:00Z",
    "updated_at": "2023-01-02T00:00:00Z"
  }
}
```

### DELETE `/api/notes/:id`
Delete a note.

**Response:**
```json
{
  "success": true,
  "message": "Note deleted successfully"
}
```

## Database Schema

### SQLite Version
```sql
CREATE TABLE IF NOT EXISTS notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT,
    user_id TEXT REFERENCES users(id) ON DELETE CASCADE,
    tags TEXT,
    is_private INTEGER DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_notes_user_id ON notes(user_id);
CREATE INDEX IF NOT EXISTS idx_notes_created_at ON notes(created_at DESC);

-- Triggers
CREATE TRIGGER IF NOT EXISTS update_notes_updated_at 
    AFTER UPDATE ON notes
    BEGIN
        UPDATE notes SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id;
    END;
```

### PostgreSQL Version
```sql
CREATE TABLE IF NOT EXISTS notes (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title TEXT NOT NULL,
    content TEXT,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    tags TEXT[],
    is_private BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_notes_user_id ON notes(user_id);
CREATE INDEX IF NOT EXISTS idx_notes_created_at ON notes(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_notes_tags ON notes USING GIN(tags);

-- Row Level Security
ALTER TABLE notes ENABLE ROW LEVEL SECURITY;

-- RLS Policies
CREATE POLICY "Users can view their own notes" ON notes
    FOR SELECT USING (user_id = auth.uid());

CREATE POLICY "Users can create their own notes" ON notes
    FOR INSERT WITH CHECK (user_id = auth.uid());

CREATE POLICY "Users can update their own notes" ON notes
    FOR UPDATE USING (user_id = auth.uid());

CREATE POLICY "Users can delete their own notes" ON notes
    FOR DELETE USING (user_id = auth.uid());

-- Triggers
CREATE TRIGGER update_notes_updated_at 
    BEFORE UPDATE ON notes
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
```

## Security

### Authentication
- All note operations require authentication via JWT tokens
- User ID is extracted from the authentication token
- Notes are isolated per user with RLS policies

### Authorization
- Users can only access their own notes
- Public notes (with NULL user_id) are accessible to all users
- Private notes are only accessible to their owner

### Data Protection
- Content is stored as plain text (in this implementation)
- In production, consider encrypting sensitive note content
- User data is properly isolated in the database

## Testing

### Unit Tests
- [NotesService](file://c:\Users\ENGR%20BILLI\VYRA-20\server\services\notes.ts#L6-L287) functionality tests
- Database migration tests
- Frontend component tests

### Integration Tests
- API endpoint tests
- End-to-end workflow tests
- PostgreSQL compatibility tests

## Future Enhancements

1. **Rich Text Editing**: Add rich text formatting capabilities
2. **Note Sharing**: Allow sharing notes with other users
3. **Attachments**: Support file attachments to notes
4. **Notebooks**: Group notes into notebooks or collections
5. **Collaboration**: Real-time collaborative note editing
6. **AI Integration**: AI-powered note organization and suggestions
7. **Export**: Export notes to various formats (PDF, Markdown, etc.)

## Usage Examples

### Creating a Note
```javascript
// Frontend example
const createNote = async (noteData) => {
  const response = await fetch('/api/notes', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${authToken}`
    },
    body: JSON.stringify(noteData)
  });
  
  const result = await response.json();
  return result.data;
};
```

### Searching Notes
```javascript
// Frontend example
const searchNotes = async (query) => {
  const response = await fetch(`/api/notes?search=${encodeURIComponent(query)}`, {
    headers: {
      'Authorization': `Bearer ${authToken}`
    }
  });
  
  const result = await response.json();
  return result.data;
};
```

## Error Handling

The API returns standardized error responses:

```json
{
  "success": false,
  "error": "Error message"
}
```

Common error scenarios:
- 400: Bad request (missing required fields)
- 401: Unauthorized (missing or invalid authentication)
- 403: Forbidden (attempting to access another user's note)
- 404: Not found (note with specified ID doesn't exist)
- 500: Internal server error (unexpected server issues)