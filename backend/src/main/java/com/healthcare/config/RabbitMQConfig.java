package com.healthcare.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "appointment.events";
    public static final String QUEUE_CREATED = "appointment.created.queue";
    public static final String QUEUE_CANCELLED = "appointment.cancelled.queue";
    public static final String ROUTING_KEY_CREATED = "appointment.created";
    public static final String ROUTING_KEY_CANCELLED = "appointment.cancelled";

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue createdQueue() {
        return QueueBuilder.durable(QUEUE_CREATED).build();
    }

    @Bean
    public Queue cancelledQueue() {
        return QueueBuilder.durable(QUEUE_CANCELLED).build();
    }

    @Bean
    public Binding createdBinding(Queue createdQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(createdQueue).to(appointmentExchange).with(ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding cancelledBinding(Queue cancelledQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(cancelledQueue).to(appointmentExchange).with(ROUTING_KEY_CANCELLED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
