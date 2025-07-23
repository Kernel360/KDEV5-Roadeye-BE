package org.re.dashboard.api;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.ListResponse;
import org.re.common.api.payload.SingleItemResponse;
import org.re.dashboard.api.payload.DrivingLogCount;
import org.re.dashboard.api.payload.StatisticsInfo;
import org.re.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/daily")
    public SingleItemResponse<StatisticsInfo> getStatistics(@RequestParam LocalDate date) {
        var page = dashboardService.getStatisticsInfo(date);
        return SingleItemResponse.of(page);
    }
}
