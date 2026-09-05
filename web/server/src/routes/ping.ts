import { RequestHandler } from 'express';

export const handlePing: RequestHandler = (req, res) => {
  res.json({
    success: true,
    message: 'Pong from VYRA Web Server',
    timestamp: Date.now()
  });
};
