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
import org.re.mdtlog.domain.TransactionUUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MdtIgnitionOnMessage(
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
    public CarLocation getLocation() {
        return new CarLocation(gpsLatitude, gpsLongitude);
    }

    public MdtLog toLogEntry(TransactionUUID transactionUUID, LocalDateTime sentAt, LocalDateTime receivedAt) {
        return MdtLog.builder()
            .packetVer(packetVersion)
            .eventType(MdtLogEventType.IGNITION)
            .carId(carId)
            .terminalId(terminalId)
            .manufactureId(manufacturerId)
            .deviceId(deviceId)
            .txUid(transactionUUID)
            .gpsCond(gpsCondition)
            .gpsLat(gpsLatitude)
            .gpsLon(gpsLongitude)
            .mdtAngle(mdtAngle)
            .mdtSpeed(mdtSpeed)
            .mdtMileageSum(mdtMileageSum)
            .mdtIgnitionOnTime(ignitionOnTime)
            .occurredAt(ignitionOnTime)
            .sentAt(sentAt)
            .receivedAt(receivedAt)
            .build();
    }
}
