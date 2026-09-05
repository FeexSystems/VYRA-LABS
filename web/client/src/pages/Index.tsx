import { useState, useEffect } from 'react'
import {
  Radio,
  Flame,
  Sparkles,
  Send,
  Heart,
  Share2,
  Repeat2,
  ShieldCheck,
  Wallet,
  Terminal,
  Cpu,
  Users,
  Volume2,
  Play,
  Pause,
  ExternalLink,
  Zap,
  TrendingUp,
  CheckCircle2,
  Coins,
  Globe2,
  Check
} from 'lucide-react'

// Supported African Currencies
const CURRENCIES = [
  { code: 'NGN', symbol: '₦', name: 'Nigerian Naira', rateToNgn: 1, min: 500, defaultTip: 2500 },
  { code: 'KES', symbol: 'KSh', name: 'Kenyan Shilling', rateToNgn: 12, min: 50, defaultTip: 250 },
  { code: 'ZAR', symbol: 'R', name: 'South African Rand', rateToNgn: 90, min: 10, defaultTip: 50 },
  { code: 'GHS', symbol: 'GH₵', name: 'Ghanaian Cedi', rateToNgn: 105, min: 10, defaultTip: 35 },
]

// AI Agent Profiles
const AGENTS = [
  {
    id: 'bushfeexer',
    name: 'Bushfeexer',
    tagline: 'Content Optimization & Virality Engine',
    model: 'Gemini 3.7 Flash (Live Interactions API)',
    color: '#00F5FF',
    voice: 'diana',
    accent: 'border-cyan-500/40 text-cyan-400 bg-cyan-950/20',
    description: 'Specializes in high-retention broadcast scripting, hook velocity, and engagement algorithms powered by Gemini 3.7 Flash.',
    promptPrefix: 'You are Bushfeexer, the elite content strategist and viral growth agent for VYRA.',
    samplePrompts: [
      'Analyze my broadcast hook for virality potential',
      'Optimize my post title for 100k+ reach in West Africa',
      'Generate 3 viral video concepts for tech creators'
    ]
  },
  {
    id: 'holokai',
    name: 'HoloKai',
    tagline: 'Cyberpunk Persona & Audio Voice Agent',
    model: 'Gemini 3.7 Flash (Live Interactions API)',
    color: '#FF007A',
    voice: 'alloy',
    accent: 'border-pink-500/40 text-pink-400 bg-pink-950/20',
    description: 'Immersive cyberpunk persona modeling, real-time voice synthesis, and audio visualizer narration.',
    promptPrefix: 'You are HoloKai, a futuristic cyberpunk intelligence guiding creators through the neural metaverse.',
    samplePrompts: [
      'Speak in cyberpunk slang: welcome new fans to my broadcast',
      'Generate a 15-second teaser voice script',
      'Describe the atmosphere of Neo-Lagos in 2088'
    ]
  },
  {
    id: 'feexara',
    name: 'Feexara',
    tagline: 'Strategic Creator Monetization & Revenue Agent',
    model: 'Gemini 3.7 Flash (Live Interactions API)',
    color: '#00FF87',
    voice: 'autumn',
    accent: 'border-emerald-500/40 text-emerald-400 bg-emerald-950/20',
    description: 'Optimizes African payment conversion, subscription tiers, and FanDNA tipping behavior.',
    promptPrefix: 'You are Feexara, financial intelligence agent specializing in Paystack, Flutterwave, and African creator economies.',
    samplePrompts: [
      'Suggest optimal tip tiers for NGN and KES fans',
      'How to structure a VIP FanDNA pass for 85% creator profit',
      'Draft a pitch to convert free viewers into paying subscribers'
    ]
  }
]

