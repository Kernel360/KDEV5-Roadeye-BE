package org.re.mdtlog.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.config.AMQPConfig;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.dto.MdtEventMessage;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.re.mdtlog.service.MdtEventService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MdtEventConsumer {
    private final MdtEventService mdtEventService;

    @RabbitListener(
        queues = AMQPConfig.QueueNames.MDT_IGNITION_ON,
        batch = "false"
    )
    public void handleMdtIgnitionOnMessage(@Payload MdtEventMessage<MdtIgnitionOnMessage> message) {
        log.debug("Received MDT ignition message: {}", message);
        mdtEventService.handleMdtIgnitionOnMessage(message);
    }

    @RabbitListener(
        queues = AMQPConfig.QueueNames.MDT_IGNITION_OFF,
        batch = "false"
    )
    public void handleMdtIgnitionOffMessage(@Payload MdtEventMessage<MdtIgnitionOffMessage> message) {
        log.debug("Received MDT ignition off message: {}", message);
        mdtEventService.handleMdtIgnitionOffMessage(message);
    }

    @RabbitListener(
        queues = AMQPConfig.QueueNames.MDT_CAR_LOCATION,
        containerFactory = "batchContainerFactory",
        batch = "true"
    )
    public void handleBatchedMdtCarLocationMessage(@Payload List<MdtEventMessage<MdtCycleLogMessage>> batch) {
        log.debug("[Thread-{}] Received dt car location with batch size: {}", Thread.currentThread(), batch.size());
        mdtEventService.handleMdtCarLocationMessageBatch(batch);
    }
}
