package org.re.mdtlog.messaging;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.config.AMQPConfig;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.dto.MdtEventMessage;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.re.messaging.amqp.AMQPService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MdtLogMessagingService {
    private final AMQPService amqpService;

    public void send(UUID txid, MdtIgnitionOnMessage dto, MdtLogRequestTimeInfo timeInfo, @Nullable String routingKey) {
        if (routingKey == null) {
            routingKey = AMQPConfig.QueueNames.MDT_IGNITION_ON;
        }
        send(routingKey, txid, dto, timeInfo);
    }

    public void send(UUID txid, MdtIgnitionOffMessage dto, MdtLogRequestTimeInfo timeInfo, @Nullable String routingKey) {
        if (routingKey == null) {
            routingKey = AMQPConfig.QueueNames.MDT_IGNITION_OFF;
        }
        send(routingKey, txid, dto, timeInfo);
    }

    public void send(UUID txid, MdtCycleLogMessage dto, MdtLogRequestTimeInfo timeInfo, @Nullable String routingKey) {
        if (routingKey == null) {
            routingKey = AMQPConfig.QueueNames.MDT_CAR_LOCATION;
        }
        send(routingKey, txid, dto, timeInfo);
    }

    private void send(String routingKey, UUID txid, Object dto, MdtLogRequestTimeInfo timeInfo) {
        var message = new MdtEventMessage<>(txid, dto, timeInfo.sentAt(), timeInfo.receivedAt());
        amqpService.send(routingKey, message);
    }
}
