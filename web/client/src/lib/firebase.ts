import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';

const firebaseConfig = {
  projectId: "govyra",
  appId: "1:281128005541:web:b6b903e189cc39b535424c",
  storageBucket: "govyra.firebasestorage.app",
  apiKey: "AIzaSyBELtEyNOOm8iaW8VVlqPJZRheBlpoNsmo",
  authDomain: "govyra.firebaseapp.com",
  messagingSenderId: "281128005541",
  measurementId: "G-1JFMKPSC34"
};

// Initialize Firebase App
export const app = initializeApp(firebaseConfig);

// Initialize Firebase Auth
export const auth = getAuth(app);

// Initialize Cloud Firestore
export const db = getFirestore(app);

// Initialize Firebase Performance Monitoring (safely checking for browser environment)
import { getPerformance } from 'firebase/performance';
export const perf = typeof window !== 'undefined' ? getPerformance(app) : null;

// Export Auth Providers
export const googleProvider = new GoogleAuthProvider();
googleProvider.setCustomParameters({ prompt: 'select_account' });

// =========================================================================
// FIREBASE AI LOGIC (GEMINI DEVELOPER API CLIENT-SIDE SDK)
// =========================================================================
import { getAI, getGenerativeModel, GoogleAIBackend } from 'firebase/ai';

export const ai = getAI(app, { backend: new GoogleAIBackend() });
export const getClientGenerativeModel = (modelName: string = 'gemini-3.7-flash') => {
  return getGenerativeModel(ai, { model: modelName });
};
