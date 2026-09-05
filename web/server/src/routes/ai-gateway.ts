import { RequestHandler } from 'express';

const TRUEFOUNDRY_API_KEY = process.env.TRUEFOUNDRY_API_KEY || '';
const TRUEFOUNDRY_BASE_URL = process.env.TRUEFOUNDRY_BASE_URL || 'https://gateway.truefoundry.ai';

export const handleAiViralityAnalysis: RequestHandler = async (req, res) => {
  try {
    const { title, content, tags, model = 'anthropic/claude-haiku-4-5-20251001' } = req.body;

    const payload = {
      model,
      messages: [
        {
          role: 'system',
          content: 'You are HoloKai AI, the cyberpunk virality analytics agent of VYRA platform.'
        },
        {
          role: 'user',
          content: `Analyze virality potential for show broadcast: "${title}". Content: "${content}". Tags: ${JSON.stringify(tags)}. Output JSON with { viralityScore: number, velocityMultiplier: number, recommendations: string[] }`
        }
      ],
      temperature: 0.6
    };

    const response = await fetch(`${TRUEFOUNDRY_BASE_URL}/api/llm/chat/completions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${TRUEFOUNDRY_API_KEY}`,
        'X-TFY-METADATA': '{}',
        'X-TFY-LOGGING-CONFIG': '{"enabled": true}'
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      return res.status(response.status).json({
        success: false,
        error: `TrueFoundry Gateway error: ${response.statusText}`
      });
    }

    const data = await response.json();
    res.json({
      success: true,
      data
    });
  } catch (error: any) {
    res.status(500).json({
      success: false,
      error: error.message || 'Internal AI Gateway error'
    });
  }
};

export const handleAiTts: RequestHandler = async (req, res) => {
  try {
    const rawInput = req.body.input || req.body.text;
    const {
      voice = 'alloy',
      model = 'openai/gpt-4o-mini-tts-2025-12-15',
      responseFormat = 'mp3',
    } = req.body;

    if (!rawInput || typeof rawInput !== 'string') {
      res.status(400).json({ success: false, error: 'Text input is required for TTS' });
      return;
    }
    const input = rawInput;

    const response = await fetch(`${TRUEFOUNDRY_BASE_URL}/api/llm/audio/speech`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${TRUEFOUNDRY_API_KEY}`,
        'X-TFY-METADATA': '{}',
        'X-TFY-LOGGING-CONFIG': '{"enabled": true}',
      },
      body: JSON.stringify({
        model,
        voice,
        input,
        response_format: responseFormat,
      }),
    });

    if (!response.ok) {
      const errText = await response.text();
      res.status(response.status).json({
        success: false,
        error: `TrueFoundry TTS error: ${errText || response.statusText}`,
      });
      return;
    }

    // Set audio headers and stream buffer back
    res.setHeader('Content-Type', responseFormat === 'wav' ? 'audio/wav' : 'audio/mpeg');
    const arrayBuffer = await response.arrayBuffer();
    res.send(Buffer.from(arrayBuffer));
  } catch (error: any) {
    res.status(500).json({
      success: false,
      error: error?.message || 'Failed to generate speech audio',
    });
  }
};

export const handleAiMultimediaSummary: RequestHandler = async (req, res) => {
  try {
    const { mediaTitle, transcript, mediaType = 'video', durationSeconds } = req.body;

    const payload = {
      model: 'anthropic/claude-haiku-4-5-20251001',
      messages: [
        {
          role: 'system',
          content: 'You are Bushfeexer, the VYRA multimedia intelligence agent. Extract key highlights, viral hooks, timestamped chapters, and social captions from transcripts.'
        },
        {
          role: 'user',
          content: `Analyze this ${mediaType} broadcast titled "${mediaTitle}" (duration: ${durationSeconds || 'unknown'}s). Transcript: "${transcript}". Generate a JSON with: { keyTopics: string[], viralHooks: string[], chapters: { title: string, startSec: number }[], suggestedCaptions: string[] }`
        }
      ],
      temperature: 0.5
    };

    const response = await fetch(`${TRUEFOUNDRY_BASE_URL}/api/llm/chat/completions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${TRUEFOUNDRY_API_KEY}`,
        'X-TFY-METADATA': '{}',
        'X-TFY-LOGGING-CONFIG': '{"enabled": true}',
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      res.status(response.status).json({
        success: false,
        error: `TrueFoundry LLM error: ${response.statusText}`,
      });
      return;
    }

    const data = await response.json();
    res.json({
      success: true,
      analysis: data,
    });
  } catch (error: any) {
    res.status(500).json({
      success: false,
      error: error?.message || 'Internal multimedia processing error',
    });
  }
};

export const handleAiGatewayStatus: RequestHandler = (req, res) => {
  res.json({
    success: true,
    gateway: 'TrueFoundry AI Gateway',
    supportedModels: [
      'anthropic/claude-haiku-4-5-20251001',
      'aws-claude-platform/claude-haiku-4-5-20251001',
      'xai/grok-build-latest',
      'openai/gpt-4o-mini-tts-2025-12-15',
      'google-vertex/gemini-2.5-flash-tts'
    ],
    features: ['Streaming completions', 'Neural Audio TTS', 'Virality Analytics', 'Multimedia Summarization']
  });
};
