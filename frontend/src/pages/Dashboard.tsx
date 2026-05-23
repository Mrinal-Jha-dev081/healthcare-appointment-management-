import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import client from '../api/client'
import StatusBadge from '../components/StatusBadge'

interface Appointment {
  id: number
  doctorName: string
  specialization: string
  date: string
  startTime: string
  endTime: string
  status: string
  createdAt: string
}

export default function Dashboard() {
  const [appointments, setAppointments] = useState<Appointment[]>([])
  const [loading, setLoading] = useState(true)

  const fetchAppointments = async () => {
    try {
      const res = await client.get('/api/appointments')
      setAppointments(res.data.data || [])
    } catch {
      // handled by interceptor
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchAppointments()
    // Poll for status updates every 3 seconds
    const interval = setInterval(fetchAppointments, 3000)
    return () => clearInterval(interval)
  }, [])

  const upcoming = appointments.filter(a => a.status !== 'CANCELLED').slice(0, 5)

  if (loading) {
    return <div className="text-center py-10">Loading...</div>
  }

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <Link
          to="/book"
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
        >
          Book Appointment
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
        <div className="bg-white p-6 rounded-lg shadow-sm border">
          <p className="text-sm text-gray-500">Total Appointments</p>
          <p className="text-3xl font-bold text-blue-600">{appointments.length}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border">
          <p className="text-sm text-gray-500">Upcoming</p>
          <p className="text-3xl font-bold text-green-600">
            {appointments.filter(a => a.status !== 'CANCELLED').length}
          </p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border">
          <p className="text-sm text-gray-500">Cancelled</p>
          <p className="text-3xl font-bold text-red-600">
            {appointments.filter(a => a.status === 'CANCELLED').length}
          </p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border">
        <div className="p-4 border-b">
          <h2 className="text-lg font-semibold">Recent Appointments</h2>
        </div>
        {upcoming.length === 0 ? (
          <div className="p-8 text-center text-gray-500">
            No appointments yet.{' '}
            <Link to="/book" className="text-blue-600 hover:underline">Book your first one!</Link>
          </div>
        ) : (
          <div className="divide-y">
            {upcoming.map((apt) => (
              <div key={apt.id} className="p-4 flex items-center justify-between">
                <div>
                  <p className="font-medium">{apt.doctorName}</p>
                  <p className="text-sm text-gray-500">{apt.specialization}</p>
                  <p className="text-sm text-gray-500">
                    {apt.date} | {apt.startTime} - {apt.endTime}
                  </p>
                </div>
                <StatusBadge status={apt.status} />
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
