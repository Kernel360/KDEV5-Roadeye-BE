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

@Slf4j
@Component
@RequiredArgsConstructor
public class MdtEventConsumer {
    private final MdtEventService mdtEventService;

    @RabbitListener(queues = AMQPConfig.QueueNames.MDT_IGNITION_ON)
    public void handleMdtIgnitionOnMessage(@Payload MdtEventMessage<MdtIgnitionOnMessage> message) {
        log.debug("Received MDT ignition message: {}", message);
        mdtEventService.handleMdtIgnitionOnMessage(message);
    }

    @RabbitListener(queues = AMQPConfig.QueueNames.MDT_IGNITION_OFF)
    public void handleMdtIgnitionOffMessage(@Payload MdtEventMessage<MdtIgnitionOffMessage> message) {
        log.debug("Received MDT ignition off message: {}", message);
        mdtEventService.handleMdtIgnitionOffMessage(message);
    }

    @RabbitListener(queues = AMQPConfig.QueueNames.MDT_CAR_LOCATION)
    public void handleMdtCarLocationMessage(@Payload MdtEventMessage<MdtCycleLogMessage> message) {
        log.debug("Received MDT car location message: {}", message);
        mdtEventService.handleMdtCarLocationMessage(message);
    }
}
