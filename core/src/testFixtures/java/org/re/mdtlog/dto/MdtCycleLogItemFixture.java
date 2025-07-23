package org.re.mdtlog.dto;

import org.re.mdtlog.domain.MdtLogGpsCondition;
import org.re.mdtlog.dto.MdtCycleLogMessage.MdtCycleLogItem;

import java.math.BigDecimal;

public class MdtCycleLogItemFixture {
    public static MdtCycleLogItem create(int sec) {
        var gcd = MdtLogGpsCondition.NORMAL;
        var lat = new BigDecimal("37.5665");
        var lon = new BigDecimal("126.978");
        var ang = 180;
        var spd = 60;
        var sum = 1000;
        var bat = 12;
        return new MdtCycleLogItem(
            sec,
            gcd,
            lat,
            lon,
            ang,
            spd,
            sum,
            bat
        );
    }
}
