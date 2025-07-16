package org.re.car.repository;

import org.re.car.domain.Car;
import org.re.car.dto.CarSearchCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarRepositoryQueryDsl {
    Page<Car> search(Long companyId, CarSearchCommand command, Pageable pageable);
}
