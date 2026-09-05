import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

interface PredictiveBottomDockProps {
  currentContext: 'BROADCASTING' | 'BROWSING_VAULT' | 'CHAT_WITH_BOT' | 'DEFAULT';
  onActionSelect: (action: string) => void;
}

export const PredictiveBottomDock: React.FC<PredictiveBottomDockProps> = ({ 
  currentContext, 
  onActionSelect 
}) => {
  return (
    <View style={styles.dockContainer}>
      <View style={styles.frostedGlassBg} />
      
      <View style={styles.content}>
        {/* Render adaptive actions depending on current context */}
        {currentContext === 'BROADCASTING' && (
          <>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('STOP_STREAM')}>
              <Text style={styles.icon}>🛑</Text>
              <Text style={styles.label}>Stop Live</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('TOGGLE_MIC')}>
              <Text style={styles.icon}>🎙️</Text>
              <Text style={styles.label}>Mute Mic</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('LAUNCH_POLL')}>
              <Text style={styles.icon}>📊</Text>
              <Text style={styles.label}>Live Poll</Text>
            </TouchableOpacity>
          </>
        )}

        {currentContext === 'BROWSING_VAULT' && (
          <>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('UNLOCK_PPV')}>
              <Text style={styles.icon}>🔓</Text>
              <Text style={styles.label}>Unlock Tier</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('FILTER_CREATORS')}>
              <Text style={styles.icon}>👑</Text>
              <Text style={styles.label}>VIP Only</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('REDEEM_POINTS')}>
              <Text style={styles.icon}>🪙</Text>
              <Text style={styles.label}>Use Points</Text>
            </TouchableOpacity>
          </>
        )}

        {currentContext === 'CHAT_WITH_BOT' && (
          <>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('CLEAR_CHAT')}>
              <Text style={styles.icon}>🧹</Text>
              <Text style={styles.label}>Clear</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('AI_UPSELL')}>
              <Text style={styles.icon}>💎</Text>
              <Text style={styles.label}>Offer Vault</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('SWITCH_BOT')}>
              <Text style={styles.icon}>🤖</Text>
              <Text style={styles.label}>HoloKai</Text>
            </TouchableOpacity>
          </>
        )}

        {currentContext === 'DEFAULT' && (
          <>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('NAV_FEED')}>
              <Text style={styles.icon}>🎬</Text>
              <Text style={styles.label}>HoloFeed</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('NAV_VAULT')}>
              <Text style={styles.icon}>🔑</Text>
              <Text style={styles.label}>The Vault</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('NAV_NEXUS')}>
              <Text style={styles.icon}>💬</Text>
              <Text style={styles.label}>Nexus</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.quickAction} onPress={() => onActionSelect('NAV_REVYRALIZE')}>
              <Text style={styles.icon}>⚡</Text>
              <Text style={styles.label}>Revyralize</Text>
            </TouchableOpacity>
          </>
        )}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  dockContainer: {
    position: 'absolute',
    bottom: 24,
    left: 24,
    right: 24,
    height: 72,
    borderRadius: 36,
    overflow: 'hidden',
    borderColor: 'rgba(0, 255, 204, 0.25)',
    borderWidth: 1.5,
    shadowColor: '#00FFCC',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 10,
    elevation: 10,
  },
  frostedGlassBg: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(10, 10, 20, 0.8)',
  },
  content: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
    paddingHorizontal: 16,
  },
  quickAction: {
    alignItems: 'center',
    justifyContent: 'center',
    minWidth: 60,
  },
  icon: {
    fontSize: 20,
    marginBottom: 2,
  },
  label: {
    color: '#A0A0C0',
    fontSize: 10,
    fontFamily: 'Inter',
    fontWeight: '600',
  }
});
