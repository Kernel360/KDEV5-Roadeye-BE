package org.re.car.api;

import lombok.RequiredArgsConstructor;
import org.re.car.api.payload.CarCreationRequest;
import org.re.car.api.payload.CarInfoDetails;
import org.re.car.api.payload.CarInfoSimple;
import org.re.car.api.payload.CarUpdateRequest;
import org.re.car.domain.CarIgnitionStatus;
import org.re.car.service.CarService;
import org.re.common.api.payload.ListResponse;
import org.re.common.api.payload.PageResponse;
import org.re.common.api.payload.SingleItemResponse;
import org.re.security.access.ManagerOnly;
import org.re.tenant.TenantId;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarApi {
    private final CarService carService;

    @GetMapping
    public PageResponse<CarInfoSimple> getCars(TenantId tenantId, Pageable pageable) {
        var page = carService.getCars(tenantId, pageable);
        return PageResponse.of(page, CarInfoSimple::from);
    }

    @GetMapping("/all")
    public ListResponse<CarInfoDetails> getAllCars(TenantId tenantId) {
        var page = carService.getAllCars(tenantId);
        return ListResponse.of(page, CarInfoDetails::from);
    }

    @GetMapping("/{carId}")
    public SingleItemResponse<CarInfoDetails> getCarById(TenantId tenantId, @PathVariable Long carId) {
        var car = carService.getCarById(tenantId, carId);
        return SingleItemResponse.of(car, CarInfoDetails::from);
    }

    @GetMapping("/search/ignition")
    public PageResponse<CarInfoSimple> getCarsByIgnition(TenantId tenantId, @RequestParam CarIgnitionStatus status, Pageable pageable) {
        var page = carService.searchByIgnitionStatus(tenantId, status, pageable);
        return PageResponse.of(page, CarInfoSimple::from);
    }

    @GetMapping("/count/ignition")
    public SingleItemResponse<Long> countCarsByIgnition(TenantId tenantId, @RequestParam CarIgnitionStatus status) {
        var count = carService.countByIgnitionStatus(tenantId, status);
        return SingleItemResponse.of(count);
    }

    @ManagerOnly
    @PostMapping
    public SingleItemResponse<CarInfoSimple> createCar(TenantId tenantId, @RequestBody CarCreationRequest request) {
        var createdCar = carService.createCar(tenantId, request);
        return SingleItemResponse.of(createdCar, CarInfoSimple::from);
    }

    @ManagerOnly
    @PatchMapping("/{carId}")
    public SingleItemResponse<CarInfoSimple> updateCarProfile(
        TenantId tenantId,
        @PathVariable Long carId,
        @RequestBody CarUpdateRequest request
    ) {
        var updatedCar = carService.updateCarProfile(tenantId, carId, request);
        return SingleItemResponse.of(updatedCar, CarInfoSimple::from);
    }

    @ManagerOnly
    @DeleteMapping("/{carId}")
    public void deleteCar(
        TenantId tenantId,
        @PathVariable Long carId
    ) {
        carService.deleteCar(tenantId, carId);
    }
}
