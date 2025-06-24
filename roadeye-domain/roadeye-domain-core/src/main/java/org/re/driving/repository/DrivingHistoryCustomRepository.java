package org.re.driving.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;

public interface DrivingHistoryCustomRepository {
    List<DrivingHistoryMonthlyCountResult> countByMonth(LocalDateTime startDate);
}
