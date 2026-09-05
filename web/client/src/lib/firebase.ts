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

// Export Auth Providers
export const googleProvider = new GoogleAuthProvider();
googleProvider.setCustomParameters({ prompt: 'select_account' });
