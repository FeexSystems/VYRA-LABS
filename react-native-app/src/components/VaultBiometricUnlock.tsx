import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import ReactNativeBiometrics from 'react-native-biometrics';

export interface VaultItem {
  id: string;
  title: string;
  priceNaira: number;
}

interface VaultBiometricUnlockProps {
  vaultItem: VaultItem;
  onUnlock: () => void;
}

export const VaultBiometricUnlock: React.FC<VaultBiometricUnlockProps> = ({ vaultItem, onUnlock }) => {
  const [txStatus, setTxStatus] = useState<'IDLE' | 'PROCESSING' | 'SUCCESS' | 'FAILED'>('IDLE');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const executeOneClickCheckout = async () => {
    setErrorMessage(null);
    const biometrics = new ReactNativeBiometrics();
    
    try {
      const { available, error } = await biometrics.isSensorAvailable();
      if (!available) {
        setErrorMessage(error || "Biometric sensor unavailable on this device.");
        return;
      }

      // 1. Trigger OS-level biometric scan (FaceID / Fingerprint) and create signature
      const { success, signature } = await biometrics.createSignature({
        promptMessage: `Pay ₦${vaultItem.priceNaira} to unlock ${vaultItem.title}`,
        payload: vaultItem.id
      });

      if (success && signature) {
        setTxStatus('PROCESSING');
        
        // 2. Route signature to VYRA backend -> Paystack Passkey API
        const response = await fetch('/api/paystack/biometric-charge', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ 
            vaultItemId: vaultItem.id, 
            biometricSignature: signature 
          })
        });

        const data = await response.json();

        if (response.ok && data.success) {
          setTxStatus('SUCCESS');
          onUnlock(); // Decrypt and render Vault media
        } else {
          setTxStatus('FAILED');
          setErrorMessage(data.message || "Cryptographic validation failed.");
        }
      } else {
        setErrorMessage("Biometric scanning cancelled or failed.");
      }
    } catch (err: any) {
      console.error("Biometric Paystack verification failed", err);
      setTxStatus('FAILED');
      setErrorMessage(err?.message || "Internal transaction error occurred.");
    }
  };

  return (
    <View style={styles.glassmorphismContainer}>
      <View style={styles.badge}>
        <Text style={styles.badgeText}>SECURE PPV VAULT</Text>
      </View>
      
      <Text style={styles.neonTitle}>LOCKED: {vaultItem.title}</Text>
      <Text style={styles.priceTag}>₦{vaultItem.priceNaira.toLocaleString()}</Text>
      
      {errorMessage && (
        <View style={styles.errorBox}>
          <Text style={styles.errorText}>{errorMessage}</Text>
        </View>
      )}

      <TouchableOpacity 
        style={[
          styles.biometricButton,
          txStatus === 'PROCESSING' && styles.buttonDisabled
        ]} 
        onPress={executeOneClickCheckout}
        disabled={txStatus === 'PROCESSING'}
      >
        {txStatus === 'PROCESSING' ? (
          <ActivityIndicator color="#111" />
        ) : (
          <Text style={styles.buttonText}>
            {txStatus === 'SUCCESS' ? 'Unlocked ✓' : 'Unlock with FaceID / TouchID'}
          </Text>
        )}
      </TouchableOpacity>

      <Text style={styles.disclaimer}>
        * Secured by Paystack Passkey API. Flat 15% Platform Fee is applied on the Fan-Side.
      </Text>
    </View>
  );
};

const styles = StyleSheet.create({
  glassmorphismContainer: {
    padding: 24,
    backgroundColor: 'rgba(10, 10, 15, 0.75)', 
    borderColor: '#00FFCC',    
    borderWidth: 1.5,
    borderRadius: 16,
    shadowColor: '#00FFCC',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 10,
    elevation: 8,
    marginVertical: 16,
  },
  badge: {
    alignSelf: 'flex-start',
    backgroundColor: 'rgba(0, 255, 204, 0.1)',
    borderColor: '#00FFCC',
    borderWidth: 1,
    borderRadius: 4,
    paddingHorizontal: 8,
    paddingVertical: 4,
    marginBottom: 16,
  },
  badgeText: {
    color: '#00FFCC',
    fontFamily: 'Inter',
    fontSize: 10,
    fontWeight: 'bold',
    letterSpacing: 1.5,
  },
  neonTitle: {
    color: '#FFFFFF',
    fontFamily: 'Denton',
    fontSize: 22,
    fontWeight: '700',
    marginBottom: 8,
    letterSpacing: 0.5,
  },
  priceTag: {
    color: '#FF9900', 
    fontFamily: 'Inter',
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 24,
  },
  errorBox: {
    backgroundColor: 'rgba(255, 7, 58, 0.1)',
    borderColor: '#FF073A',
    borderWidth: 1,
    borderRadius: 8,
    padding: 12,
    marginBottom: 16,
  },
  errorText: {
    color: '#FF073A',
    fontFamily: 'Inter',
    fontSize: 12,
  },
  biometricButton: {
    backgroundColor: '#FF9900', 
    padding: 16,
    borderRadius: 50,
    alignItems: 'center',
    shadowColor: '#FF9900',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 8,
    elevation: 4,
  },
  buttonDisabled: {
    backgroundColor: '#666',
  },
  buttonText: {
    color: '#020205',
    fontWeight: 'bold',
    fontFamily: 'Inter',
    fontSize: 15,
    letterSpacing: 1,
  },
  disclaimer: {
    color: '#A0A0C0',
    fontSize: 11,
    textAlign: 'center',
    marginTop: 16,
    lineHeight: 16,
  }
});
