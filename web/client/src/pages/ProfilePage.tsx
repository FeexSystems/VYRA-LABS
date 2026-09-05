import { useState } from 'react'
import {
  CheckCircle2,
  ExternalLink,
  CreditCard,
  Globe2
} from 'lucide-react'

export default function ProfilePage() {
  const [persona, setPersona] = useState<'creator' | 'fan'>('creator')
  const [socialHandles] = useState([
    { platform: 'Twitter / X', handle: '@vyra_creator', url: 'https://x.com' },
    { platform: 'YouTube', handle: 'VYRA Cyber Studios', url: 'https://youtube.com' },
    { platform: 'Instagram', handle: '@vyra.official', url: 'https://instagram.com' },
    { platform: 'TikTok', handle: '@vyra_live', url: 'https://tiktok.com' }
  ])

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header Profile Card */}
      <div className="cyber-panel rounded-2xl p-6 sm:p-8 border border-[#22223C] relative overflow-hidden">
        <div className="absolute top-0 right-0 w-64 h-64 bg-cyan-500/5 rounded-full blur-3xl pointer-events-none"></div>

        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-6 relative z-10">
          <div className="flex items-center space-x-5">
            <div className="w-20 h-20 rounded-2xl bg-gradient-to-tr from-cyan-500 via-indigo-500 to-pink-500 p-1 flex items-center justify-center glow-cyan shrink-0">
              <div className="w-full h-full bg-[#0B0B16] rounded-xl flex items-center justify-center text-white text-2xl font-black">
                VC
              </div>
            </div>

            <div>
              <div className="flex items-center space-x-3">
                <h1 className="text-2xl font-black text-white">Vanguard Creator Alpha</h1>
                <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-emerald-950 text-emerald-400 border border-emerald-800 flex items-center space-x-1">
                  <CheckCircle2 className="w-3.5 h-3.5" />
                  <span>VERIFIED</span>
                </span>
              </div>
              <p className="text-xs font-mono text-cyan-400 mt-1">@vanguard_lagos • Node ID: VYRA-8849-NG</p>
              <p className="text-xs text-slate-400 mt-2 max-w-xl leading-relaxed">
                Afro-futurist synthesizer artist & broadcast curator. Engineering next-generation sonic visualizers and community-owned wealth rails.
              </p>
            </div>
          </div>

          {/* Persona Switcher Button */}
          <div className="flex flex-col sm:items-end gap-2">
            <span className="text-[10px] font-mono text-slate-400">ACTIVE PERSONA</span>
            <button
              onClick={() => setPersona(persona === 'creator' ? 'fan' : 'creator')}
              className={`px-5 py-2.5 rounded-xl text-xs font-mono font-bold tracking-wider transition-all border ${
                persona === 'creator'
                  ? 'bg-cyan-500/20 border-cyan-500/60 text-cyan-300 glow-cyan'
                  : 'bg-pink-500/20 border-pink-500/60 text-pink-300 glow-magenta'
              }`}
            >
              {persona === 'creator' ? '⚡ CREATOR MODE' : '🛡️ FAN SUPPORTER MODE'}
            </button>
          </div>
        </div>
      </div>

      {/* Tabs & Details */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left: Revenue Vault */}
        <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
            <h3 className="font-bold text-base text-white flex items-center space-x-2">
              <CreditCard className="w-4 h-4 text-emerald-400" />
              <span>Earnings Vault (Paystack)</span>
            </h3>
            <span className="text-xs font-mono text-emerald-400 font-bold">85% NET</span>
          </div>

          <div className="p-4 rounded-xl bg-[#0B0B16] border border-[#202038] space-y-2 font-mono">
            <span className="text-[10px] text-slate-400">LIFETIME EARNINGS</span>
            <p className="text-2xl font-black text-emerald-300">₦4,850,200</p>
            <div className="text-[11px] text-slate-400 pt-2 border-t border-[#1A1A30] flex justify-between">
              <span>Next Auto-Settlement:</span>
              <span className="text-white font-bold">Today, 23:59</span>
            </div>
          </div>

          <div className="space-y-2 text-xs font-mono text-slate-400">
            <div className="flex justify-between">
              <span>Settlement Account:</span>
              <span className="text-white">GTBank •••• 9102</span>
            </div>
            <div className="flex justify-between">
              <span>Verified Country:</span>
              <span className="text-white">Nigeria (NGN)</span>
            </div>
          </div>
        </div>

        {/* Right: Social Media Handles */}
        <div className="lg:col-span-2 cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
            <h3 className="font-bold text-base text-white flex items-center space-x-2">
              <Globe2 className="w-4 h-4 text-cyan-400" />
              <span>Dynamic Social Handles</span>
            </h3>
            <span className="text-xs font-mono text-slate-400">SYNCED</span>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 font-mono text-xs">
            {socialHandles.map(s => (
              <div
                key={s.platform}
                className="p-3.5 rounded-xl bg-[#0E0E1C] border border-[#222240] flex items-center justify-between hover:border-slate-500 transition-all"
              >
                <div>
                  <span className="text-[10px] text-slate-400 block">{s.platform}</span>
                  <span className="text-white font-bold mt-0.5 block">{s.handle}</span>
                </div>
                <a
                  href={s.url}
                  target="_blank"
                  rel="noreferrer"
                  className="p-2 rounded-lg bg-[#141428] text-slate-300 hover:text-white"
                >
                  <ExternalLink className="w-3.5 h-3.5" />
                </a>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
