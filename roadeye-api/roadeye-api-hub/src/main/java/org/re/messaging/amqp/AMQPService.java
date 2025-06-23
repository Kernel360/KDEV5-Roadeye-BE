package org.re.messaging.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AMQPService {
    private final RabbitTemplate rabbitTemplate;

    public void send(String routingKey, Object message) {
        var exchange = rabbitTemplate.getExchange();
        log.debug("Sending AMQP message with exchange: {}, routingKey: {}", exchange, routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
