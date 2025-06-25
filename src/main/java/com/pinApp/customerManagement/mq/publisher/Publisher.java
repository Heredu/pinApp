package com.pinApp.customerManagement.mq.publisher;

import com.pinApp.customerManagement.mq.model.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EnableRabbit
@Slf4j
public class Publisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.email}")
    private String exchange;

    @Value("${rabbitmq.routing-key.email}")
    private String routingKey;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEmailEvent(EmailEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.info("Email event published for: {}", event.getTo());
        } catch (AmqpException e) {
            log.error("Failed to publish email event for: {}. Error: {}",
                    event.getTo(), e.getMessage());
            throw new RuntimeException("Error al publicar evento de email", e);
        }
    }
}
