package org.re.driving.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.re.car.CarFixture;
import org.re.common.exception.DomainException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DrivingHistoryTest {

    @Test
    @DisplayName("이력 작성 종료 시 이미 작성 완료된 이력인 경우 예외가 발생해야 한다.")
    void drive_history_end_dup_test() {
        // given
        var car = CarFixture.create();
        var driveStartAt = LocalDateTime.now();
        var driveEndAt = driveStartAt.plusMinutes(10);

        var history = DrivingHistory.createNew(car, driveStartAt);
        history.end(car, driveEndAt);

        // when & then
        assertThrows(DomainException.class, () -> {
            var driveEndAt2 = driveStartAt.plusMinutes(20);
            history.end(car, driveEndAt2);
        });
    }
}
