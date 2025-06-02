package org.re.hq.employee.domain;

import jakarta.persistence.Embeddable;
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

    private EmployeeMetadata(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public static EmployeeMetadata create(String name, String position) {
        return new EmployeeMetadata(name, position);
    }

    public void updateName(String name) {
        Optional.ofNullable(name).ifPresent(this::setName);
    }

    public void updatePosition(String position) {
        Optional.ofNullable(position).ifPresent(this::setPosition);
    }
}
