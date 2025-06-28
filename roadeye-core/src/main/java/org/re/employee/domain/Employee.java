package org.re.employee.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.re.common.domain.BaseEntity;
import org.re.employee.dto.UpdateEmployeeCommand;
import org.re.util.Integers;

@Entity
@Getter
@SQLRestriction("status != 'DELETED'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends BaseEntity {

    @Column(nullable = false)
    private Long tenantId;

    private int tryCount;

    @Embedded
    private EmployeeCredentials credentials;

    @Embedded
    private EmployeeMetadata metadata;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    private Employee(Long tenantId, int tryCount, EmployeeCredentials credentials, EmployeeMetadata metadata, EmployeeRole role) {
        this.tenantId = tenantId;
        this.tryCount = tryCount;
        this.credentials = credentials;
        this.metadata = metadata;
        this.role = role;
    }

    public static Employee of(Long tenantId, EmployeeCredentials authentication, EmployeeMetadata metadata) {
        throw new UnsupportedOperationException();
        //return new Employee(tenantId, 0, authentication, metadata);
    }

    public static Employee createNormal(Long tenantId, EmployeeCredentials authentication, EmployeeMetadata metadata) {
        return new Employee(
            tenantId,
            Integers.ZERO,
            authentication,
            metadata,
            EmployeeRole.NORMAL
        );
    }

    public static Employee createRoot(Long tenantId, EmployeeCredentials authentication, EmployeeMetadata metadata) {
        return new Employee(
            tenantId,
            Integers.ZERO,
            authentication,
            metadata,
            EmployeeRole.ROOT
        );
    }

    public void update(UpdateEmployeeCommand updateEmployeeCommand) {
        metadata.updateName(updateEmployeeCommand.name());
        metadata.updatePosition(updateEmployeeCommand.position());
    }
}
