package org.re.mdtlog.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MdtCycleLogMessageFixture {
    public static MdtCycleLogMessage create() {
        var carId = 1L;
        return create(carId);
    }

    public static MdtCycleLogMessage create(Long carId) {
        return createWithLogItems(carId, 0);
    }

    public static MdtCycleLogMessage createWithLogItems(int cycleCount) {
        return createWithLogItems(1L, cycleCount);
    }

    public static MdtCycleLogMessage createWithLogItems(Long carId, int cycleCount) {
        var oTime = LocalDateTime.now();
        var tid = "TID";
        var mid = "MID";
        var did = "DID";
        var packetVersion = 1;
        var cycleLogs = new ArrayList<MdtCycleLogMessage.MdtCycleLogItem>();
        for (int i = 0; i < cycleCount; i++) {
            var item = MdtCycleLogItemFixture.create(0);
            cycleLogs.add(item);
        }
        return new MdtCycleLogMessage(
            carId,
            tid,
            mid,
            packetVersion,
            did,
            oTime,
            cycleCount,
            cycleLogs
        );
    }
}
