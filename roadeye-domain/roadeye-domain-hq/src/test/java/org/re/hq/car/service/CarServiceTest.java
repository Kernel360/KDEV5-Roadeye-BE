package org.re.hq.car.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.re.hq.car.domain.CarIgnitionStatus;
import org.re.hq.car.dto.CarCreationCommandFixture;
import org.re.hq.car.dto.CarDisableCommand;
import org.re.hq.car.dto.CarUpdateCommand;
import org.re.hq.company.domain.Company;
import org.re.hq.domain.common.EntityLifecycleStatus;
import org.re.hq.test.supports.WithCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import({CarService.class})
@DataJpaTest
@WithCompany
class CarServiceTest {
    @Autowired
    CarService carService;

    @Nested
    @DisplayName("차량 조회 테스트")
    class CarRetrievalTests {
        @Test
        @DisplayName("회사의 차량 목록을 조회할 수 있어야 한다.")
        void 회사차량_목록조회_테스트(Company company) {
            // given
            var pageable = PageRequest.of(0, 10);
            var numCars = 10;

            // 차량 10개 등록
            for (int i = 0; i < numCars; i++) {
                var command = CarCreationCommandFixture.create();
                carService.createCar(company, command);
            }

            // when
            var carPage = carService.getCars(company, pageable);

            // then
            assertThat(carPage).isNotNull();
            assertThat(carPage.getTotalElements()).isEqualTo(numCars);
        }

        @Test
        @DisplayName("회사 차량 목록 조회시 다른 회사 차량은 조회되지 않아야 한다.")
        void 회사차량_목록조회_다른회사차량_조회되지않음_테스트(Company company1, Company company2) {
            // given
            var pageable = PageRequest.of(0, 10);
            var numCarsCompany1 = 5;
            var numCarsCompany2 = 3;

            // 회사 1에 차량 등록
            for (int i = 0; i < numCarsCompany1; i++) {
                var command = CarCreationCommandFixture.create();
                carService.createCar(company1, command);
            }

            // 회사 2에 차량 등록
            for (int i = 0; i < numCarsCompany2; i++) {
                var command = CarCreationCommandFixture.create();
                carService.createCar(company2, command);
            }

            // when
            var carPage = carService.getCars(company1, pageable);

            // then
            assertThat(carPage).isNotNull();
            assertThat(carPage.getTotalElements()).isEqualTo(numCarsCompany1);
        }

        @Test
        @DisplayName("차량 목록 조회시 모두 활성화 상태여야 한다.")
        void 차량목록조회_활성화상태_테스트(Company company) {
            // given
            var pageable = PageRequest.of(0, 10);
            var numCars = 10;

            // 차량 10개 등록
            for (int i = 0; i < numCars; i++) {
                var command = CarCreationCommandFixture.create();
                carService.createCar(company, command);
            }

            // when
            var carPage = carService.getCars(company, pageable);

            // then
            assertThat(carPage).isNotNull();
            assertThat(carPage.getContent()).allMatch(car -> car.getStatus() == EntityLifecycleStatus.ACTIVE);
        }

        @Test
        @DisplayName("회사의 차량을 ID로 조회할 수 있어야 한다.")
        void 회사차량_단건조회_테스트(Company company) {
            // given
            var command = CarCreationCommandFixture.create();
            var car = carService.createCar(company, command);

            // when
            var retrievedCar = carService.getCarById(company, car.getId());

            // then
            assertThat(retrievedCar).isNotNull();
            assertThat(retrievedCar.getId()).isEqualTo(car.getId());
            assertThat(retrievedCar.getCompany().getId()).isEqualTo(company.getId());
        }

        @Test
        @DisplayName("회사의 차량을 ID로 조회할 때, 다른 회사 차량은 조회되지 않아야 한다.")
        void 회사차량_단건조회_다른회사차량_조회되지않음_테스트(Company company1, Company company2) {
            // given
            var command = CarCreationCommandFixture.create();
            var car = carService.createCar(company1, command);

            // when
            assertThrows(Exception.class, () -> {
                carService.getCarById(company2, car.getId());
            });
        }

        @Test
        @DisplayName("차량 단건 조회시 활성화 상태여야 한다.")
        void 차량단건조회_활성화상태_테스트(Company company) {
            // given
            var command = CarCreationCommandFixture.create();
            var car = carService.createCar(company, command);

            // when
            var retrievedCar = carService.getCarById(company, car.getId());

            // then
            assertThat(retrievedCar).isNotNull();
            assertThat(retrievedCar.getStatus()).isEqualTo(EntityLifecycleStatus.ACTIVE);
        }

