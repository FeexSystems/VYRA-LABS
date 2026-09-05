import { RequestHandler } from "express";
import { paystackService } from "../services/PaystackService.js";

/**
 * GET /api/paystack/public-key
 * Return the live public key safe for client-side inline or popup initialization
 */
export const handleGetPaystackPublicKey: RequestHandler = (req, res) => {
  res.json({
    success: true,
    publicKey: paystackService.getPublicKey(),
  });
};

/**
 * POST /api/paystack/initialize
 * Initialize a transaction with Paystack Live API
 */
export const handleInitializePaystack: RequestHandler = async (req, res) => {
  try {
    const { email, amount, currency, metadata, callbackUrl } = req.body;

    if (!email || typeof email !== "string") {
      res.status(400).json({ success: false, message: "Valid email is required" });
      return;
    }

    const numAmount = Number(amount);
    if (!numAmount || numAmount <= 0) {
      res.status(400).json({ success: false, message: "Valid amount greater than 0 is required" });
      return;
    }

    // Convert standard currency unit to minor subunits (e.g., 1000 NGN -> 100000 kobo)
    const amountInSubunits = Math.round(numAmount * 100);

    const initResult = await paystackService.initializeTransaction({
      email,
      amountInSubunits,
      currency: currency || "NGN",
      callbackUrl,
      metadata: metadata || {},
    });

    res.json({
      success: true,
      data: {
        authorizationUrl: initResult.data.authorization_url,
        accessCode: initResult.data.access_code,
        reference: initResult.data.reference,
        amount: numAmount,
        currency: currency || "NGN",
      },
    });
  } catch (error: any) {
    console.error("[PaystackRoute] Initialization failed:", error);
    res.status(500).json({
      success: false,
      message: error?.message || "Failed to initialize Paystack transaction",
    });
  }
};

/**
 * GET /api/paystack/verify/:reference
 * Verify transaction status against Paystack Live API
 */
export const handleVerifyPaystack: RequestHandler = async (req, res) => {
  try {
    const rawRef = req.params.reference;
    const reference = Array.isArray(rawRef) ? rawRef[0] : rawRef;
    if (!reference) {
      res.status(400).json({ success: false, message: "Transaction reference is required" });
      return;
    }

    const verificationResult = await paystackService.verifyTransaction(reference);

    res.json({
      success: true,
      transaction: verificationResult,
    });
  } catch (error: any) {
    console.error("[PaystackRoute] Verification failed:", error);
    res.status(500).json({
      success: false,
      message: error?.message || "Failed to verify Paystack transaction",
    });
  }
};

/**
 * POST /api/paystack/webhook
 * Handle Paystack webhook events
 */
export const handlePaystackWebhook: RequestHandler = async (req, res) => {
  try {
    const signatureHeader = req.headers["x-paystack-signature"];
    const signature = Array.isArray(signatureHeader) ? signatureHeader[0] : signatureHeader;
    const rawBody = (req as any).rawBody || JSON.stringify(req.body);

    if (signature && !paystackService.verifyWebhookSignature(signature, rawBody)) {
      console.warn("[PaystackWebhook] Invalid signature received");
      res.status(400).send("Invalid signature");
      return;
    }

    const event = req.body;
    console.log(`[PaystackWebhook] Received event: ${event.event} for ref: ${event.data?.reference}`);

    // Process event types
    if (event.event === "charge.success") {
      const data = event.data;
      console.log(`[PaystackWebhook] Payment successful: ${data.reference}, amount: ${data.amount / 100} ${data.currency}`);
      // In production, update database transaction state here
    }

    res.sendStatus(200);
  } catch (error) {
    console.error("[PaystackWebhook] Error handling webhook:", error);
    res.sendStatus(500);
  }
};
