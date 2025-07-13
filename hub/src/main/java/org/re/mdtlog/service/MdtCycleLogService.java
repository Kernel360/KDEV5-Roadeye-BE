package org.re.mdtlog.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.car.repository.CarRepository;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.common.exception.AppException;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.messaging.MdtLogMessagingService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MdtCycleLogService {
    private final CarRepository carRepository;
    private final MdtLogMessagingService mdtLogMessagingService;

    public void addCycleLogs(UUID txid, MdtCycleLogMessage dto, MdtLogRequestTimeInfo timeInfo) {
        validateDto(dto);

        mdtLogMessagingService.send(txid, dto, timeInfo);
    }

    private void validateDto(MdtCycleLogMessage dto) {
        if (!carRepository.existsById(dto.carId())) {
            throw new AppException(MdtLogExceptionCode.MDN_MISMATCH);
        }
    }
}
