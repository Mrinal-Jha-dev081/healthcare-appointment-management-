import { useEffect, useState } from 'react'
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
  updatedAt: string
}

export default function MyAppointments() {
  const [appointments, setAppointments] = useState<Appointment[]>([])
  const [loading, setLoading] = useState(true)
  const [cancellingId, setCancellingId] = useState<number | null>(null)

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
    const interval = setInterval(fetchAppointments, 3000)
    return () => clearInterval(interval)
  }, [])

  const handleCancel = async (id: number) => {
    if (!confirm('Are you sure you want to cancel this appointment?')) return
    setCancellingId(id)
    try {
      await client.delete(`/api/appointments/${id}`)
      fetchAppointments()
    } catch {
      alert('Failed to cancel appointment')
    } finally {
      setCancellingId(null)
    }
  }

  if (loading) {
    return <div className="text-center py-10">Loading...</div>
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">My Appointments</h1>

      {appointments.length === 0 ? (
        <div className="bg-white p-8 rounded-lg shadow-sm border text-center text-gray-500">
          No appointments found.
        </div>
      ) : (
        <div className="space-y-4">
          {appointments.map((apt) => (
            <div key={apt.id} className="bg-white p-6 rounded-lg shadow-sm border">
              <div className="flex items-start justify-between">
                <div>
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="font-semibold text-lg">{apt.doctorName}</h3>
                    <StatusBadge status={apt.status} />
                  </div>
                  <p className="text-gray-500">{apt.specialization}</p>
                  <p className="text-sm text-gray-600 mt-1">
                    <span className="font-medium">Date:</span> {apt.date}
                  </p>
                  <p className="text-sm text-gray-600">
                    <span className="font-medium">Time:</span> {apt.startTime} - {apt.endTime}
                  </p>
                  <p className="text-xs text-gray-400 mt-2">Booked: {new Date(apt.createdAt).toLocaleString()}</p>
                </div>
                <div>
                  {apt.status?.toUpperCase() !== 'CANCELLED' && (
                    <button
                      onClick={() => handleCancel(apt.id)}
                      disabled={cancellingId === apt.id}
                      className="text-sm bg-red-50 text-red-600 px-3 py-1.5 rounded border border-red-200 hover:bg-red-100 disabled:opacity-50"
                    >
                      {cancellingId === apt.id ? 'Cancelling...' : 'Cancel'}
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
