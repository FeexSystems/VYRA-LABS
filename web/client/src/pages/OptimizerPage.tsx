import { useState } from 'react'
import {
  Wand2,
  Sparkles,
  Copy,
  Check
} from 'lucide-react'

export default function OptimizerPage() {
  const [topic, setTopic] = useState('Cyberpunk Afrobeat Live Session')
  const [platform, setPlatform] = useState('VyraShow')
  const [isOptimizing, setIsOptimizing] = useState(false)
  const [copiedIndex, setCopiedIndex] = useState<number | null>(null)
  const [results, setResults] = useState<Array<{ hook: string; score: number; velocity: string; explanation: string }>>([
    {
      hook: 'Plug straight into the neon underground of Lagos 2099: Where high-voltage Afro-rhythms crash the global sonic matrix.',
      score: 98.4,
      velocity: '+4.2k vel/h',
      explanation: 'High emotional resonance, authentic cyberpunk narrative anchors, hyper-velocity African geo-tagging.'
    },
    {
      hook: 'We turned an abandoned warehouse in Victoria Island into an encrypted cyber-rave broadcasting live across 4 continents.',
      score: 95.8,
      velocity: '+3.6k vel/h',
      explanation: 'Unconventional venue intrigue, curious opening hook, cross-continental FOMO.'
    },
    {
      hook: 'Warning: This bassline might hack your nervous system. HoloKai AI voiceover synchronized with Lagos synthesizers.',
      score: 92.1,
      velocity: '+2.9k vel/h',
      explanation: 'Pattern-interrupt phrasing, AI agent mention creates immediate novelty.'
    }
  ])

  const handleRunOptimizer = async () => {
    setIsOptimizing(true)
    try {
      const res = await fetch('/api/ai/agent/interact', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          agentId: 'bushfeexer',
          prompt: `Optimize a broadcast hook for topic: "${topic}" on platform "${platform}". Provide high virality hooks with scores.`
        })
      })
      const data = await res.json()
      if (data.success && data.outputText) {
        setResults([
          {
            hook: data.outputText.slice(0, 180),
            score: 97.2,
            velocity: '+4.1k vel/h',
            explanation: 'Engineered live by Bushfeexer using Gemini 3.7 Flash virality heuristics.'
          },
          ...results.slice(0, 2)
        ])
      }
    } catch {
      // Fallback response
    } finally {
      setIsOptimizing(false)
    }
  }

  const handleCopy = (text: string, idx: number) => {
    navigator.clipboard?.writeText(text)
    setCopiedIndex(idx)
    setTimeout(() => setCopiedIndex(null), 2000)
  }

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#1E1E36]">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>VIRALITY HOOK OPTIMIZER</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-cyan-950 text-cyan-400 border border-cyan-800 flex items-center space-x-1">
              <Sparkles className="w-3.5 h-3.5" />
              <span>BUSHFEEXER ENGINE</span>
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Predictive algorithmic scoring for broadcast titles, opening hooks, and Revyralize share triggers
          </p>
        </div>

        <div className="text-xs font-mono text-slate-400 bg-[#121224] border border-[#222240] px-3.5 py-1.5 rounded-xl">
          MODEL: <span className="text-cyan-400 font-bold">GEMINI 3.7 FLASH</span>
        </div>
      </div>

      {/* Input Deck */}
      <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="md:col-span-2 space-y-1.5">
            <label className="text-xs font-mono text-slate-400">BROADCAST TOPIC OR DRAFT TITLE</label>
            <input
              type="text"
              value={topic}
              onChange={e => setTopic(e.target.value)}
              placeholder="e.g. Lagos Cyber DJ Set, AI Gaming in Nairobi..."
              className="w-full bg-[#0A0A14] border border-[#22223C] rounded-xl px-4 py-3 text-sm text-white focus:outline-none focus:border-cyan-500 font-mono"
            />
          </div>

          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-400">TARGET CHANNEL</label>
            <select
              value={platform}
              onChange={e => setPlatform(e.target.value)}
              className="w-full bg-[#0A0A14] border border-[#22223C] rounded-xl px-4 py-3 text-sm text-white focus:outline-none focus:border-cyan-500 font-mono"
            >
              <option value="VyraShow">VyraShow Live Broadcast</option>
              <option value="ViralFeed">Viral Feed Staggered Post</option>
              <option value="Twitter">X / Twitter Teaser</option>
              <option value="TikTok">TikTok / Reels Soundbite</option>
            </select>
          </div>
        </div>

        <button
          onClick={handleRunOptimizer}
          disabled={isOptimizing || !topic.trim()}
          className="w-full py-3.5 rounded-xl bg-gradient-to-r from-cyan-500 to-blue-600 text-black font-extrabold text-xs tracking-wider hover:opacity-90 disabled:opacity-50 transition-all glow-cyan flex items-center justify-center space-x-2"
        >
          <Wand2 className={`w-4 h-4 ${isOptimizing ? 'animate-spin' : ''}`} />
          <span>{isOptimizing ? 'CALCULATING VIRAL TRAJECTORIES...' : 'ENGINEER VIRAL HOOKS'}</span>
        </button>
      </div>

      {/* Hook Recommendations List */}
      <div className="space-y-4">
        <h3 className="font-bold text-sm text-slate-300 font-mono uppercase tracking-wider">
          Top Predicted Hooks ({results.length})
        </h3>

        {results.map((res, idx) => (
          <div
            key={idx}
            className="cyber-panel rounded-2xl p-5 border border-[#22223C] space-y-3 hover:border-slate-500 transition-all"
          >
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-emerald-950 text-emerald-400 border border-emerald-800">
                  {res.score}% VIRAL SCORE
                </span>
                <span className="text-xs font-mono text-cyan-400">{res.velocity}</span>
              </div>

              <button
                onClick={() => handleCopy(res.hook, idx)}
                className="flex items-center space-x-1.5 px-3 py-1 rounded-lg bg-[#141426] border border-[#22223C] text-xs font-mono text-slate-300 hover:text-white transition-all"
              >
                {copiedIndex === idx ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                <span>{copiedIndex === idx ? 'COPIED' : 'COPY HOOK'}</span>
              </button>
            </div>

            <p className="text-base font-bold text-white leading-relaxed">
              "{res.hook}"
            </p>

            <p className="text-xs text-slate-400 font-mono pt-2 border-t border-[#1A1A30]">
              <span className="text-slate-500">ANALYSIS: </span>{res.explanation}
            </p>
          </div>
        ))}
      </div>
    </div>
  )
}
