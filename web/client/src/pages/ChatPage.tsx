import { useState } from 'react'
import {
  Send,
  Lock,
  Bot,
  Users,
  Search
} from 'lucide-react'

export default function ChatPage() {
  const [selectedChannel, setSelectedChannel] = useState('global')
  const [inputText, setInputText] = useState('')
  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: '@kofi_accra',
      badge: 'FAN // VIP',
      badgeColor: 'bg-amber-950 text-amber-400 border-amber-600',
      time: '14:20',
      text: 'Just tipped ₦10,000 via Paystack! That visualizer transition was insane.'
    },
    {
      id: 2,
      sender: 'HoloKai',
      badge: 'AI AGENT',
      badgeColor: 'bg-pink-950 text-pink-400 border-pink-600',
      time: '14:21',
      text: 'Gratitude logged to the neural ledger, @kofi_accra. Payout routed to creator balance with 85% net split.'
    },
    {
      id: 3,
      sender: '@zola_jozi',
      badge: 'VANGUARD',
      badgeColor: 'bg-purple-950 text-purple-400 border-purple-600',
      time: '14:22',
      text: 'Lagos and Joburg cyber frequencies are aligned tonight. Keep pushing the tempo!'
    }
  ])

  const handleSendMessage = (e?: React.FormEvent) => {
    e?.preventDefault()
    if (!inputText.trim()) return

    setMessages(prev => [
      ...prev,
      {
        id: Date.now(),
        sender: '@you (Creator)',
        badge: 'HOST',
        badgeColor: 'bg-cyan-950 text-cyan-400 border-cyan-600',
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        text: inputText
      }
    ])
    setInputText('')
  }

  return (
    <div className="space-y-6 max-w-7xl mx-auto h-[calc(100vh-8rem)] flex flex-col">
      {/* Header */}
      <div className="flex items-center justify-between pb-3 border-b border-[#1E1E36] shrink-0">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>NEURAL ENCRYPTED CHAT</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-emerald-950 text-emerald-400 border border-emerald-800 flex items-center space-x-1">
              <Lock className="w-3 h-3" />
              <span>E2EE ENCRYPTED</span>
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Real-time direct creator-fan interaction channels with integrated Paystack tipping rails
          </p>
        </div>

        <div className="flex items-center space-x-2 font-mono text-xs text-slate-400">
          <Users className="w-4 h-4 text-cyan-400" />
          <span>148,290 CONNECTED</span>
        </div>
      </div>

      {/* Main Chat Deck */}
      <div className="flex-1 grid grid-cols-1 md:grid-cols-4 gap-6 min-h-0">
        {/* Channel List */}
        <div className="cyber-panel rounded-2xl p-4 border border-[#22223C] flex flex-col space-y-3 shrink-0">
          <div className="flex items-center space-x-2 bg-[#0A0A14] border border-[#22223C] rounded-xl px-3 py-2 text-xs">
            <Search className="w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Search channels..."
              className="bg-transparent text-white focus:outline-none w-full font-mono"
            />
          </div>

          <div className="space-y-1 overflow-y-auto flex-1 font-mono text-xs">
            {[
              { id: 'global', name: '# broadcast-live', count: '148k', active: true },
              { id: 'vanguard', name: '# vanguard-vip', count: '1.2k', lock: true },
              { id: 'nomads', name: '# nomads-lounge', count: '45k' },
              { id: 'afro-cyber', name: '# lagos-night-grid', count: '28k' }
            ].map(ch => (
              <button
                key={ch.id}
                onClick={() => setSelectedChannel(ch.id)}
                className={`w-full flex items-center justify-between px-3 py-2.5 rounded-xl transition-all ${
                  selectedChannel === ch.id
                    ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/40 font-bold'
                    : 'text-slate-400 hover:text-white hover:bg-[#121222]'
                }`}
              >
                <span>{ch.name}</span>
                <span className="text-[10px] text-slate-400">{ch.count}</span>
              </button>
            ))}
          </div>

          <div className="p-3 rounded-xl bg-gradient-to-r from-pink-950/20 to-purple-950/20 border border-pink-500/30 text-[11px] font-mono">
            <div className="flex items-center space-x-1.5 text-pink-400 font-bold">
              <Bot className="w-3.5 h-3.5" />
              <span>AI COPILOT ACTIVE</span>
            </div>
            <p className="text-slate-400 mt-1">HoloKai is moderating and responding to fan tips automatically.</p>
          </div>
        </div>

        {/* Message Stream */}
        <div className="md:col-span-3 cyber-panel rounded-2xl p-4 border border-[#22223C] flex flex-col justify-between overflow-hidden">
          {/* Messages list */}
          <div className="flex-1 space-y-4 overflow-y-auto pr-2">
            {messages.map(msg => (
              <div key={msg.id} className="p-3.5 rounded-xl bg-[#0A0A14] border border-[#1E1E36] font-mono text-xs">
                <div className="flex items-center justify-between pb-1.5 border-b border-[#18182E] mb-2">
                  <div className="flex items-center space-x-2">
                    <span className="font-bold text-white">{msg.sender}</span>
                    <span className={`text-[9px] px-1.5 py-0.5 rounded border ${msg.badgeColor}`}>
                      {msg.badge}
                    </span>
                  </div>
                  <span className="text-[10px] text-slate-400">{msg.time}</span>
                </div>
                <p className="text-slate-200 leading-relaxed">{msg.text}</p>
              </div>
            ))}
          </div>

          {/* Chat composer */}
          <form onSubmit={handleSendMessage} className="pt-4 border-t border-[#1E1E36] flex items-center space-x-2">
            <input
              type="text"
              value={inputText}
              onChange={e => setInputText(e.target.value)}
              placeholder="Send encrypted broadcast message..."
              className="flex-1 bg-[#0A0A14] border border-[#22223C] rounded-xl px-4 py-3 text-xs text-white focus:outline-none focus:border-cyan-500 font-mono"
            />
            <button
              type="submit"
              disabled={!inputText.trim()}
              className="px-5 py-3 rounded-xl bg-cyan-500 text-black font-bold text-xs hover:bg-cyan-400 disabled:opacity-50 transition-colors flex items-center space-x-1.5"
            >
              <Send className="w-4 h-4" />
              <span>SEND</span>
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
