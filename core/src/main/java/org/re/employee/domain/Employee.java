package org.re.employee.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.re.common.domain.BaseEntity;
import org.re.employee.dto.UpdateEmployeeCommand;

@Entity
@Getter
@SQLRestriction("status != 'DELETED'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends BaseEntity {
    @Column(nullable = false)
    private Long companyId;

    @Embedded
    private EmployeeCredentials credentials;

    @Embedded
    private EmployeeMetadata metadata;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    private Employee(Long companyId, EmployeeCredentials credentials, EmployeeMetadata metadata, EmployeeRole role) {
        this.companyId = companyId;
        this.credentials = credentials;
        this.metadata = metadata;
        this.role = role;
    }

    public static Employee createNormal(Long companyId, EmployeeCredentials authentication, EmployeeMetadata metadata) {
        return new Employee(
            companyId,
            authentication,
            metadata,
            EmployeeRole.NORMAL
        );
    }

    public static Employee createRoot(Long companyId, EmployeeCredentials authentication, EmployeeMetadata metadata) {
        return new Employee(
            companyId,
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
