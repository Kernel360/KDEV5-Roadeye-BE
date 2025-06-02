package org.re.hq.car.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarProfile {
    @Setter
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 10)
    private String licenseNumber;

    @Setter
    @Column(length = 512)
    private String imageUrl;

    public CarProfile(String name, String licenseNumber, String imageUrl) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.imageUrl = imageUrl;
    }
}
