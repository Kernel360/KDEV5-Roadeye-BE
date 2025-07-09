package org.re.employee.api.payload;

import org.re.common.domain.EntityLifecycleStatus;

public enum AccountStatus {
    ENABLE,
    DISABLE;

    public EntityLifecycleStatus toEntityLifecycleStatus(AccountStatus status) {
        return switch (status) {
            case ENABLE -> EntityLifecycleStatus.ACTIVE;
            case DISABLE -> EntityLifecycleStatus.DISABLED;
        };
    }
}
