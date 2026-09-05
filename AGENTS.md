# VYRA AI Agent Platform

A production-ready AI-powered creator platform with integrated voice agents, real-time messaging, and cyberpunk aesthetics.

## AI Agents

### Core Agents
- **Bushfeexer**: Advanced content optimization and engagement analysis agent
- **HoloKai**: Cyberpunk-themed conversation enhancement and personality modeling agent  
- **Feexara**: Strategic business intelligence and creator monetization agent

### Voice Integration
- **ElevenLabs Voice Agents**: Real-time voice synthesis and conversation capabilities
  - Text-to-speech for AI responses
  - Voice cloning for personalized creator experiences
  - Multi-language support for global reach

## Platform Overview

VYRA is an AI-powered creator platform designed for encrypted real-time messaging between creators and fans, with advanced monetization tools and cyberpunk aesthetics.

## Tech Stack

- **Package Manager**: Prefer pnpm (with automatic fallback to npm/yarn)
- **Frontend**: React 18 + React Router 6 (spa) + TypeScript + Vite + TailwindCSS 3
- **Backend**: Express server integrated with Vite dev server
- **Testing**: Vitest
- **UI**: Radix UI + TailwindCSS 3 + Lucide React icons

## Project Structure

```
client/                   # React SPA frontend
├── pages/                # Route components (Index.tsx = home)
├── components/ui/        # Pre-built UI component library
├── App.tsx                # App entry point and with SPA routing setup
└── global.css            # TailwindCSS 3 theming and global styles

server/                   # Express API backend
├── index.ts              # Main server setup (express config + routes)
└── routes/               # API handlers

shared/                   # Types used by both client & server
└── api.ts                # Example of how to share api interfaces
```

## Key Features

## SPA Routing System

The routing system is powered by React Router 6:

- `client/pages/Index.tsx` represents the home page.
- Routes are defined in `client/App.tsx` using the `react-router-dom` import
- Route files are located in the `client/pages/` directory

For example, routes can be defined with:

```typescript
import { BrowserRouter, Routes, Route } from "react-router-dom";

<Routes>
  <Route path="/" element={<Index />} />
  {/* ADD ALL CUSTOM ROUTES ABOVE THE CATCH-ALL "*" ROUTE */}
  <Route path="*" element={<NotFound />} />
</Routes>;
```

### Styling System

- **Primary**: TailwindCSS 3 utility classes
- **Theme and design tokens**: Configure in `client/global.css` 
- **UI components**: Pre-built library in `client/components/ui/`
- **Utility**: `cn()` function combines `clsx` + `tailwind-merge` for conditional classes

```typescript
// cn utility usage
className={cn(
  "base-classes",
  { "conditional-class": condition },
  props.className  // User overrides
)}
```

### Express Server Integration

- **Development**: Single port (8080) for both frontend/backend
- **Hot reload**: Both client and server code
- **API endpoints**: Prefixed with `/api/`

#### Example API Routes
- `GET /api/ping` - Simple ping api
- `GET /api/demo` - Demo endpoint  

### Shared Types
Import consistent types in both client and server:
```typescript
import { DemoResponse } from '@shared/api';
```

Path aliases:
- `@shared/*` - Shared folder
- `@/*` - Client folder

## Development Commands

```bash
pnpm dev        # Start dev server (client + server) - preferred
npm run dev     # Alternative if pnpm not available
pnpm build      # Production build
pnpm start      # Start production server
pnpm typecheck  # TypeScript validation
pnpm test       # Run Vitest tests
```

**Note**: The development server automatically detects available package managers (pnpm → npm → yarn) and uses appropriate commands.

## Adding Features

### Add new colors to the theme

Open `client/global.css` and `tailwind.config.ts` and add new tailwind colors.

### New API Route
1. **Optional**: Create a shared interface in `shared/api.ts`:
```typescript
export interface MyRouteResponse {
  message: string;
  // Add other response properties here
}
```

2. Create a new route handler in `server/routes/my-route.ts`:
```typescript
import { RequestHandler } from "express";
import { MyRouteResponse } from "@shared/api"; // Optional: for type safety

export const handleMyRoute: RequestHandler = (req, res) => {
  const response: MyRouteResponse = {
    message: 'Hello from my endpoint!'
  };
  res.json(response);
};
```

3. Register the route in `server/index.ts`:
```typescript
import { handleMyRoute } from "./routes/my-route";

// Add to the createServer function:
app.get("/api/my-endpoint", handleMyRoute);
```

4. Use in React components with type safety:
```typescript
import { MyRouteResponse } from '@shared/api'; // Optional: for type safety

const response = await fetch('/api/my-endpoint');
const data: MyRouteResponse = await response.json();
```

### New Page Route
1. Create component in `client/pages/MyPage.tsx`
2. Add route in `client/App.tsx`:
```typescript
<Route path="/my-page" element={<MyPage />} />
```

## Production Deployment

- **Standard**: `pnpm build` (or `npm run build` if pnpm unavailable)
- **Binary**: Self-contained executables (Linux, macOS, Windows)
- **Cloud Deployment**: Use either Netlify or Vercel via their MCP integrations for easy deployment. Both providers work well with this starter template.

## Architecture Notes

- Single-port development with Vite + Express integration
- TypeScript throughout (client, server, shared)
- Full hot reload for rapid development
- Production-ready with multiple deployment options
- Comprehensive UI component library included
- Type-safe API communication via shared interfaces

## African Payment Systems & Paystack Integration

- **Paystack Live Credentials**:
  - `PAYSTACK_SECRET_KEY` (`sk_live_*`): Strictly server-side in Express backend (`/api/paystack/*`). Never bundle in client APK or frontend JS.
  - `VITE_PAYSTACK_PUBLIC_KEY` (`pk_live_*`): Configured for client-side inline checkout and Android SDK initialization.
- **Supported African Currencies**: NGN (₦), KES (KSh), ZAR (R), GHS (GH₵).
- **Platform Fee**: Enforce 15% platform fee on all creator tips and purchases (85% net payout to creator balance).

## High-Virality Broadcast Hub (VyraShowScreen)

- **Dedicated Broadcast Layout**: Full-screen cyberpunk media canvas optimized for trending casts and audio visualizers.
- **Action Buttons**: Prominent 52dp buttons for 'Revyralize' (repost with reach multiplier and pulsing Neon Green feedback) and 'Share' (native chooser + in-app social sheet).
- **Virality Gauge HUD**: Real-time gauge displaying virality percentage (`98.7% VIRAL`) and virality velocity (`+3.8k vel/h`).

## TrueFoundry AI Gateway Architecture

- **Standard Base URL**: `https://gateway.truefoundry.ai/api/llm`
- **Supported Models**: `anthropic/claude-haiku-4-5-20251001`, `xai/grok-build-latest`, `openai/gpt-4o-mini-tts-2025-12-15`
- **Mandatory Headers**: `X-TFY-METADATA` and `X-TFY-LOGGING-CONFIG`.

