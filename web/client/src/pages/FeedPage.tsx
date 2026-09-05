import { useState } from 'react'
import {
  Flame,
  Heart,
  Repeat2,
  Zap,
  TrendingUp,
  Play,
  Sparkles
} from 'lucide-react'
import { Link } from 'react-router-dom'

interface FeedPost {
  id: string
  creator: string
  handle: string
  title: string
  category: string
  reach: string
  velocity: string
  hearts: number
  isHearted: boolean
  revyralized: boolean
  revyralizeCount: number
  color: string
}

export default function FeedPage() {
  const [posts, setPosts] = useState<FeedPost[]>([
    {
      id: '1',
      creator: 'DJ CyberLagos',
      handle: '@cyber_lagos',
      title: 'Afro-Futurist Live Synth Odyssey: 140BPM Cyber-Kuduro Live from Victoria Island',
      category: 'AUDIO // VISUALIZER',
      reach: '240.5k',
      velocity: '+5.4k/h',
      hearts: 9420,
      isHearted: false,
      revyralized: false,
      revyralizeCount: 1480,
      color: 'from-cyan-500/20 to-blue-500/20'
    },
    {
      id: '2',
      creator: 'Nairobi Neural AI',
      handle: '@nairobi_neural',
      title: 'HoloKai agent generating real-time Swahili-Cyberpunk voiceovers for Kenyan game devs',
      category: 'AI AGENTS',
      reach: '189.2k',
      velocity: '+3.9k/h',
      hearts: 7120,
      isHearted: true,
      revyralized: true,
      revyralizeCount: 2210,
      color: 'from-pink-500/20 to-purple-500/20'
    },
    {
      id: '3',
      creator: 'Adaeze Tech',
      handle: '@adaeze_creator',
      title: 'How I monetized 4,000 FanDNA Insiders using Paystack NGN tip rails (85% net split breakdown)',
      category: 'MONETIZATION',
      reach: '112.4k',
      velocity: '+2.1k/h',
      hearts: 4890,
      isHearted: false,
      revyralized: false,
      revyralizeCount: 890,
      color: 'from-emerald-500/20 to-teal-500/20'
    },
    {
      id: '4',
      creator: 'Joburg Pulse',
      handle: '@jozi_pulse',
      title: 'Amapiano Meets Cyberpunk: Sub-bass resonance and dynamic lighting rig showcase',
      category: 'BROADCAST',
      reach: '315.8k',
      velocity: '+7.2k/h',
      hearts: 14200,
      isHearted: false,
      revyralized: false,
      revyralizeCount: 3950,
      color: 'from-amber-500/20 to-orange-500/20'
    }
  ])

  const toggleHeart = (postId: string) => {
    setPosts(prev =>
      prev.map(p => {
        if (p.id === postId) {
          return {
            ...p,
            isHearted: !p.isHearted,
            hearts: p.isHearted ? p.hearts - 1 : p.hearts + 1
          }
        }
        return p
      })
    )
  }

  const toggleRevyralize = (postId: string) => {
    setPosts(prev =>
      prev.map(p => {
        if (p.id === postId) {
          return {
            ...p,
            revyralized: !p.revyralized,
            revyralizeCount: p.revyralized ? p.revyralizeCount - 1 : p.revyralizeCount + 1
          }
        }
        return p
      })
    )
  }

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-2 border-b border-[#1E1E36]">
        <div>
          <h1 className="text-2xl font-black text-white tracking-wider flex items-center space-x-2">
            <span>VIRAL STAGGERED FEED</span>
            <span className="text-xs font-mono font-bold px-2 py-0.5 rounded bg-amber-950 text-amber-400 border border-amber-800 flex items-center space-x-1">
              <Flame className="w-3.5 h-3.5" />
              <span>HIGH VELOCITY</span>
            </span>
          </h1>
          <p className="text-xs font-mono text-slate-400 mt-1">
            Trending creator transmissions across Africa sorted by virality score and algorithmic momentum
          </p>
        </div>

        <div className="flex items-center space-x-2">
          <Link
            to="/optimizer"
            className="px-4 py-2 rounded-xl bg-cyan-500/20 text-cyan-300 border border-cyan-500/40 text-xs font-mono font-bold hover:bg-cyan-500/30 transition-all flex items-center space-x-1.5"
          >
            <Sparkles className="w-4 h-4 text-cyan-400" />
            <span>OPTIMIZE YOUR POST</span>
          </Link>
        </div>
      </div>

      {/* Feed Cards Masonry Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {posts.map(post => (
          <div
            key={post.id}
            className="cyber-panel rounded-2xl p-6 border border-[#22223C] space-y-4 hover:border-slate-500 transition-all flex flex-col justify-between"
          >
            <div>
              {/* Card Meta */}
              <div className="flex items-center justify-between text-xs font-mono pb-3 border-b border-[#1A1A30]">
                <div className="flex items-center space-x-2">
                  <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-cyan-500 to-pink-500 flex items-center justify-center font-bold text-black text-xs">
                    {post.creator[0]}
                  </div>
                  <div>
                    <h3 className="font-bold text-white text-xs">{post.creator}</h3>
                    <span className="text-[10px] text-slate-400">{post.handle}</span>
                  </div>
                </div>

                <div className="text-right">
                  <span className="text-[10px] px-2 py-0.5 rounded bg-[#161628] text-cyan-300 border border-[#262644]">
                    {post.category}
                  </span>
                  <div className="text-[10px] text-emerald-400 font-bold mt-0.5 flex items-center justify-end">
                    <TrendingUp className="w-3 h-3 mr-0.5" />
                    <span>{post.velocity}</span>
                  </div>
                </div>
              </div>

              {/* Card Title & Content preview */}
              <div className="mt-4">
                <h4 className="font-extrabold text-base text-slate-100 hover:text-cyan-300 transition-colors cursor-pointer">
                  {post.title}
                </h4>

                <div className={`mt-3 p-4 rounded-xl bg-gradient-to-br ${post.color} border border-[#222240] relative overflow-hidden flex items-center justify-center min-h-[120px]`}>
                  <div className="flex items-center space-x-2 bg-black/60 px-4 py-2 rounded-full border border-white/10 backdrop-blur-sm cursor-pointer hover:scale-105 transition-transform">
                    <Play className="w-4 h-4 text-cyan-400 fill-cyan-400" />
                    <span className="text-xs font-mono font-bold text-white">STREAM BROADCAST</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Interaction Bar */}
            <div className="pt-3 border-t border-[#1A1A30] flex items-center justify-between">
              <div className="flex items-center space-x-3 text-xs font-mono">
                {/* Heart Button */}
                <button
                  onClick={() => toggleHeart(post.id)}
                  className={`flex items-center space-x-1.5 px-3 py-1.5 rounded-lg transition-all ${
                    post.isHearted
                      ? 'bg-red-500/20 text-red-400 border border-red-500/40 glow-magenta font-bold'
                      : 'text-slate-400 hover:text-white bg-[#121222]'
                  }`}
                >
                  <Heart className={`w-4 h-4 ${post.isHearted ? 'fill-red-500 text-red-500 animate-bounce' : ''}`} />
                  <span>{post.hearts.toLocaleString()}</span>
                </button>

                {/* Revyralize Button */}
                <button
                  onClick={() => toggleRevyralize(post.id)}
                  className={`flex items-center space-x-1.5 px-3 py-1.5 rounded-lg transition-all ${
                    post.revyralized
                      ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/40 glow-green font-bold'
                      : 'text-slate-400 hover:text-white bg-[#121222]'
                  }`}
                >
                  <Repeat2 className="w-4 h-4" />
                  <span>{post.revyralizeCount.toLocaleString()}</span>
                </button>
              </div>

              {/* Tip Link */}
              <Link
                to="/monetization"
                className="flex items-center space-x-1 px-3 py-1.5 rounded-lg bg-amber-500/20 text-amber-300 border border-amber-500/40 text-xs font-mono font-bold hover:bg-amber-500/30 transition-all"
              >
                <Zap className="w-3.5 h-3.5 text-amber-400" />
                <span>TIP CREATOR</span>
              </Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
