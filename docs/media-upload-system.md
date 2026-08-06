# Media Upload System

A comprehensive, secure media upload system for the VYRA chat platform with real-time progress tracking, advanced security validation, and thumbnail generation.

## Features

### 🔒 Security
- **Advanced file validation** with MIME type checking and file signature verification
- **Malicious content detection** including SQL injection, XSS, and path traversal attempts
- **Executable file blocking** with comprehensive signature detection
- **Archive scanning** to prevent embedded executables
- **File size limits** with configurable maximum file sizes

### 📁 File Support
- **Images**: JPEG, PNG, GIF, WebP, SVG, BMP, TIFF
- **Videos**: MP4, WebM, QuickTime, AVI, MOV, MKV
- **Audio**: MP3, WAV, OGG, WebM, AAC, FLAC, M4A
- **Documents**: PDF, Word, Excel, PowerPoint, CSV, JSON, ZIP, RAR
- **Code files**: JavaScript, TypeScript, CSS, HTML, XML, Python, Java

### 🎨 Media Processing
- **Automatic thumbnail generation** for images and videos
- **Video thumbnail extraction** using FFmpeg at 10% of video duration
- **Image optimization** with Sharp for optimal file sizes
- **Metadata extraction** including dimensions, duration, and file properties
- **Progressive JPEG/WebP** for better loading performance

### 📊 Progress Tracking
- **Real-time upload progress** with percentage and status updates
- **Upload queue management** for multiple file uploads
- **Error handling** with detailed error codes and messages
- **Upload cancellation** and retry mechanisms

## Architecture

### Server-Side Components

#### MediaService (`server/services/media.ts`)
Core service handling all media operations:

```typescript
const mediaService = new MediaService(database, {
  maxFileSize: 100 * 1024 * 1024, // 100MB
  allowedTypes: ['image/jpeg', 'video/mp4', ...],
  uploadPath: './uploads',
  thumbnailPath: './uploads/thumbnails',
  maxImageWidth: 2048,
  maxImageHeight: 2048,
  compressionQuality: 0.85
});
```

**Key Methods:**
- `validateFile()` - Comprehensive file validation
- `uploadFile()` - File upload with progress tracking
- `getMediaById()` - Retrieve media by ID
- `getConversationMedia()` - Get all media for a conversation
- `deleteMedia()` - Secure media deletion with permission checks

#### API Routes (`server/routes/media.ts`)
RESTful endpoints for media operations:

- `POST /api/media/upload` - Upload media files
- `GET /api/media/upload/:id/progress` - Get upload progress
- `GET /api/media/:id` - Get media by ID
- `GET /api/media/conversation/:id` - Get conversation media
- `DELETE /api/media/:id` - Delete media
- `GET /api/media/file/:filename` - Serve media files
- `GET /api/media/thumbnails/:filename` - Serve thumbnails
- `POST /api/media/validate` - Validate file before upload

### Client-Side Components

#### useMediaUpload Hook (`client/hooks/useMediaUpload.ts`)
React hook for media upload functionality:

```typescript
const {
  isUploading,
  uploadProgress,
  uploadError,
  uploadFile,
  uploadMultipleFiles,
  validateFile,
  clearError
} = useMediaUpload({
  conversationId: 'conv-123',
  onUploadComplete: (media) => console.log('Upload complete:', media),
  onUploadError: (error) => console.error('Upload error:', error),
  maxFileSize: 100 * 1024 * 1024,
  allowedTypes: ['image/*', 'video/*', 'audio/*']
});
```

#### MediaUpload Component (`client/components/chat/MediaUpload.tsx`)
Drag-and-drop upload interface with preview:

```tsx
<MediaUpload
  conversationId="conv-123"
  onUploadComplete={(media) => handleMediaUpload(media)}
  onUploadError={(error) => showError(error)}
  disabled={false}
/>
```

#### MediaMessage Component (`client/components/chat/MediaMessage.tsx`)
Display uploaded media in chat:

```tsx
<MediaMessage
  media={mediaAttachment}
  showControls={true}
  onDelete={(mediaId) => handleDelete(mediaId)}
  canDelete={true}
/>
```

## Security Features

### File Validation
1. **MIME Type Verification** - Ensures file type matches content
2. **File Signature Checking** - Validates actual file format vs claimed type
3. **Size Limits** - Prevents oversized file uploads
4. **Extension Validation** - Blocks dangerous file extensions

### Content Scanning
1. **Executable Detection** - Blocks PE, ELF, Mach-O executables
2. **Script Injection Prevention** - Detects JavaScript, VBScript, and other scripts
3. **SQL Injection Protection** - Scans for SQL injection patterns
4. **Path Traversal Prevention** - Blocks directory traversal attempts
5. **Archive Scanning** - Checks ZIP/RAR files for embedded executables

### Office Document Validation
- Validates Office documents by checking internal structure
- Ensures ZIP files are legitimate Office documents, not malicious archives

## Usage Examples

### Basic File Upload

```typescript
// Server-side
const result = await mediaService.uploadFile(
  fileBuffer,
  'image.jpg',
  'image/jpeg',
  'conversation-123',
  'user-456',
  (progress) => {
    console.log(`Upload progress: ${progress.percentage}%`);
  }
);

if (result.success) {
  console.log('Media uploaded:', result.media);
} else {
  console.error('Upload failed:', result.error);
}
```

