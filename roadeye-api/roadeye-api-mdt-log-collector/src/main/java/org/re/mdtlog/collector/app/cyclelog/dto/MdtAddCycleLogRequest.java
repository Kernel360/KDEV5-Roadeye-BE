package org.re.mdtlog.collector.app.cyclelog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import org.re.mdtlog.collector.app.common.dto.MdtLogRequestTimeInfo;
import org.re.mdtlog.collector.app.databind.MdtLogGpsConditionDeserializer;
import org.re.mdtlog.domain.MdtLog;
import org.re.mdtlog.domain.MdtLogEventType;
import org.re.mdtlog.domain.MdtLogGpsCondition;
import org.re.mdtlog.domain.MdtTransactionId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PACKAGE)
public record MdtAddCycleLogRequest(
    @JsonProperty("mdn")
    @NotNull
    String carId,

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
    public MdtAddCycleLogRequest {
        if (cycleCount != cycleLogList.size()) {
            throw new IllegalArgumentException("Cycle count mismatch");
        }
    }

    public List<MdtLog> toMdtLogList(MdtTransactionId tuid, MdtLogRequestTimeInfo timeInfo) {
        return cycleLogList.stream()
            .map((item) -> {
                    var occurredAtWithSec = occurredAt.plusSeconds(item.sec);
                    return MdtLog.builder()
                        .eventType(MdtLogEventType.CycleLog)
                        .txUid(tuid)
                        .carId(carId)
                        .terminalId(terminalId)
                        .manufactureId(manufacturerId)
                        .packetVer(packetVersion)
                        .deviceId(deviceId)
                        .gpsCond(item.gpsCondition)
                        .gpsLat(item.gpsLatitude)
                        .gpsLon(item.gpsLongitude)
                        .mdtAngle(item.mdtAngle)
                        .mdtSpeed(item.mdtSpeed)
                        .mdtMileageSum(item.mdtMileageSum)
                        .occurredAt(occurredAtWithSec)
                        .sentAt(timeInfo.sentAt())
                        .receivedAt(timeInfo.receivedAt())
                        .build();
                }
            )
            .toList();
    }

    @Builder(access = AccessLevel.PACKAGE)
    public record MdtCycleLogItem(
        int sec,

        @JsonProperty("gcd")
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
    }
}
