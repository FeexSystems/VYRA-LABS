import crypto from "crypto";

export interface PaystackInitializeOptions {
  email: string;
  amountInSubunits: number; // e.g. amount in kobo/cents (multiply standard amount by 100)
  currency?: "NGN" | "GHS" | "ZAR" | "KES" | "USD";
  reference?: string;
  callbackUrl?: string;
  metadata?: Record<string, unknown>;
  channels?: string[]; // e.g. ['card', 'bank', 'ussd', 'qr', 'mobile_money', 'bank_transfer']
}

export interface PaystackInitializeResponse {
  status: boolean;
  message: string;
  data: {
    authorization_url: string;
    access_code: string;
    reference: string;
  };
}

export interface PaystackVerifyResponse {
  status: boolean;
  message: string;
  data: {
    id: number;
    domain: string;
    status: string; // "success", "failed", "abandoned"
    reference: string;
    amount: number;
    currency: string;
    gateway_response: string;
    paid_at: string;
    channel: string;
    fees: number | null;
    customer: {
      id: number;
      email: string;
      customer_code: string;
    };
    metadata?: Record<string, unknown>;
  };
}

export interface VyraTransactionResult {
  reference: string;
  isSuccessful: boolean;
  amount: number;
  currency: string;
  platformFee: number;
  creatorPayout: number;
  channel: string;
  customerEmail: string;
  paidAt: string;
  rawResponse?: unknown;
}

export class PaystackService {
  private readonly baseUrl = "https://api.paystack.co";
  private readonly secretKey: string;
  private readonly publicKey: string;

  constructor(secretKey?: string, publicKey?: string) {
    this.secretKey =
      secretKey ||
      process.env.PAYSTACK_SECRET_KEY ||
      "";
    this.publicKey =
      publicKey ||
      process.env.PAYSTACK_PUBLIC_KEY ||
      process.env.VITE_PAYSTACK_PUBLIC_KEY ||
      "";
  }

  /**
   * Get the public key safe for client-side consumption
   */
  getPublicKey(): string {
    return this.publicKey;
  }

  /**
   * Initialize a Paystack transaction
   */
  async initializeTransaction(
    options: PaystackInitializeOptions
  ): Promise<PaystackInitializeResponse> {
    const reference =
      options.reference ||
      `VYRA_PS_${Date.now()}_${Math.random().toString(36).substring(2, 8).toUpperCase()}`;

    const payload = {
      email: options.email,
      amount: options.amountInSubunits,
      currency: options.currency || "NGN",
      reference,
      callback_url: options.callbackUrl || "https://vyra.network/payment/callback",
      metadata: options.metadata || {},
      channels: options.channels || [
        "card",
        "bank",
        "ussd",
        "qr",
        "mobile_money",
        "bank_transfer",
      ],
    };

    try {
      const response = await fetch(`${this.baseUrl}/transaction/initialize`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${this.secretKey}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      const data = (await response.json()) as PaystackInitializeResponse;
      if (!response.ok || !data.status) {
        throw new Error(data.message || `Paystack initialization failed with status ${response.status}`);
      }

      return data;
    } catch (error) {
      console.error("[PaystackService] Initialization error:", error);
      throw error;
    }
  }

  /**
   * Verify a Paystack transaction by reference
   */
  async verifyTransaction(reference: string): Promise<VyraTransactionResult> {
    try {
      const response = await fetch(
        `${this.baseUrl}/transaction/verify/${encodeURIComponent(reference)}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${this.secretKey}`,
            "Content-Type": "application/json",
          },
        }
      );

      const result = (await response.json()) as PaystackVerifyResponse;
      if (!response.ok || !result.status) {
        throw new Error(result.message || `Paystack verification failed with status ${response.status}`);
      }

      const txData = result.data;
      const isSuccessful = txData.status === "success";
      const totalAmount = txData.amount / 100; // Convert minor units back to standard currency amount
      
      // Calculate 15% platform fee (per .kiro/steering/product.md)
      const platformFee = Math.round(totalAmount * 0.15 * 100) / 100;
      const creatorPayout = Math.round((totalAmount - platformFee) * 100) / 100;

      return {
        reference: txData.reference,
        isSuccessful,
        amount: totalAmount,
        currency: txData.currency,
        platformFee,
        creatorPayout,
        channel: txData.channel,
        customerEmail: txData.customer?.email || "",
        paidAt: txData.paid_at,
        rawResponse: txData,
      };
    } catch (error) {
      console.error("[PaystackService] Verification error:", error);
      throw error;
    }
  }

  /**
   * Verify HMAC SHA512 signature for incoming Paystack webhooks
   */
  verifyWebhookSignature(signature: string, rawBody: string | Buffer): boolean {
    if (!signature || !this.secretKey) return false;
    try {
      const hash = crypto
        .createHmac("sha512", this.secretKey)
        .update(rawBody)
        .digest("hex");
      return hash === signature;
    } catch (err) {
      console.error("[PaystackService] Webhook signature verification error:", err);
      return false;
    }
  }
}

export const paystackService = new PaystackService();
