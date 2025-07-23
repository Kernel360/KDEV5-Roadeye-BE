package org.re.location.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record DrivingMoment(
    @Column(name = "driving_datetime")
    LocalDateTime datetime,
    int speed
) {
}
