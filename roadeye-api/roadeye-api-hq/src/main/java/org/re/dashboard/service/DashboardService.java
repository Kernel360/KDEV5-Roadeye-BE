package org.re.dashboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.service.DrivingHistoryDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardService {
    private final DrivingHistoryDomainService drivingHistoryDomainService;

    public List<DrivingHistoryMonthlyCountResult> getDrivingHistoryMonthlyCountCommand() {
        return drivingHistoryDomainService.getMonthlyCount();
    }
}
