import { useState, type ReactNode } from 'react'
import { Link, useLocation } from 'react-router-dom'
import {
  Radio,
  LayoutDashboard,
  Bot,
  MessageSquare,
  Flame,
  CreditCard,
  Dna,
  Wand2,
  User,
  Settings,
  Zap,
  Menu,
  X,
  ChevronRight,
  Terminal
} from 'lucide-react'

interface LayoutProps {
  children: ReactNode
}

export const NAV_ITEMS = [
  {
    path: '/',
    label: 'VyraShow',
    sublabel: 'Live Broadcast Hub',
    icon: Radio,
    badge: 'LIVE 98%',
    badgeColor: 'bg-emerald-500/20 text-emerald-400 border-emerald-500/40'
  },
  {
    path: '/console',
    label: 'System Console',
    sublabel: 'Mobile Engine & DB',
    icon: Terminal,
    badge: 'V2.4 ALPHA',
    badgeColor: 'bg-cyan-500/20 text-cyan-400 border-cyan-500/40'
  },
  {
    path: '/dashboard',
    label: 'Dashboard',
    sublabel: 'Creator Analytics',
    icon: LayoutDashboard
  },
  {
    path: '/agents',
    label: 'AI Neural Agents',
    sublabel: 'Gemini 3.7 Flash',
    icon: Bot,
    badge: '3 ACTIVE',
    badgeColor: 'bg-cyan-500/20 text-cyan-400 border-cyan-500/40'
  },
  {
    path: '/chat',
    label: 'Neural Chat',
    sublabel: 'Encrypted Stream',
    icon: MessageSquare,
    badge: '148K',
    badgeColor: 'bg-pink-500/20 text-pink-400 border-pink-500/40'
  },
  {
    path: '/feed',
    label: 'Viral Feed',
    sublabel: 'Staggered Grid',
    icon: Flame,
    badge: 'TRENDING',
    badgeColor: 'bg-amber-500/20 text-amber-400 border-amber-500/40'
  },
  {
    path: '/monetization',
    label: 'African Rails',
    sublabel: 'Paystack & Tips',
    icon: CreditCard,
    badge: '85% NET',
    badgeColor: 'bg-emerald-500/20 text-emerald-400 border-emerald-500/40'
  },
  {
    path: '/fandna',
    label: 'FanDNA™',
    sublabel: 'Audience Tiers',
    icon: Dna
  },
  {
    path: '/optimizer',
    label: 'Virality Optimizer',
    sublabel: 'Hook Velocity',
    icon: Wand2
  },
  {
    path: '/profile',
    label: 'Creator Profile',
    sublabel: 'Persona & Vault',
    icon: User
  },
  {
    path: '/settings',
    label: 'Settings',
    sublabel: 'Currency & Gateways',
    icon: Settings
  }
]

