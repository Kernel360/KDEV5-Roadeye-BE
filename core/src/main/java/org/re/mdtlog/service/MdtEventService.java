package org.re.mdtlog.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.service.CarDomainService;
import org.re.driving.domain.DrivingSnapShot;
import org.re.driving.service.DrivingHistoryDomainService;
import org.re.location.service.LocationHistoryDomainService;
import org.re.mdtlog.domain.MdtLogRepository;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.dto.MdtEventMessage;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MdtEventService {
    private final MdtLogRepository mdtLogRepository;
    private final CarDomainService carDomainService;
    private final DrivingHistoryDomainService drivingHistoryDomainService;
    private final LocationHistoryDomainService locationHistoryDomainService;

    @Transactional
    public void handleMdtCarLocationMessageBatch(List<MdtEventMessage<MdtCycleLogMessage>> batch) {
        for (MdtEventMessage<MdtCycleLogMessage> message : batch) {
            var logs = message.payload().toLogEntries(message.transactionId(), message.sentAt(), message.receivedAt());
            mdtLogRepository.saveAll(logs);

            var drivingHistory = drivingHistoryDomainService.findHistoryInProgress(message.payload().carId(), message.transactionId());
            if (drivingHistory == null) {
                log.warn("No driving history found for car: {}, TUID: {}.", message.payload().carId(), message.transactionId());
            } else {
                locationHistoryDomainService.sampling(drivingHistory, logs);
            }
        }
    }

    @Transactional
    public void handleMdtIgnitionOnMessage(MdtEventMessage<MdtIgnitionOnMessage> message) {
        var log = message.payload().toLogEntry(message.transactionId(), message.sentAt(), message.receivedAt());
        mdtLogRepository.save(log);

        var car = carDomainService.getCarById(message.payload().carId());
        car.turnOnIgnition(message);

        drivingHistoryDomainService.createNew(car, message.payload().ignitionOnTime());
    }

    @Transactional
    public void handleMdtIgnitionOffMessage(MdtEventMessage<MdtIgnitionOffMessage> message) {
        var mdtlog = message.payload().toLogEntry(message.transactionId(), message.sentAt(), message.receivedAt());
        mdtLogRepository.save(mdtlog);

        var car = carDomainService.getCarById(message.payload().carId());
        car.turnOffIgnition(message);

        var driving = drivingHistoryDomainService.findHistoryInProgress(car, message.transactionId());
        if (driving == null) {
            log.warn("No driving history found for car: {}, TUID: {}.", car.getId(), message.transactionId());
        } else {
            var snapshot = DrivingSnapShot.from(car, message.payload().ignitionOffTime());
            drivingHistoryDomainService.end(driving, snapshot, message.payload().ignitionOffTime());
        }
    }
}
