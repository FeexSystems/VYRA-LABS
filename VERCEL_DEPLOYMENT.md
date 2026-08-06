# VYRA Vercel Deployment Guide

## 🚀 Migration from Netlify to Vercel

This guide covers the complete migration of VYRA from Netlify to Vercel, enabling native WebSocket support for real-time communication.

### 📋 Prerequisites

- **Node.js 18+** installed locally
- **pnpm 8+** package manager
- **Vercel CLI** (`npm i -g vercel`)
- **Git repository** with your VYRA code
- **Vercel account** (free tier available)

### 🎯 Migration Benefits

✅ **Native WebSocket Support** - Real-time chat functionality  
✅ **Edge Functions** - Better performance globally  
✅ **Automatic Scaling** - Handle traffic spikes  
✅ **Integrated Analytics** - Built-in performance monitoring  
✅ **Preview Deployments** - Test before production  

---

## 🔧 Deployment Steps

### Step 1: Install Vercel CLI

```bash
npm install -g vercel
vercel login
```

### Step 2: Configure Environment Variables

1. **Copy environment template:**
```bash
cp .env.example .env.local
```

2. **Set production variables in Vercel dashboard:**
   - `NODE_ENV=production`
   - `JWT_SECRET=your_secure_jwt_secret`
   - `ENCRYPTION_KEY=your_encryption_key`
   - `CORS_ORIGIN=https://your-domain.vercel.app`

### Step 3: Deploy to Vercel

```bash
# From your project root
vercel

# Follow the prompts:
# ? Set up and deploy "~/VYRA"? Y
# ? Which scope do you want to deploy to? [Your Team]
# ? Link to existing project? N
# ? What's your project's name? vyra
# ? In which directory is your code located? ./
```

### Step 4: Configure Custom Domain (Optional)

```bash
vercel domains add your-domain.com
vercel alias your-deployment-url.vercel.app your-domain.com
```

---

## 📁 Project Structure for Vercel

```
VYRA/
├── api/                          # Vercel API Routes
│   ├── [...slug].ts             # Dynamic API handler
│   ├── health.ts                # Health check endpoint
│   └── websocket.ts             # WebSocket endpoint
├── client/                       # React frontend
│   └── lib/websocket/
│       └── VercelWebSocketManager.ts
├── public/                       # Static assets
├── server/                       # Original server code (for reference)
├── vercel.json                   # Vercel configuration
├── .env.example                  # Environment template
├── .env.local                    # Local development vars
└── package.json                  # Dependencies and scripts
```

---

## ⚙️ Configuration Files

### vercel.json
```json
{
  "version": 2,
  "name": "vyra",
  "builds": [
    {
      "src": "package.json",
      "use": "@vercel/static-build",
      "config": { "distDir": "dist/spa" }
    }
  ],
  "routes": [
    { "src": "/api/(.*)", "dest": "/api/$1" },
    { "src": "/ws/(.*)", "dest": "/api/websocket" },
    { "src": "/(.*)", "dest": "/index.html" }
  ]
}
```

### Environment Variables Setup

**Required Variables:**
- `NODE_ENV` - Set to 'production'
- `JWT_SECRET` - Secure random string
- `ENCRYPTION_KEY` - For message encryption
- `CORS_ORIGIN` - Your domain URL

**Optional Variables:**
- `OPENAI_API_KEY` - For AI features
- `STRIPE_SECRET_KEY` - For payments
- `SENTRY_DSN` - For error monitoring

---

## 🌐 WebSocket Implementation

### Client-Side Usage

```typescript
import { useVercelWebSocket } from '@/lib/websocket/VercelWebSocketManager';

function ChatComponent() {
  const { 
    manager, 
    connectionStatus, 
    isConnected, 
    sendChatMessage 
  } = useVercelWebSocket({
    userId: 'user123',
    role: 'creator',
    tier: 'premium',
    cyberpunkTheme: true
  });

  const handleSendMessage = (message: string) => {
    sendChatMessage('conversation-id', message);
  };

  return (
    <div className="cyberpunk-chat">
      <div className="status-indicator">
        Status: {connectionStatus}
      </div>
      {/* Chat interface */}
    </div>
  );
}
```

### WebSocket Features

✅ **Real-Time Messaging** - Instant chat delivery  
✅ **Typing Indicators** - Live typing status  
✅ **Presence Tracking** - Online/offline status  
✅ **Fan Tier Support** - Premium/VIP access  
✅ **Cyberpunk Effects** - Neon styling integration  
✅ **Auto-Reconnection** - Reliable connections  

