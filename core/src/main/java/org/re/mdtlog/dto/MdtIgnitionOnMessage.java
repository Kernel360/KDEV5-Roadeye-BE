package org.re.mdtlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @JsonProperty("mdn")
    Long carId,

    @JsonProperty("tid")
    String terminalId,

    @JsonProperty("mid")
    String manufacturerId,

    @JsonProperty("pv")
    @ValidPacketVersion
    int packetVersion,

    @JsonProperty("did")
    String deviceId,

    @JsonProperty("onTime")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime ignitionOnTime,

    @NotNull
    @JsonProperty("gcd")
    @JsonSerialize(using = MdtLogGpsConditionSerializer.class)
    @JsonDeserialize(using = MdtLogGpsConditionDeserializer.class)
    MdtLogGpsCondition gpsCondition,

    @JsonProperty("lat")
    @ValidLatitude
    BigDecimal gpsLatitude,

    @JsonProperty("lon")
    @ValidLongitude
    BigDecimal gpsLongitude,

    @JsonProperty("ang")
    @ValidAngle
    int mdtAngle,

    @JsonProperty("spd")
    @ValidSpeed
    int mdtSpeed,

    @JsonProperty("sum")
    @Min(0)
    @Max(9999999)
    int mdtMileageSum
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
