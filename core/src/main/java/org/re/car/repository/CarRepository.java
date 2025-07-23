package org.re.car.repository;

import org.re.car.domain.Car;
import org.re.car.domain.CarIgnitionStatus;
import org.re.common.domain.EntityLifecycleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryQueryDsl {
    Optional<Car> findByIdAndStatus(Long carId, EntityLifecycleStatus entityLifecycleStatus);

    Page<Car> findByCompanyIdAndStatus(Long companyId, EntityLifecycleStatus entityLifecycleStatus, Pageable pageable);

    List<Car> findAllByCompanyIdAndStatus(Long companyId, EntityLifecycleStatus entityLifecycleStatus);

    @Query("""
        SELECT c
        FROM Car c
        WHERE c.company.id = :companyId
          AND c.mdtStatus.ignition = :ignitionStatus
          AND c.status = :entityLifecycleStatus
        """)
    Page<Car> findByCompanyIdAndIgnitionStatusAndStatus(Long companyId, CarIgnitionStatus ignitionStatus, EntityLifecycleStatus entityLifecycleStatus, Pageable pageable);

    @Query("""
        SELECT c
        FROM Car c
        WHERE c.company.id = :companyId
          AND c.mdtStatus.ignition = :ignitionStatus
          AND c.status = :entityLifecycleStatus
        """)
    List<Car> findByCompanyIdAndIgnitionStatusAndStatus(Long companyId, CarIgnitionStatus ignitionStatus, EntityLifecycleStatus entityLifecycleStatus);

    Long countByCompanyIdAndStatus(Long companyId, EntityLifecycleStatus status);

    @Query("""
        SELECT COUNT(c)
        FROM Car c
        WHERE c.company.id = :companyId
          AND c.mdtStatus.ignition = :ignitionStatus
          AND c.status = :entityLifecycleStatus
        """)
    Long countByCompanyIdAndIgnitionStatusAndStatus(Long companyId, CarIgnitionStatus ignitionStatus, EntityLifecycleStatus entityLifecycleStatus);

    Optional<Car> findByCompanyIdAndIdAndStatus(Long companyId, Long id, EntityLifecycleStatus status);

    boolean existsById(Long carId);
}
