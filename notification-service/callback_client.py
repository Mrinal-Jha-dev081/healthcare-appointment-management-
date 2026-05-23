import os
import logging
import requests

logger = logging.getLogger(__name__)

BACKEND_URL = os.getenv('BACKEND_URL', 'http://localhost:8080')
INTERNAL_API_KEY = os.getenv('INTERNAL_API_KEY', 'internal-service-key-2024')


def update_appointment_status(appointment_id, status):
    """Call Spring Boot internal API to update appointment status."""
    url = f"{BACKEND_URL}/api/internal/appointments/{appointment_id}/status"
    headers = {
        'Content-Type': 'application/json',
        'X-Internal-Key': INTERNAL_API_KEY
    }
    payload = {'status': status}

    try:
        response = requests.put(url, json=payload, headers=headers, timeout=10)
        if response.status_code == 200:
            logger.info(f"Updated appointment {appointment_id} status to {status}")
        else:
            logger.error(f"Failed to update status: {response.status_code} - {response.text}")
    except requests.exceptions.RequestException as e:
        logger.error(f"Error calling backend API: {e}")
