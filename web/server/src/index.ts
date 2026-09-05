try {
  process.loadEnvFile?.();
} catch {}
import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import { handlePing } from './routes/ping';
import { handleDemo } from './routes/demo';
import {
  handleAiViralityAnalysis,
  handleAiGatewayStatus,
  handleAiTts,
  handleAiMultimediaSummary,
} from './routes/ai-gateway';
import {
  handleGetPaystackPublicKey,
  handleInitializePaystack,
  handleVerifyPaystack,
  handlePaystackWebhook,
} from './routes/paystack';
import {
  handleListAgents,
  handleAgentInteract,
  handleAgentStream,
} from './routes/ai-agents';
import {
  handleRegisterBiometrics,
  handleBiometricCharge,
} from './routes/paystack-biometrics.js';

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());

// Routes
app.get('/api/ping', handlePing);
app.get('/api/demo', handleDemo);
app.post('/api/ai/virality-analysis', handleAiViralityAnalysis);
app.get('/api/ai/gateway-status', handleAiGatewayStatus);
app.post('/api/ai/tts', handleAiTts);
app.post('/api/ai/multimedia-summary', handleAiMultimediaSummary);

// Paystack African Live Payment Routes
app.get('/api/paystack/public-key', handleGetPaystackPublicKey);
app.post('/api/paystack/initialize', handleInitializePaystack);
app.get('/api/paystack/verify/:reference', handleVerifyPaystack);
app.post('/api/paystack/webhook', handlePaystackWebhook);
app.post('/api/paystack/register-biometrics', handleRegisterBiometrics);
app.post('/api/paystack/biometric-charge', handleBiometricCharge);

// Gemini Interactions API Live Agents Routes
app.get('/api/ai/agent/list', handleListAgents);
app.post('/api/ai/agent/interact', handleAgentInteract);
app.get('/api/ai/agent/stream', handleAgentStream);

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: Date.now() });
});

// Start server
app.listen(Number(PORT), '0.0.0.0', () => {
  console.log(`VYRA Web & Android API Server running on port ${PORT}`);
  console.log(`Local:   http://localhost:${PORT}/api`);
  console.log(`Network: http://0.0.0.0:${PORT}/api (Android Emulator: http://10.0.2.2:${PORT}/api)`);
});
