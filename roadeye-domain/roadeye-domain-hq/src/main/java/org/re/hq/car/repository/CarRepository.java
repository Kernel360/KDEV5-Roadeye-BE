package org.re.hq.car.repository;

import org.re.hq.car.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findByCompanyId(Integer companyId, Pageable pageable);
}
