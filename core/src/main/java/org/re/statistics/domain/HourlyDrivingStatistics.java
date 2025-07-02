package org.re.statistics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HourlyDrivingStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime date;

    private int hour;

    private int vehicleCount;

    private HourlyDrivingStatistics(LocalDateTime date, int hour, int vehicleCount) {
        this.date = date;
        this.hour = hour;
        this.vehicleCount = vehicleCount;
    }
    public static HourlyDrivingStatistics of(LocalDateTime date, int hour, int vehicleCount) {
        return new HourlyDrivingStatistics(date, hour, vehicleCount);
    }
}
