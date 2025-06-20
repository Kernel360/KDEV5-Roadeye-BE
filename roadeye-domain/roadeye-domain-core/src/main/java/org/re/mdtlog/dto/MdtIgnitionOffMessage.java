package org.re.mdtlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import org.re.car.domain.CarLocation;
import org.re.mdtlog.databind.MdtLogGpsConditionDeserializer;
import org.re.mdtlog.domain.MdtLog;
import org.re.mdtlog.domain.MdtLogEventType;
import org.re.mdtlog.domain.MdtLogGpsCondition;
import org.re.mdtlog.domain.TransactionUUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MdtIgnitionOffMessage(
    @JsonProperty("mdn")
    Long carId,

    @JsonProperty("tid")
    String terminalId,

    @JsonProperty("mid")
    String manufacturerId,

    @JsonProperty("pv")
    @Min(0)
    @Max(65535)
    int packetVersion,

    @JsonProperty("did")
    String deviceId,

    @JsonProperty("onTime")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime ignitionOnTime,

    @JsonProperty("offTime")
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    LocalDateTime ignitionOffTime,

    @NotNull
    @JsonProperty("gcd")
    @JsonDeserialize(using = MdtLogGpsConditionDeserializer.class)
    MdtLogGpsCondition gpsCondition,

    @JsonProperty("lat")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    BigDecimal gpsLatitude,

    @JsonProperty("lon")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    BigDecimal gpsLongitude,

    @JsonProperty("ang")
    @Min(0)
    @Max(365)
    int mdtAngle,

    @JsonProperty("spd")
    @Min(0)
    @Max(255)
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
            .mdtIgnitionOffTime(ignitionOffTime)
            .occurredAt(ignitionOffTime)
            .sentAt(sentAt)
            .receivedAt(receivedAt)
            .build();
    }
}
