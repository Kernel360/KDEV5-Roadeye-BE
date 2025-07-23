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

    private int distance;

    private int duration;

    public DailyDrivingStatistics(LocalDateTime date, int totalTripCount, int distance, int duration) {
        this.date = date;
        this.totalTripCount = totalTripCount;
        this.distance = distance;
        this.duration = duration;
    }

    public static DailyDrivingStatistics create(LocalDateTime date){
        return new DailyDrivingStatistics(date, 0, 0, 0);
    }
}
