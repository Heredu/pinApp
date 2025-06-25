package com.pinApp.customerManagement.mq.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinApp.customerManagement.mq.model.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Publisher {
    private final StringRedisTemplate redisTemplate;

    @Value("${redis.queue.name}")
    private String redisQueueName;

    public Publisher(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;


    public void publishEmailEvent(EmailEvent emailEvent) {
        try {
            String message = objectMapper.writeValueAsString(emailEvent);
            redisTemplate.convertAndSend(redisQueueName, message);
            log.info("Published EmailEvent to Redis queue: {}", redisQueueName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize EmailEvent", e);
        }
    }
}
