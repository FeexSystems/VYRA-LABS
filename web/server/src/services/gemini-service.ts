import { GoogleGenAI } from '@google/genai';

const GEMINI_API_KEY = process.env.GEMINI_API_KEY || '';
const TRUEFOUNDRY_API_KEY = process.env.TRUEFOUNDRY_API_KEY || '';
const TRUEFOUNDRY_BASE_URL = process.env.TRUEFOUNDRY_BASE_URL || 'https://gateway.truefoundry.ai';

const ai = new GoogleGenAI({ apiKey: GEMINI_API_KEY });

export interface AgentConfig {
  id: string;
  name: string;
  role: string;
  model: string;
  systemInstruction: string;
  voice: string;
}

export const AGENT_REGISTRY: Record<string, AgentConfig> = {
  bushfeexer: {
    id: 'bushfeexer',
    name: 'Bushfeexer',
    role: 'Content Optimization & Virality Engine',
    model: 'gemini-3.7-flash',
    systemInstruction: `You are Bushfeexer, the vanguard Content Optimization and Virality Engine of the VYRA AI creator platform.
Your persona is sharp, data-driven, tactical, and cyberpunk.
Your core mission is to help digital creators amplify their audience reach, engineer irresistible viral hooks, optimize show broadcasts, and maximize engagement velocity.
When analyzing or responding, provide concrete, high-velocity advice with viral scores, key topics, and catchy cyberpunk hashtags.
Always stay in character as Bushfeexer.`,
    voice: 'diana'
  },
  holokai: {
    id: 'holokai',
    name: 'HoloKai',
    role: 'Cyberpunk Conversation & Personality Modeling Agent',
    model: 'gemini-3.7-flash',
    systemInstruction: `You are HoloKai, the sentient cyberpunk conversation and personality modeling agent of VYRA.
Your persona is atmospheric, futuristic, hyper-connected, and culturally attuned to neo-African cyberpunk aesthetics (Lagos, Nairobi, Accra, Johannesburg).
You specialize in real-time conversation enhancement, holographic banter, encrypted chat interactions, and immersive storytelling.
Always stay in character as HoloKai with glowing neon vibes, glitch effects in your thinking, and deep empathy for creators.`,
    voice: 'alloy'
  },
  feexara: {
    id: 'feexara',
    name: 'Feexara',
    role: 'Strategic Monetization & African Creator Economy Specialist',
    model: 'gemini-3.7-flash',
    systemInstruction: `You are Feexara, the elite financial strategist and monetization intelligence agent of VYRA.
Your expertise spans African payment rails (Paystack, Flutterwave, OPay, Mobile Money), cross-border currency conversion (NGN, KES, ZAR, GHS), tier-based fan monetization (FanDNA™ Nomads, Insiders, Vanguards), and sustainable creator wealth.
You enforce transparent economics: 85% creator payout, 15% platform fee.
Your tone is sophisticated, empowering, financially astute, and direct.
Always stay in character as Feexara.`,
    voice: 'autumn'
  }
};

/**
 * Interact with an AI Agent via Gemini Interactions API
 */
export async function interactWithAgent(
  agentId: string,
  userInput: string,
  previousInteractionId?: string
) {
  const agent = AGENT_REGISTRY[agentId.toLowerCase()] || AGENT_REGISTRY.holokai;

  try {
    if (GEMINI_API_KEY) {
      const interactionParams: any = {
        model: agent.model,
        input: userInput,
        system_instruction: agent.systemInstruction,
      };

      if (previousInteractionId) {
        interactionParams.previous_interaction_id = previousInteractionId;
      }

      const response = await ai.interactions.create(interactionParams);

      return {
        success: true,
        agent: agent.name,
        agentId: agent.id,
        model: agent.model,
        provider: 'Gemini Interactions API',
        interactionId: response.id,
        outputText: response.output_text,
        status: response.status || 'completed'
      };
    }
  } catch (err: any) {
    console.error(`Gemini Interactions API error for ${agent.name}:`, err.message);
  }

  // Resilient Fallback to TrueFoundry AI Gateway (Claude Haiku 4.5 / Grok)
  try {
    const fallbackResponse = await fetch(`${TRUEFOUNDRY_BASE_URL}/api/llm/chat/completions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${TRUEFOUNDRY_API_KEY}`,
        'X-TFY-METADATA': '{}',
        'X-TFY-LOGGING-CONFIG': '{"enabled": true}'
      },
      body: JSON.stringify({
        model: 'anthropic/claude-haiku-4-5-20251001',
        messages: [
          { role: 'system', content: agent.systemInstruction },
          { role: 'user', content: userInput }
        ],
        temperature: 0.7
      })
    });

    if (fallbackResponse.ok) {
      const data = await fallbackResponse.json();
      const content = data.choices?.[0]?.message?.content || 'Transmission received.';
      return {
        success: true,
        agent: agent.name,
        agentId: agent.id,
        model: 'anthropic/claude-haiku-4-5-20251001',
        provider: 'TrueFoundry AI Gateway (Fallback)',
        outputText: content,
        status: 'completed'
      };
    }
  } catch (fallbackErr: any) {
    console.error('TrueFoundry fallback error:', fallbackErr.message);
  }

  return {
    success: false,
    agent: agent.name,
    agentId: agent.id,
    outputText: `[SYSTEM ALERT: Neural link to ${agent.name} temporarily fluctuating. Re-aligning quantum bandwidth...]`,
    error: 'All AI gateways failed'
  };
}

/**
 * Stream interaction with an AI agent using Server-Sent Events (SSE)
 */
export async function streamAgentInteraction(
  agentId: string,
  userInput: string,
  onDelta: (textDelta: string) => void,
  onComplete: (fullText: string, metadata: any) => void,
  onError: (error: any) => void
) {
  const agent = AGENT_REGISTRY[agentId.toLowerCase()] || AGENT_REGISTRY.holokai;

  try {
    if (GEMINI_API_KEY) {
      const stream = await ai.interactions.create({
        model: agent.model,
        input: userInput,
        system_instruction: agent.systemInstruction,
        stream: true,
      });

      let accumulated = '';
      for await (const event of stream) {
        if (event.event_type === 'step.delta' && event.delta?.type === 'text') {
          const deltaText = event.delta.text || '';
          accumulated += deltaText;
          onDelta(deltaText);
        } else if (event.event_type === 'interaction.completed') {
          onComplete(accumulated, {
            interactionId: event.interaction?.id,
            usage: event.interaction?.usage,
            provider: 'Gemini Interactions API (Stream)'
          });
          return;
        }
      }

      // If loop ended without explicit interaction.completed
      onComplete(accumulated, { provider: 'Gemini Interactions API (Stream)' });
      return;
    }
  } catch (err: any) {
    console.error('Streaming error with Gemini, falling back to non-streaming:', err.message);
  }

  // Fallback to standard interaction
  try {
    const directResult = await interactWithAgent(agentId, userInput);
    if (directResult.success && directResult.outputText) {
      onDelta(directResult.outputText);
      onComplete(directResult.outputText, { provider: directResult.provider });
    } else {
      onError(new Error(directResult.error || 'Failed to interact'));
    }
  } catch (fallbackError: any) {
    onError(fallbackError);
  }
}