        @Test
        @DisplayName("차량 상태별 목록 조회가 가능해야 한다.")
        void 차량상태별_목록조회_테스트(Company company) {
            // given
            var numActiveCars = 5;
            var numDisabledCars = 3;
            var creationCommand = CarCreationCommandFixture.create();

            // 차량 활성화 상태 등록
            for (int i = 0; i < numActiveCars; i++) {
                carService.createCar(company, creationCommand);
            }

            // 차량 비활성화 상태 등록
            for (int i = 0; i < numDisabledCars; i++) {
                var car = carService.createCar(company, creationCommand);
                carService.disable(car, new CarDisableCommand("비활성화 이유"));
            }

            // when
            var activeCars = carService.getCarsByStatus(company, EntityLifecycleStatus.ACTIVE, PageRequest.of(0, 10));
            var disabledCars = carService.getCarsByStatus(company, EntityLifecycleStatus.DISABLED, PageRequest.of(0, 10));

            // then
            assertThat(activeCars).isNotNull();
            assertThat(activeCars.getTotalElements()).isEqualTo(numActiveCars);
            assertThat(disabledCars).isNotNull();
            assertThat(disabledCars.getTotalElements()).isEqualTo(numDisabledCars);
        }

        @Test
        @DisplayName("차량 상태별 목록 조회시 다른 회사 차량은 조회되지 않아야 한다.")
        void 차량상태별_목록조회_다른회사차량_조회되지않음_테스트(Company company1, Company company2) {
            // given
            var numCarsCompany1 = 5;
            var numCarsCompany2 = 3;
            var creationCommand = CarCreationCommandFixture.create();

            // 회사 1에 차량 활성화 상태 등록
            for (int i = 0; i < numCarsCompany1; i++) {
                carService.createCar(company1, creationCommand);
            }

            // 회사 2에 차량 활성화 상태 등록
            for (int i = 0; i < numCarsCompany2; i++) {
                carService.createCar(company2, creationCommand);
            }

            // when
            var activeCars = carService.getCarsByStatus(company1, EntityLifecycleStatus.ACTIVE, PageRequest.of(0, 10));
            var disabledCars = carService.getCarsByStatus(company2, EntityLifecycleStatus.ACTIVE, PageRequest.of(0, 10));

            // then
            assertThat(activeCars).isNotNull();
            assertThat(activeCars.getTotalElements()).isEqualTo(numCarsCompany1);
            assertThat(disabledCars).isNotNull();
            assertThat(disabledCars.getTotalElements()).isEqualTo(numCarsCompany2);
        }

        @Test
        @DisplayName("차량 상태별 카운트를 조회할 수 있어야 한다.")
        void 차량상태별_카운트조회_테스트(Company company) {
            // given
            var numActiveCars = 5;
            var numDisabledCars = 3;
            var creationCommand = CarCreationCommandFixture.create();

            // 차량 활성화 상태 등록
            for (int i = 0; i < numActiveCars; i++) {
                carService.createCar(company, creationCommand);
            }

            // 차량 비활성화 상태 등록
            for (int i = 0; i < numDisabledCars; i++) {
                var car = carService.createCar(company, creationCommand);
                carService.disable(car, new CarDisableCommand("비활성화 이유"));
            }

            // when
            var activeCount = carService.countCarsByStatus(company, EntityLifecycleStatus.ACTIVE);
            var disabledCount = carService.countCarsByStatus(company, EntityLifecycleStatus.DISABLED);

            // then
            assertThat(activeCount).isEqualTo(numActiveCars);
            assertThat(disabledCount).isEqualTo(numDisabledCars);
        }
    }

    @Nested
    @DisplayName("차량 등록 테스트")
    class CarCreationTests {

        @Test
        @DisplayName("차량 등록에 성공해야 한다.")
        void 차량등록_성공_테스트(Company company) {
            // given
            var command = CarCreationCommandFixture.create();

            // when
            var car = carService.createCar(company, command);

            // then
            assertThat(car).isNotNull();
            assertThat(car.getCompany().getId()).isEqualTo(company.getId());
            assertThat(car.getProfile().getName()).isEqualTo(command.name());
            assertThat(car.getProfile().getLicenseNumber()).isEqualTo(command.licenseNumber());
            assertThat(car.getProfile().getImageUrl()).isEqualTo(command.imageUrl());
            assertThat(car.getMileage().getInitial()).isEqualTo(command.mileageInitial());
        }

        @Test
        @DisplayName("차량 최초 등록 시 시동 상태가 OFF가 되야 한다.")
        void 차량등록_시_시동상태_OFF_테스트(Company company) {
            // given
            var command = CarCreationCommandFixture.create();

            // when
            var car = carService.createCar(company, command);

            // then
            assertThat(car).isNotNull();
            assertThat(car.getMdtStatus().getIgnition()).isEqualTo(CarIgnitionStatus.OFF);
        }
    }

    @Nested
    @DisplayName("차량 정보 수정 테스트")
    class CarUpdateTests {
        @Test
        @DisplayName("차량 이름 변경이 가능해야 한다.")
        void 차량이름_변경_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var nextName = "Next Car Name";
            var updatedCommand = new CarUpdateCommand(nextName, null);

