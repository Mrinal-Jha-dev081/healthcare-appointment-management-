package com.healthcare.service;

import com.healthcare.model.Appointment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE = "appointment.events";
    public static final String ROUTING_KEY_CREATED = "appointment.created";
    public static final String ROUTING_KEY_CANCELLED = "appointment.cancelled";

    public void publishAppointmentCreated(Appointment appointment) {
        Map<String, Object> event = buildEvent(appointment, "APPOINTMENT_CREATED");
        publishAfterCommit(EXCHANGE, ROUTING_KEY_CREATED, event, appointment.getId(), "APPOINTMENT_CREATED");
    }

    public void publishAppointmentCancelled(Appointment appointment) {
        Map<String, Object> event = buildEvent(appointment, "APPOINTMENT_CANCELLED");
        publishAfterCommit(EXCHANGE, ROUTING_KEY_CANCELLED, event, appointment.getId(), "APPOINTMENT_CANCELLED");
    }

    private void publishAfterCommit(String exchange, String routingKey, Map<String, Object> event,
                                    Long appointmentId, String eventType) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    rabbitTemplate.convertAndSend(exchange, routingKey, event);
                    log.info("Published {} event for appointment {} after transaction commit", eventType, appointmentId);
                }
            });
        } else {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.info("Published {} event for appointment {}", eventType, appointmentId);
        }
    }

    private Map<String, Object> buildEvent(Appointment appointment, String eventType) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("appointmentId", appointment.getId());
        event.put("patientName", appointment.getPatient().getFullName());
        event.put("patientEmail", appointment.getPatient().getEmail());
        event.put("doctorName", appointment.getDoctor().getName());
        event.put("date", appointment.getSlot().getDate().toString());
        event.put("startTime", appointment.getSlot().getStartTime().toString());
        event.put("endTime", appointment.getSlot().getEndTime().toString());
        event.put("timestamp", LocalDateTime.now().toString());
        return event;
    }
}
