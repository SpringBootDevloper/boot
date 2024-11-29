package com.presidio.hiringchallenge.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {
    @Value("${rabbitmq.queue.name}")
    String queue;

    @Value("${rabbitmq.exchange.name}")
    String exchange;

    @Value("${rabbitmq.routing.key}")
    String routingKey;

    private RabbitTemplate rabbitTemplate;

    EmailProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailMessageToExchange(String message){
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
    }
}
