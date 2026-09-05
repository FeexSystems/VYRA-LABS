import { useState } from 'react'
import {
  CreditCard,
  Cpu,
  CheckCircle2,
  Save
} from 'lucide-react'

export default function SettingsPage() {
  const [currency, setCurrency] = useState<'NGN' | 'KES' | 'ZAR' | 'GHS'>('NGN')
  const [modelChoice, setModelChoice] = useState('gemini-3.7-flash')
  const [ttsEngine, setTtsEngine] = useState('canopylabs')
  const [notificationsEnabled, setNotificationsEnabled] = useState(true)
  const [isSaved, setIsSaved] = useState(false)

  const handleSave = () => {
    setIsSaved(true)
    setTimeout(() => setIsSaved(false), 2500)
  }

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#1E1E36]">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>PLATFORM SETTINGS & PROTOCOL CONFIG</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-cyan-950 text-cyan-400 border border-cyan-800">
              SYS_ADMIN
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Manage billing currency defaults, AI gateway routing, and neural voice synthesis engines
          </p>
        </div>

        <button
          onClick={handleSave}
          className="px-5 py-2.5 rounded-xl bg-gradient-to-r from-cyan-500 to-blue-600 text-black font-extrabold text-xs font-mono tracking-wider hover:opacity-90 transition-all glow-cyan flex items-center space-x-2"
        >
          {isSaved ? <CheckCircle2 className="w-4 h-4 text-black" /> : <Save className="w-4 h-4" />}
          <span>{isSaved ? 'CONFIG SAVED' : 'SAVE PREFERENCES'}</span>
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Billing & Settlement Currency */}
        <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
          <div className="flex items-center space-x-2 pb-3 border-b border-[#1E1E36]">
            <CreditCard className="w-4 h-4 text-emerald-400" />
            <h3 className="font-bold text-sm text-white font-mono">PREFERRED BILLING CURRENCY</h3>
          </div>

          <div className="space-y-2 font-mono text-xs">
            {[
              { id: 'NGN', name: 'Nigerian Naira (₦)', desc: 'Primary rail for Paystack direct bank settlement' },
              { id: 'KES', name: 'Kenyan Shilling (KSh)', desc: 'M-Pesa and local East African card processing' },
              { id: 'ZAR', name: 'South African Rand (R)', desc: 'EFT and instant payment rails' },
              { id: 'GHS', name: 'Ghanaian Cedi (GH₵)', desc: 'MTN MoMo and Ghanaian banking network' }
            ].map(c => (
              <label
                key={c.id}
                onClick={() => setCurrency(c.id as any)}
                className={`p-3.5 rounded-xl border flex items-center justify-between cursor-pointer transition-all ${
                  currency === c.id
                    ? 'bg-emerald-500/15 border-emerald-500/50 text-white'
                    : 'bg-[#0E0E1C] border-[#222240] text-slate-400 hover:text-white'
                }`}
              >
                <div>
                  <span className="font-bold block text-sm">{c.name}</span>
                  <span className="text-[10px] text-slate-500 block mt-0.5">{c.desc}</span>
                </div>
                <input
                  type="radio"
                  name="currency"
                  checked={currency === c.id}
                  onChange={() => setCurrency(c.id as any)}
                  className="accent-emerald-400"
                />
              </label>
            ))}
          </div>
        </div>

        {/* AI Gateway Architecture */}
        <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
          <div className="flex items-center space-x-2 pb-3 border-b border-[#1E1E36]">
            <Cpu className="w-4 h-4 text-cyan-400" />
            <h3 className="font-bold text-sm text-white font-mono">AI GATEWAY & NEURAL ENGINE</h3>
          </div>

          <div className="space-y-3 font-mono text-xs">
            <div>
              <label className="text-[11px] text-slate-400 block mb-1">PRIMARY LLM INTERACTION ENGINE</label>
              <select
                value={modelChoice}
                onChange={e => setModelChoice(e.target.value)}
                className="w-full bg-[#0A0A14] border border-[#22223C] rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-cyan-500 font-mono"
              >
                <option value="gemini-3.7-flash">Google Gemini 3.7 Flash (Interactions API - Active)</option>
                <option value="claude-haiku">TrueFoundry / Anthropic Claude Haiku 4.5</option>
                <option value="grok-latest">TrueFoundry / xAI Grok Build Latest</option>
              </select>
            </div>

            <div>
              <label className="text-[11px] text-slate-400 block mb-1">NEURAL SPEECH (TTS) VOICEOVER ENGINE</label>
              <select
                value={ttsEngine}
                onChange={e => setTtsEngine(e.target.value)}
                className="w-full bg-[#0A0A14] border border-[#22223C] rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-cyan-500 font-mono"
              >
                <option value="canopylabs">CanopyLabs Orpheus V1 (Autum, Diana, Hannah)</option>
                <option value="openai-tts">OpenAI GPT-4o-mini-TTS (Alloy, Echo, Shimmer)</option>
              </select>
            </div>

            <div className="pt-2 border-t border-[#1E1E36] flex items-center justify-between">
              <div>
                <span className="font-bold text-slate-200 block text-xs">Real-Time Fan Notifications</span>
                <span className="text-[10px] text-slate-500 block">Browser notifications for incoming tips & revyralizes</span>
              </div>
              <button
                onClick={() => setNotificationsEnabled(!notificationsEnabled)}
                className={`px-3 py-1.5 rounded-lg font-bold text-xs ${
                  notificationsEnabled ? 'bg-cyan-500 text-black' : 'bg-slate-800 text-slate-400'
                }`}
              >
                {notificationsEnabled ? 'ENABLED' : 'DISABLED'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
