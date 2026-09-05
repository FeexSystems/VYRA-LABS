import { useState } from 'react'
import {
  Dna,
  TrendingUp
} from 'lucide-react'

export default function FanDnaPage() {
  const [activeTier, setActiveTier] = useState<'all' | 'nomads' | 'insiders' | 'vanguards'>('all')

  const fanTiers = [
    {
      id: 'nomads',
      name: 'Nomads',
      tag: 'Tier 1 // Discovery',
      count: '112,400',
      pct: 75.8,
      avgSpend: '₦500 - ₦1,500',
      color: 'border-slate-500/40 text-slate-300 bg-slate-950/20',
      badge: 'CASUAL LISTENERS',
      description: 'Browsers discovering transmissions via algorithmic viral feeds and Revyralize reposts.'
    },
    {
      id: 'insiders',
      name: 'Insiders',
      tag: 'Tier 2 // Sustained',
      count: '31,250',
      pct: 21.1,
      avgSpend: '₦2,500 - ₦15,000',
      color: 'border-cyan-500/40 text-cyan-300 bg-cyan-950/20',
      badge: 'CORE SUPPORTERS',
      description: 'Monthly tip subscribers, active in live broadcast chats, unlocking exclusive stems and drops.'
    },
    {
      id: 'vanguards',
      name: 'Vanguards',
      tag: 'Tier 3 // Sovereign',
      count: '4,640',
      pct: 3.1,
      avgSpend: '₦25,000 - ₦150,000+',
      color: 'border-pink-500/40 text-pink-300 bg-pink-950/20',
      badge: 'PATRON WHALES',
      description: 'Top patrons with direct VIP holographic channels, exclusive backstage passes, and custom AI agent greetings.'
    }
  ]

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#1E1E36]">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>FANDNA™ AUDIENCE INTELLIGENCE</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-pink-950 text-pink-400 border border-pink-800 flex items-center space-x-1">
              <Dna className="w-3.5 h-3.5" />
              <span>BEHAVIOR MATRIX</span>
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Algorithmic segmentation clustering viewers by monetization velocity, attention span, and retention loyalty
          </p>
        </div>

        <div className="flex items-center space-x-1 bg-[#121222] border border-[#22223C] rounded-xl p-1 text-xs font-mono">
          {(['all', 'nomads', 'insiders', 'vanguards'] as const).map(tier => (
            <button
              key={tier}
              onClick={() => setActiveTier(tier)}
              className={`px-3 py-1.5 rounded-lg font-bold transition-all ${
                activeTier === tier
                  ? 'bg-pink-500/20 text-pink-300 border border-pink-500/50'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              {tier.toUpperCase()}
            </button>
          ))}
        </div>
      </div>

      {/* Tier Overview Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
        {fanTiers.map(tier => (
          <div
            key={tier.id}
            className={`cyber-panel rounded-2xl p-6 border ${tier.color} space-y-4 hover:scale-[1.01] transition-transform`}
          >
            <div className="flex items-center justify-between">
              <span className="text-xs font-mono text-slate-400">{tier.tag}</span>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-black/40 border border-current font-bold">
                {tier.badge}
              </span>
            </div>

            <div>
              <h3 className="text-2xl font-black text-white">{tier.name}</h3>
              <div className="flex items-baseline space-x-2 mt-1">
                <span className="text-3xl font-mono font-bold text-white">{tier.count}</span>
                <span className="text-xs font-mono text-emerald-400">({tier.pct}%)</span>
              </div>
            </div>

            <p className="text-xs text-slate-400 leading-relaxed">{tier.description}</p>

            <div className="pt-3 border-t border-[#1E1E36] flex items-center justify-between text-xs font-mono">
              <span className="text-slate-400">Avg Value / Season:</span>
              <span className="font-bold text-emerald-300">{tier.avgSpend}</span>
            </div>
          </div>
        ))}
      </div>

      {/* Fan Loyalty & Conversion Flow Deck */}
      <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
        <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
          <h3 className="font-bold text-base text-white flex items-center space-x-2">
            <TrendingUp className="w-4 h-4 text-cyan-400" />
            <span>Audience Conversion Funnel (Nomad ➔ Insider ➔ Vanguard)</span>
          </h3>
          <span className="text-xs font-mono text-cyan-400 font-bold">Conversion Rate: 8.4%</span>
        </div>

        <div className="space-y-4 font-mono text-xs">
          <div className="space-y-1">
            <div className="flex justify-between text-slate-300">
              <span>Nomads (Discovery Stage)</span>
              <span>112,400 (100%)</span>
            </div>
            <div className="w-full bg-[#141426] h-3 rounded-full overflow-hidden">
              <div className="bg-slate-400 h-full rounded-full w-full"></div>
            </div>
          </div>

          <div className="space-y-1">
            <div className="flex justify-between text-cyan-300">
              <span>Insiders (First Tip / Subscribed)</span>
              <span>31,250 (27.8%)</span>
            </div>
            <div className="w-full bg-[#141426] h-3 rounded-full overflow-hidden">
              <div className="bg-cyan-400 h-full rounded-full w-[27.8%]"></div>
            </div>
          </div>

          <div className="space-y-1">
            <div className="flex justify-between text-pink-300">
              <span>Vanguards (Repeat Whales & VIPs)</span>
              <span>4,640 (4.1%)</span>
            </div>
            <div className="w-full bg-[#141426] h-3 rounded-full overflow-hidden">
              <div className="bg-pink-500 h-full rounded-full w-[4.1%]"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
