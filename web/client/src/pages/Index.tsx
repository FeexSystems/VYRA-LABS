import { DesignTokens } from '@shared/api'

function Index() {
  return (
    <div className="min-h-screen bg-cyber-bg p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-4xl font-bold text-neon-cyan mb-4">
          VYRA Web Platform
        </h1>
        <p className="text-gray-400 mb-8">
          Hybrid native/web architecture implementation
        </p>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-cyber-surface border border-cyber-border rounded-lg p-6">
            <h2 className="text-xl font-semibold text-neon-magenta mb-2">
              Native Integration
            </h2>
            <p className="text-gray-400">
              WebView integration with Android native components
            </p>
          </div>
          
          <div className="bg-cyber-surface border border-cyber-border rounded-lg p-6">
            <h2 className="text-xl font-semibold text-neon-violet mb-2">
              AI Agents
            </h2>
            <p className="text-gray-400">
              Multi-agent collaboration system with hybrid models
            </p>
          </div>
          
          <div className="bg-cyber-surface border border-cyber-border rounded-lg p-6">
            <h2 className="text-xl font-semibold text-neon-green mb-2">
              Cloud Sync
            </h2>
            <p className="text-gray-400">
              Hybrid data layer with real-time synchronization
            </p>
          </div>
          
          <div className="bg-cyber-surface border border-cyber-border rounded-lg p-6">
            <h2 className="text-xl font-semibold text-neon-amber mb-2">
              Multi-Platform
            </h2>
            <p className="text-gray-400">
              Shared code architecture across platforms
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Index
