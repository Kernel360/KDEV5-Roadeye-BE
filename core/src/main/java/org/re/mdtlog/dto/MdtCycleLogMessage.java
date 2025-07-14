package org.re.mdtlog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PACKAGE)
public record MdtCycleLogMessage(
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
    @JsonProperty("oTime")
    @JsonFormat(pattern = "yyyyMMddHHmm")
    LocalDateTime occurredAt,

    @NotNull
    @JsonProperty("cCnt")
    Integer cycleCount,

    @Valid
    @NotNull
    @JsonProperty("cList")
    List<MdtCycleLogItem> cycleLogList
) {
    public List<MdtLog> toLogEntries(UUID txid, LocalDateTime sentAt, @NotNull LocalDateTime receivedAt) {
        return cycleLogList.stream()
            .map(item -> {
                var occurredAt = this.occurredAt.plusSeconds(item.sec());
                return MdtLog.builder()
                    .packetVer(packetVersion)
                    .eventType(MdtLogEventType.CYCLE_LOG)
                    .txUid(txid)
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
        @NotNull
        @Min(0)
        Integer sec,

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
        Integer mdtMileageSum,

        @NotNull
        @JsonProperty("bat")
        @ValidBatteryVoltage
        Integer batteryVoltage
    ) {
        public CarLocation toCarLocation() {
            return new CarLocation(gpsLatitude, gpsLongitude);
        }
    }
}
