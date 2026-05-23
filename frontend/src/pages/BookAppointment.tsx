import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import client from '../api/client'

interface Doctor {
  id: number
  name: string
  specialization: string
}

interface Slot {
  id: number
  doctorId: number
  doctorName: string
  date: string
  startTime: string
  endTime: string
  available: boolean
}

type BookingStep = 'doctor' | 'date' | 'slot' | 'confirming' | 'done'

export default function BookAppointment() {
  const [doctors, setDoctors] = useState<Doctor[]>([])
  const [slots, setSlots] = useState<Slot[]>([])
  const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null)
  const [selectedDate, setSelectedDate] = useState('')
  const [selectedSlot, setSelectedSlot] = useState<Slot | null>(null)
  const [step, setStep] = useState<BookingStep>('doctor')
  const [statusMessage, setStatusMessage] = useState('')
  const [error, setError] = useState('')
  const [loadingSlots, setLoadingSlots] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    client.get('/api/slots/doctors').then(res => {
      setDoctors(res.data.data || [])
    })
  }, [])

  const fetchSlots = async (doctorId: number, date: string) => {
    setLoadingSlots(true)
    setError('')
    try {
      const res = await client.get(`/api/slots?doctorId=${doctorId}&date=${date}`)
      setSlots(res.data.data || [])
    } catch {
      setError('Failed to fetch slots')
    } finally {
      setLoadingSlots(false)
    }
  }

  const handleDoctorSelect = (doctor: Doctor) => {
    setSelectedDoctor(doctor)
    setStep('date')
  }

  const handleDateSelect = (date: string) => {
    setSelectedDate(date)
    setStep('slot')
    fetchSlots(selectedDoctor!.id, date)
  }

  const handleSlotSelect = (slot: Slot) => {
    setSelectedSlot(slot)
  }

  const handleBooking = async () => {
    if (!selectedSlot) return
    setStep('confirming')
    setStatusMessage('Booking appointment...')
    setError('')

    try {
      await client.post('/api/appointments', { slotId: selectedSlot.id })
      setStatusMessage('Appointment booked successfully!')
      setTimeout(() => {
        setStatusMessage('Processing notification event...')
      }, 500)
      setTimeout(() => {
        setStep('done')
        setStatusMessage('All done! Notification sent.')
      }, 3000)
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } }
      setStep('slot')
      setError(error.response?.data?.message || 'Booking failed')
    }
  }

  // Generate next 7 days for date selection
  const getNextDays = () => {
    const days = []
    for (let i = 0; i < 7; i++) {
      const d = new Date()
      d.setDate(d.getDate() + i)
      days.push(d.toISOString().split('T')[0])
    }
    return days
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Book Appointment</h1>

      {/* Progress indicator */}
      <div className="flex items-center gap-2 mb-8">
        {['Select Doctor', 'Choose Date', 'Pick Slot', 'Confirm'].map((label, i) => {
          const stepIndex = ['doctor', 'date', 'slot', 'confirming'].indexOf(step)
          const isActive = i <= stepIndex || step === 'done'
          return (
            <div key={label} className="flex items-center gap-2">
              <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium ${
                isActive ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-600'
              }`}>
                {i + 1}
              </div>
              <span className={`text-sm ${isActive ? 'text-blue-600 font-medium' : 'text-gray-500'}`}>
                {label}
              </span>
              {i < 3 && <div className="w-8 h-0.5 bg-gray-200 mx-1" />}
            </div>
          )
        })}
      </div>

      {error && (
        <div className="bg-red-50 text-red-600 p-3 rounded mb-4 text-sm">{error}</div>
      )}

      {/* Status animation for booking process */}
      {(step === 'confirming' || step === 'done') && (
        <div className="bg-white rounded-lg shadow-sm border p-8 text-center">
          {step === 'confirming' && (
            <div className="animate-pulse">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <svg className="animate-spin h-8 w-8 text-blue-600" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
                </svg>
              </div>
              <p className="text-lg font-medium text-gray-700">{statusMessage}</p>
            </div>
          )}
          {step === 'done' && (
            <div>
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <svg className="h-8 w-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <p className="text-lg font-medium text-green-700">{statusMessage}</p>
              <button
                onClick={() => navigate('/appointments')}
                className="mt-4 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
              >
                View My Appointments
              </button>
            </div>
          )}
        </div>
      )}

      {/* Step 1: Select Doctor */}
      {step === 'doctor' && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {doctors.map(doctor => (
            <button
              key={doctor.id}
              onClick={() => handleDoctorSelect(doctor)}
              className="bg-white p-6 rounded-lg shadow-sm border text-left hover:border-blue-500 hover:shadow-md transition"
            >
              <p className="font-semibold text-lg">{doctor.name}</p>
              <p className="text-gray-500">{doctor.specialization}</p>
            </button>
          ))}
        </div>
      )}

      {/* Step 2: Select Date */}
      {step === 'date' && (
        <div>
          <p className="text-gray-600 mb-4">
            Booking with <span className="font-semibold">{selectedDoctor?.name}</span> ({selectedDoctor?.specialization})
          </p>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
            {getNextDays().map(date => (
              <button
                key={date}
                onClick={() => handleDateSelect(date)}
                className="bg-white p-4 rounded-lg border text-center hover:border-blue-500 hover:shadow-md transition"
              >
                <p className="font-medium">{new Date(date + 'T00:00:00').toLocaleDateString('en-US', { weekday: 'short' })}</p>
                <p className="text-sm text-gray-500">{date}</p>
              </button>
            ))}
          </div>
        </div>
      )}

      {/* Step 3: Select Slot */}
      {step === 'slot' && (
        <div>
          <p className="text-gray-600 mb-4">
            <span className="font-semibold">{selectedDoctor?.name}</span> | {selectedDate}
          </p>

          {loadingSlots ? (
            <div className="text-center py-8">
              <p className="text-gray-500">Fetching available slots...</p>
            </div>
          ) : slots.length === 0 ? (
            <div className="text-center py-8 bg-white rounded-lg border">
              <p className="text-gray-500">No available slots for this date.</p>
              <button onClick={() => setStep('date')} className="text-blue-600 mt-2 hover:underline">
                Choose another date
              </button>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-6">
                {slots.map(slot => (
                  <button
                    key={slot.id}
                    onClick={() => handleSlotSelect(slot)}
                    className={`p-3 rounded-lg border text-center transition ${
                      selectedSlot?.id === slot.id
                        ? 'border-blue-500 bg-blue-50 shadow-md'
                        : 'bg-white hover:border-blue-300'
                    }`}
                  >
                    <p className="font-medium">{slot.startTime} - {slot.endTime}</p>
                  </button>
                ))}
              </div>
              {selectedSlot && (
                <button
                  onClick={handleBooking}
                  className="bg-blue-600 text-white px-6 py-3 rounded-md hover:bg-blue-700 font-medium"
                >
                  Confirm Booking
                </button>
              )}
            </>
          )}
        </div>
      )}
    </div>
  )
}