### Client-Side Upload

```typescript
// React component
const handleFileUpload = async (file: File) => {
  const result = await uploadFile(file);
  
  if (result.success) {
    // Add media to chat message
    addMediaToMessage(result.media);
  } else {
    // Show error to user
    showError(result.error);
  }
};
```

### Progress Tracking

```typescript
// Monitor upload progress
const progressCallback = (progress) => {
  setUploadProgress(progress.percentage);
  setUploadStatus(progress.status);
  
  if (progress.status === 'completed') {
    // Upload finished
    onUploadComplete(progress);
  }
};
```

## Configuration

### Server Configuration

```typescript
const config = {
  maxFileSize: 100 * 1024 * 1024, // 100MB
  allowedTypes: [
    'image/jpeg', 'image/png', 'image/gif', 'image/webp',
    'video/mp4', 'video/webm', 'video/quicktime',
    'audio/mpeg', 'audio/wav', 'audio/ogg',
    'application/pdf', 'text/plain'
  ],
  uploadPath: './uploads',
  thumbnailPath: './uploads/thumbnails',
  maxImageWidth: 2048,
  maxImageHeight: 2048,
  compressionQuality: 0.85
};
```

### Client Configuration

```typescript
const uploadOptions = {
  conversationId: 'conv-123',
  maxFileSize: 100 * 1024 * 1024,
  allowedTypes: ['image/*', 'video/*', 'audio/*'],
  onUploadComplete: (media) => handleComplete(media),
  onUploadError: (error) => handleError(error),
  onProgress: (progress) => updateProgress(progress)
};
```

## Database Schema

The media system uses the `media_attachments` table:

```sql
CREATE TABLE media_attachments (
    id TEXT PRIMARY KEY,
    conversation_id TEXT NOT NULL REFERENCES conversations(id),
    uploader_id TEXT NOT NULL REFERENCES users(id),
    file_type VARCHAR(20) NOT NULL CHECK (file_type IN ('image', 'video', 'voice', 'file')),
    file_url TEXT NOT NULL,
    thumbnail_url TEXT,
    file_size INTEGER NOT NULL,
    duration INTEGER, -- for audio/video in seconds
    width INTEGER, -- for images/video
    height INTEGER, -- for images/video
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

## Error Handling

### Common Error Codes

- `FILE_TOO_LARGE` - File exceeds maximum size limit
- `INVALID_FILE_TYPE` - File type not allowed
- `SECURITY_VIOLATION` - File failed security scan
- `VALIDATION_ERROR` - General validation failure
- `UPLOAD_ERROR` - Upload process failed
- `NETWORK_ERROR` - Network connectivity issue

### Error Response Format

```typescript
{
  success: false,
  error: "File size exceeds maximum allowed size",
  errorCode: "FILE_TOO_LARGE"
}
```

## Performance Considerations

### File Size Limits
- Default maximum: 100MB
- Configurable per file type
- Progressive upload for large files

### Thumbnail Generation
- Images: Optimized WebP thumbnails (300x300px)
- Videos: JPEG thumbnails extracted at 10% duration
- Cached thumbnails for better performance

### Database Optimization
- Indexed queries for fast media retrieval
- Pagination support for large media collections
- Automatic cleanup of orphaned files

## Testing

Run the comprehensive test suite:

```bash
# Unit tests
pnpm test server/services/__tests__/media.test.ts

# Integration tests
pnpm test server/websocket/__tests__/media-integration.test.ts

# Demo
tsx server/websocket/demo/media-upload-demo.ts
```

## Dependencies

### Server Dependencies
- `sharp` - Image processing and optimization
- `fluent-ffmpeg` - Video processing and thumbnail generation
- `multer` - File upload handling
- `better-sqlite3` - Database operations

### Client Dependencies
- `react` - UI components
- `lucide-react` - Icons
- `@radix-ui/*` - UI primitives

## Security Best Practices

1. **Always validate files** on both client and server
2. **Use HTTPS** for all file uploads
3. **Implement rate limiting** to prevent abuse
4. **Regular security audits** of uploaded content
5. **Monitor upload patterns** for suspicious activity
6. **Keep dependencies updated** for security patches

## Troubleshooting

### Common Issues

**Upload fails with "Security scan failed"**
- Check if file contains malicious content
- Verify file type matches content
- Ensure file isn't corrupted

**Thumbnail generation fails**
- Verify FFmpeg is installed and accessible
- Check file permissions on thumbnail directory
- Ensure video file is valid and not corrupted

**Large file uploads timeout**
- Increase server timeout settings
- Consider implementing chunked uploads
- Check network stability

**Database errors**
- Verify database connection
- Check table schema matches expected format
- Ensure proper permissions on database

## Future Enhancements

- [ ] **Chunked uploads** for very large files
- [ ] **CDN integration** for global file distribution
- [ ] **Advanced image processing** (filters, effects)
- [ ] **Video transcoding** for multiple formats
- [ ] **AI-powered content moderation**
- [ ] **Duplicate file detection** with content hashing
- [ ] **Automatic backup** and recovery
- [ ] **Usage analytics** and reporting
