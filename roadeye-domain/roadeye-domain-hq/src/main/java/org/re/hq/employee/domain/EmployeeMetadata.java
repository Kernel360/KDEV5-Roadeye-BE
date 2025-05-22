package org.re.hq.employee.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeMetadata {

    @Setter(AccessLevel.PRIVATE)
    private String name;

    @Setter(AccessLevel.PRIVATE)
    private String position;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    private EmployeeMetadata(String name, String position, EmployeeRole role) {
        this.name = name;
        this.position = position;
        this.role = role;
    }

    public static EmployeeMetadata createRoot(String name, String position) {
        return new EmployeeMetadata(
            name,
            position,
            EmployeeRole.ROOT
        );
    }

    public static EmployeeMetadata createNormal(String name, String position) {
        return new EmployeeMetadata(
            name,
            position,
            EmployeeRole.NORMAL
        );
    }

    public void updateName(String name) {
        Optional.ofNullable(name).ifPresent(this::setName);
    }

    public void updatePosition(String position) {
        Optional.ofNullable(position).ifPresent(this::setPosition);
    }
}
