package org.re.mdtlog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.service.CarDomainService;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.re.mdtlog.messaging.MdtLogMessagingService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MdtIgnitionService {
    private final CarDomainService carDomainService;
    private final MdtLogMessagingService mdtLogMessagingService;

    public void ignitionOn(TransactionUUID tuid, MdtIgnitionOnMessage dto, MdtLogRequestTimeInfo timeInfo) {
        var car = carDomainService.getCarById(dto.carId());
        if (car.isIgnitionOn()) {
            if (Objects.equals(car.getMdtStatus().getActiveTuid(), tuid)) {
                log.warn("Ignition is already on for carId: {}, TUID: {}", dto.carId(), tuid);
                return; // no need to send a message again.
            }
            throw new AppException(MdtLogExceptionCode.IGNITION_ALREADY_ON);
        }

        mdtLogMessagingService.send(tuid, dto, timeInfo);
    }

    public void ignitionOff(TransactionUUID tuid, MdtIgnitionOffMessage dto, MdtLogRequestTimeInfo timeInfo) {
        var car = carDomainService.getCarById(dto.carId());
        if (!Objects.equals(car.getMdtStatus().getActiveTuid(), tuid)) {
            throw new AppException(MdtLogExceptionCode.TUID_ERROR);
        }

        mdtLogMessagingService.send(tuid, dto, timeInfo);
    }
}
