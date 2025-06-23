package org.re.mdtlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import org.re.car.domain.CarLocation;
import org.re.mdtlog.databind.MdtLogGpsConditionDeserializer;
import org.re.mdtlog.databind.MdtLogGpsConditionSerializer;
import org.re.mdtlog.domain.MdtLog;
import org.re.mdtlog.domain.MdtLogEventType;
import org.re.mdtlog.domain.MdtLogGpsCondition;
import org.re.mdtlog.domain.TransactionUUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PACKAGE)
public record MdtCycleLogMessage(
    @JsonProperty("mdn")
    @NotNull
    Long carId,

    @JsonProperty("tid")
    @NotNull
    String terminalId,

    @JsonProperty("mid")
    @NotNull
    String manufacturerId,

    @JsonProperty("pv")
    @Min(0)
    @Max(65535)
    int packetVersion,

    @JsonProperty("did")
    @NotNull
    String deviceId,

    @JsonProperty("oTime")
    @JsonFormat(pattern = "yyyyMMddHHmm")
    @NotNull
    LocalDateTime occurredAt,

    @JsonProperty("cCnt")
    int cycleCount,

    @JsonProperty("cList")
    @NotNull
    List<MdtCycleLogItem> cycleLogList
) {
    public MdtCycleLogMessage {
        if (cycleCount != cycleLogList.size()) {
            throw new IllegalArgumentException("Cycle count mismatch");
        }
    }

    public List<MdtLog> toLogEntries(TransactionUUID transactionUUID, LocalDateTime sentAt, @NotNull LocalDateTime receivedAt) {
        return cycleLogList.stream()
            .map(item -> {
                var occurredAt = this.occurredAt.plusSeconds(item.sec());
                return MdtLog.builder()
                    .packetVer(packetVersion)
                    .eventType(MdtLogEventType.CYCLE_LOG)
                    .txUid(transactionUUID)
                    .carId(carId)
                    .terminalId(terminalId)
                    .manufactureId(manufacturerId)
                    .deviceId(deviceId)
                    .gpsCond(item.gpsCondition())
                    .gpsLat(item.gpsLatitude())
                    .gpsLon(item.gpsLongitude())
                    .mdtAngle(item.mdtAngle())
                    .mdtSpeed(item.mdtSpeed())
                    .mdtMileageSum(item.mdtMileageSum())
                    .mdtBatteryVoltage(item.batteryVoltage())
                    .occurredAt(occurredAt)
                    .sentAt(sentAt)
                    .receivedAt(receivedAt)
                    .build();
            })
            .toList();
    }

    @Builder(access = AccessLevel.PACKAGE)
    public record MdtCycleLogItem(
        int sec,

        @JsonProperty("gcd")
        @JsonSerialize(using = MdtLogGpsConditionSerializer.class)
        @JsonDeserialize(using = MdtLogGpsConditionDeserializer.class)
        @NotNull
        MdtLogGpsCondition gpsCondition,

        @JsonProperty("lat")
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        @NotNull
        BigDecimal gpsLatitude,

        @JsonProperty("lon")
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        @NotNull
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
        int mdtMileageSum,

        @JsonProperty("bat")
        @Min(0)
        @Max(9999)
        int batteryVoltage
    ) {
        public CarLocation toCarLocation() {
            return new CarLocation(gpsLatitude, gpsLongitude);
        }
    }
}
