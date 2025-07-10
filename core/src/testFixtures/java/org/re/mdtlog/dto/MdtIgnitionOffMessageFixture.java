package org.re.mdtlog.dto;

import org.re.mdtlog.domain.MdtLogGpsCondition;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MdtIgnitionOffMessageFixture {
    public static MdtIgnitionOffMessage create() {
        var mdn = 123456789L;
        var terminalId = "TID";
        var manufacturerId = "MID";
        var packetVersion = 1;
        var deviceId = "DID";
        var ignitionOnTime = LocalDateTime.of(2020, 1, 1, 0, 0);
        var ignitionOffTime = LocalDateTime.of(2020, 1, 1, 1, 0);
        var gpsCondition = MdtLogGpsCondition.NORMAL;
        var gpsLatitude = new BigDecimal("37.5665");
        var gpsLongitude = new BigDecimal("126.978");
        var mdtAngle = 180;
        var gpsSpeed = 60;
        var mileageSum = 1000;
        return new MdtIgnitionOffMessage(
            mdn,
            terminalId,
            manufacturerId,
            packetVersion,
            deviceId,
            ignitionOnTime,
            ignitionOffTime,
            gpsCondition,
            gpsLatitude,
            gpsLongitude,
            mdtAngle,
            gpsSpeed,
            mileageSum
        );
    }
}
