package org.re.car.service;

import lombok.RequiredArgsConstructor;
import org.re.car.domain.Car;
import org.re.car.domain.CarIgnitionStatus;
import org.re.car.dto.CarCreationCommand;
import org.re.car.dto.CarDisableCommand;
import org.re.car.dto.CarUpdateCommand;
import org.re.car.exception.CarDomainException;
import org.re.car.repository.CarRepository;
import org.re.common.domain.EntityLifecycleStatus;
import org.re.common.exception.DomainException;
import org.re.common.stereotype.DomainService;
import org.re.company.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@Transactional
@RequiredArgsConstructor
public class CarDomainService {
    private final CarRepository carRepository;

    public Page<Car> getCars(Company company, Pageable pageable) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndStatus(companyId, EntityLifecycleStatus.ACTIVE, pageable);
    }

    public List<Car> getCars(Company company) {
        var companyId = company.getId();
        return carRepository.findAllByCompanyIdAndStatus(companyId, EntityLifecycleStatus.ACTIVE);
    }

    public Page<Car> getCarsByStatus(Company company, EntityLifecycleStatus status, Pageable pageable) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndStatus(companyId, status, pageable);
    }

    public Car getCarById(Long carId) {
        return carRepository.findByIdAndStatus(carId, EntityLifecycleStatus.ACTIVE)
            .orElseThrow(() -> new DomainException(CarDomainException.CAR_NOT_FOUND));
    }

    public Car getCarById(Company company, Long carId) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndIdAndStatus(companyId, carId, EntityLifecycleStatus.ACTIVE)
            .orElseThrow(() -> new DomainException(CarDomainException.CAR_NOT_FOUND));
    }

    public Page<Car> searchByIgnitionStatus(Company company, CarIgnitionStatus status, Pageable pageable) {
        var companyId = company.getId();
        return carRepository.findByCompanyIdAndIgnitionStatusAndStatus(companyId, status, EntityLifecycleStatus.ACTIVE, pageable);
    }

    public Long countCarsByStatus(Company company, EntityLifecycleStatus status) {
        var companyId = company.getId();
        return carRepository.countByCompanyIdAndStatus(companyId, status);
    }

    public Long countByIgnitionStatus(Company company, CarIgnitionStatus status) {
        var companyId = company.getId();
        return carRepository.countByCompanyIdAndIgnitionStatusAndStatus(companyId, status, EntityLifecycleStatus.ACTIVE);
    }

    public Car createCar(Company company, CarCreationCommand command) {
        var car = command.toEntity(company);
        return carRepository.save(car);
    }

    public Car updateCarProfile(Car car, CarUpdateCommand command) {
        car.update(command);
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
