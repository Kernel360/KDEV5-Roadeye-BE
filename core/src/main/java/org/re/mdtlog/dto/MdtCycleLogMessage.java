package org.re.mdtlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
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
    @ValidPacketVersion
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
        @ValidLatitude
        @NotNull
        BigDecimal gpsLatitude,

        @JsonProperty("lon")
        @ValidLongitude
        @NotNull
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
