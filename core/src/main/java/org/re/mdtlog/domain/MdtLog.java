package org.re.mdtlog.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.car.domain.CarLocation;
import org.re.mdtlog.converter.MdtLogEventTypeConverter;
import org.re.mdtlog.converter.MdtLogGpsConditionConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MdtLog {
    @Id
    @Tsid // @see io.hypersistence.utils.hibernate.id.TsidGenerator
    @Column(name = "log_id", nullable = false)
    private Long id;

    @Column(name = "packet_ver", nullable = false)
    private int packetVer;

    @Convert(converter = MdtLogEventTypeConverter.class)
    @Column(name = "event_type", length = 10, nullable = false)
    private MdtLogEventType eventType;

    @Column(name = "tx_Uid", columnDefinition = "BINARY(16)", nullable = false)
    private UUID txUid;

    @Column(name = "car_id", nullable = false)
    private Long carId;

    @Column(name = "terminal_id", length = 16, nullable = false)
    private String terminalId;

    @Column(name = "manufacture_id", length = 16, nullable = false)
    private String manufactureId;

    @Column(name = "device_id", length = 16, nullable = false)
    private String deviceId;

    @Convert(converter = MdtLogGpsConditionConverter.class)
    @Column(name = "gps_cond", length = 1, nullable = false, columnDefinition = "CHAR(1)")
    private MdtLogGpsCondition gpsCond;

    @Column(name = "gps_lat", precision = 9, scale = 6, nullable = false)
    private BigDecimal gpsLat;

    @Column(name = "gps_lon", precision = 10, scale = 6, nullable = false)
    private BigDecimal gpsLon;

    @Column(name = "mdt_angle", nullable = false)
    private int mdtAngle;

    @Column(name = "mdt_speed", nullable = false)
    private int mdtSpeed;

    @Column(name = "mdt_mileage_sum", nullable = false)
    private int mdtMileageSum;

    @Column(name = "mdt_battery_voltage")
    private Integer mdtBatteryVoltage;

    @Column(name = "mdt_ignition_onTime")
    private LocalDateTime mdtIgnitionOnTime;

    @Column(name = "mdt_ignition_offTime")
    private LocalDateTime mdtIgnitionOffTime;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @Builder
    MdtLog(
        int packetVer, MdtLogEventType eventType, UUID txUid, Long carId, String terminalId, String manufactureId, String deviceId, MdtLogGpsCondition gpsCond, BigDecimal gpsLat,
        BigDecimal gpsLon, int mdtAngle, int mdtSpeed, int mdtMileageSum, Integer mdtBatteryVoltage, LocalDateTime mdtIgnitionOnTime, LocalDateTime mdtIgnitionOffTime, LocalDateTime occurredAt,
        LocalDateTime sentAt, LocalDateTime receivedAt
    ) {
        this.packetVer = packetVer;
        this.eventType = eventType;
        this.txUid = txUid;
        this.carId = carId;
        this.terminalId = terminalId;
        this.manufactureId = manufactureId;
        this.deviceId = deviceId;
        this.gpsCond = gpsCond;
        this.gpsLat = gpsLat;
        this.gpsLon = gpsLon;
        this.mdtAngle = mdtAngle;
        this.mdtSpeed = mdtSpeed;
        this.mdtMileageSum = mdtMileageSum;
        this.mdtBatteryVoltage = mdtBatteryVoltage;
        this.mdtIgnitionOnTime = mdtIgnitionOnTime;
        this.mdtIgnitionOffTime = mdtIgnitionOffTime;
        this.occurredAt = occurredAt;
        this.sentAt = sentAt;
        this.receivedAt = receivedAt;
    }

    public CarLocation toCarLocation() {
        return new CarLocation(gpsLat, gpsLon);
    }
}
