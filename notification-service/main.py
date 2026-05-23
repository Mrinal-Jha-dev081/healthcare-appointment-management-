import os
import json
import time
import logging
import pika
from notification_handler import process_event
from callback_client import update_appointment_status

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [%(levelname)s] %(message)s'
)
logger = logging.getLogger(__name__)

RABBITMQ_HOST = os.getenv('RABBITMQ_HOST', 'localhost')
RABBITMQ_PORT = int(os.getenv('RABBITMQ_PORT', '5672'))
RABBITMQ_USER = os.getenv('RABBITMQ_USER', 'guest')
RABBITMQ_PASS = os.getenv('RABBITMQ_PASS', 'guest')

QUEUES = ['appointment.created.queue', 'appointment.cancelled.queue']


def connect_with_retry(max_retries=10, delay=5):
    """Connect to RabbitMQ with retry logic."""
    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    parameters = pika.ConnectionParameters(
        host=RABBITMQ_HOST,
        port=RABBITMQ_PORT,
        credentials=credentials,
        heartbeat=600,
        blocked_connection_timeout=300
    )

    for attempt in range(1, max_retries + 1):
        try:
            connection = pika.BlockingConnection(parameters)
            logger.info("Connected to RabbitMQ successfully")
            return connection
        except pika.exceptions.AMQPConnectionError as e:
            logger.warning(f"Connection attempt {attempt}/{max_retries} failed: {e}")
            if attempt < max_retries:
                time.sleep(delay)
            else:
                raise


def on_message(channel, method, properties, body):
    """Callback for processing incoming messages."""
    try:
        event = json.loads(body)
        event_type = event.get('eventType')
        appointment_id = event.get('appointmentId')
        logger.info(f"Received event: {event_type} for appointment {appointment_id}")

        # Only set notification lifecycle statuses for appointment creation events.
        # For cancellation events we should not overwrite the CANCELLED status set by the backend.
        if event_type == 'APPOINTMENT_CREATED':
            # Update status to NOTIFICATION_SENDING
            update_appointment_status(appointment_id, 'NOTIFICATION_SENDING')

            # Process the event (simulate notification)
            process_event(event)

            # Update status to NOTIFICATION_SENT
            update_appointment_status(appointment_id, 'NOTIFICATION_SENT')
        else:
            # For other event types (e.g., APPOINTMENT_CANCELLED) just process notification without changing appointment status
            process_event(event)

        # Acknowledge the message
        channel.basic_ack(delivery_tag=method.delivery_tag)
        logger.info(f"Successfully processed event for appointment {appointment_id}")

    except Exception as e:
        logger.error(f"Error processing message: {e}")
        # Reject and requeue on failure
        channel.basic_nack(delivery_tag=method.delivery_tag, requeue=True)


def main():
    logger.info("Starting Healthcare Notification Service...")
    logger.info(f"Connecting to RabbitMQ at {RABBITMQ_HOST}:{RABBITMQ_PORT}")

    connection = connect_with_retry()
    channel = connection.channel()

    # Set prefetch count to process one message at a time
    channel.basic_qos(prefetch_count=1)

    # Consume from all queues
    for queue in QUEUES:
        channel.basic_consume(queue=queue, on_message_callback=on_message)
        logger.info(f"Listening on queue: {queue}")

    logger.info("Notification service is ready. Waiting for events...")

    try:
        channel.start_consuming()
    except KeyboardInterrupt:
        logger.info("Shutting down gracefully...")
        channel.stop_consuming()
    finally:
        connection.close()
        logger.info("Connection closed")


if __name__ == '__main__':
    main()
