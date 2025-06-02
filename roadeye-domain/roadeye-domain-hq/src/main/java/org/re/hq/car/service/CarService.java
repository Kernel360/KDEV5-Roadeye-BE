package org.re.hq.car.service;

import lombok.RequiredArgsConstructor;
import org.re.hq.car.domain.Car;
import org.re.hq.car.dto.CarCreationCommand;
import org.re.hq.car.dto.CarDisableCommand;
import org.re.hq.car.dto.CarUpdateCommand;
import org.re.hq.car.repository.CarRepository;
import org.re.hq.company.domain.Company;
import org.re.hq.domain.common.EntityLifecycleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public Page<Car> getCars(Company company, Pageable pageable) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndStatus(companyId, EntityLifecycleStatus.ACTIVE, pageable);
    }

    public Page<Car> getCarsByStatus(Company company, EntityLifecycleStatus status, Pageable pageable) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndStatus(companyId, status, pageable);
    }

    public Car getCarById(Company company, Long carId) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndIdAndStatus(companyId, carId, EntityLifecycleStatus.ACTIVE)
            .orElseThrow(() -> new IllegalArgumentException("Car not found for companyId: " + companyId + " and carId: " + carId));
    }

    public Long countCarsByStatus(Company company, EntityLifecycleStatus status) {
        var companyId = company.getId();
        return carRepository.countByCompanyIdAndStatus(companyId, status);
    }

    public Car createCar(Company company, CarCreationCommand command) {
        var car = command.toEntity(company);
        return carRepository.save(car);
    }

    public Car updateCarProfile(Car car, CarUpdateCommand command) {
        car.update(command);
        return car;
    }

    public Car turnOnIgnition(Car car, UUID transactionId) {
        car.turnOnIgnition(transactionId);
        return car;
    }

    public Car turnOffIgnition(Car car, UUID transactionId) {
        car.turnOffIgnition(transactionId);
        return car;
    }

    public Car resetIgnitionStatus(Car car) {
        car.resetIgnitionStatus();
        return car;
    }

    public void deleteCar(Car car) {
        car.delete();
    }

    public Car disable(Car car, CarDisableCommand command) {
        car.disable(command.reason());
        return car;
    }
}
