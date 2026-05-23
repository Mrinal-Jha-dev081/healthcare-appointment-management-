import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import BookAppointment from './pages/BookAppointment'
import MyAppointments from './pages/MyAppointments'
import Navbar from './components/Navbar'

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { token } = useAuth()
  return token ? <>{children}</> : <Navigate to="/login" />
}

function AppRoutes() {
  const { token } = useAuth()

  return (
    <div className="min-h-screen">
      {token && <Navbar />}
      <div className="max-w-7xl mx-auto px-4 py-6">
        <Routes>
          <Route path="/login" element={token ? <Navigate to="/" /> : <Login />} />
          <Route path="/register" element={token ? <Navigate to="/" /> : <Register />} />
          <Route path="/" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
          <Route path="/book" element={<PrivateRoute><BookAppointment /></PrivateRoute>} />
          <Route path="/appointments" element={<PrivateRoute><MyAppointments /></PrivateRoute>} />
        </Routes>
      </div>
    </div>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  )
}
