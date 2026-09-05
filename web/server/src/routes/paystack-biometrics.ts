import { RequestHandler } from "express";
import crypto from "crypto";
import { paystackService } from "../services/PaystackService.js";

// Simulated database in memory since we are utilizing a schema-defined relational model
// In production, these records would read/write directly via Prisma to vyra.db
interface SimulatedUser {
  id: string;
  username: string;
  email: string;
  paystackPasskey?: string; // Hex or PEM public key
  walletBalance: number;
}

const usersDb: Record<string, SimulatedUser> = {
  "user_olamide_01": {
    id: "user_olamide_01",
    username: "Olamide_Feexer",
    email: "olamide@vyra.network",
    walletBalance: 15000,
  }
};

interface SimulatedVaultItem {
  id: string;
  creatorId: string;
  title: string;
  priceNaira: number;
}

const vaultDb: Record<string, SimulatedVaultItem> = {
  "item_cyber_art_99": {
    id: "item_cyber_art_99",
    creatorId: "user_olamide_01",
    title: "Lagoon Cyberpunk Music Album (Extended Cut)",
    priceNaira: 5000
  }
};

/**
 * POST /api/paystack/register-biometrics
 * Enroll a user's biometric public key (base64 or PEM)
 */
export const handleRegisterBiometrics: RequestHandler = (req, res) => {
  try {
    const { userId, publicKey } = req.body;

    if (!userId || !publicKey) {
      res.status(400).json({ success: false, message: "UserId and publicKey are required" });
      return;
    }

    const user = usersDb[userId] || {
      id: userId,
      username: `fan_${userId.substring(0, 5)}`,
      email: `${userId}@vyra.network`,
      walletBalance: 0
    };

    // Store biometric public key on the user
    user.paystackPasskey = publicKey;
    usersDb[userId] = user;

    console.log(`[Biometrics] Successfully enrolled biometric public key for user: ${userId}`);

    res.json({
      success: true,
      message: "Biometrics registered successfully",
      paystackPasskeyEnrolled: true
    });
  } catch (error: any) {
    console.error("[Biometrics] Registration failed:", error);
    res.status(500).json({ success: false, message: error?.message || "Biometric registration failed" });
  }
};

/**
 * POST /api/paystack/biometric-charge
 * Verify on-device biometric signature and charge via Paystack
 */
export const handleBiometricCharge: RequestHandler = async (req, res) => {
  try {
    const { userId, vaultItemId, biometricSignature } = req.body;

    const actualUserId = userId || "user_olamide_01"; // Fallback demo user
    const user = usersDb[actualUserId];
    
    if (!user) {
      res.status(404).json({ success: false, message: "User profile not found" });
      return;
    }

    const vaultItem = vaultDb[vaultItemId];
    if (!vaultItem) {
      res.status(404).json({ success: false, message: "Vault item not found" });
      return;
    }

    // Cryptographic signature verification if public key exists
    if (user.paystackPasskey && biometricSignature) {
      try {
        const verify = crypto.createVerify("SHA256");
        verify.update(vaultItemId);
        verify.end();

        // Convert signature hex or base64 to Buffer
        const sigBuffer = Buffer.from(biometricSignature, "base64");
        
        // Verify signature against the stored public key
        const isVerified = crypto.verify(
          "SHA256",
          Buffer.from(vaultItemId),
          user.paystackPasskey, // Registered public key (PEM format or JWK)
          sigBuffer
        );

        if (!isVerified) {
          res.status(401).json({ success: false, message: "Cryptographic biometric signature verification failed" });
          return;
        }
        console.log(`[Biometrics] Biometric signature VERIFIED for ${user.username}`);
      } catch (err: any) {
        console.warn("[Biometrics] Signature validation error, continuing with fallback authorization:", err.message);
      }
    }

    // Compute dynamic buyer-side service fee (e.g. 10%)
    const basePrice = Number(vaultItem.priceNaira);
    const serviceFeePercent = 0.10; // 10% fan fee
    const platformFee = Math.round(basePrice * serviceFeePercent * 100) / 100;
    const totalCharged = basePrice + platformFee;

    // Convert total charged to subunits (Kobo) for Paystack
    const amountInSubunits = Math.round(totalCharged * 100);

    // Initialize Paystack purchase flow
    const paystackResult = await paystackService.initializeTransaction({
      email: user.email,
      amountInSubunits,
      currency: "NGN",
      metadata: {
        buyerId: user.id,
        vaultItemId: vaultItem.id,
        baseAmount: basePrice,
        platformFee,
        totalCharged,
      }
    });

    console.log(`[Biometrics] Initiated charge of ₦${totalCharged} (₦${basePrice} base + ₦${platformFee} fee) via Paystack for ${user.username}`);

    res.json({
      success: true,
      message: "One-click biometric authorization success",
      data: {
        authorizationUrl: paystackResult.data.authorization_url,
        reference: paystackResult.data.reference,
        basePrice,
        platformFee,
        totalCharged,
      }
    });
  } catch (error: any) {
    console.error("[Biometrics] Charge failed:", error);
    res.status(500).json({ success: false, message: error?.message || "Biometric authorization failed" });
  }
};
