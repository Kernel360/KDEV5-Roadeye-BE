package org.re.driving.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.re.driving.domain.DrivingHistory;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DrivingHistoryCustomRepository {
    List<DrivingHistoryMonthlyCountResult> countByMonth(LocalDateTime startDate);

    Page<DrivingHistory> findDrivingHistoryByCompanyId(Long companyId, Pageable pageable);
}
