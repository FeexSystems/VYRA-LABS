import { useState, useEffect } from 'react'
import {
  Radio,
  Repeat2,
  Share2,
  Heart,
  Zap,
  TrendingUp,
  Play,
  Pause,
  Terminal,
  Send,
  Check
} from 'lucide-react'
import { Link } from 'react-router-dom'

export default function VyraShowPage() {
  const [revyralizeCount, setRevyralizeCount] = useState(148290)
  const [hasRevyralized, setHasRevyralized] = useState(false)
  const [heartsCount, setHeartsCount] = useState(4821)
  const [hasHearted, setHasHearted] = useState(false)
  const [isPlayingVisualizer, setIsPlayingVisualizer] = useState(true)
  const [copiedShare, setCopiedShare] = useState(false)
  const [chatInput, setChatInput] = useState('')
  const [chatMessages, setChatMessages] = useState([
    { user: '@neo_lagos_vanguard', time: '14:02', text: 'The audio visualizer frequency response is incredible tonight 🔥', color: 'text-cyan-400' },
    { user: '@ada_crypto', time: '14:04', text: 'TIPPED ₦5,000! Keep preaching the creator economy gospel! Supporting via Paystack ⚡', color: 'text-emerald-400', isTip: true },
    { user: 'HoloKai (AI Voice)', time: '14:05', text: 'Broadcast trajectory synced with Nairobi feed. Reach amplified +15%.', color: 'text-pink-400', isAgent: true },
    { user: '@kwame_accra', time: '14:06', text: 'REVYRALIZED! Shared to 12,000 Accra tech followers!', color: 'text-amber-400' }
  ])

  // Waveform equalizer bars
  const [waveformHeights, setWaveformHeights] = useState<number[]>([40, 65, 30, 85, 95, 45, 70, 60, 90, 50, 75, 40, 85, 60, 70, 95])
  useEffect(() => {
    if (!isPlayingVisualizer) return
    const interval = setInterval(() => {
      setWaveformHeights(prev => prev.map(() => Math.floor(Math.random() * 75) + 20))
    }, 180)
    return () => clearInterval(interval)
  }, [isPlayingVisualizer])

  const handleRevyralize = () => {
    if (!hasRevyralized) {
      setRevyralizeCount(prev => prev + 22243)
      setHasRevyralized(true)
      setChatMessages(prev => [
        ...prev,
        {
          user: '@you (Creator)',
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
          text: 'REVYRALIZED! Amplifying broadcast network reach by +15%!',
          color: 'text-emerald-400'
        }
      ])
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

  const handleShareClick = () => {
    navigator.clipboard?.writeText(window.location.href)
    setCopiedShare(true)
    setTimeout(() => setCopiedShare(false), 2000)
  }

  const handleSendChat = (e?: React.FormEvent) => {
    e?.preventDefault()
    if (!chatInput.trim()) return
    setChatMessages(prev => [
      ...prev,
      {
        user: '@you',
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        text: chatInput,
        color: 'text-cyan-400'
      }
    ])
    setChatInput('')
  }

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* ========================================================================= */}
      {/* 1. VIRALITY GAUGE HUD                                                     */}
      {/* ========================================================================= */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {/* Gauge 1: Real-time Virality */}
        <div className="cyber-panel p-4 rounded-2xl border border-[#22223C] relative overflow-hidden group">
          <div className="absolute top-0 right-0 w-24 h-24 bg-cyan-500/10 rounded-full blur-2xl pointer-events-none"></div>
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>VIRALITY GAUGE</span>
            <span className="w-2 h-2 rounded-full bg-cyan-400 animate-ping"></span>
          </div>
          <div className="mt-2 flex items-baseline space-x-2">
            <span className="text-3xl lg:text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 to-blue-500 font-mono">
              98.7%
            </span>
            <span className="text-xs font-bold font-mono text-cyan-300 px-1.5 py-0.5 rounded bg-cyan-950 border border-cyan-800">
              VIRAL
            </span>
          </div>
          <p className="text-[11px] text-slate-400 mt-1 font-mono">Top 1% Global Transmission</p>
        </div>

        {/* Gauge 2: Velocity Surge */}
        <div className="cyber-panel p-4 rounded-2xl border border-[#22223C] relative overflow-hidden">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>VELOCITY SURGE</span>
            <TrendingUp className="w-4 h-4 text-emerald-400" />
          </div>
          <div className="mt-2 flex items-baseline space-x-2">
            <span className="text-3xl lg:text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 to-green-500 font-mono">
              +3.8k
            </span>
            <span className="text-xs font-bold font-mono text-emerald-400">vel/h</span>
          </div>
          <p className="text-[11px] text-slate-400 mt-1 font-mono">Accelerating in Lagos & Nairobi</p>
        </div>

        {/* Gauge 3: Total Reach */}
        <div className="cyber-panel p-4 rounded-2xl border border-[#22223C] relative overflow-hidden">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>BROADCAST REACH</span>
            <Radio className="w-4 h-4 text-pink-400" />
          </div>
          <div className="mt-2 flex items-baseline space-x-2">
            <span className="text-3xl lg:text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-pink-400 to-violet-500 font-mono">
              {revyralizeCount.toLocaleString()}
            </span>
          </div>
          <p className="text-[11px] text-slate-400 mt-1 font-mono">+15% per Revyralize share</p>
        </div>

        {/* Gauge 4: Net Payout */}
        <div className="cyber-panel p-4 rounded-2xl border border-[#22223C] relative overflow-hidden">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>CREATOR EARNINGS</span>
            <Zap className="w-4 h-4 text-amber-400" />
          </div>
          <div className="mt-2 flex items-baseline space-x-2">
            <span className="text-3xl lg:text-4xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-amber-400 to-yellow-500 font-mono">
              ₦4,850,200
            </span>
          </div>
          <p className="text-[11px] text-slate-400 mt-1 font-mono">85% Net via Paystack</p>
        </div>
      </div>

      {/* ========================================================================= */}
      {/* 2. MAIN BROADCAST CANVAS & NEURAL CHAT                                    */}
      {/* ========================================================================= */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Large Media Stage */}
        <div className="lg:col-span-2 cyber-panel rounded-2xl p-6 border border-[#22223C] flex flex-col justify-between relative overflow-hidden">
          {/* Top Stage Bar */}
          <div className="flex items-center justify-between pb-4 border-b border-[#1E1E36]">
            <div className="flex items-center space-x-2.5">
              <span className="px-2.5 py-1 rounded-full text-xs font-mono font-bold bg-red-500/20 text-red-400 border border-red-500/40 flex items-center space-x-1.5">
                <span className="w-2 h-2 rounded-full bg-red-500 animate-ping"></span>
                <span>LIVE CAST</span>
              </span>
              <span className="text-xs font-mono text-slate-400">CH-088 // NEO-AFRICA</span>
            </div>

            <div className="flex items-center space-x-2">
              <button
                onClick={() => setIsPlayingVisualizer(!isPlayingVisualizer)}
                className="p-2 rounded-xl bg-[#121222] border border-[#22223C] text-slate-300 hover:text-white"
              >
                {isPlayingVisualizer ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4" />}
              </button>
            </div>
          </div>

          {/* Interactive Cyber Media Visualizer Screen */}
          <div className="my-6 relative min-h-[300px] sm:min-h-[360px] rounded-xl bg-gradient-to-b from-[#0B0B14] via-[#090912] to-[#05050A] border border-[#1A1A30] flex flex-col items-center justify-center p-6 overflow-hidden">
            {/* Background Grid Accent */}
            <div className="absolute inset-0 bg-[radial-gradient(circle_at_center,rgba(0,245,255,0.06)_0%,transparent_70%)]"></div>

            {/* Glowing Orb */}
            <div className="relative mb-6">
              <div className="w-28 h-28 rounded-full bg-gradient-to-tr from-cyan-500/20 to-pink-500/20 blur-xl absolute inset-0 animate-pulse"></div>
              <div className="w-28 h-28 rounded-full border-2 border-cyan-400/50 flex items-center justify-center relative bg-[#070710]/80">
                <Radio className="w-12 h-12 text-cyan-400 animate-pulse" />
              </div>
            </div>

            {/* Realtime Waveform Equalizer */}
            <div className="flex items-end space-x-1 sm:space-x-1.5 h-20 mb-4 z-10">
              {waveformHeights.map((h, i) => (
                <div
                  key={i}
                  style={{ height: `${h}%` }}
                  className={`w-1.5 sm:w-2.5 rounded-full transition-all duration-150 ${
                    i % 3 === 0
                      ? 'bg-cyan-400 shadow-[0_0_8px_rgba(0,245,255,0.8)]'
                      : i % 3 === 1
                      ? 'bg-pink-500 shadow-[0_0_8px_rgba(255,0,122,0.8)]'
                      : 'bg-emerald-400 shadow-[0_0_8px_rgba(0,255,135,0.8)]'
                  }`}
                />
              ))}
            </div>

            <div className="text-center z-10">
              <h2 className="font-extrabold text-base sm:text-lg tracking-wider text-white">
                CYBERPUNK BROADCAST: AFRO-FUTURIST SONIC SYNCHRONIZATION
              </h2>
              <p className="text-xs text-slate-400 mt-1 font-mono">
                Featuring HoloKai AI Neural Voiceover • Real-time encrypted feed to 148k viewers
              </p>
            </div>
          </div>

          {/* Action Bar with 52dp Prominent Revyralize and Share Buttons */}
          <div className="flex flex-wrap items-center justify-between gap-3 pt-4 border-t border-[#1E1E36]">
            <div className="flex items-center space-x-3">
              {/* Revyralize Button (52dp / 52px height) */}
              <button
                onClick={handleRevyralize}
                className={`h-12 px-6 rounded-xl font-black text-xs tracking-wider flex items-center space-x-2 transition-all ${
                  hasRevyralized
                    ? 'bg-emerald-500 text-black glow-green shadow-lg'
                    : 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/50 hover:bg-emerald-500/30'
                }`}
              >
                <Repeat2 className="w-5 h-5" />
                <span>{hasRevyralized ? 'REVYRALIZED (+15%)' : 'REVYRALIZE (+15% REACH)'}</span>
              </button>

              {/* Share Button (52dp / 52px height) */}
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
                    : 'bg-[#121222] border-[#22223C] text-slate-300 hover:text-white'
                }`}
              >
                <Heart className={`w-4 h-4 ${hasHearted ? 'fill-red-500 text-red-500 animate-bounce' : ''}`} />
                <span>{heartsCount.toLocaleString()}</span>
              </button>

              {/* Tip Button */}
              <Link
                to="/monetization"
                className="h-12 px-4 rounded-xl flex items-center space-x-2 text-xs font-bold bg-amber-500/20 border border-amber-500/40 text-amber-300 hover:bg-amber-500/30 transition-all"
              >
                <Zap className="w-4 h-4 text-amber-400" />
                <span>TIP CREATOR</span>
              </Link>
            </div>
          </div>
        </div>

        {/* Side Real-Time Live Ticker & Neural Chat Stream */}
        <div className="cyber-panel rounded-2xl p-5 border border-[#22223C] flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
              <div className="flex items-center space-x-2">
                <Terminal className="w-4 h-4 text-cyan-400" />
                <span className="font-bold text-sm text-white">Neural Chat Stream</span>
              </div>
              <span className="text-[10px] font-mono text-emerald-400 bg-emerald-950/60 px-2 py-0.5 rounded border border-emerald-500/30">
                ENCRYPTED
              </span>
            </div>

            {/* Chat items */}
            <div className="space-y-3 mt-4 max-h-[380px] overflow-y-auto pr-1">
              {chatMessages.map((msg, idx) => (
                <div
                  key={idx}
                  className={`p-2.5 rounded-lg border text-xs ${
                    msg.isTip
                      ? 'bg-emerald-950/20 border-emerald-500/40'
                      : msg.isAgent
                      ? 'bg-pink-950/20 border-pink-500/40'
                      : 'bg-[#121222] border-[#22223C]/60'
                  }`}
                >
                  <div className="flex items-center justify-between text-slate-400 font-mono text-[10px]">
                    <span className={`font-bold ${msg.color}`}>{msg.user}</span>
                    <span>{msg.time}</span>
                  </div>
                  <p className="mt-1 text-slate-200">{msg.text}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Send chat input */}
          <form onSubmit={handleSendChat} className="mt-4 pt-3 border-t border-[#1E1E36] flex items-center space-x-2">
            <input
              type="text"
              value={chatInput}
              onChange={e => setChatInput(e.target.value)}
              placeholder="Broadcast encrypted reaction..."
              className="flex-1 bg-[#0A0A14] border border-[#22223C] rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-cyan-500 font-mono"
            />
            <button
              type="submit"
              disabled={!chatInput.trim()}
              className="p-2.5 rounded-xl bg-cyan-500 text-black hover:bg-cyan-400 disabled:opacity-50 transition-colors"
            >
              <Send className="w-4 h-4" />
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
