package org.re.hq.car.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarLocation {

    @Column(name = "gps_lat", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "gps_lon", precision = 9, scale = 6)
    private BigDecimal longitude;

    public CarLocation(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static CarLocation createDefault() {
        return new CarLocation(BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