            // when
            var car = carService.createCar(company, creationCommand);
            var updatedCar = carService.updateCarProfile(car, updatedCommand);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getProfile().getName()).isEqualTo(nextName);
            assertThat(updatedCar.getProfile().getLicenseNumber()).isEqualTo(creationCommand.licenseNumber());
        }

        @Test
        @DisplayName("차량 이미지 변경이 가능해야 한다.")
        void 차량이미지_변경_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var nextImageUrl = "http://example.com/next_car.jpg";
            var updatedCommand = new CarUpdateCommand(null, nextImageUrl);

            // when
            var car = carService.createCar(company, creationCommand);
            var updatedCar = carService.updateCarProfile(car, updatedCommand);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getProfile().getImageUrl()).isEqualTo(nextImageUrl);
        }

        @Test
        @DisplayName("비활성화 이유가 등록되어야 한다.")
        void 차량비활성화_이유_등록_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var disableReason = "차량 고장";

            // when
            var car = carService.createCar(company, creationCommand);
            var disabledCar = carService.disable(car, new CarDisableCommand(disableReason));

            // then
            assertThat(disabledCar).isNotNull();
            assertThat(disabledCar.getStatus()).isEqualTo(EntityLifecycleStatus.DISABLED);
            assertThat(disabledCar.getDisableReason()).isEqualTo(disableReason);
        }
    }

    @Nested
    @DisplayName("차량 삭제 테스트")
    class CarDeletionTests {
        @Test
        @DisplayName("차량 삭제 후 목록 조회에서 제외되어야 한다.")
        void 차량삭제_후_목록조회_제외_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var numCars = 10;
            var numCarsToDelete = 5;
            var pageable = PageRequest.of(0, numCars);

            // 차량 10개 등록
            var cars = IntStream.range(0, numCars)
                .mapToObj(i -> carService.createCar(company, creationCommand))
                .toList();
            // 차량 5개 삭제
            cars.stream()
                .limit(numCarsToDelete)
                .forEach(car -> carService.deleteCar(car));

            // when
            var carPage = carService.getCars(company, pageable);

            // then
            assertThat(carPage).isNotNull();
            assertThat(carPage.getTotalElements()).isEqualTo(numCars - numCarsToDelete);
        }

        @Test
        @DisplayName("차량 삭제 후 단건 조회가 불가능해야 한다.")
        void 차량삭제_후_단건조회_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();

            // when
            var car = carService.createCar(company, creationCommand);
            carService.deleteCar(car);

            // then
            assertThrows(Exception.class, () -> {
                carService.getCarById(company, car.getId());
            });
        }
    }

    @Nested
    @DisplayName("차량 시동 테스트")
    class CarIgnitionTests {
        @Test
        @DisplayName("시동 OFF 상태에서만 시동 ON이 가능해야 한다.")
        void 시동OFF_상태에서_시동ON_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            var updatedCar = carService.turnOnIgnition(car, transactionId);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getMdtStatus().getIgnition()).isEqualTo(CarIgnitionStatus.ON);
        }

        @Test
        @DisplayName("시동을 킬 때 트랜잭션 ID가 설정되어야 한다.")
        void 시동킬때_트랜잭션ID_설정_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            var updatedCar = carService.turnOnIgnition(car, transactionId);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getMdtStatus().getActiveTuid()).isEqualTo(transactionId);
        }

        @Test
        @DisplayName("시동 ON 상태에서만 시동 OFF가 가능해야 한다.")
        void 시동ON_상태에서_시동OFF_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            carService.turnOnIgnition(car, transactionId);
            var updatedCar = carService.turnOffIgnition(car, transactionId);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getMdtStatus().getIgnition()).isEqualTo(CarIgnitionStatus.OFF);
        }

        @Test
        @DisplayName("트랜잭션 ID가 일치하지 않으면 시동 OFF가 실패해야 한다.")
        void 시동끄기_트랜잭션ID_일치_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();
            var wrongTransactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            carService.turnOnIgnition(car, transactionId);

            // then
            assertThrows(Exception.class, () -> {
                carService.turnOffIgnition(car, wrongTransactionId);
            });
        }

        @Test
        @DisplayName("시동을 끈 후 트랜잭션 ID가 null이 되어야 한다.")
        void 시동끄기_후_트랜잭션ID_null_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            carService.turnOnIgnition(car, transactionId);
            var updatedCar = carService.turnOffIgnition(car, transactionId);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getMdtStatus().getActiveTuid()).isNull();
        }

        @Test
        @DisplayName("시동 상태를 초기화할 수 있어야 한다.")
        void 시동상태_초기화_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            carService.turnOnIgnition(car, transactionId);
            var updatedCar = carService.resetIgnitionStatus(car);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getMdtStatus().getIgnition()).isEqualTo(CarIgnitionStatus.OFF);
        }

        @Test
        @DisplayName("시동 상태를 초기화할 때 트랜잭션 ID가 null이 되어야 한다.")
        void 시동상태_초기화_후_트랜잭션ID_null_테스트(Company company) {
            // given
            var creationCommand = CarCreationCommandFixture.create();
            var transactionId = UUID.randomUUID();

            // when
            var car = carService.createCar(company, creationCommand);
            carService.turnOnIgnition(car, transactionId);
            var updatedCar = carService.resetIgnitionStatus(car);

            // then
            assertThat(updatedCar).isNotNull();
            assertThat(updatedCar.getMdtStatus().getActiveTuid()).isNull();
        }
    }
}
