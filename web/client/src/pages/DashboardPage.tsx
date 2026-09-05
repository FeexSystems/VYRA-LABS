import { useState } from 'react'
import {
  Users,
  Globe2,
  ArrowUpRight,
  ShieldCheck,
  Activity,
  Bot,
  CreditCard,
  Repeat2
} from 'lucide-react'
import { Link } from 'react-router-dom'

export default function DashboardPage() {
  const [timeframe, setTimeframe] = useState<'24h' | '7d' | '30d'>('7d')

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#1E1E36]">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>CREATOR ANALYTICS DASHBOARD</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-cyan-950 text-cyan-400 border border-cyan-800">
              PRO HUD
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Real-time telemetry across African broadcasting nodes (Lagos, Nairobi, Accra, Johannesburg)
          </p>
        </div>

        {/* Timeframe selector */}
        <div className="flex items-center space-x-1 bg-[#121222] border border-[#22223C] rounded-xl p-1 text-xs font-mono">
          {(['24h', '7d', '30d'] as const).map(tf => (
            <button
              key={tf}
              onClick={() => setTimeframe(tf)}
              className={`px-3 py-1.5 rounded-lg font-bold transition-all ${
                timeframe === tf
                  ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/50'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              {tf.toUpperCase()}
            </button>
          ))}
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {/* Metric 1 */}
        <div className="cyber-panel p-5 rounded-2xl border border-[#22223C]">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>TOTAL BROADCAST REACH</span>
            <Users className="w-4 h-4 text-cyan-400" />
          </div>
          <div className="mt-3 flex items-baseline justify-between">
            <span className="text-3xl font-black text-white font-mono">1,482,900</span>
            <span className="text-xs font-bold font-mono text-emerald-400 flex items-center">
              <ArrowUpRight className="w-3.5 h-3.5 mr-0.5" /> +28.4%
            </span>
          </div>
          <p className="text-[11px] font-mono text-slate-500 mt-2">Unique impressions this cycle</p>
        </div>

        {/* Metric 2 */}
        <div className="cyber-panel p-5 rounded-2xl border border-[#22223C]">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>REVYRALIZE MULTIPLIER</span>
            <Repeat2 className="w-4 h-4 text-emerald-400" />
          </div>
          <div className="mt-3 flex items-baseline justify-between">
            <span className="text-3xl font-black text-emerald-300 font-mono">4.82x</span>
            <span className="text-xs font-bold font-mono text-emerald-400 flex items-center">
              <ArrowUpRight className="w-3.5 h-3.5 mr-0.5" /> +15.2%
            </span>
          </div>
          <p className="text-[11px] font-mono text-slate-500 mt-2">Organic reach amplification</p>
        </div>

        {/* Metric 3 */}
        <div className="cyber-panel p-5 rounded-2xl border border-[#22223C]">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>NET CREATOR EARNINGS</span>
            <CreditCard className="w-4 h-4 text-amber-400" />
          </div>
          <div className="mt-3 flex items-baseline justify-between">
            <span className="text-3xl font-black text-amber-300 font-mono">₦4,850,200</span>
            <span className="text-xs font-bold font-mono text-emerald-400 flex items-center">
              <ArrowUpRight className="w-3.5 h-3.5 mr-0.5" /> +34.0%
            </span>
          </div>
          <p className="text-[11px] font-mono text-slate-500 mt-2">85% Net Payout via Paystack</p>
        </div>

        {/* Metric 4 */}
        <div className="cyber-panel p-5 rounded-2xl border border-[#22223C]">
          <div className="flex items-center justify-between text-xs font-mono text-slate-400">
            <span>AI AGENT VELOCITY</span>
            <Bot className="w-4 h-4 text-pink-400" />
          </div>
          <div className="mt-3 flex items-baseline justify-between">
            <span className="text-3xl font-black text-pink-400 font-mono">142ms</span>
            <span className="text-xs font-bold font-mono text-emerald-400 flex items-center">
              <ShieldCheck className="w-3.5 h-3.5 mr-0.5" /> LIVE
            </span>
          </div>
          <p className="text-[11px] font-mono text-slate-500 mt-2">Gemini 3.7 Flash avg latency</p>
        </div>
      </div>

      {/* Charts & Geospatial Virality Matrix */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Virality Trend Simulation */}
        <div className="lg:col-span-2 cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
            <div>
              <h3 className="font-bold text-sm text-white flex items-center space-x-2">
                <Activity className="w-4 h-4 text-cyan-400" />
                <span>Audience Engagement & Virality Velocity Flow</span>
              </h3>
              <p className="text-xs font-mono text-slate-400">Hour-by-hour propagation across decentralized viewer nodes</p>
            </div>
            <span className="text-xs font-mono text-emerald-400 bg-emerald-950/60 px-2 py-0.5 rounded border border-emerald-500/30">
              +3.8k vel/h
            </span>
          </div>

          {/* Graphical Bar Visualization */}
          <div className="h-48 flex items-end justify-between gap-2 pt-6 pb-2 px-2">
            {[45, 60, 52, 78, 65, 88, 92, 84, 98, 75, 89, 95, 91, 99].map((val, idx) => (
              <div key={idx} className="flex-1 flex flex-col items-center gap-2 group">
                <div
                  style={{ height: `${val * 1.6}px` }}
                  className={`w-full rounded-t-md transition-all duration-300 group-hover:brightness-125 ${
                    val > 90
                      ? 'bg-gradient-to-t from-cyan-500 to-pink-500 shadow-[0_0_12px_rgba(0,245,255,0.4)]'
                      : 'bg-gradient-to-t from-[#1A1A30] to-cyan-500/60'
                  }`}
                />
                <span className="text-[9px] font-mono text-slate-400 group-hover:text-cyan-300">
                  {idx + 10}:00
                </span>
              </div>
            ))}
          </div>

          <div className="flex items-center justify-between text-xs font-mono text-slate-400 pt-3 border-t border-[#1E1E36]">
            <span className="flex items-center space-x-2">
              <span className="w-2.5 h-2.5 rounded-full bg-cyan-400"></span>
              <span>Organic Cast Reach</span>
            </span>
            <span className="flex items-center space-x-2">
              <span className="w-2.5 h-2.5 rounded-full bg-pink-500"></span>
              <span>Revyralize Multiplier Impact</span>
            </span>
            <span className="text-emerald-400 font-bold">Peak: 148.2k Concurrent</span>
          </div>
        </div>

        {/* African Regional Virality Hubs */}
        <div className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
            <h3 className="font-bold text-sm text-white flex items-center space-x-2">
              <Globe2 className="w-4 h-4 text-emerald-400" />
              <span>African Hub Virality</span>
            </h3>
            <span className="text-[10px] font-mono text-slate-400">REALTIME</span>
          </div>

          <div className="space-y-3.5">
            {[
              { city: 'Lagos, Nigeria', currency: 'NGN', pct: 44, amount: '₦2,134,000', flag: '🇳🇬' },
              { city: 'Nairobi, Kenya', currency: 'KES', pct: 28, amount: 'KSh 1,358,000', flag: '🇰🇪' },
              { city: 'Johannesburg, SA', currency: 'ZAR', pct: 16, amount: 'R 776,000', flag: '🇿🇦' },
              { city: 'Accra, Ghana', currency: 'GHS', pct: 12, amount: 'GH₵ 582,000', flag: '🇬🇭' }
            ].map(hub => (
              <div key={hub.city} className="space-y-1">
                <div className="flex items-center justify-between text-xs">
                  <span className="font-bold text-slate-200 flex items-center space-x-1.5">
                    <span>{hub.flag}</span>
                    <span>{hub.city}</span>
                  </span>
                  <span className="font-mono text-emerald-300 font-semibold">{hub.amount}</span>
                </div>
                <div className="w-full bg-[#161628] rounded-full h-2 overflow-hidden">
                  <div
                    style={{ width: `${hub.pct}%` }}
                    className="bg-gradient-to-r from-emerald-500 to-cyan-400 h-full rounded-full"
                  />
                </div>
                <div className="flex items-center justify-between text-[10px] font-mono text-slate-400">
                  <span>{hub.currency} Rail</span>
                  <span>{hub.pct}% of total volume</span>
                </div>
              </div>
            ))}
          </div>

          <Link
            to="/monetization"
            className="w-full py-2.5 mt-2 rounded-xl bg-[#121222] border border-[#22223C] text-xs font-mono font-bold text-center text-slate-300 hover:text-white hover:border-cyan-500/50 transition-all flex items-center justify-center space-x-1"
          >
            <span>VIEW PAYSTACK SETTLEMENTS</span>
            <ArrowUpRight className="w-3.5 h-3.5" />
          </Link>
        </div>
      </div>
    </div>
  )
}
