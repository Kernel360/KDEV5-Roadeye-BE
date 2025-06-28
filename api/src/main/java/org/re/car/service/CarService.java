package org.re.car.service;

import lombok.RequiredArgsConstructor;
import org.re.car.api.payload.CarCreationRequest;
import org.re.car.api.payload.CarUpdateRequest;
import org.re.car.domain.Car;
import org.re.car.domain.CarIgnitionStatus;
import org.re.common.domain.EntityLifecycleStatus;
import org.re.company.domain.CompanyId;
import org.re.company.service.CompanyDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {
    private final CompanyDomainService companyDomainService;
    private final CarDomainService carDomainService;

    public Page<Car> getCars(CompanyId companyId, Pageable pageable) {
        var company = companyDomainService.findById(companyId.value());
        return carDomainService.getCars(company, pageable);
    }

    public List<Car> getAllCars(CompanyId companyId) {
        var company = companyDomainService.findById(companyId.value());
        return carDomainService.getCars(company);
    }

    public Car getCarById(CompanyId companyId, Long carId) {
        var company = companyDomainService.findById(companyId.value());
        return carDomainService.getCarById(company, carId);
    }

    public Page<Car> searchByIgnitionStatus(CompanyId companyId, CarIgnitionStatus status, Pageable pageable) {
        var company = companyDomainService.findById(companyId.value());
        if (status == null) {
            return carDomainService.getCars(company, pageable);
        }
        return carDomainService.searchByIgnitionStatus(company, status, pageable);
    }

    public List<Car> searchByIgnitionStatus(CompanyId companyId, CarIgnitionStatus status) {
        var company = companyDomainService.findById(companyId.value());
        return carDomainService.searchByIgnitionStatus(company, status);
    }

    public Long countByIgnitionStatus(
        CompanyId companyId, CarIgnitionStatus status,
        EntityLifecycleStatus entityLifecycleStatus
    ) {
        var company = companyDomainService.findById(companyId.value());
        return carDomainService.countByIgnitionStatus(company, status, entityLifecycleStatus);
    }

    public Car createCar(CompanyId companyId, CarCreationRequest request) {
        var company = companyDomainService.findById(companyId.value());
        var command = request.toCommand();
        return carDomainService.createCar(company, command);
    }

    public Car updateCarProfile(CompanyId companyId, Long carId, CarUpdateRequest request) {
        var company = companyDomainService.findById(companyId.value());
        var car = carDomainService.getCarById(company, carId);
        var command = request.toCommand();
        return carDomainService.updateCarProfile(car, command);
    }

    public void deleteCar(CompanyId companyId, Long carId) {
        var company = companyDomainService.findById(companyId.value());
        var car = carDomainService.getCarById(company, carId);
        carDomainService.deleteCar(car);
    }
}
