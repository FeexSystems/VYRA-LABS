import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Layout from './components/Layout'
import VyraShowPage from './pages/VyraShowPage'
import DashboardPage from './pages/DashboardPage'
import AgentsPage from './pages/AgentsPage'
import ChatPage from './pages/ChatPage'
import FeedPage from './pages/FeedPage'
import MonetizationPage from './pages/MonetizationPage'
import FanDnaPage from './pages/FanDnaPage'
import OptimizerPage from './pages/OptimizerPage'
import ProfilePage from './pages/ProfilePage'
import SettingsPage from './pages/SettingsPage'
import Index from './pages/Index'

function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          {/* Main Stage & VyraShow */}
          <Route path="/" element={<VyraShowPage />} />
          <Route path="/vyra-show" element={<VyraShowPage />} />

          {/* Core Feature Pages */}
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/agents" element={<AgentsPage />} />
          <Route path="/chat" element={<ChatPage />} />
          <Route path="/feed" element={<FeedPage />} />
          <Route path="/monetization" element={<MonetizationPage />} />
          <Route path="/fandna" element={<FanDnaPage />} />
          <Route path="/optimizer" element={<OptimizerPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/settings" element={<SettingsPage />} />

          {/* Consolidated Terminal Suite */}
          <Route path="/console" element={<Index />} />

          {/* Catch-all redirect to VyraShow */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  )
}

export default App
