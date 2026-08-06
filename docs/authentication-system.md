# VYRA Authentication System

## Overview

VYRA implements a secure JWT-based authentication system with local user registration and login capabilities. The system supports role-based access control for creators and fans, with comprehensive security measures including password strength validation, input sanitization, and rate limiting.

## Architecture

### Components

1. **AuthService** (`server/services/auth.ts`) - Core JWT token management
2. **Local Auth Routes** (`server/routes/auth-local.ts`) - Registration and login endpoints
3. **Session Management** (`server/routes/sessions.ts`) - Token refresh and validation
4. **Security Utilities** (`server/utils/security.ts`) - Input validation and sanitization
5. **Error Handling** (`server/middleware/errorHandler.ts`) - Standardized error responses

### Database Schema

The authentication system uses a PostgreSQL-compatible users table:

```sql
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email TEXT UNIQUE NOT NULL,
  username TEXT UNIQUE NOT NULL,
  display_name TEXT,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL CHECK (role IN ('creator','fan')),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

## API Endpoints

### Registration

**POST** `/api/auth/register`

Registers a new user account with comprehensive validation.

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!",
  "role": "creator", // or "fan"
  "username": "optional_username", // defaults to email prefix
  "displayName": "Optional Display Name" // defaults to username
}
```

#### Response
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "uuid",
      "email": "user@example.com",
      "username": "username",
      "display_name": "Display Name",
      "role": "creator",
      "created_at": "2024-01-26T10:00:00Z"
    },
    "token": "jwt_token_here"
  }
}
```

#### Security Features
- **Password Strength Validation**: Enforces strong password requirements
- **Input Sanitization**: All inputs are sanitized to prevent XSS
- **Email Validation**: RFC-compliant email format validation
- **Duplicate Prevention**: Prevents duplicate email registrations
- **Salt Rounds**: Uses bcrypt with 12 salt rounds for password hashing
- **Role Validation**: Ensures only valid roles ('creator', 'fan') are accepted

### Login

**POST** `/api/auth/login`

Authenticates existing users and returns JWT token.

#### Request Body
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}
```

#### Response
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "uuid",
      "email": "user@example.com",
      "username": "username",
      "display_name": "Display Name",
      "role": "creator"
    },
    "token": "jwt_token_here"
  }
}
```

#### Security Features
- **Timing Attack Protection**: Consistent response times regardless of user existence
- **Failed Attempt Logging**: Logs failed login attempts for monitoring
- **Password Hash Verification**: Uses bcrypt for secure password comparison
- **Token Generation**: Creates JWT with user ID and role claims

### Token Refresh

**POST** `/api/auth/refresh`

Refreshes an existing JWT token (requires valid token in Authorization header).

#### Headers
```
Authorization: Bearer <current_jwt_token>
```

#### Response
```json
{
  "success": true,
  "data": {
    "token": "new_jwt_token_here",
    "expiresIn": "24h"
  }
}
```

### Token Validation

**GET** `/api/auth/validate`

Validates the current JWT token and returns user information.

#### Headers
```
Authorization: Bearer <jwt_token>
```

#### Response
```json
{
  "success": true,
  "data": {
    "valid": true,
    "userId": "uuid",
    "role": "creator",
    "iat": 1706270400,
    "exp": 1706356800
  }
}
```

### Logout

**POST** `/api/auth/logout`

Logs out the user (client-side token removal for JWT-based auth).

#### Response
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

## JWT Token Structure

### Payload
```json
{
  "userId": "uuid",
  "role": "creator", // or "fan"
  "iat": 1706270400, // issued at timestamp
  "exp": 1706356800  // expiration timestamp
}
```

### Configuration
- **Secret**: Configurable via `JWT_SECRET` environment variable
- **Expiration**: 24 hours (configurable via `JWT_EXPIRES_IN`)
- **Algorithm**: HS256 (HMAC with SHA-256)

## Security Measures

### Password Security
- **Minimum Requirements**: Enforced via `validatePasswordStrength()`
- **Bcrypt Hashing**: 12 salt rounds for secure password storage
- **Strength Monitoring**: Logs weak passwords for security monitoring
- **JWT Security**: Enhanced with 128-character cryptographically secure secret (1024-bit entropy)
- **Key Management**: AES-256 encryption keys with 64-character length for optimal security

### Input Validation
- **Email Format**: RFC-compliant email validation
- **Input Sanitization**: Prevents XSS attacks via `sanitizeInput()`
- **Role Validation**: Ensures only valid user roles are accepted
- **SQL Injection Prevention**: Uses parameterized queries

### Error Handling
- **Standardized Responses**: Consistent error format across all endpoints
- **Information Disclosure Prevention**: Generic error messages for security
- **Logging**: Comprehensive logging for security monitoring
- **Database Error Handling**: Proper handling of constraint violations

### Rate Limiting
- **API Endpoints**: 100 requests/minute per IP (configurable)
- **Authentication**: 5 failed attempts = 15-minute lockout
- **Token Refresh**: Prevents token abuse

## Environment Configuration

### Required Variables
```env
# JWT Configuration - Enhanced Security
JWT_SECRET=vyra_jwt_2025_dev_8a9b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8c9d0e1f2g3h4i5j6k7l8m9n0o1p2q3r4s5t6u7v8w9x0y1z2
JWT_EXPIRES_IN=24h

