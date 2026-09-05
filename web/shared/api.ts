// Shared TypeScript types for VYRA platform
// Used by both client (React) and server (Express)

export interface AiAgent {
  id: string;
  name: string;
  description: string;
  systemPrompt: string;
  voiceId: string;
  voiceName: string;
  speed: number;
  pitch: number;
  icon: string;
  color: string;
}

export interface ChatMessage {
  id: string;
  agentId: string;
  content: string;
  isFromUser: boolean;
  timestamp: number;
  isRead: boolean;
}

export interface VoiceInteraction {
  id: string;
  agentId: string;
  agentName: string;
  transcript: string;
  agentResponse: string;
  timestamp: string;
  durationSeconds: number;
  sentimentScore: number;
}

export interface FanProfile {
  id: string;
  name: string;
  handle: string;
  tier: string;
  engagementScore: number;
  lifetimeValue: number;
  joinDate: number;
  lastInteraction: number;
  isVip: boolean;
  totalSpend: number;
}

export interface ContentPost {
  id: string;
  title: string;
  content: string;
  platform: string;
  viralityScore: number;
  hashtags: string[];
  createdAt: number;
  publishedAt: number | null;
  isOptimized: boolean;
  imageUrl: string | null;
}

export interface AnalyticsCacheData {
  monthlyRevenue: number;
  totalFans: number;
  viralityScore: number;
  revenueGrowthPercent: number;
}

export interface DashboardMetrics {
  monthlyRevenue: number;
  totalFans: number;
  viralityScore: number;
  revenueGrowthPercent: number;
  activeEngagement: number;
}

export interface RecentActivity {
  id: string;
  type: string;
  description: string;
  timestamp: number;
  agentId?: string;
}

// API Response Types
export interface ApiSuccessResponse<T> {
  success: true;
  data: T;
}

export interface ApiErrorResponse {
  success: false;
  error: string;
  message: string;
}

export type ApiResponse<T> = ApiSuccessResponse<T> | ApiErrorResponse;

// Agent API Types
export interface AgentChatRequest {
  agentId: string;
  message: string;
  context?: Record<string, any>;
}

export interface AgentChatResponse {
  response: string;
  agentId: string;
  timestamp: number;
}

export interface AgentListResponse {
  agents: AiAgent[];
}

// Voice API Types
export interface VoiceGenerationRequest {
  text: string;
  agentId: string;
  voiceId?: string;
  speed?: number;
  pitch?: number;
}

export interface VoiceGenerationResponse {
  audioUrl: string;
  duration: number;
  agentId: string;
}

// Sync API Types
export interface SyncStatus {
  lastSync: number;
  isSyncing: boolean;
  pendingChanges: number;
  conflicts: number;
}

export interface SyncRequest {
  entityType: string;
  operation: 'create' | 'update' | 'delete';
  data: any;
}

export interface SyncResponse {
  success: boolean;
  syncId: string;
  timestamp: number;
}

// WebView Bridge Types
export interface WebViewMessage {
  type: string;
  payload: any;
  timestamp: number;
}

export interface NativeToWebMessage extends WebViewMessage {
  type: 'navigation' | 'data' | 'auth' | 'sync';
}

export interface WebToNativeMessage extends WebViewMessage {
  type: 'action' | 'request' | 'event';
}

// Design System Types
export interface DesignTokens {
  colors: {
    primary: string;
    secondary: string;
    accent: string;
    background: string;
    surface: string;
    text: {
      primary: string;
      secondary: string;
      muted: string;
    };
  };
  spacing: {
    xs: number;
    sm: number;
    md: number;
    lg: number;
    xl: number;
  };
  typography: {
    fontSize: {
      xs: number;
      sm: number;
      md: number;
      lg: number;
      xl: number;
    };
    fontWeight: {
      normal: number;
      medium: number;
      bold: number;
    };
  };
  borderRadius: {
    sm: number;
    md: number;
    lg: number;
    xl: number;
  };
}
