package org.re.mdtlog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.messaging.MdtLogMessagingService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MdtCycleLogService {
    private final MdtLogMessagingService mdtLogMessagingService;

    public void addCycleLogs(TransactionUUID tuid, MdtCycleLogMessage dto, MdtLogRequestTimeInfo timeInfo) {
        mdtLogMessagingService.send(tuid, dto, timeInfo);
    }
}
