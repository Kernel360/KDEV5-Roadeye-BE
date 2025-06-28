package org.re.dashboard.api;

import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.ListResponse;
import org.re.dashboard.api.payload.DrivingLogCount;
import org.re.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApi {
    private final DashboardService dashboardService;

    @GetMapping("/month")
    public ListResponse<DrivingLogCount> getDrivingLogCount() {
        var page = dashboardService.getDrivingHistoryMonthlyCountCommand();
        return ListResponse.of(page, DrivingLogCount::from);
    }
}
