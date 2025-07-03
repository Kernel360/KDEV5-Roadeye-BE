package org.re.mdtlog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MdtCycleLogMessageFixture {
    public static MdtCycleLogMessage create(Long carId) {
        var oTime = LocalDateTime.now();
        return MdtCycleLogMessage.builder()
            .carId(carId)
            .terminalId("TID")
            .manufacturerId("MID")
            .packetVersion(1)
            .deviceId("DID")
            .occurredAt(oTime)
            .cycleCount(0)
            .cycleLogList(List.of())
            .build();
    }
}
