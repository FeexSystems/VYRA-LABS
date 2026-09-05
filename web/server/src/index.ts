import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import { handlePing } from './routes/ping';
import { handleDemo } from './routes/demo';

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());

// Routes
app.get('/api/ping', handlePing);
app.get('/api/demo', handleDemo);

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: Date.now() });
});

// Start server
app.listen(PORT, () => {
  console.log(`VYRA Web Server running on port ${PORT}`);
  console.log(`API endpoints available at http://localhost:${PORT}/api`);
});
