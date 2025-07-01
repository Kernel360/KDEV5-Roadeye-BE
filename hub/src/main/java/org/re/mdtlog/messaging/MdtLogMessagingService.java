package org.re.mdtlog.messaging;

import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.config.AMQPConfig;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.dto.MdtEventMessage;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.re.messaging.amqp.AMQPService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MdtLogMessagingService {
    private final AMQPService amqpService;

    public void send(TransactionUUID tuid, MdtIgnitionOnMessage dto, MdtLogRequestTimeInfo timeInfo) {
        var routingKey = AMQPConfig.QueueNames.MDT_IGNITION_ON;
        send(routingKey, tuid, dto, timeInfo);
    }

    public void send(TransactionUUID tuid, MdtIgnitionOffMessage dto, MdtLogRequestTimeInfo timeInfo) {
        var routingKey = AMQPConfig.QueueNames.MDT_IGNITION_OFF;
        send(routingKey, tuid, dto, timeInfo);
    }

    public void send(TransactionUUID tuid, MdtCycleLogMessage dto, MdtLogRequestTimeInfo timeInfo) {
        var routingKey = AMQPConfig.QueueNames.MDT_CAR_LOCATION;
        send(routingKey, tuid, dto, timeInfo);
    }

    private void send(String routingKey, TransactionUUID tuid, Object dto, MdtLogRequestTimeInfo timeInfo) {
        var message = new MdtEventMessage<>(tuid, dto, timeInfo.sentAt(), timeInfo.receivedAt());
        amqpService.send(routingKey, message);
    }
}