export default function Layout({ children }: LayoutProps) {
  const location = useLocation()
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false)
  const [persona, setPersona] = useState<'creator' | 'fan'>('creator')
  const [selectedCurrency, setSelectedCurrency] = useState<'NGN' | 'KES' | 'ZAR' | 'GHS'>('NGN')

  const currencySymbols = {
    NGN: '₦',
    KES: 'KSh',
    ZAR: 'R',
    GHS: 'GH₵'
  }

  const currentNavItem = NAV_ITEMS.find(item => item.path === location.pathname) || NAV_ITEMS[0]

  return (
    <div className="min-h-screen bg-[#07070D] text-slate-100 flex flex-col font-sans selection:bg-cyan-500 selection:text-black">
      {/* ========================================================================= */}
      {/* TOP NAVIGATION BAR                                                        */}
      {/* ========================================================================= */}
      <header className="sticky top-0 z-50 h-16 bg-[#0B0B14]/90 backdrop-blur-md border-b border-[#1E1E36] px-4 lg:px-6 flex items-center justify-between">
        {/* Left: Brand & Mobile Hamburger */}
        <div className="flex items-center space-x-4">
          <button
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
            className="lg:hidden p-2 rounded-lg bg-[#141424] text-slate-300 hover:text-white border border-[#262642]"
            aria-label="Toggle navigation"
          >
            {isMobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
          </button>

          <Link to="/" className="flex items-center space-x-3 group">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-cyan-500 via-indigo-500 to-pink-500 p-0.5 flex items-center justify-center glow-cyan group-hover:scale-105 transition-transform">
              <div className="w-full h-full bg-[#0A0A14] rounded-[10px] flex items-center justify-center">
                <Radio className="w-4 h-4 text-cyan-400 group-hover:animate-spin" />
              </div>
            </div>
            <div>
              <div className="flex items-center space-x-1.5">
                <span className="font-black tracking-widest text-base text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 via-white to-pink-500">
                  VYRA
                </span>
                <span className="text-[10px] font-mono px-1.5 py-0.2 rounded bg-cyan-950 text-cyan-400 border border-cyan-800">
                  OS
                </span>
              </div>
              <p className="text-[9px] font-mono tracking-wider text-slate-400 -mt-0.5 hidden sm:block">
                NEURAL CREATOR SUITE
              </p>
            </div>
          </Link>

          {/* Breadcrumb indicator */}
          <div className="hidden md:flex items-center space-x-2 text-xs font-mono text-slate-400 pl-4 border-l border-[#1E1E36]">
            <span className="text-slate-500">SYSTEM</span>
            <ChevronRight className="w-3 h-3 text-slate-600" />
            <span className="text-cyan-400 font-bold">{currentNavItem.label.toUpperCase()}</span>
          </div>
        </div>

        {/* Center: Live Engine Telemetry Badge */}
        <div className="hidden xl:flex items-center space-x-3 bg-[#111120] border border-[#22223C] rounded-full px-4 py-1.5 text-xs font-mono">
          <div className="flex items-center space-x-1.5">
            <span className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
            <span className="text-emerald-400 font-bold">GEMINI 3.7 FLASH</span>
          </div>
          <span className="text-slate-600">•</span>
          <span className="text-slate-300">INTERACTIONS API</span>
          <span className="text-slate-600">•</span>
          <span className="text-cyan-400">PAYSTACK LIVE</span>
        </div>

        {/* Right: Currency Selector, Persona Toggle & Creator Earnings */}
        <div className="flex items-center space-x-2.5 sm:space-x-3">
          {/* African Currency Selector */}
          <div className="hidden sm:flex items-center space-x-1 bg-[#121222] border border-[#22223C] rounded-lg p-0.5 text-xs font-mono">
            {(['NGN', 'KES', 'ZAR', 'GHS'] as const).map(curr => (
              <button
                key={curr}
                onClick={() => setSelectedCurrency(curr)}
                className={`px-2 py-1 rounded text-[11px] font-bold transition-all ${
                  selectedCurrency === curr
                    ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/50'
                    : 'text-slate-400 hover:text-slate-200'
                }`}
              >
                {curr}
              </button>
            ))}
          </div>

          {/* Earnings Badge */}
          <div className="flex items-center space-x-2 px-3 py-1.5 rounded-xl bg-gradient-to-r from-emerald-950/40 to-[#121222] border border-emerald-500/40 text-right">
            <Zap className="w-3.5 h-3.5 text-emerald-400 hidden sm:block" />
            <div>
              <p className="text-[9px] font-mono text-emerald-400/80 leading-none">85% NET BALANCE</p>
              <p className="text-xs font-mono font-black text-emerald-300">
                {currencySymbols[selectedCurrency]}
                {selectedCurrency === 'NGN' ? '4,850,200' : selectedCurrency === 'KES' ? '394,000' : selectedCurrency === 'ZAR' ? '54,200' : '42,800'}
              </p>
            </div>
          </div>

          {/* Studio Persona Switcher */}
          <button
            onClick={() => setPersona(persona === 'creator' ? 'fan' : 'creator')}
            className={`px-3 py-1.5 rounded-xl text-xs font-mono font-bold tracking-wider transition-all border ${
              persona === 'creator'
                ? 'bg-cyan-500/20 border-cyan-500/50 text-cyan-300 glow-cyan'
                : 'bg-pink-500/20 border-pink-500/50 text-pink-300 glow-magenta'
            }`}
          >
            {persona === 'creator' ? '⚡ CREATOR' : '🛡️ FAN'}
          </button>
        </div>
      </header>

      {/* ========================================================================= */}
      {/* BODY WITH PERSISTENT SIDEBAR & MAIN CONTENT                               */}
      {/* ========================================================================= */}
      <div className="flex-1 flex overflow-hidden">
        {/* DESKTOP SIDEBAR */}
        <aside className="hidden lg:flex w-64 flex-col bg-[#090912] border-r border-[#1E1E36] p-4 justify-between shrink-0 overflow-y-auto">
          <div className="space-y-6">
            {/* Quick Status Box */}
            <div className="p-3.5 rounded-xl bg-gradient-to-br from-[#121224] to-[#0D0D18] border border-[#222240]">
              <div className="flex items-center justify-between text-[11px] font-mono">
                <span className="text-slate-400">ENGINE STATUS</span>
                <span className="text-emerald-400 flex items-center space-x-1 font-bold">
                  <span className="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-ping"></span>
                  <span>ONLINE</span>
                </span>
              </div>
              <p className="text-xs font-bold text-slate-200 mt-1">Lagos Cyberstage Alpha</p>
              <div className="mt-2.5 flex items-center justify-between text-[10px] font-mono text-slate-400 pt-2 border-t border-[#1E1E36]">
                <span>Virality: 98.7%</span>
                <span className="text-cyan-400">+3.8k vel/h</span>
              </div>
            </div>

            {/* Navigation Menu */}
            <div>
              <p className="text-[10px] font-mono tracking-widest text-slate-500 px-3 mb-2 font-bold uppercase">
                Navigation Deck
              </p>
              <nav className="space-y-1">
                {NAV_ITEMS.map(item => {
                  const Icon = item.icon
                  const isActive = location.pathname === item.path

                  return (
                    <Link
                      key={item.path}
                      to={item.path}
                      className={`flex items-center justify-between px-3.5 py-2.5 rounded-xl text-xs font-medium transition-all group ${
                        isActive
                          ? 'bg-gradient-to-r from-cyan-500/20 via-indigo-500/20 to-transparent text-white border-l-4 border-l-cyan-400 border border-[#2A2A48] shadow-lg'
                          : 'text-slate-400 hover:text-slate-200 hover:bg-[#121222] border border-transparent'
                      }`}
                    >
                      <div className="flex items-center space-x-3">
                        <Icon className={`w-4 h-4 transition-colors ${isActive ? 'text-cyan-400' : 'text-slate-400 group-hover:text-cyan-400'}`} />
                        <div>
                          <div className={`font-semibold ${isActive ? 'text-white' : 'text-slate-300'}`}>{item.label}</div>
                          <div className="text-[10px] font-mono text-slate-500 leading-tight">{item.sublabel}</div>
                        </div>
                      </div>

                      {item.badge && (
                        <span className={`text-[9px] font-mono font-bold px-1.5 py-0.5 rounded border ${item.badgeColor || 'bg-slate-800 text-slate-300 border-slate-700'}`}>
                          {item.badge}
                        </span>
                      )}
                    </Link>
                  )
                })}
              </nav>
            </div>
          </div>

          {/* Sidebar Footer with Quick Actions */}
          <div className="pt-4 border-t border-[#1E1E36] space-y-2 font-mono text-[11px]">
            <div className="p-3 rounded-xl bg-[#101020] border border-[#202038] text-slate-400">
              <div className="flex items-center justify-between">
                <span className="text-[10px] text-slate-400">PAYOUT RAIL</span>
                <span className="text-[10px] font-bold text-emerald-400">PAYSTACK LIVE</span>
              </div>
              <p className="text-[10px] text-slate-300 mt-1">Split: 85% Creator • 15% Platform</p>
            </div>
            <div className="text-[10px] text-slate-400 text-center pt-1">
              VYRA v2.4 • Cyberpunk OS
            </div>
          </div>
        </aside>

        {/* MOBILE DRAWER OVERLAY */}
        {isMobileMenuOpen && (
          <div className="lg:hidden fixed inset-0 z-40 bg-black/80 backdrop-blur-sm flex">
            <div className="w-72 bg-[#090912] border-r border-[#1E1E36] p-4 flex flex-col justify-between overflow-y-auto">
              <div className="space-y-4">
                <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36]">
                  <span className="font-black text-sm tracking-wider text-cyan-400">ALL PLATFORM PAGES</span>
                  <button onClick={() => setIsMobileMenuOpen(false)} className="p-1 rounded text-slate-400 hover:text-white">
                    <X className="w-5 h-5" />
                  </button>
                </div>

                <nav className="space-y-1">
                  {NAV_ITEMS.map(item => {
                    const Icon = item.icon
                    const isActive = location.pathname === item.path

                    return (
                      <Link
                        key={item.path}
                        to={item.path}
                        onClick={() => setIsMobileMenuOpen(false)}
                        className={`flex items-center justify-between px-3 py-2.5 rounded-xl text-xs font-medium ${
                          isActive
                            ? 'bg-cyan-500/20 text-white border-l-4 border-l-cyan-400 border border-cyan-500/40'
                            : 'text-slate-400 hover:text-white hover:bg-[#121222]'
                        }`}
                      >
                        <div className="flex items-center space-x-3">
                          <Icon className={`w-4 h-4 ${isActive ? 'text-cyan-400' : 'text-slate-400'}`} />
                          <div>
                            <div className="font-semibold text-slate-200">{item.label}</div>
                            <div className="text-[10px] font-mono text-slate-400">{item.sublabel}</div>
                          </div>
                        </div>

                        {item.badge && (
                          <span className={`text-[9px] font-mono font-bold px-1.5 py-0.5 rounded border ${item.badgeColor || 'bg-slate-800 text-slate-300'}`}>
                            {item.badge}
                          </span>
                        )}
                      </Link>
                    )
                  })}
                </nav>
              </div>

              <div className="pt-4 border-t border-[#1E1E36] text-[10px] font-mono text-slate-400 text-center">
                VYRA AI Creator Platform • Gemini 3.7 Flash
              </div>
            </div>
            <div className="flex-1" onClick={() => setIsMobileMenuOpen(false)}></div>
          </div>
        )}

        {/* MAIN SCROLLABLE CONTENT AREA */}
        <main className="flex-1 overflow-y-auto bg-[#07070D] p-4 lg:p-8">
          {children}
        </main>
      </div>
    </div>
  )
}
