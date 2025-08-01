package org.re.statistics.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HourlyDrivingStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime date;

    @Column(name = "\"hour\"")
    private int hour;

    private int vehicleCount;

    public HourlyDrivingStatistics(LocalDateTime date, int hour, int vehicleCount) {
        this.date = date;
        this.hour = hour;
        this.vehicleCount = vehicleCount;
    }
}
