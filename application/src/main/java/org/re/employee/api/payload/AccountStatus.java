package org.re.employee.api.payload;

import org.re.common.domain.EntityLifecycleStatus;

public enum AccountStatus {
    ACTIVE,
    DISABLED;

    public EntityLifecycleStatus toEntityLifecycleStatus(AccountStatus status) {
        return switch (status) {
            case ACTIVE -> EntityLifecycleStatus.ACTIVE;
            case DISABLED -> EntityLifecycleStatus.DISABLED;
        };
    }
}
