package org.re.mdtlog.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.domain.Car;
import org.re.car.domain.CarLocation;
import org.re.car.service.CarDomainService;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.service.DrivingHistoryDomainService;
import org.re.location.domain.DrivingMoment;
import org.re.location.domain.LocationHistory;
import org.re.location.service.LocationHistoryDomainService;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.dto.MdtEventMessage;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MdtEventHandler {
    private final MdtLogService mdtLogService;
    private final CarDomainService carDomainService;
    private final DrivingHistoryDomainService drivingHistoryDomainService;
    private final LocationHistoryDomainService locationHistoryDomainService;

    @Transactional
    public void handleMdtCarLocationMessage(MdtEventMessage<MdtCycleLogMessage> message) {
        var logs = message.payload().toLogEntries(message.transactionId(), message.sentAt(), message.receivedAt());
        mdtLogService.save(logs);

        var car = carDomainService.getCarById(message.payload().carId());
        car.updateMdtStatus(message);

        // logs 중 10개마다 하나씩 locationHistory 저장
        var drivingHistory = findHistoryInProgress(car, message);
        List<LocationHistory> sampledLogs = new ArrayList<>();
        for (int i = 0; i < logs.size(); i++) {
            if (i % 10 == 0) {
                DrivingMoment drivingMoment = new DrivingMoment(logs.get(i).getOccurredAt(),logs.get(i).getMdtSpeed());
                CarLocation carLocation = logs.get(i).toCarLocation();
                LocationHistory history = LocationHistory.of(drivingHistory.getId(),carLocation,drivingMoment);
                sampledLogs.add(history);
            }
        }
        locationHistoryDomainService.save(sampledLogs);
    }

    @Transactional
    public void handleMdtIgnitionOnMessage(MdtEventMessage<MdtIgnitionOnMessage> message) {
        var log = message.payload().toLogEntry(message.transactionId(), message.sentAt(), message.receivedAt());
        mdtLogService.save(log);

        var car = carDomainService.getCarById(message.payload().carId());
        car.turnOnIgnition(message);

        drivingHistoryDomainService.createNew(car, message.payload().ignitionOnTime());
    }

    @Transactional
    public void handleMdtIgnitionOffMessage(MdtEventMessage<MdtIgnitionOffMessage> message) {
        var mdtlog = message.payload().toLogEntry(message.transactionId(), message.sentAt(), message.receivedAt());
        mdtLogService.save(mdtlog);

        var car = carDomainService.getCarById(message.payload().carId());
        car.turnOffIgnition(message);

        var drivingHistory = findHistoryInProgress(car, message);
        if (drivingHistory == null) {
            log.warn("No driving history found for car {} and transaction ID {}", car.getId(), message.transactionId());
        } else {
            drivingHistory.end(car, message.payload().ignitionOffTime());
        }
    }

    // TODO: 다른 방법이 더 좋을 것 같다는 생각이 듦.
    private DrivingHistory findHistoryInProgress(Car car, MdtEventMessage<?> message) {
        return drivingHistoryDomainService.findHistoryInProgress(car, message.transactionId());
    }
}
