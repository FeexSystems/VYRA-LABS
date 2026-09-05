import { RequestHandler } from 'express';

const ELEVENLABS_API_KEY = process.env.ELEVENLABS_API_KEY || '';

// Map local friendly agent IDs to ElevenLabs Agent hashes
const AGENT_MAPPING: Record<string, string> = {
  bushfeexer: process.env.ELEVENLABS_VOICE_CYBER_AMINA || 'f662977c4b2a0b1396fdb1567c802f18d5c429e5c008867104b4ccf45b836672',
  holokai: process.env.ELEVENLABS_VOICE_NEON_VYRA || 'f662977c4b2a0b1396fdb1567c802f18d5c429e5c008867104b4ccf45b836672',
  feexara: process.env.ELEVENLABS_VOICE_QUANTUM_KENJI || 'f662977c4b2a0b1396fdb1567c802f18d5c429e5c008867104b4ccf45b836672',
};

export const handleElevenLabsSignedUrl: RequestHandler = async (req, res) => {
  try {
    const { agentId } = req.query;

    if (!agentId || typeof agentId !== 'string') {
      res.status(400).json({
        success: false,
        error: 'Missing required string query parameter: agentId',
      });
      return;
    }

    const elevenLabsAgentId = AGENT_MAPPING[agentId.toLowerCase()] || agentId;

    // Graceful fallback for local development without credentials
    if (!ELEVENLABS_API_KEY) {
      console.warn('ElevenLabs API Key is missing. Returning local simulation URL.');
      res.json({
        success: true,
        simulation: true,
        signedUrl: `wss://api.elevenlabs.io/v1/convai/conversation/simulated?agent_id=${elevenLabsAgentId}`,
      });
      return;
    }

    const response = await fetch(
      `https://api.elevenlabs.io/v1/convai/conversation/get-signed-url?agent_id=${elevenLabsAgentId}`,
      {
        method: 'GET',
        headers: {
          'xi-api-key': ELEVENLABS_API_KEY,
        },
      }
    );

    if (!response.ok) {
      const errText = await response.text();
      console.error('ElevenLabs API error:', errText);
      res.status(response.status).json({
        success: false,
        error: `ElevenLabs gateway error: ${errText || response.statusText}`,
      });
      return;
    }

    const data = await response.json();
    res.json({
      success: true,
      signedUrl: data.signed_url,
    });
  } catch (error: any) {
    res.status(500).json({
      success: false,
      error: error?.message || 'Internal server error during ElevenLabs signed-url generation',
    });
  }
};
