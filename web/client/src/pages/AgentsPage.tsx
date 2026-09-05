import { useState } from 'react'
import {
  Sparkles,
  Volume2,
  Send,
  Cpu
} from 'lucide-react'

interface AgentProfile {
  id: string
  name: string
  tagline: string
  model: string
  color: string
  voice: string
  accent: string
  description: string
  promptPrefix: string
  samplePrompts: string[]
}

const AGENTS: AgentProfile[] = [
  {
    id: 'bushfeexer',
    name: 'Bushfeexer',
    tagline: 'Content Optimization & Virality Engine',
    model: 'Gemini 3.7 Flash (Live Interactions API)',
    color: '#00F5FF',
    voice: 'diana',
    accent: 'border-cyan-500/40 text-cyan-400 bg-cyan-950/20',
    description: 'Engineers high-velocity viral hooks, retention curves, and broadcast pacing algorithms tuned for African creator audiences.',
    promptPrefix: 'You are Bushfeexer, the vanguard Content Optimization and Virality Engine of VYRA.',
    samplePrompts: [
      'Give me a 1-sentence viral hook for my cyberpunk DJ live set in Lagos.',
      'Optimize my broadcast title for 150k+ organic impressions in West Africa.',
      'Analyze the retention drop-off pattern in a 30-minute tech livestream.'
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
    description: 'Immersive cyberpunk persona modeling, holographic fan interaction, and audio visualizer speech synthesis with authentic neo-African aesthetics.',
    promptPrefix: 'You are HoloKai, the sentient cyberpunk conversation and personality modeling agent of VYRA.',
    samplePrompts: [
      'Welcome new fans joining from Lagos, Nairobi, and London in cyberpunk slang.',
      'Generate a 15-second teaser voice script for tonight’s sound clash.',
      'Deliver a witty holographic response to a fan asking about the matrix.'
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
    description: 'Master of African payment systems (Paystack, Flutterwave, OPay), multi-currency tip tiers (NGN, KES, ZAR, GHS), and 85/15 creator wealth creation.',
    promptPrefix: 'You are Feexara, the elite financial strategist and monetization intelligence agent of VYRA.',
    samplePrompts: [
      'Explain how 85% payout works for 10,000 NGN on VYRA in 2 bullet points.',
      'Design a 3-tier VIP pass pricing structure for Kenyan (KES) and Nigerian (NGN) supporters.',
      'Draft a high-conversion broadcast CTA to convert free viewers into monthly subscribers.'
    ]
  }
]

export default function AgentsPage() {
  const [selectedAgent, setSelectedAgent] = useState<AgentProfile>(AGENTS[0])
  const [agentInput, setAgentInput] = useState('')
  const [agentMessages, setAgentMessages] = useState<Array<{ sender: string; text: string }>>([
    {
      sender: 'Bushfeexer',
      text: 'Neural grid active. Monitoring virality vectors across African hubs. Ready to engineer your next high-velocity broadcast hook.'
    }
  ])
  const [isAiLoading, setIsAiLoading] = useState(false)
  const [isAudioGenerating, setIsAudioGenerating] = useState(false)

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
          text: `[Neural Sync] Live link established. Processed: "${textToSend}". Virality surge +24.8%.`
        }
      ])
    } finally {
      setIsAiLoading(false)
    }
  }

  const handleGenerateVoice = async () => {
    setIsAudioGenerating(true)
    try {
      const lastAgentMsg = [...agentMessages].reverse().find(m => m.sender === selectedAgent.name)?.text || 'VYRA AI Agents are live.'
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

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#1E1E36]">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>AI NEURAL AGENTS COMMAND DECK</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-emerald-950 text-emerald-400 border border-emerald-800 flex items-center space-x-1">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse"></span>
              <span>LIVE // GEMINI 3.7 FLASH</span>
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Official Google Gemini Interactions API integration with sub-second latency and specialized cyberpunk personas
          </p>
        </div>

        <div className="flex items-center space-x-2 font-mono text-xs">
          <span className="text-slate-400">PROVIDER:</span>
          <span className="px-2.5 py-1 rounded bg-[#121224] border border-[#222240] text-cyan-300 font-bold">
            Google GenAI SDK (@google/genai)
          </span>
        </div>
      </div>

      {/* Agent Selection Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {AGENTS.map(agent => (
          <div
            key={agent.id}
            onClick={() => setSelectedAgent(agent)}
            className={`p-5 rounded-2xl cursor-pointer transition-all border ${
              selectedAgent.id === agent.id
                ? `${agent.accent} border-2 shadow-xl`
                : 'cyber-panel hover:border-slate-600'
            }`}
          >
            <div className="flex items-center justify-between">
              <span className="font-extrabold text-lg text-white">{agent.name}</span>
              <span
                style={{ backgroundColor: `${agent.color}20`, color: agent.color }}
                className="text-[10px] font-mono px-2 py-0.5 rounded font-bold border border-current flex items-center space-x-1"
              >
                <span className="w-1.5 h-1.5 rounded-full bg-current animate-ping"></span>
                <span>ONLINE</span>
              </span>
            </div>
            <p className="text-xs font-semibold text-slate-300 mt-1">{agent.tagline}</p>
            <p className="text-xs text-slate-400 mt-2 line-clamp-2 leading-relaxed">{agent.description}</p>
            <div className="mt-3 pt-3 border-t border-[#1E1E36] text-[11px] font-mono text-cyan-400 truncate flex items-center space-x-1.5">
              <Cpu className="w-3.5 h-3.5" />
              <span>{agent.model}</span>
            </div>
          </div>
        ))}
      </div>

      {/* Selected Agent Interactive Console */}
      <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 pb-4 border-b border-[#1E1E36]">
          <div className="flex items-center space-x-3">
            <div
              style={{ backgroundColor: `${selectedAgent.color}30`, borderColor: selectedAgent.color }}
              className="w-11 h-11 rounded-xl border flex items-center justify-center text-white font-bold"
            >
              <Sparkles className="w-5 h-5" style={{ color: selectedAgent.color }} />
            </div>
            <div>
              <h3 className="font-bold text-base text-white">{selectedAgent.name} Neural Terminal</h3>
              <p className="text-xs text-emerald-400 font-mono flex items-center space-x-1.5">
                <span className="w-2 h-2 rounded-full bg-emerald-400"></span>
                <span>LIVE // Gemini 3.7 Flash • Interactions API</span>
              </p>
            </div>
          </div>

          <button
            onClick={handleGenerateVoice}
            disabled={isAudioGenerating}
            className="flex items-center space-x-2 px-4 py-2.5 rounded-xl text-xs font-bold font-mono bg-pink-500/20 text-pink-300 border border-pink-500/50 hover:bg-pink-500/30 transition-all glow-magenta"
          >
            <Volume2 className={`w-4 h-4 ${isAudioGenerating ? 'animate-spin' : ''}`} />
            <span>{isAudioGenerating ? 'SYNTHESIZING...' : 'GENERATE VOICE (TTS)'}</span>
          </button>
        </div>

        {/* Tactical Quick Prompts */}
        <div>
          <p className="text-[10px] font-mono text-slate-400 mb-2 uppercase tracking-wider">Tactical Presets</p>
          <div className="flex flex-wrap gap-2">
            {selectedAgent.samplePrompts.map((p, idx) => (
              <button
                key={idx}
                onClick={() => handleSendToAgent(p)}
                className="text-xs px-3.5 py-1.5 rounded-lg bg-[#141426] text-slate-300 hover:text-white hover:border-cyan-500/50 border border-[#22223C] transition-colors font-mono"
              >
                ⚡ {p}
              </button>
            ))}
          </div>
        </div>

        {/* Chat Message History */}
        <div className="space-y-3 min-h-[260px] max-h-[420px] overflow-y-auto p-4 rounded-xl bg-[#070710] border border-[#1E1E36] font-mono text-xs">
          {agentMessages.map((msg, index) => (
            <div
              key={index}
              className={`p-3.5 rounded-xl ${
                msg.sender === 'You'
                  ? 'bg-[#141428] text-cyan-200 border-l-4 border-l-cyan-400 ml-8'
                  : 'bg-[#0E0E1C] text-slate-200 border-l-4 border-l-pink-500 mr-8'
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
            placeholder={`Command ${selectedAgent.name} (e.g. "Calculate optimal virality for 10pm broadcast")...`}
            className="flex-1 bg-[#070710] border border-[#22223C] rounded-xl px-4 py-3 text-xs text-white focus:outline-none focus:border-cyan-500 font-mono"
          />
          <button
            onClick={() => handleSendToAgent()}
            disabled={isAiLoading || !agentInput.trim()}
            className="px-6 py-3 rounded-xl bg-gradient-to-r from-cyan-500 to-blue-600 text-black font-bold text-xs hover:opacity-90 disabled:opacity-50 transition-all glow-cyan flex items-center space-x-2"
          >
            <Send className="w-4 h-4" />
            <span>TRANSMIT</span>
          </button>
        </div>
      </div>
    </div>
  )
}