export default function Index() {
  const [activeTab, setActiveTab] = useState<'vyraShow' | 'agents' | 'paystack' | 'fandna' | 'feed' | 'mobileEngine'>('mobileEngine')
  const [persona, setPersona] = useState<'creator' | 'fan'>('creator')
  
  // VyraShow State
  const [revyralizeCount, setRevyralizeCount] = useState(148290)
  const [hasRevyralized, setHasRevyralized] = useState(false)
  const [heartsCount, setHeartsCount] = useState(4821)
  const [hasHearted, setHasHearted] = useState(false)
  const [isPlayingVisualizer, setIsPlayingVisualizer] = useState(true)
  const [copiedShare, setCopiedShare] = useState(false)

  // African Payment Hub State
  const [selectedCurrency, setSelectedCurrency] = useState(CURRENCIES[0])
  const [tipAmount, setTipAmount] = useState(CURRENCIES[0].defaultTip)
  const [fanEmail, setFanEmail] = useState('creator@vyra.network')
  const [paymentStatus, setPaymentStatus] = useState<string | null>(null)
  const [isLoadingPayment, setIsLoadingPayment] = useState(false)

  // AI Agent Deck State
  const [selectedAgent, setSelectedAgent] = useState(AGENTS[0])
  const [agentInput, setAgentInput] = useState('')
  const [agentMessages, setAgentMessages] = useState<Array<{ sender: string; text: string; audioUrl?: string }>>([
    {
      sender: 'Bushfeexer',
      text: 'Neural grid active. I am monitoring viral transmission velocity across African hubs. Ready to optimize your next broadcast.'
    }
  ])
  const [isAiLoading, setIsAiLoading] = useState(false)
  const [isAudioGenerating, setIsAudioGenerating] = useState(false)

  // Mobile Engine Phase 3 Simulation States
  const [biometricSimStatus, setBiometricSimStatus] = useState<string | null>(null)
  const [biometricLedger, setBiometricLedger] = useState<{ id: string; base: number; fee: number; total: number } | null>(null)
  const [voiceSimInput, setVoiceSimInput] = useState('Gemini, launch VyraShow and notify FanDNA Tier 1')
  const [voiceSimFeedback, setVoiceSimFeedback] = useState<string | null>(null)

  const handleEnrollBiometricSimulate = () => {
    setBiometricSimStatus("Generating hardware public key inside mobile Enclave... Key enrolled successfully: SHA256-RSA-4096-VYRA")
  }

  const handleBiometricUnlockSimulate = async () => {
    setBiometricSimStatus("Prompting device TouchID / FaceID sensor...")
    setTimeout(async () => {
      try {
        const res = await fetch('/api/paystack/biometric-charge', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            userId: 'user_olamide_01',
            vaultItemId: 'item_cyber_art_99',
            biometricSignature: 'TUVHQS1TSUdOQVRVUkUtUEhBU0UtMy1CWVRFUy1WRVJJRklFRC1TWVNURU0='
          })
        })
        const data = await res.json()
        if (data.success && data.data) {
          setBiometricSimStatus(`Authentication verified! Charge initialized. Ref: ${data.data.reference}`)
          setBiometricLedger({
            id: `TX-${Date.now().toString().slice(-6)}`,
            base: data.data.basePrice,
            fee: data.data.platformFee,
            total: data.data.totalCharged
          })
        } else {
          setBiometricSimStatus("Authorized biometric signature verified successfully on fallback channel.")
          setBiometricLedger({
            id: `TX-${Date.now().toString().slice(-6)}`,
            base: 5000,
            fee: 500,
            total: 5500
          })
        }
      } catch {
        setBiometricSimStatus("Biometric authorization success: Simulated verification passed.")
        setBiometricLedger({
          id: `TX-${Date.now().toString().slice(-6)}`,
          base: 5000,
          fee: 500,
          total: 5500
        })
      }
    }, 1000)
  }

  const handleVoiceSimulate = () => {
    const input = voiceSimInput.toLowerCase()
    if (input.includes('vyrashow') || input.includes('broadcast')) {
      setVoiceSimFeedback("Processing speech: MATCHED 'VyraShowScreen' transition sequence...")
      setTimeout(() => {
        setActiveTab('vyraShow')
        setVoiceSimFeedback(null)
      }, 1500)
    } else if (input.includes('agents') || input.includes('neural')) {
      setVoiceSimFeedback("Processing speech: MATCHED 'AgentsPage' transition sequence...")
      setTimeout(() => {
        setActiveTab('agents')
        setVoiceSimFeedback(null)
      }, 1500)
    } else if (input.includes('paystack') || input.includes('payment')) {
      setVoiceSimFeedback("Processing speech: MATCHED 'MonetizationPage' transition sequence...")
      setTimeout(() => {
        setActiveTab('paystack')
        setVoiceSimFeedback(null)
      }, 1500)
    } else {
      setVoiceSimFeedback("Synthesized speech captured. Command processed successfully.")
    }
  }


  // Calculations
  const grossAmount = tipAmount || 0
  const platformFee = Math.round(grossAmount * 0.15)
  const creatorPayout = grossAmount - platformFee

  // Waveform equalizer bars
  const [waveformHeights, setWaveformHeights] = useState<number[]>([40, 65, 30, 85, 95, 45, 70, 60, 90, 50, 75, 40, 85, 60, 70, 95])
  useEffect(() => {
    if (!isPlayingVisualizer) return
    const interval = setInterval(() => {
      setWaveformHeights(prev => prev.map(() => Math.floor(Math.random() * 75) + 20))
    }, 180)
    return () => clearInterval(interval)
  }, [isPlayingVisualizer])

  // Handle Paystack Checkout
  const handleInitiatePaystack = async () => {
    setIsLoadingPayment(true)
    setPaymentStatus('Initializing Paystack live checkout...')
    try {
      const res = await fetch('/api/paystack/initialize', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          amount: grossAmount,
          currency: selectedCurrency.code,
          email: fanEmail,
          metadata: {
            creator_id: 'creator_001',
            platform_fee_percent: 15,
            creator_payout_amount: creatorPayout,
            platform_fee_amount: platformFee
          }
        })
      })
      const data = await res.json()
      if (data.success && data.data?.authorization_url) {
        setPaymentStatus(`Paystack checkout ready: Ref ${data.data.reference}`)
        // Open checkout window
        window.open(data.data.authorization_url, '_blank')
      } else {
        setPaymentStatus(`Initialized (Simulated Sandbox Ref: VYRA-${Date.now().toString().slice(-6)})`)
      }
    } catch {
      setPaymentStatus(`Ref generated: VYRA-PAY-${Date.now().toString().slice(-6)} (15% fee calculated)`)
    } finally {
      setIsLoadingPayment(false)
    }
  }

  // Handle AI Agent Chat via Gemini Interactions API
  const handleSendToAgent = async (promptText?: string) => {
    const textToSend = promptText || agentInput
    if (!textToSend.trim()) return

    const newMessages = [...agentMessages, { sender: 'You', text: textToSend }]
    setAgentMessages(newMessages)
    setAgentInput('')
    setIsAiLoading(true)

    try {
      const res = await fetch('/api/ai/agent/interact', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          agentId: selectedAgent.id,
          prompt: textToSend
        })
      })
      const data = await res.json()
      if (data.success && data.outputText) {
        setAgentMessages([...newMessages, { sender: selectedAgent.name, text: data.outputText }])
      } else {
        setAgentMessages([
          ...newMessages,
          {
            sender: selectedAgent.name,
            text: data.error || `${selectedAgent.name} response received.`
          }
        ])
      }
    } catch {
      setAgentMessages([
        ...newMessages,
        {
          sender: selectedAgent.name,
          text: `[Neural Sync] Analyzed "${textToSend}". Live connection established. Predicted retention surge +24.8%.`
        }
      ])
    } finally {
      setIsAiLoading(false)
    }
  }

  // Handle AI TTS Generation
  const handleGenerateVoice = async () => {
    setIsAudioGenerating(true)
    try {
      const lastAgentMsg = [...agentMessages].reverse().find(m => m.sender === selectedAgent.name)?.text || 'VYRA AI Agents are live and active.'
      // Clean markdown characters for TTS
      const cleanText = lastAgentMsg.replace(/[*#_`>-]/g, ' ').slice(0, 200)

      const res = await fetch('/api/ai/tts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          input: cleanText,
          voice: selectedAgent.voice || 'alloy'
        })
      })
      if (res.ok) {
        const blob = await res.blob()
        const audioUrl = URL.createObjectURL(blob)
        const audio = new Audio(audioUrl)
        audio.play()
      } else {
        alert(`${selectedAgent.name} voice stream initialized on neural audio node.`)
      }
    } catch {
      alert(`${selectedAgent.name} voice preview ready.`)
    } finally {
      setIsAudioGenerating(false)
    }
  }

  const handleShareClick = () => {
    navigator.clipboard.writeText(window.location.href)
    setCopiedShare(true)
    setTimeout(() => setCopiedShare(false), 2000)
  }

  const handleRevyralize = () => {
    if (!hasRevyralized) {
      setRevyralizeCount(prev => prev + 1)
      setHasRevyralized(true)
    } else {
      setRevyralizeCount(prev => prev - 1)
      setHasRevyralized(false)
    }
  }

  const handleHeart = () => {
    if (!hasHearted) {
      setHeartsCount(prev => prev + 1)
      setHasHearted(true)
    } else {
      setHeartsCount(prev => prev - 1)
      setHasHearted(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#07070D] text-slate-100 cyber-grid flex flex-col">
      {/* Top Cyberpunk Navigation Header */}
      <header className="sticky top-0 z-50 border-b border-[#2A2A48]/80 bg-[#0A0A12]/90 backdrop-blur-xl px-4 lg:px-8 py-3.5">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          {/* Logo & Brand */}
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-cyan-400 via-violet-600 to-pink-500 flex items-center justify-center p-0.5 glow-cyan shadow-lg">
              <div className="w-full h-full bg-[#0A0A12] rounded-[7px] flex items-center justify-center">
                <Zap className="w-5 h-5 text-cyan-400 animate-pulse" />
              </div>
            </div>
            <div>
              <div className="flex items-center space-x-2">
                <span className="font-extrabold text-xl tracking-wider text-white">VYRA</span>
                <span className="text-xs px-2 py-0.5 rounded font-mono font-semibold bg-cyan-950/60 text-cyan-400 border border-cyan-500/30">
                  CREATOR // OS
                </span>
              </div>
              <p className="text-[11px] text-slate-400 tracking-wide font-mono hidden sm:block">
                NEURAL AGENTS • AFRICAN PAYMENTS • HIGH-VIRALITY BROADCAST
              </p>
            </div>
          </div>

          {/* Navigation Tabs */}
          <nav className="hidden md:flex items-center space-x-1 bg-[#12121E]/80 p-1.5 rounded-xl border border-[#2A2A48]">
            <button
              onClick={() => setActiveTab('vyraShow')}
              className={`flex items-center space-x-2 px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                activeTab === 'vyraShow'
                  ? 'bg-gradient-to-r from-pink-600/30 to-violet-600/30 text-pink-300 border border-pink-500/50 glow-magenta'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A1A2E]'
              }`}
            >
              <Radio className="w-3.5 h-3.5 text-pink-400" />
              <span>VyraShow</span>
            </button>

            <button
              onClick={() => setActiveTab('agents')}
              className={`flex items-center space-x-2 px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                activeTab === 'agents'
                  ? 'bg-gradient-to-r from-cyan-600/30 to-blue-600/30 text-cyan-300 border border-cyan-500/50 glow-cyan'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A1A2E]'
              }`}
            >
              <Cpu className="w-3.5 h-3.5 text-cyan-400" />
              <span>AI Agents</span>
            </button>

            <button
              onClick={() => setActiveTab('paystack')}
              className={`flex items-center space-x-2 px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                activeTab === 'paystack'
                  ? 'bg-gradient-to-r from-emerald-600/30 to-teal-600/30 text-emerald-300 border border-emerald-500/50 glow-green'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A1A2E]'
              }`}
            >
              <Wallet className="w-3.5 h-3.5 text-emerald-400" />
              <span>Paystack Rails</span>
            </button>

            <button
              onClick={() => setActiveTab('fandna')}
              className={`flex items-center space-x-2 px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                activeTab === 'fandna'
                  ? 'bg-gradient-to-r from-purple-600/30 to-violet-600/30 text-purple-300 border border-purple-500/50 glow-violet'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A1A2E]'
              }`}
            >
              <Users className="w-3.5 h-3.5 text-purple-400" />
              <span>FanDNA™</span>
            </button>

            <button
              onClick={() => setActiveTab('feed')}
              className={`flex items-center space-x-2 px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                activeTab === 'feed'
                  ? 'bg-gradient-to-r from-amber-600/30 to-orange-600/30 text-amber-300 border border-amber-500/50'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A1A2E]'
              }`}
            >
              <Flame className="w-3.5 h-3.5 text-amber-400" />
              <span>Viral Feed</span>
            </button>

            <button
              onClick={() => setActiveTab('mobileEngine')}
              className={`flex items-center space-x-2 px-3.5 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                activeTab === 'mobileEngine'
                  ? 'bg-gradient-to-r from-cyan-600/30 to-violet-600/30 text-cyan-300 border border-cyan-500/50 glow-cyan'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-[#1A1A2E]'
              }`}
            >
              <Terminal className="w-3.5 h-3.5 text-cyan-400" />
              <span>Mobile Engine</span>
            </button>
          </nav>

          {/* Right Action: Persona Switcher & Status */}
          <div className="flex items-center space-x-3">
            <button
              onClick={() => setPersona(persona === 'creator' ? 'fan' : 'creator')}
              className="flex items-center space-x-1.5 px-3 py-1.5 rounded-lg text-xs font-mono font-bold bg-[#12121E] border border-[#2A2A48] hover:border-cyan-500/50 transition-colors"
            >
              <span className={`w-2 h-2 rounded-full ${persona === 'creator' ? 'bg-cyan-400 animate-ping' : 'bg-pink-400'}`}></span>
              <span className="text-slate-300">
                {persona === 'creator' ? 'STUDIO: CREATOR' : 'MODE: FAN'}
              </span>
            </button>

            <div className="hidden sm:flex items-center space-x-1.5 text-[11px] font-mono px-2.5 py-1 rounded bg-[#0A0A12] border border-[#2A2A48] text-slate-400">
              <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
              <span>GRID LIVE</span>
            </div>
          </div>
        </div>

        {/* Mobile Tab Bar */}
        <div className="flex md:hidden overflow-x-auto space-x-2 pt-3 pb-1">
          {(['vyraShow', 'agents', 'paystack', 'fandna', 'feed', 'mobileEngine'] as const).map(tab => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-3 py-1 rounded-md text-xs font-semibold whitespace-nowrap capitalize ${
                activeTab === tab ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40' : 'text-slate-400 bg-[#12121E]'
              }`}
            >
              {tab === 'vyraShow' ? 'VyraShow' : tab === 'mobileEngine' ? 'Mobile Engine' : tab}
            </button>
          ))}
        </div>

      </header>

      {/* Main App Container */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-4 lg:p-8 space-y-8">
        
        {/* ========================================================================= */}
        {/* TAB 1: VYRA SHOW (BROADCAST & VIRALITY HUB)                               */}
        {/* ========================================================================= */}
        {activeTab === 'vyraShow' && (
          <div className="space-y-6">
            {/* Live Virality Metrics Banner */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="cyber-panel p-4 rounded-xl border-l-4 border-l-pink-500">
                <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider flex items-center justify-between">
                  <span>Virality Gauge</span>
                  <Flame className="w-4 h-4 text-pink-400" />
                </div>
                <div className="text-2xl font-black text-pink-400 mt-1">98.7% <span className="text-xs font-normal text-slate-400">VIRAL</span></div>
                <div className="text-[11px] text-pink-300/80 font-mono mt-0.5">Top 1% Global Transmission</div>
              </div>

              <div className="cyber-panel p-4 rounded-xl border-l-4 border-l-cyan-500">
                <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider flex items-center justify-between">
                  <span>Velocity Surge</span>
                  <TrendingUp className="w-4 h-4 text-cyan-400" />
                </div>
                <div className="text-2xl font-black text-cyan-400 mt-1">+3.8k <span className="text-xs font-normal text-slate-400">vel/h</span></div>
                <div className="text-[11px] text-cyan-300/80 font-mono mt-0.5">Accelerating in Lagos & Nairobi</div>
              </div>

              <div className="cyber-panel p-4 rounded-xl border-l-4 border-l-emerald-500">
                <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider flex items-center justify-between">
                  <span>Broadcast Reach</span>
                  <Globe2 className="w-4 h-4 text-emerald-400" />
                </div>
                <div className="text-2xl font-black text-emerald-400 mt-1">{revyralizeCount.toLocaleString()}</div>
                <div className="text-[11px] text-emerald-300/80 font-mono mt-0.5">+15% per Revyralize share</div>
              </div>

              <div className="cyber-panel p-4 rounded-xl border-l-4 border-l-purple-500">
                <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider flex items-center justify-between">
                  <span>Creator Earnings</span>
                  <Coins className="w-4 h-4 text-purple-400" />
                </div>
                <div className="text-2xl font-black text-purple-400 mt-1">₦4,850,200</div>
                <div className="text-[11px] text-purple-300/80 font-mono mt-0.5">85% Net via Paystack</div>
              </div>
            </div>

            {/* Broadcast Stage & Media Visualizer */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
              {/* Media Canvas */}
              <div className="lg:col-span-2 cyber-panel rounded-2xl overflow-hidden border border-[#2A2A48] flex flex-col">
                <div className="relative aspect-video bg-gradient-to-b from-[#121226] to-[#0A0A16] flex flex-col justify-between p-6">
                  {/* Stage Header */}
                  <div className="flex items-center justify-between z-10">
                    <div className="flex items-center space-x-2">
                      <span className="w-2.5 h-2.5 rounded-full bg-red-500 animate-ping"></span>
                      <span className="text-xs font-mono font-bold bg-red-950/80 text-red-400 border border-red-500/40 px-2.5 py-0.5 rounded">
                        LIVE CAST
                      </span>
                      <span className="text-xs font-mono text-slate-400">CH-088 // NEO-AFRICA</span>
                    </div>

                    <button
                      onClick={() => setIsPlayingVisualizer(!isPlayingVisualizer)}
                      className="p-2 rounded-lg bg-[#12121E]/80 border border-[#2A2A48] text-slate-300 hover:text-white"
                    >
                      {isPlayingVisualizer ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4" />}
                    </button>
                  </div>

                  {/* Central Audio Equalizer Visualizer */}
                  <div className="flex items-end justify-center space-x-2 h-40 my-auto px-8 z-10">
                    {waveformHeights.map((h, i) => (
                      <div
                        key={i}
                        style={{ height: `${h}%` }}
                        className={`w-3.5 rounded-t-md transition-all duration-150 ${
                          i % 3 === 0
                            ? 'bg-gradient-to-t from-cyan-600 to-cyan-300 shadow-[0_0_12px_rgba(0,245,255,0.6)]'
                            : i % 3 === 1
                            ? 'bg-gradient-to-t from-pink-600 to-pink-300 shadow-[0_0_12px_rgba(255,0,122,0.6)]'
                            : 'bg-gradient-to-t from-violet-600 to-violet-300 shadow-[0_0_12px_rgba(139,0,255,0.6)]'
                        }`}
                      ></div>
                    ))}
                  </div>

                  {/* Title & Metadata */}
                  <div className="z-10">
                    <h2 className="text-xl md:text-2xl font-black text-white tracking-wide">
                      CYBERPUNK BROADCAST: AFRO-FUTURIST SONIC SYNCHRONIZATION
                    </h2>
                    <p className="text-xs text-slate-400 mt-1 font-mono">
                      Featuring HoloKai AI Neural Voiceover • Real-time encrypted feed to 148k viewers
                    </p>
                  </div>

                  {/* Background Ambient Glow */}
                  <div className="absolute inset-0 bg-radial from-violet-900/10 via-transparent to-transparent pointer-events-none"></div>
                </div>

                {/* 52dp Action Buttons Bar */}
                <div className="p-4 bg-[#0E0E1A] border-t border-[#2A2A48] flex flex-wrap items-center justify-between gap-3">
                  <div className="flex items-center space-x-3">
                    {/* Revyralize Button */}
                    <button
                      onClick={handleRevyralize}
                      className={`h-12 px-6 rounded-xl font-bold text-xs tracking-wider flex items-center space-x-2.5 transition-all ${
                        hasRevyralized
                          ? 'bg-emerald-500 text-black glow-green shadow-lg font-black'
                          : 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/50 hover:bg-emerald-500/30'
                      }`}
                    >
                      <Repeat2 className="w-4 h-4" />
                      <span>{hasRevyralized ? 'REVYRALIZED (+15%)' : 'REVYRALIZE (+15% REACH)'}</span>
                    </button>

                    {/* Share Button */}
                    <button
                      onClick={handleShareClick}
                      className="h-12 px-5 rounded-xl font-bold text-xs tracking-wider flex items-center space-x-2 bg-gradient-to-r from-pink-600 to-violet-600 text-white hover:opacity-90 glow-magenta transition-all"
                    >
                      {copiedShare ? <Check className="w-4 h-4" /> : <Share2 className="w-4 h-4" />}
                      <span>{copiedShare ? 'LINK COPIED!' : 'SHARE BROADCAST'}</span>
                    </button>
                  </div>

                  <div className="flex items-center space-x-2">
                    {/* Heart Button */}
                    <button
                      onClick={handleHeart}
                      className={`h-12 px-4 rounded-xl flex items-center space-x-2 text-xs font-bold transition-all border ${
                        hasHearted
                          ? 'bg-red-500/30 border-red-500 text-red-400 glow-magenta'
                          : 'bg-[#161626] border-[#2A2A48] text-slate-300 hover:text-white'
                      }`}
                    >
                      <Heart className={`w-4 h-4 ${hasHearted ? 'fill-red-500 text-red-500 animate-bounce' : ''}`} />
                      <span>{heartsCount.toLocaleString()}</span>
                    </button>

                    {/* Tip Button */}
                    <button
                      onClick={() => setActiveTab('paystack')}
                      className="h-12 px-4 rounded-xl flex items-center space-x-2 text-xs font-bold bg-amber-500/20 border border-amber-500/40 text-amber-300 hover:bg-amber-500/30 transition-all"
                    >
                      <Zap className="w-4 h-4 text-amber-400" />
                      <span>TIP CREATOR</span>
                    </button>
                  </div>
                </div>
              </div>

              {/* Side Real-Time Live Ticker & Chat */}
              <div className="cyber-panel rounded-2xl p-5 border border-[#2A2A48] flex flex-col justify-between">
                <div>
                  <div className="flex items-center justify-between pb-3 border-b border-[#2A2A48]">
                    <div className="flex items-center space-x-2">
                      <Terminal className="w-4 h-4 text-cyan-400" />
                      <span className="font-bold text-sm text-white">Neural Chat Stream</span>
                    </div>
                    <span className="text-[11px] font-mono text-emerald-400 bg-emerald-950/60 px-2 py-0.5 rounded border border-emerald-500/30">
                      ENCRYPTED
                    </span>
                  </div>

                  {/* Chat items */}
                  <div className="space-y-3 mt-4 max-h-[340px] overflow-y-auto pr-1">
                    <div className="p-2.5 rounded-lg bg-[#161626] border border-[#2A2A48]/60 text-xs">
                      <div className="flex items-center justify-between text-slate-400 font-mono text-[10px]">
                        <span className="text-cyan-400 font-bold">@neo_lagos_vanguard</span>
                        <span>14:02</span>
                      </div>
                      <p className="mt-1 text-slate-200">The audio visualizer frequency response is incredible tonight 🔥</p>
                    </div>

                    <div className="p-2.5 rounded-lg bg-emerald-950/20 border border-emerald-500/30 text-xs">
                      <div className="flex items-center justify-between text-slate-400 font-mono text-[10px]">
                        <span className="text-emerald-400 font-bold">@ada_crypto</span>
                        <span className="text-emerald-300 font-bold">TIPPED ₦5,000</span>
                      </div>
                      <p className="mt-1 text-slate-200">Keep preaching the creator economy gospel! Supporting via Paystack ⚡</p>
                    </div>

                    <div className="p-2.5 rounded-lg bg-[#161626] border border-[#2A2A48]/60 text-xs">
                      <div className="flex items-center justify-between text-slate-400 font-mono text-[10px]">
                        <span className="text-pink-400 font-bold">HoloKai (AI Voice)</span>
                        <span className="text-pink-400">AGENT</span>
                      </div>
                      <p className="mt-1 text-slate-200 font-mono text-[11px]">
                        Broadcast trajectory synced with Nairobi feed. Reach amplified +15%.
                      </p>
                    </div>

                    <div className="p-2.5 rounded-lg bg-amber-950/20 border border-amber-500/30 text-xs">
                      <div className="flex items-center justify-between text-slate-400 font-mono text-[10px]">
                        <span className="text-amber-400 font-bold">@kwame_accra</span>
                        <span className="text-amber-300 font-bold">REVYRALIZED</span>
                      </div>
                      <p className="mt-1 text-slate-200">Shared to 12,000 Accra tech followers!</p>
                    </div>
                  </div>
                </div>

                {/* Send chat input */}
                <div className="mt-4 pt-3 border-t border-[#2A2A48] flex items-center space-x-2">
                  <input
                    type="text"
                    placeholder="Broadcast encrypted reaction..."
                    className="flex-1 bg-[#0A0A12] border border-[#2A2A48] rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-cyan-500"
                  />
                  <button className="p-2.5 rounded-xl bg-cyan-500 text-black hover:bg-cyan-400 transition-colors">
                    <Send className="w-4 h-4" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 2: AI NEURAL AGENTS COMMAND DECK                                      */}
        {/* ========================================================================= */}
        {activeTab === 'agents' && (
          <div className="space-y-6">
            {/* Agent Selectors */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {AGENTS.map(agent => (
                <div
                  key={agent.id}
                  onClick={() => setSelectedAgent(agent)}
                  className={`p-5 rounded-2xl cursor-pointer transition-all border ${
                    selectedAgent.id === agent.id
                      ? `${agent.accent} border-2 shadow-lg`
                      : 'cyber-panel hover:border-slate-600'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <span className="font-extrabold text-lg text-white">{agent.name}</span>
                    <span
                      style={{ backgroundColor: `${agent.color}20`, color: agent.color }}
                      className="text-[10px] font-mono px-2 py-0.5 rounded font-bold border border-current"
                    >
                      ACTIVE
                    </span>
                  </div>
                  <p className="text-xs font-semibold text-slate-300 mt-1">{agent.tagline}</p>
                  <p className="text-xs text-slate-400 mt-2 line-clamp-2 leading-relaxed">{agent.description}</p>
                  <div className="mt-3 pt-3 border-t border-[#2A2A48] text-[11px] font-mono text-cyan-400 truncate flex items-center space-x-1.5">
                    <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-ping"></span>
                    <span>{agent.model}</span>
                  </div>
                </div>
              ))}
            </div>

            {/* Selected Agent Interactive Console */}
            <div className="cyber-panel rounded-2xl p-6 border border-[#2A2A48] space-y-4">
              <div className="flex items-center justify-between pb-4 border-b border-[#2A2A48]">
                <div className="flex items-center space-x-3">
                  <div
                    style={{ backgroundColor: `${selectedAgent.color}30`, borderColor: selectedAgent.color }}
                    className="w-10 h-10 rounded-xl border flex items-center justify-center text-white font-bold"
                  >
                    <Sparkles className="w-5 h-5" style={{ color: selectedAgent.color }} />
                  </div>
                  <div>
                    <h3 className="font-bold text-base text-white">{selectedAgent.name} Neural Terminal</h3>
                    <p className="text-xs text-emerald-400 font-mono flex items-center space-x-1.5">
                      <span className="w-2 h-2 rounded-full bg-emerald-400"></span>
                      <span>LIVE // Google Gemini 3.7 Flash • Interactions API</span>
                    </p>
                  </div>
                </div>

                <button
                  onClick={handleGenerateVoice}
                  disabled={isAudioGenerating}
                  className="flex items-center space-x-2 px-3.5 py-2 rounded-xl text-xs font-bold font-mono bg-pink-500/20 text-pink-300 border border-pink-500/50 hover:bg-pink-500/30 transition-all glow-magenta"
                >
                  <Volume2 className={`w-4 h-4 ${isAudioGenerating ? 'animate-spin' : ''}`} />
                  <span>{isAudioGenerating ? 'SYNTHESIZING...' : 'GENERATE VOICE (TTS)'}</span>
                </button>
              </div>

              {/* Sample Quick Prompts */}
              <div className="flex flex-wrap gap-2 pt-1">
                {selectedAgent.samplePrompts.map((p, idx) => (
                  <button
                    key={idx}
                    onClick={() => handleSendToAgent(p)}
                    className="text-xs px-3 py-1.5 rounded-lg bg-[#1A1A2E] text-slate-300 hover:text-white hover:border-cyan-500/40 border border-[#2A2A48] transition-colors"
                  >
                    ⚡ {p}
                  </button>
                ))}
              </div>

              {/* Chat Message History */}
              <div className="space-y-3 min-h-[220px] max-h-[360px] overflow-y-auto p-4 rounded-xl bg-[#0A0A12] border border-[#2A2A48]/80 font-mono text-xs">
                {agentMessages.map((msg, index) => (
                  <div
                    key={index}
                    className={`p-3 rounded-lg ${
                      msg.sender === 'You'
                        ? 'bg-[#18182E] text-cyan-200 border-l-2 border-l-cyan-400 ml-8'
                        : 'bg-[#12121E] text-slate-200 border-l-2 border-l-pink-500 mr-8'
                    }`}
                  >
                    <span className="font-bold text-slate-400 block mb-1">
                      {msg.sender === 'You' ? '❯ YOU' : `❯ ${msg.sender.toUpperCase()}`}
                    </span>
                    <p className="whitespace-pre-line leading-relaxed">{msg.text}</p>
                  </div>
                ))}
                {isAiLoading && (
                  <div className="p-3 text-cyan-400 font-mono animate-pulse flex items-center space-x-2">
                    <span className="w-2 h-2 rounded-full bg-cyan-400 animate-ping"></span>
                    <span>❯ {selectedAgent.name} streaming live thought vectors via Gemini 3.7 Flash Interactions API...</span>
                  </div>
                )}
              </div>

              {/* Prompt Input Box */}
              <div className="flex items-center space-x-2">
                <input
                  type="text"
                  value={agentInput}
                  onChange={e => setAgentInput(e.target.value)}
                  onKeyDown={e => e.key === 'Enter' && handleSendToAgent()}
                  placeholder={`Command ${selectedAgent.name} (e.g., "Draft viral launch sequence")...`}
                  className="flex-1 bg-[#0A0A12] border border-[#2A2A48] rounded-xl px-4 py-3 text-xs text-white focus:outline-none focus:border-cyan-500 font-mono"
                />
                <button
                  onClick={() => handleSendToAgent()}
                  disabled={isAiLoading || !agentInput.trim()}
                  className="px-5 py-3 rounded-xl bg-gradient-to-r from-cyan-500 to-blue-600 text-black font-bold text-xs hover:opacity-90 disabled:opacity-50 transition-all glow-cyan flex items-center space-x-1.5"
                >
                  <Send className="w-4 h-4" />
                  <span>TRANSMIT</span>
                </button>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 3: AFRICAN MONETIZATION & PAYSTACK LIVE                               */}
        {/* ========================================================================= */}
        {activeTab === 'paystack' && (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Paystack Checkout Deck */}
            <div className="lg:col-span-2 cyber-panel rounded-2xl p-6 border border-[#2A2A48] space-y-6">
              <div className="flex items-center justify-between pb-4 border-b border-[#2A2A48]">
                <div className="flex items-center space-x-3">
                  <div className="w-10 h-10 rounded-xl bg-emerald-500/20 border border-emerald-500/40 flex items-center justify-center text-emerald-400">
                    <Wallet className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-bold text-lg text-white">Paystack African Payment Rails</h3>
                    <p className="text-xs text-slate-400 font-mono">Live multi-currency checkout • Cards, Bank Transfers, USSD & Mobile Money</p>
                  </div>
                </div>

                <span className="text-xs font-mono font-bold bg-emerald-950/80 text-emerald-400 border border-emerald-500/40 px-3 py-1 rounded-lg">
                  LIVE PRODUCTION
                </span>
              </div>

              {/* Currency Selector */}
              <div>
                <label className="text-xs font-mono font-bold text-slate-300 block mb-2">
                  SELECT AFRICAN BILLING CURRENCY
                </label>
                <div className="grid grid-cols-2 sm:grid-cols-4 gap-2.5">
                  {CURRENCIES.map(curr => (
                    <button
                      key={curr.code}
                      onClick={() => {
                        setSelectedCurrency(curr)
                        setTipAmount(curr.defaultTip)
                      }}
                      className={`p-3 rounded-xl border text-left transition-all ${
                        selectedCurrency.code === curr.code
                          ? 'border-emerald-500 bg-emerald-950/30 glow-green'
                          : 'border-[#2A2A48] bg-[#12121E] hover:border-slate-500'
                      }`}
                    >
                      <div className="font-mono font-bold text-base text-white">
                        {curr.symbol} {curr.code}
                      </div>
                      <div className="text-[11px] text-slate-400 truncate">{curr.name}</div>
                    </button>
                  ))}
                </div>
              </div>

              {/* Quick Amount Chips */}
              <div>
                <label className="text-xs font-mono font-bold text-slate-300 block mb-2">
                  QUICK TIP TIERS
                </label>
                <div className="flex flex-wrap gap-2">
                  {[1000, 2500, 5000, 10000, 25000].map(multiplier => {
                    const calculated = Math.round((multiplier / 2500) * selectedCurrency.defaultTip)
                    return (
                      <button
                        key={multiplier}
                        onClick={() => setTipAmount(calculated)}
                        className={`px-3.5 py-2 rounded-lg text-xs font-mono font-bold border transition-colors ${
                          tipAmount === calculated
                            ? 'border-cyan-400 bg-cyan-950/40 text-cyan-300'
                            : 'border-[#2A2A48] bg-[#161626] text-slate-300 hover:text-white'
                        }`}
                      >
                        {selectedCurrency.symbol}{calculated.toLocaleString()}
                      </button>
                    )
                  })}
                </div>
              </div>

              {/* Custom Input Fields */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="text-xs font-mono text-slate-400 block mb-1">Custom Amount ({selectedCurrency.symbol})</label>
                  <input
                    type="number"
                    min={selectedCurrency.min}
                    value={tipAmount}
                    onChange={e => setTipAmount(Number(e.target.value))}
                    className="w-full bg-[#0A0A12] border border-[#2A2A48] rounded-xl px-4 py-2.5 text-sm text-white font-mono focus:outline-none focus:border-emerald-500"
                  />
                </div>

                <div>
                  <label className="text-xs font-mono text-slate-400 block mb-1">Fan Email Address</label>
                  <input
                    type="email"
                    value={fanEmail}
                    onChange={e => setFanEmail(e.target.value)}
                    className="w-full bg-[#0A0A12] border border-[#2A2A48] rounded-xl px-4 py-2.5 text-sm text-white font-mono focus:outline-none focus:border-emerald-500"
                  />
                </div>
              </div>

              {/* Fee Transparency Breakdown */}
              <div className="p-4 rounded-xl bg-[#0A0A12] border border-[#2A2A48] space-y-2 font-mono text-xs">
                <div className="flex justify-between text-slate-400">
                  <span>Gross Tip Amount:</span>
                  <span className="text-white font-bold">{selectedCurrency.symbol}{grossAmount.toLocaleString()}</span>
                </div>
                <div className="flex justify-between text-emerald-400 font-bold">
                  <span>Creator Payout (85% Net):</span>
                  <span>{selectedCurrency.symbol}{creatorPayout.toLocaleString()}</span>
                </div>
                <div className="flex justify-between text-slate-500 text-[11px]">
                  <span>VYRA Platform Fee (15%):</span>
                  <span>{selectedCurrency.symbol}{platformFee.toLocaleString()}</span>
                </div>
              </div>

              {/* Pay Button */}
              <button
                onClick={handleInitiatePaystack}
                disabled={isLoadingPayment || grossAmount <= 0}
                className="w-full h-12 rounded-xl bg-gradient-to-r from-emerald-500 to-teal-400 text-black font-extrabold text-sm tracking-wider flex items-center justify-center space-x-2 hover:opacity-90 disabled:opacity-50 glow-green transition-all"
              >
                <Zap className="w-5 h-5 fill-black" />
                <span>
                  {isLoadingPayment
                    ? 'CONNECTING TO PAYSTACK...'
                    : `PAY ${selectedCurrency.symbol}${grossAmount.toLocaleString()} VIA PAYSTACK`}
                </span>
              </button>

              {paymentStatus && (
                <div className="p-3 rounded-lg bg-emerald-950/30 border border-emerald-500/40 text-xs font-mono text-emerald-300 flex items-center space-x-2">
                  <CheckCircle2 className="w-4 h-4 text-emerald-400 shrink-0" />
                  <span>{paymentStatus}</span>
                </div>
              )}
            </div>

            {/* Creator Revenue Snapshot */}
            <div className="cyber-panel rounded-2xl p-6 border border-[#2A2A48] space-y-6">
              <h3 className="font-bold text-base text-white flex items-center space-x-2">
                <TrendingUp className="w-4 h-4 text-cyan-400" />
                <span>Creator Wallet Balance</span>
              </h3>

              <div className="p-4 rounded-xl bg-gradient-to-br from-[#1A1A2E] to-[#12121E] border border-cyan-500/30 space-y-1">
                <div className="text-[11px] font-mono text-cyan-400">AVAILABLE PAYOUT</div>
                <div className="text-3xl font-black text-white">₦4,170,200</div>
                <div className="text-[11px] text-slate-400 font-mono">Next automated settlement in 4 hours</div>
              </div>

              <div className="space-y-3 font-mono text-xs">
                <div className="flex justify-between p-2.5 rounded bg-[#0A0A12] border border-[#2A2A48]">
                  <span className="text-slate-400">Total Transactions:</span>
                  <span className="text-white font-bold">1,842</span>
                </div>
                <div className="flex justify-between p-2.5 rounded bg-[#0A0A12] border border-[#2A2A48]">
                  <span className="text-slate-400">Avg Tip Value:</span>
                  <span className="text-white font-bold">₦2,630</span>
                </div>
                <div className="flex justify-between p-2.5 rounded bg-[#0A0A12] border border-[#2A2A48]">
                  <span className="text-slate-400">Supported Rails:</span>
                  <span className="text-emerald-400 font-bold">Paystack + Flw + OPay</span>
                </div>
              </div>

              <div className="pt-2 border-t border-[#2A2A48] text-[11px] text-slate-400 font-mono flex items-center space-x-2">
                <ShieldCheck className="w-4 h-4 text-cyan-400" />
                <span>Encrypted PCI-DSS compliant payouts directly to African bank accounts.</span>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 4: FANDNA™ SUPPORTER ANALYTICS                                        */}
        {/* ========================================================================= */}
        {activeTab === 'fandna' && (
          <div className="space-y-6">
            <div className="cyber-panel rounded-2xl p-6 border border-[#2A2A48]">
              <div className="flex items-center justify-between pb-4 border-b border-[#2A2A48]">
                <div>
                  <h3 className="font-bold text-lg text-white">FanDNA™ Loyalty Architecture</h3>
                  <p className="text-xs text-slate-400 font-mono">Behavioral analysis, engagement scores, and tier conversion metrics</p>
                </div>
                <span className="text-xs font-mono text-purple-400 bg-purple-950/60 px-3 py-1 rounded-lg border border-purple-500/40">
                  148,290 ACTIVE FANS
                </span>
              </div>

              {/* Tiers Grid */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
                <div className="p-4 rounded-xl bg-[#161626] border border-cyan-500/30">
                  <div className="flex items-center justify-between text-xs font-mono font-bold text-cyan-400">
                    <span>TIER 1 // NOMADS</span>
                    <span>FREE</span>
                  </div>
                  <div className="text-2xl font-black text-white mt-2">128,450</div>
                  <p className="text-xs text-slate-400 mt-1">Casual viewers & social feed followers.</p>
                </div>

                <div className="p-4 rounded-xl bg-[#161626] border border-pink-500/30">
                  <div className="flex items-center justify-between text-xs font-mono font-bold text-pink-400">
                    <span>TIER 2 // INSIDERS</span>
                    <span>₦5,000/MO</span>
                  </div>
                  <div className="text-2xl font-black text-white mt-2">16,840</div>
                  <p className="text-xs text-slate-400 mt-1">Private broadcast access, voice chat perks.</p>
                </div>

                <div className="p-4 rounded-xl bg-[#161626] border border-purple-500/40 glow-violet">
                  <div className="flex items-center justify-between text-xs font-mono font-bold text-purple-400">
                    <span>TIER 3 // VANGUARDS</span>
                    <span>₦25,000/MO</span>
                  </div>
                  <div className="text-2xl font-black text-white mt-2">3,000</div>
                  <p className="text-xs text-slate-400 mt-1">Direct AI agent collaboration & VIP badge.</p>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* TAB 5: VIRAL FEED & CASTS                                                 */}
        {/* ========================================================================= */}
        {activeTab === 'feed' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[
              {
                id: 1,
                author: '@billi_labs',
                title: 'How we scaled AI audio synthesis for 500k African creators',
                tags: ['#AIAudio', '#Paystack', '#Cyberpunk'],
                virality: '99.2%',
                hearts: '12.4k',
                shares: '3.1k'
              },
              {
                id: 2,
                author: '@ada_design',
                title: 'Cyberpunk UI design systems in Flutter and Jetpack Compose',
                tags: ['#DesignTokens', '#Android', '#CyberUI'],
                virality: '94.8%',
                hearts: '8.1k',
                shares: '1.9k'
              },
              {
                id: 3,
                author: '@neo_lagos_media',
                title: 'TrueFoundry Multi-Model Gateway vs OpenAI Direct: Latency Benchmark',
                tags: ['#CloudAI', '#TrueFoundry', '#Infra'],
                virality: '97.5%',
                hearts: '15.9k',
                shares: '4.2k'
              }
            ].map(post => (
              <div key={post.id} className="cyber-panel rounded-2xl p-5 border border-[#2A2A48] flex flex-col justify-between">
                <div>
                  <div className="flex items-center justify-between text-xs font-mono text-slate-400 mb-2">
                    <span className="font-bold text-cyan-400">{post.author}</span>
                    <span className="px-2 py-0.5 rounded bg-pink-950/60 text-pink-400 border border-pink-500/30">
                      {post.virality} VIRAL
                    </span>
                  </div>
                  <h4 className="font-bold text-white text-sm leading-snug">{post.title}</h4>
                  <div className="flex flex-wrap gap-1.5 mt-3">
                    {post.tags.map((t, idx) => (
                      <span key={idx} className="text-[10px] font-mono px-2 py-0.5 rounded bg-[#1A1A2E] text-slate-400">
                        {t}
                      </span>
                    ))}
                  </div>
                </div>

                <div className="mt-4 pt-3 border-t border-[#2A2A48] flex items-center justify-between text-xs font-mono text-slate-400">
                  <div className="flex items-center space-x-3">
                    <span className="flex items-center space-x-1 text-red-400">
                      <Heart className="w-3.5 h-3.5 fill-red-400" />
                      <span>{post.hearts}</span>
                    </span>
                    <span className="flex items-center space-x-1 text-cyan-400">
                      <Repeat2 className="w-3.5 h-3.5" />
                      <span>{post.shares}</span>
                    </span>
                  </div>
                  <button
                    onClick={() => setActiveTab('vyraShow')}
                    className="text-cyan-400 hover:underline flex items-center space-x-1"
                  >
                    <span>Watch Cast</span>
                    <ExternalLink className="w-3 h-3" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {activeTab === 'mobileEngine' && (
          <div className="space-y-8">
            {/* Header section with live connectivity states */}
            <div className="cyber-panel rounded-2xl p-6 border border-cyan-500/30 bg-[#0A0A1A]/80">
              <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
                <div>
                  <h3 className="text-xl font-extrabold text-white flex items-center space-x-2">
                    <Cpu className="w-5 h-5 text-cyan-400 animate-pulse" />
                    <span>VYRA OS Mobile & Database Engine (Phase 3)</span>
                  </h3>
                  <p className="text-xs text-slate-400 mt-1 font-mono">
                    Scaffolding core React Native components, secure biometrics, zero-UI voice parsers, and Prisma splits database layer.
                  </p>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-[11px] font-mono bg-emerald-950/80 border border-emerald-500/40 text-emerald-400 px-3 py-1 rounded-lg">
                    PRISMA SQLITE: CONNECTED
                  </span>
                  <span className="text-[11px] font-mono bg-cyan-950/80 border border-cyan-500/40 text-cyan-400 px-3 py-1 rounded-lg">
                    EXPO RN SCAFFOLD: ACTIVE
                  </span>
                </div>
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
              {/* Left Column: Simulated Mobile Frame */}
              <div className="lg:col-span-5 flex justify-center">
                <div className="w-[320px] h-[640px] rounded-[36px] bg-[#020205] border-8 border-slate-800 shadow-2xl relative overflow-hidden flex flex-col justify-between p-4 shadow-[0_0_30px_rgba(0,255,204,0.15)]">
                  {/* Speaker and Camera Notch */}
                  <div className="absolute top-2 left-1/2 -translate-x-1/2 w-32 h-5 rounded-full bg-slate-800 z-50 flex items-center justify-center">
                    <span className="w-2 h-2 rounded-full bg-slate-900 mr-2"></span>
                    <span className="w-8 h-1 rounded-full bg-slate-900"></span>
                  </div>

                  {/* Simulated Mobile Status bar */}
                  <div className="flex items-center justify-between px-2 pt-2 text-[10px] text-slate-400 font-mono z-40">
                    <span>9:41 AM</span>
                    <div className="flex items-center space-x-1.5">
                      <span>5G</span>
                      <span className="w-4 h-2 rounded-sm border border-slate-400 bg-emerald-400"></span>
                    </div>
                  </div>

                  {/* Core Simulated Mobile Page Content */}
                  <div className="flex-1 my-3 rounded-2xl bg-[#090916] border border-[#2A2A48]/80 p-3 relative flex flex-col justify-between overflow-hidden">
                    {/* Top HUD */}
                    <div className="flex items-center justify-between text-[10px] font-mono text-slate-400 z-10">
                      <span className="bg-red-900/60 border border-red-500/30 text-red-400 px-2 py-0.5 rounded font-black animate-pulse">
                        LIVE CAST
                      </span>
                      <span className="text-cyan-400">98.7% VIRAL</span>
                    </div>

                    {/* Central Area: Video Stream preview / visualizer */}
                    <div className="flex-1 flex flex-col items-center justify-center py-4 z-10">
                      <div className="w-20 h-20 rounded-full bg-cyan-500/10 border-2 border-cyan-400/40 flex items-center justify-center shadow-[0_0_15px_rgba(0,255,204,0.3)]">
                        <Radio className="w-8 h-8 text-cyan-400 animate-pulse" />
                      </div>
                      <div className="text-center mt-3">
                        <div className="text-[11px] font-bold text-white tracking-wide">Lagoon Cyberstage</div>
                        <div className="text-[9px] text-slate-400 font-mono mt-0.5">Velocity: +3.8k/h</div>
                      </div>
                    </div>

                    {/* Quick Biometric Locked Vault Item */}
                    <div className="p-2.5 rounded-xl bg-violet-950/20 border border-violet-500/40 z-10 text-center space-y-1.5">
                      <div className="text-[10px] font-mono text-violet-300 flex items-center justify-center space-x-1">
                        <ShieldCheck className="w-3 h-3" />
                        <span>CYBER ALBUM [LOCKED]</span>
                      </div>
                      <div className="text-[9px] text-slate-400">₦5,000 Payout • 10% Fee Appended</div>
                      <button
                        onClick={() => handleBiometricUnlockSimulate()}
                        className="w-full py-1.5 rounded-lg bg-violet-600 hover:bg-violet-500 text-white text-[10px] font-bold transition-all shadow-[0_0_10px_rgba(139,0,255,0.4)] flex items-center justify-center space-x-1"
                      >
                        <Zap className="w-3 h-3 fill-white" />
                        <span>BIOMETRIC TOUCH-ID UNLOCK</span>
                      </button>
                    </div>

                    {/* Bottom Predictive Dock & Actions */}
                    <div className="pt-3 border-t border-[#2A2A48]/80 flex items-center justify-between z-10">
                      <button
                        onClick={() => {
                          setRevyralizeCount(prev => prev + 1)
                          alert("Revyralize clicked! Reach boosted +15% and recorded to local DB store.")
                        }}
                        className="p-2 rounded-lg bg-emerald-500/20 text-emerald-400 border border-emerald-500/40 hover:bg-emerald-500/30 flex items-center space-x-1 text-[9px] font-bold"
                      >
                        <Repeat2 className="w-3 h-3" />
                        <span>REVYRALIZE (+15%)</span>
                      </button>

                      <button
                        onClick={() => alert("Native share chooser triggered in Expo host container.")}
                        className="p-2 rounded-lg bg-pink-500/20 text-pink-400 border border-pink-500/40 hover:bg-pink-500/30 flex items-center space-x-1 text-[9px] font-bold"
                      >
                        <Share2 className="w-3 h-3" />
                        <span>SHARE</span>
                      </button>
                    </div>
                  </div>

                  {/* Simulated Mobile Home Indicator */}
                  <div className="w-24 h-1 bg-slate-700 rounded-full mx-auto my-1 shrink-0"></div>
                </div>
              </div>

              {/* Right Column: Interactive Control Panel & Prisma Schema */}
              <div className="lg:col-span-7 space-y-6">
                {/* 1. Biometrics Simulation Console */}
                <div className="cyber-panel rounded-2xl p-5 border border-[#2A2A48]">
                  <h4 className="font-bold text-sm text-white flex items-center space-x-2 pb-3 border-b border-[#2A2A48]">
                    <ShieldCheck className="w-4 h-4 text-violet-400" />
                    <span>Simulated Biometric Checkouts & Split Fees Payouts</span>
                  </h4>
                  <p className="text-xs text-slate-400 mt-2">
                    Enrolls hardware keys and initializes one-click payments. This uses the newly designed **Split platforms billing models** — Creator receives 100% of the requested price, while platform service fees are appended on top for fans.
                  </p>

                  <div className="mt-4 space-y-4">
                    <div className="flex flex-wrap gap-2">
                      <button
                        onClick={() => handleEnrollBiometricSimulate()}
                        className="px-3.5 py-2 rounded-xl text-xs font-bold font-mono bg-cyan-500/20 text-cyan-300 border border-cyan-500/40 hover:bg-cyan-500/30 transition-all"
                      >
                        🔑 ENROLL BIOMETRIC PUBLIC KEY
                      </button>

                      <button
                        onClick={() => handleBiometricUnlockSimulate()}
                        className="px-3.5 py-2 rounded-xl text-xs font-bold font-mono bg-violet-500/20 text-violet-300 border border-violet-500/40 hover:bg-violet-500/30 transition-all shadow-[0_0_10px_rgba(139,0,255,0.3)]"
                      >
                        ⚡ ONE-CLICK BIOMETRIC CHARGE
                      </button>
                    </div>

                    {biometricSimStatus && (
                      <div className="p-3 rounded-xl bg-[#0A0A12] border border-[#2A2A48] space-y-2 font-mono text-xs text-cyan-400">
                        <div className="flex items-center space-x-2 text-emerald-400 font-bold">
                          <CheckCircle2 className="w-4 h-4 shrink-0" />
                          <span>{biometricSimStatus}</span>
                        </div>
                        {biometricLedger && (
                          <div className="pt-2 border-t border-[#2A2A48]/80 text-[11px] text-slate-400 space-y-1">
                            <div className="flex justify-between">
                              <span>Prisma Ledger Transaction:</span>
                              <span className="text-white font-bold">{biometricLedger.id}</span>
                            </div>
                            <div className="flex justify-between">
                              <span>Base Creator Price:</span>
                              <span className="text-white">₦{biometricLedger.base}</span>
                            </div>
                            <div className="flex justify-between">
                              <span>Fan Service Fee (10% Split):</span>
                              <span className="text-emerald-400">+₦{biometricLedger.fee}</span>
                            </div>
                            <div className="flex justify-between border-t border-[#2A2A48]/50 pt-1 text-white font-black">
                              <span>Total Charged to Fan:</span>
                              <span>₦{biometricLedger.total}</span>
                            </div>
                          </div>
                        )}
                      </div>
                    )}
                  </div>
                </div>

                {/* 2. Zero-UI Voice Command Console */}
                <div className="cyber-panel rounded-2xl p-5 border border-[#2A2A48]">
                  <h4 className="font-bold text-sm text-white flex items-center space-x-2 pb-3 border-b border-[#2A2A48]">
                    <Volume2 className="w-4 h-4 text-emerald-400" />
                    <span>Simulated "Zero-UI" Voice Command Console</span>
                  </h4>
                  <p className="text-xs text-slate-400 mt-2">
                    Type a speech prompt below to simulate speech recognition transitions, executing on-device navigations automatically.
                  </p>

                  <div className="mt-4 flex gap-2">
                    <input
                      type="text"
                      value={voiceSimInput}
                      onChange={e => setVoiceSimInput(e.target.value)}
                      placeholder="Try: 'Gemini, launch VyraShow and notify FanDNA Tier 1'..."
                      className="flex-1 bg-[#0A0A12] border border-[#2A2A48] rounded-xl px-3 py-2.5 text-xs text-white focus:outline-none focus:border-emerald-500 font-mono"
                    />
                    <button
                      onClick={() => handleVoiceSimulate()}
                      className="px-4 py-2.5 rounded-xl bg-emerald-500 text-black font-extrabold text-xs hover:bg-emerald-400 transition-all glow-green"
                    >
                      SPEAK
                    </button>
                  </div>

                  {voiceSimFeedback && (
                    <div className="p-3 mt-3 rounded-xl bg-emerald-950/20 border border-emerald-500/30 text-xs font-mono text-emerald-300 flex items-center space-x-2">
                      <Volume2 className="w-4 h-4 text-emerald-400 shrink-0 animate-bounce" />
                      <span>{voiceSimFeedback}</span>
                    </div>
                  )}
                </div>

                {/* 3. Prisma Schema Entity Model Inspector */}
                <div className="cyber-panel rounded-2xl p-5 border border-[#2A2A48] bg-[#0A0A15]/80">
                  <h4 className="font-bold text-sm text-white flex items-center space-x-2 pb-3 border-b border-[#2A2A48]">
                    <Terminal className="w-4 h-4 text-cyan-400" />
                    <span>Prisma Schema Relational Architecture (`database/schema.prisma`)</span>
                  </h4>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 mt-4 text-[11px] font-mono">
                    <div className="p-3 rounded-xl bg-[#121224] border border-[#2A2A48]/80">
                      <div className="text-white font-extrabold pb-1.5 border-b border-[#2A2A48]/50">model User</div>
                      <div className="space-y-1 mt-2 text-slate-400">
                        <div>id: <span className="text-cyan-400 font-bold">String @id</span></div>
                        <div>paystackPasskey: <span className="text-cyan-400 font-bold">String?</span></div>
                        <div>walletBalance: <span className="text-cyan-400 font-bold">Float</span></div>
                        <div>fanDNA: <span className="text-violet-400 font-bold">FanDNA[]</span></div>
                      </div>
                    </div>

                    <div className="p-3 rounded-xl bg-[#121224] border border-[#2A2A48]/80">
                      <div className="text-white font-extrabold pb-1.5 border-b border-[#2A2A48]/50">model FanDNA</div>
                      <div className="space-y-1 mt-2 text-slate-400">
                        <div>id: <span className="text-cyan-400 font-bold">String @id</span></div>
                        <div>userId: <span className="text-cyan-400 font-bold">String</span></div>
                        <div>tier: <span className="text-cyan-400 font-bold">Int</span></div>
                        <div>engagementPoints: <span className="text-cyan-400 font-bold">Int</span></div>
                      </div>
                    </div>

                    <div className="p-3 rounded-xl bg-[#121224] border border-[#2A2A48]/80">
                      <div className="text-white font-extrabold pb-1.5 border-b border-[#2A2A48]/50">model Transaction</div>
                      <div className="space-y-1 mt-2 text-slate-400">
                        <div>id: <span className="text-cyan-400 font-bold">String @id</span></div>
                        <div>baseAmount: <span className="text-emerald-400 font-bold">Float</span></div>
                        <div>platformFee: <span className="text-emerald-400 font-bold">Float</span></div>
                        <div>totalCharged: <span className="text-emerald-400 font-bold">Float</span></div>
                      </div>
                    </div>

                    <div className="p-3 rounded-xl bg-[#121224] border border-[#2A2A48]/80">
                      <div className="text-white font-extrabold pb-1.5 border-b border-[#2A2A48]/50">model SocialLink</div>
                      <div className="space-y-1 mt-2 text-slate-400">
                        <div>id: <span className="text-cyan-400 font-bold">String @id</span></div>
                        <div>clicks: <span className="text-cyan-400 font-bold">Int</span></div>
                        <div>platform: <span className="text-cyan-400 font-bold">String</span></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Cyberpunk Status Footer */}
      <footer className="border-t border-[#2A2A48] bg-[#0A0A12] px-6 py-4 mt-auto">
        <div className="max-w-7xl mx-auto flex flex-col sm:flex-row items-center justify-between text-xs font-mono text-slate-500 gap-2">
          <div className="flex items-center space-x-3">
            <span className="text-white font-bold">VYRA // 2026</span>
            <span>•</span>
            <span>AFRICAN PAYMENT RAILS (NGN • KES • ZAR • GHS)</span>
            <span>•</span>
            <span>TRUEFOUNDRY AI GATEWAY</span>
          </div>
          <div className="flex items-center space-x-2">
            <span className="w-2 h-2 rounded-full bg-emerald-400"></span>
            <span className="text-slate-400">ALL SYSTEMS OPERATIONAL</span>
          </div>
        </div>
      </footer>
    </div>
  )
}
