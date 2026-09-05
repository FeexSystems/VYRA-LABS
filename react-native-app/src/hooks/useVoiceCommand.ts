import { useState } from 'react';

export interface VoiceCommandResult {
  action: 'NAVIGATE' | 'TRIGGER_NOTIFICATION' | 'UNKNOWN';
  targetScreen?: 'VyraShow' | 'Vault' | 'NeuralNexus' | 'Revyralize';
  meta?: Record<string, any>;
  transcript: string;
}

export const useVoiceCommand = (onExecute: (result: VoiceCommandResult) => void) => {
  const [isListening, setIsListening] = useState(false);

  const startVoiceCapture = async () => {
    setIsListening(true);
    // In a real device setup, this would integrate with react-native-voice
    // or a speech recognition provider.
  };

  const stopVoiceCaptureAndProcess = async (simulatedPrompt?: string) => {
    setIsListening(false);
    
    // Default simulated prompt if none is provided
    const prompt = simulatedPrompt || "Gemini, launch VyraShow and notify FanDNA Tier 1";
    
    try {
      // Route the transcript to VYRA server API which interacts with Gemini AI Router
      const response = await fetch('/api/ai/voice-command', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ prompt })
      });

      if (response.ok) {
        const result: VoiceCommandResult = await response.json();
        onExecute(result);
      } else {
        // Fallback local regex parsing if the server is unreachable
        const lowerPrompt = prompt.toLowerCase();
        let result: VoiceCommandResult = { action: 'UNKNOWN', transcript: prompt };

        if (lowerPrompt.includes('launch') || lowerPrompt.includes('open') || lowerPrompt.includes('go to')) {
          result.action = 'NAVIGATE';
          if (lowerPrompt.includes('vyrashow') || lowerPrompt.includes('broadcast')) {
            result.targetScreen = 'VyraShow';
          } else if (lowerPrompt.includes('vault') || lowerPrompt.includes('premium')) {
            result.targetScreen = 'Vault';
          } else if (lowerPrompt.includes('nexus') || lowerPrompt.includes('chat') || lowerPrompt.includes('telegram')) {
            result.targetScreen = 'NeuralNexus';
          } else if (lowerPrompt.includes('revyralize') || lowerPrompt.includes('shares')) {
            result.targetScreen = 'Revyralize';
          }
        }

        if (lowerPrompt.includes('notify') || lowerPrompt.includes('alert')) {
          result.meta = {
            ...result.meta,
            notifyGroup: lowerPrompt.includes('tier 1') ? 'FanDNA Tier 1' : 'All Followers',
          };
          if (result.action === 'UNKNOWN') {
            result.action = 'TRIGGER_NOTIFICATION';
          }
        }

        onExecute(result);
      }
    } catch (error) {
      console.error("Failed to process voice command via AI Gateway:", error);
    }
  };

  return {
    isListening,
    startVoiceCapture,
    stopVoiceCaptureAndProcess
  };
};
