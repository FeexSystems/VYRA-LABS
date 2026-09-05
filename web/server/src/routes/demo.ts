import { RequestHandler } from 'express';
import type { ApiResponse, DemoResponse } from '@shared/api';

export const handleDemo: RequestHandler = (req, res) => {
  const response: ApiResponse<DemoResponse> = {
    success: true,
    data: {
      message: 'Demo endpoint working',
      platform: 'VYRA Hybrid Architecture',
      features: ['Native Integration', 'AI Agents', 'Cloud Sync', 'Multi-Platform']
    }
  };
  res.json(response);
};
