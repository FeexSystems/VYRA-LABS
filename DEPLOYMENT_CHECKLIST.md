# VYRA Production Deployment Checklist

## Pre-Deployment

### Environment Setup
- [ ] Copy .env.production.template to .env.production
- [ ] Update all placeholder values in .env.production
- [ ] Generate production secrets (run: npm run deploy:prepare)
- [ ] Verify database connection string is correct
- [ ] Update CORS_ORIGIN with production domain

### Code Preparation
- [ ] Run tests: npm run test
- [ ] Build frontend: npm run build
- [ ] Verify build output in dist/spa/
- [ ] Check for TypeScript errors: npm run typecheck
- [ ] Review and commit all changes

## Netlify Deployment (Frontend)

### Setup
- [ ] Create Netlify account at https://netlify.com
- [ ] Install Netlify CLI: npm install -g netlify-cli
- [ ] Login: netlify login
- [ ] Initialize site: netlify init

### Configuration
- [ ] Set build command: npm run build
- [ ] Set publish directory: dist/spa
- [ ] Set Node version: 24
- [ ] Add environment variables in Netlify dashboard:
  - [ ] VITE_API_URL (backend URL)
  - [ ] VITE_WS_URL (WebSocket URL)
  - [ ] VITE_SUPABASE_URL
  - [ ] VITE_SUPABASE_ANON_KEY
  - [ ] VITE_APP_VERSION

### Deploy
- [ ] Deploy: netlify deploy --prod
- [ ] Verify deployment at Netlify URL
- [ ] Test frontend loads correctly

## Render Deployment (Backend)

### Setup
- [ ] Create Render account at https://render.com
- [ ] Create new Web Service
- [ ] Connect GitHub repository
- [ ] Select branch: main

### Configuration
- [ ] Set build command: npm install
- [ ] Set start command: npm run dev:server
- [ ] Set environment: Node
- [ ] Add environment variables:
  - [ ] NODE_ENV=production
  - [ ] DATABASE_URL
  - [ ] JWT_SECRET
  - [ ] SESSION_SECRET
  - [ ] ENCRYPTION_KEY
  - [ ] OPENAI_API_KEY (if using)
  - [ ] ANTHROPIC_API_KEY (if using)
  - [ ] GOOGLE_AI_API_KEY (if using)
  - [ ] SENTRY_DSN (if using)
  - [ ] CORS_ORIGIN (Netlify URL)

### Deploy
- [ ] Click "Create Web Service"
- [ ] Wait for deployment to complete
- [ ] Note the backend URL (e.g., https://vyra-backend.onrender.com)

## Integration

### Update Frontend Configuration
- [ ] Update VITE_API_URL in Netlify to Render backend URL
- [ ] Update VITE_WS_URL in Netlify to Render backend URL (wss://)
- [ ] Redeploy frontend on Netlify

### Update Backend CORS
- [ ] Add Netlify URL to CORS_ORIGIN in Render
- [ ] Redeploy backend if needed

## Testing

### Functionality Tests
- [ ] Visit Netlify URL
- [ ] Test user registration
- [ ] Test user login
- [ ] Test WebSocket chat
- [ ] Test API endpoints
- [ ] Verify database operations
- [ ] Check error logging

### Performance Tests
- [ ] Test page load speed
- [ ] Test API response times
- [ ] Monitor WebSocket connection stability
- [ ] Check for console errors

## Post-Deployment

### Monitoring Setup
- [ ] Verify Sentry is receiving errors
- [ ] Set up uptime monitoring (UptimeRobot, Pingdom)
- [ ] Configure alert notifications
- [ ] Monitor /api/health endpoint

### Security Audit
- [ ] Verify HTTPS is enforced
- [ ] Check security headers
- [ ] Verify rate limiting is active
- [ ] Test CORS configuration
- [ ] Confirm secrets are not exposed

### Documentation
- [ ] Update README with production URLs
- [ ] Document deployment process
- [ ] Create runbook for common issues
- [ ] Share credentials securely with team

## Custom Domain (Optional)

### Netlify
- [ ] Add custom domain in Netlify dashboard
- [ ] Configure DNS records
- [ ] Enable HTTPS (automatic)
- [ ] Update environment variables with new domain

### Render
- [ ] Add custom domain in Render dashboard
- [ ] Configure DNS records
- [ ] Enable HTTPS (automatic)
- [ ] Update CORS configuration

## Rollback Plan

- [ ] Document current deployment version
- [ ] Keep previous deployment accessible
- [ ] Test rollback procedure
- [ ] Document rollback steps

## Success Criteria

- [ ] Frontend loads without errors
- [ ] User registration works
- [ ] User login works
- [ ] Chat functionality works
- [ ] All API endpoints respond correctly
- [ ] Database operations successful
- [ ] No critical errors in logs
- [ ] Performance meets expectations

---

**Deployment Date**: _________________
**Deployed By**: _________________
**Production URLs**:
- Frontend: _________________
- Backend: _________________
