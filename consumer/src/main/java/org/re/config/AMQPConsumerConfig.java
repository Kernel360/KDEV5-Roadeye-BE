package org.re.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class AMQPConsumerConfig {
    @Bean
    public SimpleRabbitListenerContainerFactory batchContainerFactory(
        ConnectionFactory connectionFactory,
        MessageConverter messageConverter,
        RabbitBatchProperties properties
    ) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setBatchListener(true);
        factory.setConsumerBatchEnabled(true);
        factory.setDeBatchingEnabled(false);
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(properties.prefetch);
        factory.setBatchSize(properties.batchSize);
        factory.setConcurrentConsumers(properties.consumers);
        factory.setMaxConcurrentConsumers(properties.maxConsumers);
        factory.setBatchReceiveTimeout(properties.timeout);
        return factory;
    }

    @Data
    @Configuration
    @ConfigurationProperties("spring.rabbitmq.listener.batch")
    public static class RabbitBatchProperties {
        int prefetch = 30;
        int batchSize = 30;
        int consumers = 10;
        int maxConsumers = 10;
        long timeout = 5000L;
    }
}
