package org.re.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private EntityLifecycleStatus status = EntityLifecycleStatus.ACTIVE;

    public void delete() {
        status = EntityLifecycleStatus.DELETED;
    }

    public void enable() {
        status = EntityLifecycleStatus.ACTIVE;
    }

    public void disable() {
        status = EntityLifecycleStatus.DISABLED;
    }
}
