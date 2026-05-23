import time
import logging

logger = logging.getLogger(__name__)


def process_event(event):
    """Process appointment event and simulate sending notification."""
    event_type = event.get('eventType')
    appointment_id = event.get('appointmentId')
    patient_name = event.get('patientName')
    patient_email = event.get('patientEmail')
    doctor_name = event.get('doctorName')
    date = event.get('date')
    start_time = event.get('startTime')
    end_time = event.get('endTime')

    if event_type == 'APPOINTMENT_CREATED':
        send_booking_confirmation(patient_name, patient_email, doctor_name, date, start_time, end_time, appointment_id)
    elif event_type == 'APPOINTMENT_CANCELLED':
        send_cancellation_notice(patient_name, patient_email, doctor_name, date, start_time, end_time, appointment_id)
    else:
        logger.warning(f"Unknown event type: {event_type}")


def send_booking_confirmation(patient_name, patient_email, doctor_name, date, start_time, end_time, appointment_id):
    """Simulate sending a booking confirmation email."""
    logger.info("=" * 60)
    logger.info("SENDING EMAIL NOTIFICATION - Appointment Confirmation")
    logger.info("=" * 60)
    logger.info(f"  To: {patient_email}")
    logger.info(f"  Subject: Appointment Confirmation - #{appointment_id}")
    logger.info(f"  ---")
    logger.info(f"  Dear {patient_name},")
    logger.info(f"")
    logger.info(f"  Your appointment has been confirmed!")
    logger.info(f"")
    logger.info(f"  Details:")
    logger.info(f"    Doctor: {doctor_name}")
    logger.info(f"    Date: {date}")
    logger.info(f"    Time: {start_time} - {end_time}")
    logger.info(f"    Appointment ID: #{appointment_id}")
    logger.info(f"")
    logger.info(f"  Please arrive 10 minutes early.")
    logger.info(f"")
    logger.info(f"  Best regards,")
    logger.info(f"  Healthcare Platform Team")
    logger.info("=" * 60)

    # Simulate email sending delay
    time.sleep(2)
    logger.info(f"Email notification sent successfully to {patient_email}")


def send_cancellation_notice(patient_name, patient_email, doctor_name, date, start_time, end_time, appointment_id):
    """Simulate sending a cancellation notification email."""
    logger.info("=" * 60)
    logger.info("SENDING EMAIL NOTIFICATION - Appointment Cancellation")
    logger.info("=" * 60)
    logger.info(f"  To: {patient_email}")
    logger.info(f"  Subject: Appointment Cancelled - #{appointment_id}")
    logger.info(f"  ---")
    logger.info(f"  Dear {patient_name},")
    logger.info(f"")
    logger.info(f"  Your appointment has been cancelled.")
    logger.info(f"")
    logger.info(f"  Cancelled Appointment:")
    logger.info(f"    Doctor: {doctor_name}")
    logger.info(f"    Date: {date}")
    logger.info(f"    Time: {start_time} - {end_time}")
    logger.info(f"    Appointment ID: #{appointment_id}")
    logger.info(f"")
    logger.info(f"  You can book a new appointment anytime.")
    logger.info(f"")
    logger.info(f"  Best regards,")
    logger.info(f"  Healthcare Platform Team")
    logger.info("=" * 60)

    # Simulate email sending delay
    time.sleep(2)
    logger.info(f"Cancellation notification sent to {patient_email}")
