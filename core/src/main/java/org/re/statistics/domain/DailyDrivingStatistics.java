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
public class DailyDrivingStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime date;

    private int totalTripCount;

    private int averageDistance;

    private int averageDuration;

    private Long maxDrivingId;

    private DailyDrivingStatistics(LocalDateTime date, int totalTripCount, int averageDistance, int averageDuration, Long maxDrivingId) {
        this.date = date;
        this.totalTripCount = totalTripCount;
        this.averageDistance = averageDistance;
        this.averageDuration = averageDuration;
        this.maxDrivingId = maxDrivingId;
    }

    public static DailyDrivingStatistics of(LocalDateTime date, int totalTripCount, int averageDistance, int averageDuration, Long maxDrivingId) {
        return new DailyDrivingStatistics(date, totalTripCount, averageDistance, averageDuration, maxDrivingId);
    }
}
