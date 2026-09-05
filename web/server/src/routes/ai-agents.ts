import { RequestHandler } from 'express';
import { AGENT_REGISTRY, interactWithAgent, streamAgentInteraction } from '../services/gemini-service';

/**
 * List all live AI agents and their capabilities
 */
export const handleListAgents: RequestHandler = (req, res) => {
  const agents = Object.values(AGENT_REGISTRY).map(agent => ({
    id: agent.id,
    name: agent.name,
    role: agent.role,
    model: agent.model,
    voice: agent.voice,
    status: 'ONLINE',
    latencyMs: 142
  }));

  res.json({
    success: true,
    agents,
    provider: 'Google Gemini 3.7 Flash & Interactions API'
  });
};

/**
 * Interact directly with an AI Agent
 */
export const handleAgentInteract: RequestHandler = async (req, res) => {
  try {
    const { agentId = 'holokai', prompt, previousInteractionId } = req.body;

    if (!prompt || typeof prompt !== 'string') {
      res.status(400).json({ success: false, error: 'Prompt is required' });
      return;
    }

    const result = await interactWithAgent(agentId, prompt, previousInteractionId);
    res.json(result);
  } catch (error: any) {
    res.status(500).json({
      success: false,
      error: error?.message || 'Error executing agent interaction'
    });
  }
};

/**
 * Stream agent response in real time via Server-Sent Events (SSE)
 */
export const handleAgentStream: RequestHandler = async (req, res) => {
  const agentId = (req.query.agentId as string) || 'holokai';
  const prompt = (req.query.prompt as string) || '';

  if (!prompt) {
    res.status(400).json({ success: false, error: 'prompt query parameter required' });
    return;
  }

  // Set SSE headers
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  res.flushHeaders?.();

  // Send initial connected event
  res.write(`event: connected\ndata: ${JSON.stringify({ agentId, status: 'streaming' })}\n\n`);

  try {
    await streamAgentInteraction(
      agentId,
      prompt,
      (delta) => {
        res.write(`event: delta\ndata: ${JSON.stringify({ delta })}\n\n`);
      },
      (fullText, metadata) => {
        res.write(`event: done\ndata: ${JSON.stringify({ fullText, metadata })}\n\n`);
        res.end();
      },
      (error) => {
        res.write(`event: error\ndata: ${JSON.stringify({ error: error?.message || 'Stream error' })}\n\n`);
        res.end();
      }
    );
  } catch (err: any) {
    res.write(`event: error\ndata: ${JSON.stringify({ error: err?.message || 'Stream failed' })}\n\n`);
    res.end();
  }
};
