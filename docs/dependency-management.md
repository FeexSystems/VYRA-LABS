# VYRA Dependency Management Guide

## Overview

This document outlines the dependency management strategy for VYRA, including package selection, version management, and maintenance practices.

## Package Manager

### pnpm (Required)

VYRA uses **pnpm** exclusively for package management:

```bash
# Install dependencies
pnpm install

# Add new dependency
pnpm add package-name

# Add dev dependency
pnpm add -D package-name

# Remove dependency
pnpm remove package-name

# Update dependencies
pnpm update
```

**Why pnpm?**
- **Faster installs**: Symlinks to global store
- **Disk space efficient**: Shared dependencies across projects
- **Strict dependency resolution**: Prevents phantom dependencies
- **Better monorepo support**: Native workspace support

## Dependency Categories

### Core Dependencies

#### Frontend Framework
- `react@18.3.1` - Core React library
- `react-dom@18.3.1` - React DOM renderer
- `@types/react` & `@types/react-dom` - TypeScript definitions

#### State Management
- `@tanstack/react-query@^5.25.0` - Server state management
  - **Note**: Includes query core functionality (no need for separate `@tanstack/query-core`)
- React hooks for local state
- Context API for global state

#### Routing & Navigation
- `react-router@^6.30.1` - Client-side routing
- `react-router-dom@^6.30.1` - DOM bindings for React Router

#### UI Framework
- `tailwindcss@^3.4.17` - Utility-first CSS framework
- `@radix-ui/*` - Headless UI components (shadcn/ui foundation)
- `framer-motion@^12.23.12` - Animation library
- `lucide-react@^0.453.0` - Icon library

### Backend Dependencies

#### Server Framework
- `express@^5.1.0` - Web application framework
- `cors@^2.8.5` - Cross-origin resource sharing
- `helmet@^8.1.0` - Security middleware
- `compression@^1.8.1` - Response compression

#### Database & ORM
- `pg@^8.16.3` - PostgreSQL client
- `@types/pg@^8.15.5` - TypeScript definitions

#### Authentication & Security
- `jsonwebtoken@^9.0.2` - JWT token handling
- `bcryptjs@^3.0.2` - Password hashing
- `express-rate-limit@^8.1.0` - Rate limiting middleware

#### Validation & Utilities
- `zod@^3.25.76` - Schema validation
- `dotenv@^17.2.1` - Environment variable loading
- `winston@^3.17.0` - Logging library

### Development Dependencies

#### Build Tools
- `vite@^7.1.2` - Build tool and dev server
- `@vitejs/plugin-react-swc@^4.0.0` - React plugin with SWC
- `typescript@^5.9.2` - TypeScript compiler
- `tsx@^4.20.5` - TypeScript execution

#### Testing
- `vitest@^3.2.4` - Test framework
- `@testing-library/jest-dom@^6.8.0` - Testing utilities
- `jsdom@^26.1.0` - DOM implementation for testing
- `supertest@^7.1.4` - HTTP assertion library

#### Code Quality
- `prettier@^3.6.2` - Code formatter
- `@types/node@^24.2.1` - Node.js type definitions

## Dependency Management Best Practices

### Version Management

#### Semantic Versioning Strategy
- **Patch updates** (`~1.2.3`): Automatic for bug fixes
- **Minor updates** (`^1.2.3`): Default for new features (backward compatible)
- **Major updates**: Manual review required for breaking changes

#### Lock File Management
- **Always commit** `pnpm-lock.yaml`
- **Use frozen lockfile** in CI/CD: `pnpm install --frozen-lockfile`
- **Regular updates** with testing: `pnpm update`

### Dependency Auditing

#### Security Audits
```bash
# Check for vulnerabilities
pnpm audit

# Fix automatically fixable issues
pnpm audit --fix

# Generate audit report
pnpm audit --json > audit-report.json
```

#### Bundle Analysis
```bash
# Analyze bundle size
pnpm build
npx vite-bundle-analyzer dist

# Check for duplicate dependencies
pnpm ls --depth=0
```

### Cleanup Practices

#### Regular Maintenance Tasks

1. **Monthly Dependency Review**
   ```bash
   # Check for outdated packages
   pnpm outdated
   
   # Update non-breaking changes
   pnpm update
   ```

2. **Quarterly Major Updates**
   - Review breaking changes
   - Test thoroughly before updating
   - Update documentation if needed

3. **Duplicate Detection**
   ```bash
   # Find duplicate packages
   pnpm ls --depth=Infinity | grep -E "├─|└─" | sort | uniq -d
   ```

#### Recent Cleanup Examples

**@tanstack/react-query Consolidation**
- **Issue**: Both `@tanstack/query-core` and `@tanstack/react-query` were installed
- **Solution**: Removed `@tanstack/query-core` (included in react-query)
- **Benefit**: Reduced bundle size, eliminated version conflicts

## Package Selection Criteria

### Evaluation Checklist

When adding new dependencies, consider:

1. **Maintenance Status**
   - Active development and regular updates
   - Responsive maintainers
   - Good issue resolution rate

2. **Bundle Size Impact**
   - Check bundlephobia.com for size analysis
   - Consider tree-shaking support
   - Evaluate alternatives for smaller footprint

3. **TypeScript Support**
   - Native TypeScript or quality type definitions
   - Good IDE integration
   - Type safety without `any` types

4. **Security**
   - No known vulnerabilities
   - Good security track record
   - Regular security updates

5. **Compatibility**
   - Works with ES modules
   - Compatible with current React version
   - No conflicts with existing dependencies

### Preferred Alternatives

| Category | Preferred | Avoid | Reason |
|----------|-----------|-------|---------|
| State Management | @tanstack/react-query | Redux (for server state) | Simpler, built for async data |
| Styling | Tailwind CSS | Styled-components | Better performance, smaller bundle |
| Icons | Lucide React | Font Awesome | Tree-shakeable, consistent design |
| Date Handling | date-fns | Moment.js | Smaller, modular, immutable |
| HTTP Client | Fetch API | Axios | Native, smaller bundle |

## Troubleshooting

### Common Issues

#### Phantom Dependencies
```bash
# Check for packages not in package.json but used in code
pnpm ls --depth=Infinity | grep -v "node_modules"
```

#### Version Conflicts
```bash
# Check for multiple versions of same package
pnpm ls package-name
```

#### Cache Issues
```bash
# Clear pnpm cache
pnpm store prune

# Clear node_modules and reinstall
rm -rf node_modules pnpm-lock.yaml
pnpm install
```

### Performance Optimization

#### Bundle Size Monitoring
- Set up bundle size budgets in CI/CD
- Monitor core web vitals impact
- Regular bundle analysis reports

#### Dependency Tree Optimization
- Prefer packages with fewer dependencies
- Use dynamic imports for large libraries
- Consider code splitting for optional features

## Migration Guidelines

### Adding New Dependencies

1. **Research and evaluate** using criteria above
2. **Test in development** environment first
3. **Update documentation** if it affects APIs
4. **Add to appropriate category** in package.json
5. **Update lock file** and commit changes

### Removing Dependencies

1. **Check usage** across codebase
2. **Remove imports** and update code
3. **Test thoroughly** to ensure no runtime errors
4. **Remove from package.json**
5. **Clean up related configuration**

### Major Version Updates

1. **Read changelog** and breaking changes
2. **Update in development** branch first
3. **Run full test suite**
4. **Update related code** if needed
5. **Update documentation**
6. **Deploy to staging** for integration testing

---

This dependency management strategy ensures VYRA maintains a clean, secure, and performant codebase while enabling rapid development and easy maintenance.