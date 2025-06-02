package org.re.hq.car.repository;

import org.re.hq.car.domain.Car;
import org.re.hq.domain.common.EntityLifecycleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findByCompanyIdAndStatus(Long companyId, EntityLifecycleStatus entityLifecycleStatus, Pageable pageable);

    Long countByCompanyIdAndStatus(Long companyId, EntityLifecycleStatus status);

    Optional<Car> findByCompanyIdAndIdAndStatus(Long companyId, Long id, EntityLifecycleStatus status);
}
