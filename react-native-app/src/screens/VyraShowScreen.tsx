import React, { useState } from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity, Share, ScrollView, Dimensions } from 'react-native';

const { width, height } = Dimensions.get('window');

export const VyraShowScreen = () => {
  const [viralScore, setViralScore] = useState(98.7);
  const [viralVelocity, setViralVelocity] = useState(3.8);
  const [revyralCount, setRevyralCount] = useState(1420);
  const [shareCount, setShareCount] = useState(840);
  const [isRevyralized, setIsRevyralized] = useState(false);

  const handleRevyralize = () => {
    if (!isRevyralized) {
      setIsRevyralized(true);
      setRevyralCount(prev => prev + 1);
      setViralScore(prev => Math.min(100, prev + 0.5));
      setViralVelocity(prev => prev + 0.3);
    } else {
      setIsRevyralized(false);
      setRevyralCount(prev => prev - 1);
      setViralScore(prev => Math.max(0, prev - 0.5));
      setViralVelocity(prev => Math.max(0, prev - 0.3));
    }
  };

  const handleShare = async () => {
    try {
      const result = await Share.share({
        message: 'Watch my latest VyraShow broadcast live on the Lagos Cyberstage! https://vyra.network/show/cyberstage-live-24',
        title: 'VYRA OS Live Broadcast'
      });
      if (result.action === Share.sharedAction) {
        setShareCount(prev => prev + 1);
      }
    } catch (error: any) {
      console.error("Error sharing broadcast", error);
    }
  };

  return (
    <View style={styles.container}>
      {/* Edge-to-edge media canvas background */}
      <View style={styles.mediaCanvas}>
        {/* Placeholder for video / audio visualizer streams */}
        <View style={styles.placeholderOverlay}>
          <Text style={styles.mediaPlaceholderText}>[ LAGOS CYBERSTAGE LIVE BROADCAST ]</Text>
          <View style={styles.audioVisualizerBarContainer}>
            <View style={[styles.visualizerBar, { height: 40 }]} />
            <View style={[styles.visualizerBar, { height: 80 }]} />
            <View style={[styles.visualizerBar, { height: 60 }]} />
            <View style={[styles.visualizerBar, { height: 90 }]} />
            <View style={[styles.visualizerBar, { height: 50 }]} />
          </View>
        </View>
      </View>

      {/* Top Overlay - Virality Gauge HUD */}
      <View style={styles.topHud}>
        <View style={styles.glassBadge}>
          <Text style={styles.hudLabel}>VIRALITY INDEX</Text>
          <Text style={styles.hudValue}>{viralScore.toFixed(1)}% VIRAL</Text>
        </View>
        <View style={styles.glassBadge}>
          <Text style={styles.hudLabel}>VELOCITY</Text>
          <Text style={styles.hudValue}>+{viralVelocity.toFixed(1)}k vel/h</Text>
        </View>
      </View>

      {/* Right Side Action Dock */}
      <View style={styles.actionDock}>
        {/* Revyralize Action Button (52dp height) */}
        <TouchableOpacity 
          style={[
            styles.actionButton, 
            styles.revyralizeButton, 
            isRevyralized && styles.revyralizeActive
          ]} 
          onPress={handleRevyralize}
        >
          <Text style={styles.actionIcon}>{isRevyralized ? '⚡' : '⟲'}</Text>
          <Text style={styles.actionText}>{revyralCount}</Text>
        </TouchableOpacity>

        {/* Share Action Button (52dp height) */}
        <TouchableOpacity 
          style={[styles.actionButton, styles.shareButton]} 
          onPress={handleShare}
        >
          <Text style={styles.actionIcon}>🔗</Text>
          <Text style={styles.actionText}>{shareCount}</Text>
        </TouchableOpacity>

        {/* Creator Tipping / Support Button */}
        <TouchableOpacity style={[styles.actionButton, styles.tipButton]}>
          <Text style={styles.actionIcon}>₦</Text>
          <Text style={styles.actionText}>Tip</Text>
        </TouchableOpacity>
      </View>

      {/* Bottom Information overlay */}
      <View style={styles.bottomOverlay}>
        <Text style={styles.creatorName}>@Olamide_Feexer</Text>
        <Text style={styles.broadcastDescription}>
          Lagos Cyberstage Alpha - Dynamic neural-dubbing stream direct to global networks. Powered by Gemini API. 🌌⚡
        </Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#020205',
  },
  mediaCanvas: {
    position: 'absolute',
    top: 0,
    left: 0,
    width: width,
    height: height,
    backgroundColor: '#050510',
    justifyContent: 'center',
    alignItems: 'center',
  },
  placeholderOverlay: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  mediaPlaceholderText: {
    color: '#00FFCC',
    fontFamily: 'Denton',
    fontSize: 14,
    fontWeight: 'bold',
    letterSpacing: 2,
    textShadowColor: '#00FFCC',
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 8,
    marginBottom: 20,
  },
  audioVisualizerBarContainer: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    height: 100,
  },
  visualizerBar: {
    width: 6,
    marginHorizontal: 3,
    backgroundColor: '#FF9900',
    borderRadius: 3,
  },
  topHud: {
    position: 'absolute',
    top: 50,
    left: 16,
    right: 16,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  glassBadge: {
    backgroundColor: 'rgba(15, 15, 25, 0.7)',
    borderColor: 'rgba(0, 255, 204, 0.4)',
    borderWidth: 1,
    borderRadius: 12,
    paddingVertical: 8,
    paddingHorizontal: 16,
    alignItems: 'center',
  },
  hudLabel: {
    color: '#A0A0C0',
    fontSize: 9,
    fontWeight: 'bold',
    letterSpacing: 1.5,
  },
  hudValue: {
    color: '#00FFCC',
    fontSize: 14,
    fontWeight: 'bold',
    fontFamily: 'Inter',
    marginTop: 2,
  },
  actionDock: {
    position: 'absolute',
    right: 16,
    bottom: 120,
    alignItems: 'center',
    justifyContent: 'center',
  },
  actionButton: {
    width: 52,
    height: 52,
    borderRadius: 26,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 5,
    elevation: 6,
  },
  revyralizeButton: {
    backgroundColor: 'rgba(0, 255, 204, 0.15)',
    borderColor: '#00FFCC',
    borderWidth: 1.5,
  },
  revyralizeActive: {
    backgroundColor: '#00FFCC',
    shadowColor: '#00FFCC',
    shadowOpacity: 0.6,
    shadowRadius: 10,
  },
  shareButton: {
    backgroundColor: 'rgba(255, 0, 122, 0.15)',
    borderColor: '#FF007A',
    borderWidth: 1.5,
  },
  tipButton: {
    backgroundColor: 'rgba(255, 153, 0, 0.15)',
    borderColor: '#FF9900',
    borderWidth: 1.5,
  },
  actionIcon: {
    fontSize: 20,
    color: '#FFFFFF',
  },
  actionText: {
    color: '#FFFFFF',
    fontSize: 10,
    fontWeight: 'bold',
    marginTop: 2,
    position: 'absolute',
    bottom: -18,
  },
  bottomOverlay: {
    position: 'absolute',
    bottom: 40,
    left: 16,
    right: 80,
  },
  creatorName: {
    color: '#FFFFFF',
    fontFamily: 'Denton',
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  broadcastDescription: {
    color: '#A0A0C0',
    fontFamily: 'Inter',
    fontSize: 13,
    lineHeight: 18,
  },
});
