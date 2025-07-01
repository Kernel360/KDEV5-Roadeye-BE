package org.re.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("amqp")
@Configuration
@RequiredArgsConstructor
public class AMQPConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Exchange roadeyeDirectExchange() {
        return new DirectExchange("roadeye.direct", true, false);
    }

    @Bean
    public Binding mdtCarLocationQueueBinding() {
        var exchange = roadeyeDirectExchange();
        var queue = mdtCarLocationQueue();
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(queue.getName())
            .noargs();
    }

    @Bean
    public Binding mdtIgnitionOnQueueBinding() {
        var exchange = roadeyeDirectExchange();
        var queue = mdtIgnitionOnQueue();
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(queue.getName())
            .noargs();
    }

    @Bean
    public Binding mdtIgnitionOffQueueBinding() {
        var exchange = roadeyeDirectExchange();
        var queue = mdtIgnitionOffQueue();
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(queue.getName())
            .noargs();
    }

    @Bean
    public Queue mdtCarLocationQueue() {
        return new Queue(QueueNames.MDT_CAR_LOCATION, true, false, false);
    }

    @Bean
    public Queue mdtIgnitionOnQueue() {
        return new Queue(QueueNames.MDT_IGNITION_ON, true, false, false);
    }

    @Bean
    public Queue mdtIgnitionOffQueue() {
        return new Queue(QueueNames.MDT_IGNITION_OFF, true, false, false);
    }

    @UtilityClass
    public static class QueueNames {
        public static final String MDT_CAR_LOCATION = "mdt.car.location";
        public static final String MDT_IGNITION_ON = "mdt.ignition.on";
        public static final String MDT_IGNITION_OFF = "mdt.ignition.off";
    }
}
