package org.re.driving.api;

import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.ListResponse;
import org.re.common.api.payload.PageResponse;
import org.re.driving.api.payload.DrivingHistoryInfo;
import org.re.driving.api.payload.DrivingLocationDetail;
import org.re.driving.service.DrivingHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driving")
@RequiredArgsConstructor
public class DrivingHistoryApi {
    private final DrivingHistoryService drivingHistoryService;

    @GetMapping
    public PageResponse<DrivingHistoryInfo> getDrivingHistory(Pageable pageable) {
        var page = drivingHistoryService.getDrivingHistory(pageable);
        return PageResponse.of(page,DrivingHistoryInfo::from);
    }

    @GetMapping("/{drivingId}")
    public ListResponse<DrivingLocationDetail> getDrivingHistoryLog(@PathVariable Long drivingId) {
        var page = drivingHistoryService.getDrivingHistoryLogs(drivingId);
        return ListResponse.of(page,DrivingLocationDetail::from);
    }

}
