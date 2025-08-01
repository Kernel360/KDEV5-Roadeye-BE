package org.re.company.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.re.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "bizno", nullable = false, length = 50)
    private String businessNumber;

    @Column(nullable = false, length = 50)
    private String email;

    public Company(String name, String businessNumber, String email) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.email = email;
    }
}