---

## 🎨 Cyberpunk Theme Integration

### CSS Variables
```css
:root {
  --neon-cyan: #00FFFF;
  --neon-gold: #FFD700;
  --neon-pink: #FF1493;
  --dark-bg: #0A0A0A;
  --glow-effect: 0 0 10px currentColor;
}
```

### Component Styling
```tsx
<div className="
  bg-vyra-bg 
  border border-neon-cyan/30 
  shadow-[0_0_20px_rgba(0,255,255,0.3)] 
  animate-pulse
">
  <div className="text-neon-gold font-bold">
    VYRA MESSAGE
  </div>
</div>
```

---

## 🔍 Testing and Validation

### Local Development
```bash
# Start development server
pnpm dev

# Test WebSocket connection
# Open browser console and test:
const ws = new WebSocket('ws://localhost:8080/api/websocket');
ws.onopen = () => console.log('Connected!');
```

### Production Testing
```bash
# Test deployed API
curl https://your-app.vercel.app/api/health

# Test WebSocket (use browser dev tools)
const ws = new WebSocket('wss://your-app.vercel.app/api/websocket');
```

---

## 📊 Performance Monitoring

### Built-in Analytics
- **Real-time metrics** in Vercel dashboard
- **Function execution times**
- **WebSocket connection stats**
- **Error rate monitoring**

### Custom Monitoring
```typescript
// Add to your WebSocket manager
const analytics = {
  connectionTime: Date.now(),
  messageCount: 0,
  reconnections: 0
};

// Track usage
manager.subscribe('message', () => {
  analytics.messageCount++;
});
```

---

## 🚨 Troubleshooting

### Common Issues

**Lockfile Mismatch Error:**
```
ERR_PNPM_OUTDATED_LOCKFILE  Cannot install with "frozen-lockfile" because pnpm-lock.yaml is not up to date
```

**Solution:**
1. The project has been configured to use `--no-frozen-lockfile` in vercel.json
2. Dependencies have been simplified to remove serverless-incompatible packages
3. Vercel will regenerate the lockfile automatically

**Alternative Fix (if needed):**
```bash
# Locally, if you need to regenerate lockfile:
rm pnpm-lock.yaml
pnpm install
git add pnpm-lock.yaml
git commit -m "Update lockfile"
git push
```

**WebSocket Connection Failed:**
```bash
# Check environment variables
vercel env ls

# Verify API endpoint
curl https://your-app.vercel.app/api/websocket
```

**Build Errors:**
```bash
# Clean build cache
rm -rf .vercel dist node_modules
pnpm install
vercel --prod
```

**TypeScript Errors:**
```bash
# Check TypeScript config
pnpm typecheck

# Update imports for Vercel API format
import { NextRequest, NextResponse } from 'next/server';
```

### Support Resources

- **Vercel Documentation**: https://vercel.com/docs
- **WebSocket Guide**: https://vercel.com/guides/websockets
- **VYRA Discord**: [Community support]
- **GitHub Issues**: [Project repository]

---

## 🎉 Post-Deployment Checklist

- [ ] ✅ Frontend loads correctly
- [ ] ✅ API endpoints respond
- [ ] ✅ WebSocket connections work
- [ ] ✅ Environment variables set
- [ ] ✅ Custom domain configured
- [ ] ✅ SSL certificate active
- [ ] ✅ Cyberpunk theme displays
- [ ] ✅ Real-time chat functional
- [ ] ✅ Typing indicators work
- [ ] ✅ Fan tiers respected
- [ ] ✅ Mobile responsiveness
- [ ] ✅ Performance metrics good

---

## 🔄 Rollback Plan

If issues occur, you can quickly rollback:

```bash
# Revert to previous deployment
vercel rollback

# Or redeploy Netlify version
# (Keep Netlify config as backup)
```

---

## 🚀 Next Steps

1. **Monitor Performance** - Watch Vercel analytics
2. **Add AI Features** - Integrate OpenAI API
3. **Payment Integration** - Add Stripe processing
4. **Mobile App** - WebSocket works with React Native
5. **Global CDN** - Utilize Vercel Edge Network

---

**🎯 Result: VYRA now has full real-time capabilities with WebSocket support, cyberpunk aesthetic intact, and scalable infrastructure on Vercel!**