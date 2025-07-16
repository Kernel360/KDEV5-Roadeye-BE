package org.re.car.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.re.car.CarFixture;
import org.re.car.dto.CarSearchCommand;
import org.re.company.CompanyFixture;
import org.re.config.QueryDslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({
    QueryDslConfig.class
})
@DataJpaTest
public class CarRepositoryQueryDslTest {
    @Autowired
    EntityManager em;

    @Autowired
    CarRepository carRepository;

    @Test
    @DisplayName("이름으로 검색이 가능해야 한다.")
    void search_by_name_returns_correct_car() {
        // given
        var company = em.merge(CompanyFixture.create());
        var car = CarFixture.create(company);
        var carName = car.getProfile().getName();
        carRepository.save(car);

        var command = CarSearchCommand.builder()
            .name(carName)
            .build();
        var result = carRepository.search(company.getId(), command, PageRequest.of(0, 10));

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(carName, result.getContent().getFirst().getProfile().getName());
    }

    @Test
    @DisplayName("차량 번호로 검색이 가능해야 한다.")
    void search_by_license_number_returns_correct_car() {
        // given
        var company = em.merge(CompanyFixture.create());
        var car = CarFixture.create(company);
        var licenseNumber = car.getProfile().getLicenseNumber();
        carRepository.save(car);

        var command = CarSearchCommand.builder()
            .licenseNumber(licenseNumber)
            .build();
        var result = carRepository.search(company.getId(), command, PageRequest.of(0, 10));

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(licenseNumber, result.getContent().getFirst().getProfile().getLicenseNumber());
    }
}