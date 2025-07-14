package org.re.mdtlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import org.re.car.domain.CarLocation;
import org.re.mdtlog.constraints.*;
import org.re.mdtlog.databind.MdtLogGpsConditionDeserializer;
import org.re.mdtlog.databind.MdtLogGpsConditionSerializer;
import org.re.mdtlog.domain.MdtLog;
import org.re.mdtlog.domain.MdtLogEventType;
import org.re.mdtlog.domain.MdtLogGpsCondition;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MdtIgnitionOffMessage(
    @NotNull
    @JsonProperty("mdn")
    Long carId,

    @NotNull
    @JsonProperty("tid")
    String terminalId,

    @NotNull
    @JsonProperty("mid")
    String manufacturerId,

    @NotNull
    @JsonProperty("pv")
    @ValidPacketVersion
    Integer packetVersion,

    @NotNull
    @JsonProperty("did")
    String deviceId,

    @NotNull
    @JsonProperty("onTime")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime ignitionOnTime,

    @NotNull
    @JsonProperty("offTime")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime ignitionOffTime,

    @NotNull
    @JsonProperty("gcd")
    @JsonSerialize(using = MdtLogGpsConditionSerializer.class)
    @JsonDeserialize(using = MdtLogGpsConditionDeserializer.class)
    MdtLogGpsCondition gpsCondition,

    @NotNull
    @JsonProperty("lat")
    @ValidLatitude
    BigDecimal gpsLatitude,

    @NotNull
    @JsonProperty("lon")
    @ValidLongitude
    BigDecimal gpsLongitude,

    @NotNull
    @JsonProperty("ang")
    @ValidAngle
    Integer mdtAngle,

    @NotNull
    @JsonProperty("spd")
    @ValidSpeed
    Integer mdtSpeed,

    @NotNull
    @JsonProperty("sum")
    @ValidMileageSum
    Integer mdtMileageSum
) {
    public MdtLog toLogEntry(UUID txid, LocalDateTime sentAt, LocalDateTime receivedAt) {
        return MdtLog.builder()
            .packetVer(packetVersion)
            .eventType(MdtLogEventType.IGNITION)
            .carId(carId)
            .terminalId(terminalId)
            .manufactureId(manufacturerId)
            .deviceId(deviceId)
            .txUid(txid)
            .gpsCond(gpsCondition)
            .gpsLat(gpsLatitude)
            .gpsLon(gpsLongitude)
            .mdtAngle(mdtAngle)
            .mdtSpeed(mdtSpeed)
            .mdtMileageSum(mdtMileageSum)
            .mdtIgnitionOnTime(ignitionOnTime)
            .mdtIgnitionOffTime(ignitionOffTime)
            .occurredAt(ignitionOffTime)
            .sentAt(sentAt)
            .receivedAt(receivedAt)
            .build();
    }

    public CarLocation toCarLocation() {
        return new CarLocation(gpsLatitude, gpsLongitude);
    }
}
