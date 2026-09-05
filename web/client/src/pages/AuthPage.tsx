import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  signInWithPopup,
  signInWithPhoneNumber,
  RecaptchaVerifier,
  ConfirmationResult,
  signOut,
  onAuthStateChanged,
  User as FirebaseUser
} from 'firebase/auth'
import { doc, setDoc, getDoc } from 'firebase/firestore'
import { auth, db, googleProvider } from '../lib/firebase'
import {
  Mail,
  Lock,
  Phone,
  ShieldCheck,
  AlertCircle,
  Loader2,
  LogOut,
  User,
  Sparkles,
  Zap,
  Fingerprint
} from 'lucide-react'

export default function AuthPage() {
  const navigate = useNavigate()
  const [currentUser, setCurrentUser] = useState<FirebaseUser | null>(null)
  const [loading, setLoading] = useState(true)
  const [actionLoading, setActionLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState<string | null>(null)

  // Tab views: 'signin' | 'signup' | 'phone'
  const [activeTab, setActiveTab] = useState<'signin' | 'signup' | 'phone'>('signin')

  // Email/Password states
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [displayName, setDisplayName] = useState('')

  // Phone states
  const [phoneNumber, setPhoneNumber] = useState('') // Format: +234... or local
  const [verificationCode, setVerificationCode] = useState('')
  const [confirmationResult, setConfirmationResult] = useState<ConfirmationResult | null>(null)
  const [recaptchaVerifier, setRecaptchaVerifier] = useState<RecaptchaVerifier | null>(null)

  // Track auth state
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      setCurrentUser(user)
      setLoading(false)
      if (user) {
        // Sync user profile to Firestore
        await syncUserProfile(user)
      }
    })
    return () => unsubscribe()
  }, [])

  // Setup Recaptcha for Phone Auth
  useEffect(() => {
    if (activeTab === 'phone' && !recaptchaVerifier && !currentUser) {
      try {
        const verifier = new RecaptchaVerifier(auth, 'recaptcha-container', {
          size: 'normal',
          callback: () => {
            console.log('reCAPTCHA solved')
          },
          'expired-callback': () => {
            setError('reCAPTCHA expired. Please try again.')
          }
        })
        verifier.render()
        setRecaptchaVerifier(verifier)
      } catch (err: any) {
        console.error('reCAPTCHA initialization error:', err)
      }
    }

    // Cleanup verifier on tab change
    return () => {
      if (recaptchaVerifier) {
        recaptchaVerifier.clear()
        setRecaptchaVerifier(null)
      }
    }
  }, [activeTab])

  // Sync user details to Firestore
  const syncUserProfile = async (user: FirebaseUser, customName?: string) => {
    try {
      const userRef = doc(db, 'users', user.uid)
      const userSnap = await getDoc(userRef)

      const profileData = {
        uid: user.uid,
        email: user.email || null,
        phoneNumber: user.phoneNumber || null,
        displayName: customName || user.displayName || userSnap.data()?.displayName || 'Anon Creator',
        photoURL: user.photoURL || null,
        updatedAt: new Date().toISOString(),
        role: userSnap.data()?.role || 'fan',
        currency: userSnap.data()?.currency || 'NGN',
        createdAt: userSnap.data()?.createdAt || new Date().toISOString()
      }

      await setDoc(userRef, profileData, { merge: true })
      console.log('User profile synced to Firestore successfully')
    } catch (err: any) {
      console.error('Error syncing user profile:', err)
    }
  }

  // Handle Email Sign In
  const handleEmailSignIn = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!email || !password) return setError('Email and Password are required.')
    setError(null)
    setActionLoading(true)

    try {
      await signInWithEmailAndPassword(auth, email, password)
      setSuccess('Logged in successfully!')
      setTimeout(() => navigate('/dashboard'), 1500)
    } catch (err: any) {
      setError(err.message.replace('Firebase: ', ''))
    } finally {
      setActionLoading(false)
    }
  }

  // Handle Email Sign Up
  const handleEmailSignUp = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!email || !password || !displayName) return setError('All fields are required.')
    setError(null)
    setActionLoading(true)

    try {
      const userCredential = await createUserWithEmailAndPassword(auth, email, password)
      setSuccess('Account created successfully!')
      await syncUserProfile(userCredential.user, displayName)
      setTimeout(() => navigate('/dashboard'), 1500)
    } catch (err: any) {
      setError(err.message.replace('Firebase: ', ''))
    } finally {
      setActionLoading(false)
    }
  }

  // Handle Google Sign In
  const handleGoogleSignIn = async () => {
    setError(null)
    setActionLoading(true)
    try {
      const userCredential = await signInWithPopup(auth, googleProvider)
      setSuccess('Google sign-in successful!')
      await syncUserProfile(userCredential.user)
      setTimeout(() => navigate('/dashboard'), 1500)
    } catch (err: any) {
      setError(err.message.replace('Firebase: ', ''))
    } finally {
      setActionLoading(false)
    }
  }

  // Handle Phone Auth - Send OTP
  const handleSendOtp = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!phoneNumber) return setError('Please enter a valid phone number (including country code).')
    if (!recaptchaVerifier) return setError('reCAPTCHA verifier not initialized.')
    setError(null)
    setActionLoading(true)

    try {
      const confirmation = await signInWithPhoneNumber(auth, phoneNumber, recaptchaVerifier)
      setConfirmationResult(confirmation)
      setSuccess('Verification code sent to ' + phoneNumber)
    } catch (err: any) {
      setError(err.message.replace('Firebase: ', ''))
    } finally {
      setActionLoading(false)
    }
  }

  // Handle Phone Auth - Verify OTP
  const handleVerifyOtp = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!verificationCode) return setError('Please enter the 6-digit verification code.')
    if (!confirmationResult) return setError('No active verification session found.')
    setError(null)
    setActionLoading(true)

    try {
      const userCredential = await confirmationResult.confirm(verificationCode)
      setSuccess('Phone verification successful!')
      await syncUserProfile(userCredential.user)
      setTimeout(() => navigate('/dashboard'), 1500)
    } catch (err: any) {
      setError('Invalid verification code. Please check and try again.')
    } finally {
      setActionLoading(false)
    }
  }

  // Handle Sign Out
  const handleSignOut = async () => {
    setActionLoading(true)
    try {
      await signOut(auth)
      setSuccess('Signed out successfully!')
      setConfirmationResult(null)
      setCurrentUser(null)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setActionLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="flex-1 flex flex-col items-center justify-center bg-[#07070D] font-mono">
        <Loader2 className="w-8 h-8 text-cyan-400 animate-spin mb-4" />
        <p className="text-xs text-slate-400 tracking-widest uppercase">INITIALIZING NEURAL AUTH SYSTEM...</p>
      </div>
    )
  }

  return (
    <div className="flex-1 flex items-center justify-center p-4 md:p-8 bg-[#07070D] relative overflow-y-auto">
      {/* Absolute Decorative Grid & Glows */}
      <div className="absolute inset-0 bg-[linear-gradient(to_right,#141424_1px,transparent_1px),linear-gradient(to_bottom,#141424_1px,transparent_1px)] bg-[size:4rem_4rem] [mask-image:radial-gradient(ellipse_60%_50%_at_50%_50%,#000_70%,transparent_100%)] opacity-30"></div>
      <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-cyan-500/10 rounded-full blur-[100px] pointer-events-none"></div>
      <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-pink-500/10 rounded-full blur-[100px] pointer-events-none"></div>

      <div className="w-full max-w-md relative z-10">
        {/* Main Card */}
        <div className="rounded-2xl bg-[#0B0B14]/80 backdrop-blur-xl border border-[#22223C] p-6 md:p-8 shadow-2xl glow-cyan/10">
          
          {/* Header */}
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-12 h-12 rounded-xl bg-gradient-to-tr from-cyan-500 via-indigo-500 to-pink-500 p-0.5 mb-4 shadow-lg shadow-cyan-500/20">
              <div className="w-full h-full bg-[#0A0A14] rounded-[10px] flex items-center justify-center">
                <Fingerprint className="w-5 h-5 text-cyan-400" />
              </div>
            </div>
            <h1 className="text-2xl font-black tracking-widest text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 via-white to-pink-500 uppercase">
              VYRA NEURAL GATE
            </h1>
            <p className="text-xs font-mono text-slate-400 mt-1 uppercase tracking-wider">
              {currentUser ? 'Active Security Session' : 'Encrypted Access Protocol'}
            </p>
          </div>

          {/* Feedback Badges */}
          {error && (
            <div className="mb-6 p-3.5 rounded-xl bg-pink-950/30 border border-pink-500/30 flex items-start space-x-3 text-xs text-pink-300 font-mono">
              <AlertCircle className="w-4 h-4 text-pink-400 shrink-0 mt-0.5" />
              <span className="break-all">{error}</span>
            </div>
          )}

          {success && (
            <div className="mb-6 p-3.5 rounded-xl bg-emerald-950/30 border border-emerald-500/30 flex items-start space-x-3 text-xs text-emerald-300 font-mono">
              <ShieldCheck className="w-4 h-4 text-emerald-400 shrink-0 mt-0.5" />
              <span>{success}</span>
            </div>
          )}

          {currentUser ? (
            /* ========================================================================= */
            /* SIGNED IN USER PROFILE VIEW                                               */
            /* ========================================================================= */
            <div className="space-y-6">
              <div className="p-4 rounded-xl bg-[#141424] border border-[#2A2A48] space-y-4">
                <div className="flex items-center space-x-3.5">
                  <div className="w-12 h-12 rounded-full bg-gradient-to-tr from-cyan-500 to-pink-500 p-0.5">
                    {currentUser.photoURL ? (
                      <img src={currentUser.photoURL} alt="Avatar" className="w-full h-full rounded-full object-cover" />
                    ) : (
                      <div className="w-full h-full rounded-full bg-[#0F0F1A] flex items-center justify-center">
                        <User className="w-5 h-5 text-slate-300" />
                      </div>
                    )}
                  </div>
                  <div>
                    <h3 className="font-bold text-slate-200">{currentUser.displayName || 'Creator User'}</h3>
                    <p className="text-[10px] font-mono text-cyan-400 tracking-wider">SECURE CREATOR NODES ACTIVE</p>
                  </div>
                </div>

                <div className="pt-3 border-t border-[#22223C] space-y-2 text-xs font-mono text-slate-400">
                  <div className="flex justify-between">
                    <span>UID:</span>
                    <span className="text-slate-300 select-all">{currentUser.uid.slice(0, 16)}...</span>
                  </div>
                  {currentUser.email && (
                    <div className="flex justify-between">
                      <span>Email:</span>
                      <span className="text-slate-300">{currentUser.email}</span>
                    </div>
                  )}
                  {currentUser.phoneNumber && (
                    <div className="flex justify-between">
                      <span>Phone:</span>
                      <span className="text-slate-300">{currentUser.phoneNumber}</span>
                    </div>
                  )}
                </div>
              </div>

              <div className="flex flex-col gap-2">
                <button
                  onClick={() => navigate('/dashboard')}
                  className="w-full py-3 px-4 rounded-xl bg-gradient-to-r from-cyan-500 via-indigo-500 to-pink-500 text-black font-black tracking-widest text-xs uppercase shadow-lg shadow-cyan-500/20 hover:opacity-90 transition-all flex items-center justify-center space-x-2"
                >
                  <Zap className="w-4 h-4 fill-black" />
                  <span>Enter Creator Dashboard</span>
                </button>

                <button
                  onClick={handleSignOut}
                  disabled={actionLoading}
                  className="w-full py-3 px-4 rounded-xl bg-[#141424] hover:bg-[#1C1C34] text-pink-400 border border-pink-500/20 font-bold tracking-widest text-xs uppercase transition-all flex items-center justify-center space-x-2"
                >
                  {actionLoading ? (
                    <Loader2 className="w-4 h-4 animate-spin" />
                  ) : (
                    <>
                      <LogOut className="w-4 h-4" />
                      <span>Terminated Session</span>
                    </>
                  )}
                </button>
              </div>
            </div>
          ) : (
            /* ========================================================================= */
            /* SIGN IN / SIGN UP TAB ACTIONS                                             */
            /* ========================================================================= */
            <div className="space-y-6">
              {/* Tab Selector */}
              <div className="flex bg-[#121222] border border-[#22223C] rounded-xl p-1 text-xs font-mono">
                <button
                  onClick={() => { setActiveTab('signin'); setError(null); }}
                  className={`flex-1 py-2 rounded-lg font-bold tracking-wider uppercase transition-all ${
                    activeTab === 'signin' ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Sign In
                </button>
                <button
                  onClick={() => { setActiveTab('signup'); setError(null); }}
                  className={`flex-1 py-2 rounded-lg font-bold tracking-wider uppercase transition-all ${
                    activeTab === 'signup' ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Sign Up
                </button>
                <button
                  onClick={() => { setActiveTab('phone'); setError(null); }}
                  className={`flex-1 py-2 rounded-lg font-bold tracking-wider uppercase transition-all ${
                    activeTab === 'phone' ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40' : 'text-slate-400 hover:text-slate-200'
                  }`}
                >
                  Phone OTP
                </button>
              </div>

              {activeTab === 'signin' && (
                /* ========================================================================= */
                /* EMAIL PASSWORD SIGN IN FORM                                               */
                /* ========================================================================= */
                <form onSubmit={handleEmailSignIn} className="space-y-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">EMAIL ADDRESS</label>
                    <div className="relative">
                      <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                      <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="creator@vyra.ai"
                        className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                        required
                      />
                    </div>
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">ACCESS DECK KEY</label>
                    <div className="relative">
                      <Lock className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                      <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="••••••••"
                        className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                        required
                      />
                    </div>
                  </div>

                  <button
                    type="submit"
                    disabled={actionLoading}
                    className="w-full py-3 rounded-xl bg-gradient-to-r from-cyan-500 via-indigo-500 to-pink-500 text-black font-black tracking-widest text-xs uppercase shadow-lg shadow-cyan-500/10 hover:opacity-95 transition-all flex items-center justify-center space-x-2"
                  >
                    {actionLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <span>INITIALIZE CREATOR SIGN-IN</span>}
                  </button>
                </form>
              )}

              {activeTab === 'signup' && (
                /* ========================================================================= */
                /* EMAIL PASSWORD SIGN UP FORM                                               */
                /* ========================================================================= */
                <form onSubmit={handleEmailSignUp} className="space-y-4">
                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">CREATOR STAGE NAME</label>
                    <div className="relative">
                      <Sparkles className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                      <input
                        type="text"
                        value={displayName}
                        onChange={(e) => setDisplayName(e.target.value)}
                        placeholder="HoloDancer_X"
                        className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                        required
                      />
                    </div>
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">EMAIL ADDRESS</label>
                    <div className="relative">
                      <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                      <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="creator@vyra.ai"
                        className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                        required
                      />
                    </div>
                  </div>

                  <div className="space-y-1.5">
                    <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">ENCRYPTED KEYPHRASE</label>
                    <div className="relative">
                      <Lock className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                      <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="••••••••"
                        className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                        required
                      />
                    </div>
                  </div>

                  <button
                    type="submit"
                    disabled={actionLoading}
                    className="w-full py-3 rounded-xl bg-gradient-to-r from-cyan-500 via-indigo-500 to-pink-500 text-black font-black tracking-widest text-xs uppercase shadow-lg shadow-cyan-500/10 hover:opacity-95 transition-all flex items-center justify-center space-x-2"
                  >
                    {actionLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <span>PROVISION NEURAL ACCOUNT</span>}
                  </button>
                </form>
              )}

              {activeTab === 'phone' && (
                /* ========================================================================= */
                /* PHONE OTP AUTHENTICATION FORM                                             */
                /* ========================================================================= */
                <div className="space-y-4">
                  {!confirmationResult ? (
                    <form onSubmit={handleSendOtp} className="space-y-4">
                      <div className="space-y-1.5">
                        <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">MOBILE INT NUMBER (WITH +)</label>
                        <div className="relative">
                          <Phone className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                          <input
                            type="tel"
                            value={phoneNumber}
                            onChange={(e) => setPhoneNumber(e.target.value)}
                            placeholder="+2348012345678"
                            className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                            required
                          />
                        </div>
                      </div>

                      {/* Recaptcha Container */}
                      <div id="recaptcha-container" className="my-3 flex justify-center scale-90 md:scale-100"></div>

                      <button
                        type="submit"
                        disabled={actionLoading}
                        className="w-full py-3 rounded-xl bg-gradient-to-r from-cyan-500 via-indigo-500 to-pink-500 text-black font-black tracking-widest text-xs uppercase shadow-lg shadow-cyan-500/10 hover:opacity-95 transition-all flex items-center justify-center space-x-2"
                      >
                        {actionLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <span>SEND SMS ONE-TIME PASSCODE</span>}
                      </button>
                    </form>
                  ) : (
                    <form onSubmit={handleVerifyOtp} className="space-y-4">
                      <div className="space-y-1.5">
                        <label className="text-[10px] font-mono tracking-widest text-slate-400 uppercase">ENTER 6-DIGIT CODE</label>
                        <div className="relative">
                          <ShieldCheck className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                          <input
                            type="text"
                            maxLength={6}
                            value={verificationCode}
                            onChange={(e) => setVerificationCode(e.target.value)}
                            placeholder="123456"
                            className="w-full bg-[#121222] border border-[#222240] focus:border-cyan-500 rounded-xl py-3 pl-11 pr-4 text-xs font-mono tracking-widest text-center text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500/30 transition-all placeholder:text-slate-600"
                            required
                          />
                        </div>
                      </div>

                      <button
                        type="submit"
                        disabled={actionLoading}
                        className="w-full py-3 rounded-xl bg-gradient-to-r from-cyan-500 via-indigo-500 to-pink-500 text-black font-black tracking-widest text-xs uppercase shadow-lg shadow-cyan-500/10 hover:opacity-95 transition-all flex items-center justify-center space-x-2"
                      >
                        {actionLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <span>VERIFY VERIFICATION OTP</span>}
                      </button>

                      <button
                        type="button"
                        onClick={() => { setConfirmationResult(null); setError(null); }}
                        className="w-full text-center text-[10px] font-mono text-slate-500 hover:text-slate-400 uppercase tracking-wider"
                      >
                        Change Phone Number
                      </button>
                    </form>
                  )}
                </div>
              )}

              {/* Federated Divider */}
              <div className="relative flex py-2 items-center">
                <div className="flex-grow border-t border-[#1E1E36]"></div>
                <span className="flex-shrink mx-4 text-[10px] font-mono text-slate-500 uppercase tracking-widest">OR CONNECT SECURELY</span>
                <div className="flex-grow border-t border-[#1E1E36]"></div>
              </div>

              {/* Federated Login Button */}
              {/* Federated Login Button */}
              <button
                type="button"
                onClick={handleGoogleSignIn}
                disabled={actionLoading}
                className="w-full py-3 rounded-xl bg-[#141424] hover:bg-[#1C1C34] text-slate-300 border border-[#2A2A48] text-xs font-bold font-mono tracking-wide transition-all flex items-center justify-center space-x-3"
              >
                {actionLoading ? (
                  <Loader2 className="w-4 h-4 animate-spin" />
                ) : (
                  <>
                    <svg className="w-4 h-4" viewBox="0 0 24 24" fill="currentColor">
                      <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.18 1-.78 1.85-1.63 2.42v2.77h2.64c1.55-2.43 2.44-6 2.44-9.67z" fill="#4285F4" />
                      <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.56-2.77c-.98.66-2.23 1.06-3.72 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853" />
                      <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l3.66-2.85z" fill="#FBBC05" />
                      <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335" />
                    </svg>
                    <span>AUTHENTICATE WITH GOOGLE</span>
                  </>
                )}
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
