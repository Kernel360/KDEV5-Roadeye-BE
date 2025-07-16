package org.re.car.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.re.car.api.payload.*;
import org.re.car.domain.Car;
import org.re.car.domain.CarIgnitionStatus;
import org.re.car.service.CarService;
import org.re.common.api.payload.ListResponse;
import org.re.common.api.payload.PageResponse;
import org.re.common.api.payload.SingleItemResponse;
import org.re.common.domain.EntityLifecycleStatus;
import org.re.company.domain.CompanyId;
import org.re.security.access.ManagerOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarApi {
    private final CarService carService;

    @GetMapping
    public PageResponse<CarInfoSimple> getCars(CompanyId companyId, Pageable pageable) {
        var page = carService.getCars(companyId, pageable);
        return PageResponse.of(page, CarInfoSimple::from);
    }

    @GetMapping("/all")
    public ListResponse<CarInfoDetails> getAllCars(CompanyId companyId) {
        var page = carService.getAllCars(companyId);
        return ListResponse.of(page, CarInfoDetails::from);
    }

    @GetMapping("/{carId}")
    public SingleItemResponse<CarInfoDetails> getCarById(CompanyId companyId, @PathVariable Long carId) {
        var car = carService.getCarById(companyId, carId);
        return SingleItemResponse.of(car, CarInfoDetails::from);
    }

    @GetMapping("/ignition")
    public ListResponse<CarInfoDetails> getCarsByIgnition(CompanyId companyId, @RequestParam CarIgnitionStatus status) {
        var page = carService.searchByIgnitionStatus(companyId, status);
        return ListResponse.of(page, CarInfoDetails::from);
    }

    @GetMapping("/search")
    public PageResponse<CarInfoDetails> searchCars(
        @Valid CarSearchRequest request,
        Pageable pageable,
        CompanyId companyId
    ) {
        var page = carService.search(companyId, request, pageable);
        return PageResponse.of(page, CarInfoDetails::from);
    }

    @GetMapping("/search/ignition")
    public PageResponse<CarInfoDetails> getCarsByIgnition(
        CompanyId companyId, @RequestParam(required = false) CarIgnitionStatus status,
        Pageable pageable
    ) {
        Page<Car> page = carService.searchByIgnitionStatus(companyId, status, pageable);
        return PageResponse.of(page, CarInfoDetails::from);
    }

    @GetMapping("/count/ignition")
    public SingleItemResponse<Long> countCarsByIgnition(
        CompanyId companyId, @RequestParam CarIgnitionStatus status,
        @RequestParam EntityLifecycleStatus lifecycleStatus
    ) {
        var count = carService.countByIgnitionStatus(companyId, status, lifecycleStatus);
        return SingleItemResponse.of(count);
    }

    @ManagerOnly
    @PostMapping
    public SingleItemResponse<CarInfoSimple> createCar(CompanyId companyId, @RequestBody CarCreationRequest request) {
        var createdCar = carService.createCar(companyId, request);
        return SingleItemResponse.of(createdCar, CarInfoSimple::from);
    }

    @ManagerOnly
    @PatchMapping("/{carId}")
    public SingleItemResponse<CarInfoSimple> updateCarProfile(
        CompanyId companyId,
        @PathVariable Long carId,
        @RequestBody CarUpdateRequest request
    ) {
        var updatedCar = carService.updateCarProfile(companyId, carId, request);
        return SingleItemResponse.of(updatedCar, CarInfoSimple::from);
    }

    @ManagerOnly
    @DeleteMapping("/{carId}")
    public void deleteCar(
        CompanyId companyId,
        @PathVariable Long carId
    ) {
        carService.deleteCar(companyId, carId);
    }
}