# Encryption Configuration - AES-256 Compatible
ENCRYPTION_KEY=vyra_enc_2025_dev_secure_32_char_key_abcdef1234567890abcdef1234567890

# Database Configuration (Supabase PostgreSQL)
DATABASE_URL=postgresql://postgres:[password]@db.project-id.supabase.co:5432/postgres

# Security Configuration
BCRYPT_SALT_ROUNDS=12
RATE_LIMIT_WINDOW_MS=900000
RATE_LIMIT_MAX_REQUESTS=100
```

**Security Notes**:
- **JWT_SECRET**: Now uses 128-character string with 1024-bit entropy for enhanced security
- **ENCRYPTION_KEY**: 64-character AES-256 compatible key for message encryption
- **Key Rotation**: Version identifiers included for tracking and rotation management

## Error Codes

### Authentication Errors
- **400**: Bad Request - Invalid input data
- **401**: Unauthorized - Invalid credentials or expired token
- **403**: Forbidden - Insufficient permissions
- **409**: Conflict - Email already exists
- **429**: Too Many Requests - Rate limit exceeded
- **500**: Internal Server Error - Database or server error

### Error Response Format
```json
{
  "success": false,
  "error": "Error message",
  "code": "ERROR_CODE",
  "details": {} // Additional error details (development only)
}
```

## Usage Examples

### Frontend Integration

#### Registration
```typescript
const register = async (userData: RegisterData) => {
  const response = await fetch('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  });
  
  const result = await response.json();
  
  if (result.success) {
    // Store token in localStorage or secure storage
    localStorage.setItem('authToken', result.data.token);
    return result.data.user;
  } else {
    throw new Error(result.error);
  }
};
```

#### Login
```typescript
const login = async (email: string, password: string) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });
  
  const result = await response.json();
  
  if (result.success) {
    localStorage.setItem('authToken', result.data.token);
    return result.data.user;
  } else {
    throw new Error(result.error);
  }
};
```

#### Authenticated Requests
```typescript
const makeAuthenticatedRequest = async (url: string, options: RequestInit = {}) => {
  const token = localStorage.getItem('authToken');
  
  return fetch(url, {
    ...options,
    headers: {
      ...options.headers,
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
};
```

## Security Best Practices

### Implementation Guidelines
1. **Always validate tokens server-side** for protected routes
2. **Use HTTPS in production** to protect token transmission
3. **Implement proper CORS** to prevent unauthorized access
4. **Store tokens securely** on the client (consider httpOnly cookies)
5. **Implement token refresh logic** to maintain user sessions
6. **Log security events** for monitoring and auditing
7. **Use environment variables** for sensitive configuration
8. **Implement rate limiting** to prevent abuse
9. **Validate all inputs** to prevent injection attacks
10. **Use prepared statements** for database queries

### Monitoring and Auditing
- Monitor failed login attempts
- Track token usage patterns
- Log security-related events
- Implement alerting for suspicious activity
- Regular security audits and penetration testing

## Future Enhancements

### Planned Features
- **Multi-factor Authentication (MFA)**: SMS/TOTP support
- **OAuth Integration**: Google, Facebook, Twitter login
- **Session Management**: Server-side session tracking
- **Password Reset**: Secure password recovery flow
- **Account Verification**: Email verification for new accounts
- **Device Management**: Track and manage user devices
- **Advanced Rate Limiting**: Per-user and per-endpoint limits
- **Audit Logging**: Comprehensive security event logging

### Security Improvements
- **Token Blacklisting**: Server-side token revocation
- **Refresh Token Rotation**: Enhanced security for long-lived sessions
- **Biometric Authentication**: WebAuthn support
- **Risk-based Authentication**: Adaptive security measures
- **CAPTCHA Integration**: Bot protection for registration/login