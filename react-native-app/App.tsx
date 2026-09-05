import React, { useState } from 'react';
import { StyleSheet, Text, View, SafeAreaView, StatusBar, TouchableOpacity, Modal } from 'react-native';
import { VyraShowScreen } from './src/screens/VyraShowScreen';
import { VaultBiometricUnlock } from './src/components/VaultBiometricUnlock';
import { PredictiveBottomDock } from './src/components/ui/PredictiveBottomDock';
import { useVoiceCommand, VoiceCommandResult } from './src/hooks/useVoiceCommand';

type ScreenType = 'HOLO_STREAM' | 'VyraShow' | 'Vault' | 'NeuralNexus' | 'Revyralize';
type ContextType = 'BROADCASTING' | 'BROWSING_VAULT' | 'CHAT_WITH_BOT' | 'DEFAULT';

export default function App() {
  const [activeScreen, setActiveScreen] = useState<ScreenType>('VyraShow');
  const [context, setContext] = useState<ContextType>('DEFAULT');
  const [isUnlocked, setIsUnlocked] = useState(false);
  const [voiceNotification, setVoiceNotification] = useState<string | null>(null);

  // Initialize the voice hook
  const { isListening, startVoiceCapture, stopVoiceCaptureAndProcess } = useVoiceCommand(
    (result: VoiceCommandResult) => {
      console.log("Voice Command Executed:", result);
      
      // Handle navigation
      if (result.action === 'NAVIGATE' && result.targetScreen) {
        setActiveScreen(result.targetScreen);
        
        // Update context based on page destination
        if (result.targetScreen === 'VyraShow') {
          setContext('BROADCASTING');
        } else if (result.targetScreen === 'Vault') {
          setContext('BROWSING_VAULT');
        } else if (result.targetScreen === 'NeuralNexus') {
          setContext('CHAT_WITH_BOT');
        } else {
          setContext('DEFAULT');
        }
      }

      // Handle custom metadata/actions
      if (result.meta?.notifyGroup) {
        setVoiceNotification(`Successfully notified: ${result.meta.notifyGroup}`);
        setTimeout(() => setVoiceNotification(null), 5000);
      }
    }
  );

  const handleDockAction = (action: string) => {
    console.log("Dock Action Selected:", action);
    if (action.startsWith('NAV_')) {
      const screenName = action.replace('NAV_', '') as ScreenType;
      setActiveScreen(screenName);
      if (screenName === 'VyraShow') setContext('BROADCASTING');
      else if (screenName === 'Vault') setContext('BROWSING_VAULT');
      else if (screenName === 'NeuralNexus') setContext('CHAT_WITH_BOT');
      else setContext('DEFAULT');
    }
  };

  const sampleVaultItem = {
    id: "item_cyber_art_99",
    title: "Lagoon Cyberpunk Music Album (Extended Cut)",
    priceNaira: 5000
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="light-content" backgroundColor="#020205" />

      {/* Main Screen Content rendering based on activeScreen state */}
      {activeScreen === 'VyraShow' && <VyraShowScreen />}

      {activeScreen === 'Vault' && (
        <View style={styles.centerScreen}>
          {!isUnlocked ? (
            <VaultBiometricUnlock 
              vaultItem={sampleVaultItem} 
              onUnlock={() => setIsUnlocked(true)} 
            />
          ) : (
            <View style={styles.unlockedBox}>
              <Text style={styles.successIcon}>✓ 🔓</Text>
              <Text style={styles.unlockedTitle}>Media Unlocked</Text>
              <Text style={styles.unlockedDescription}>
                Playing: "Lagoon Cyberpunk Music Album" - Stream Decrypted Successfully.
              </Text>
            </View>
          )}
        </View>
      )}

      {activeScreen === 'NeuralNexus' && (
        <View style={styles.centerScreen}>
          <Text style={styles.screenTitle}>🤖 NEURAL NEXUS</Text>
          <Text style={styles.screenDesc}>Ephemeral Chats with custom Gemini Bots (HoloKai, Bushfeexer).</Text>
        </View>
      )}

      {activeScreen === 'Revyralize' && (
        <View style={styles.centerScreen}>
          <Text style={styles.screenTitle}>⚡ REVYRALIZE HUB</Text>
          <Text style={styles.screenDesc}>Earn FanDNA points and micro-commissions automatically for shares.</Text>
        </View>
      )}

      {/* Mic/Voice trigger button */}
      <TouchableOpacity 
        style={[styles.voiceTrigger, isListening && styles.voiceListening]}
        onPress={() => {
          if (isListening) {
            stopVoiceCaptureAndProcess();
          } else {
            startVoiceCapture();
            // Simulate voice input processing after 3 seconds for demonstration
            setTimeout(() => {
              stopVoiceCaptureAndProcess("Gemini, launch VyraShow and notify FanDNA Tier 1");
            }, 3000);
          }
        }}
      >
        <Text style={styles.voiceTriggerText}>{isListening ? "🔴 LISTENING..." : "🎙️ Voice Command"}</Text>
      </TouchableOpacity>

      {/* Floating notifications for Voice Commands */}
      {voiceNotification && (
        <View style={styles.floatingNotification}>
          <Text style={styles.notificationText}>{voiceNotification}</Text>
        </View>
      )}

      {/* Predictive bottom-dock */}
      <PredictiveBottomDock 
        currentContext={context} 
        onActionSelect={handleDockAction} 
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#020205',
  },
  centerScreen: {
    flex: 1,
    justifyContent: 'center',
    padding: 24,
    alignItems: 'center',
  },
  screenTitle: {
    color: '#00FFCC',
    fontFamily: 'Denton',
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 8,
    textAlign: 'center',
    textShadowColor: '#00FFCC',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 6,
  },
  screenDesc: {
    color: '#A0A0C0',
    fontFamily: 'Inter',
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
  },
  voiceTrigger: {
    position: 'absolute',
    top: 120,
    alignSelf: 'center',
    backgroundColor: 'rgba(0, 255, 204, 0.1)',
    borderColor: '#00FFCC',
    borderWidth: 1,
    borderRadius: 20,
    paddingVertical: 10,
    paddingHorizontal: 20,
    shadowColor: '#00FFCC',
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.3,
    shadowRadius: 5,
  },
  voiceListening: {
    backgroundColor: 'rgba(255, 7, 58, 0.2)',
    borderColor: '#FF073A',
    shadowColor: '#FF073A',
  },
  voiceTriggerText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
    fontFamily: 'Inter',
    fontSize: 12,
    letterSpacing: 1,
  },
  floatingNotification: {
    position: 'absolute',
    top: 180,
    alignSelf: 'center',
    backgroundColor: 'rgba(255, 153, 0, 0.9)',
    borderRadius: 8,
    paddingVertical: 12,
    paddingHorizontal: 24,
    shadowColor: '#FF9900',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.5,
    shadowRadius: 5,
  },
  notificationText: {
    color: '#020205',
    fontWeight: 'bold',
    fontFamily: 'Inter',
    fontSize: 12,
  },
  unlockedBox: {
    padding: 24,
    backgroundColor: 'rgba(57, 255, 20, 0.05)',
    borderColor: '#39FF14',
    borderWidth: 1.5,
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  successIcon: {
    fontSize: 48,
    marginBottom: 16,
  },
  unlockedTitle: {
    color: '#FFFFFF',
    fontFamily: 'Denton',
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  unlockedDescription: {
    color: '#A0A0C0',
    fontFamily: 'Inter',
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
  }
});
