import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-6">
          <Link to="/" className="text-xl font-bold text-blue-600">
            HealthCare
          </Link>
          <Link to="/" className="text-gray-600 hover:text-blue-600">Dashboard</Link>
          <Link to="/book" className="text-gray-600 hover:text-blue-600">Book Appointment</Link>
          <Link to="/appointments" className="text-gray-600 hover:text-blue-600">My Appointments</Link>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-sm text-gray-600">Hello, {user?.fullName}</span>
          <button
            onClick={handleLogout}
            className="text-sm bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  )
}
