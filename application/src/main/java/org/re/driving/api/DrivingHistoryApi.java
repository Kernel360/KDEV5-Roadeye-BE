package org.re.driving.api;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.common.api.payload.ListResponse;
import org.re.common.api.payload.PageResponse;
import org.re.common.api.payload.SingleItemResponse;
import org.re.company.domain.CompanyId;
import org.re.driving.api.payload.CurrentDrivingInfo;
import org.re.driving.api.payload.DrivingHistoryInfo;
import org.re.driving.api.payload.DrivingLocationDetail;
import org.re.driving.service.DrivingHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driving")
@RequiredArgsConstructor
public class DrivingHistoryApi {
    private final DrivingHistoryService drivingHistoryService;

    @GetMapping
    public PageResponse<DrivingHistoryInfo> getDrivingHistory(CompanyId companyId, Pageable pageable) {
        var page = drivingHistoryService.getDrivingHistory(companyId, pageable);
        return PageResponse.of(page, DrivingHistoryInfo::from);
    }

    @GetMapping("/{drivingId}")
    public SingleItemResponse<DrivingHistoryInfo> getDrivingHistoryById(@PathVariable Long drivingId) {
        var item = drivingHistoryService.getDrivingHistoryById(drivingId);
        return SingleItemResponse.of(item, DrivingHistoryInfo::from);
    }

    @GetMapping("/{drivingId}/path")
    public ListResponse<DrivingLocationDetail> getDrivingHistoryPathById(@PathVariable Long drivingId) {
        var page = drivingHistoryService.getDrivingHistoryLogs(drivingId);
        return ListResponse.of(page, DrivingLocationDetail::from);
    }

    @GetMapping("/car/{carId}")
    public ListResponse<DrivingLocationDetail> getDrivingHistoryLogs(
        @PathVariable Long carId,
        @Nullable @RequestParam(required = false) Long cursor
    ) {
        var page = drivingHistoryService.getDrivingLocationLogs(carId, cursor);
        return ListResponse.of(page, DrivingLocationDetail::from);
    }

    @GetMapping("/car/{carId}/current")
    public SingleItemResponse<CurrentDrivingInfo> getCurrentDrivingLocation(@PathVariable Long carId) {
        var item = drivingHistoryService.getCurrentDriving(carId);
        return SingleItemResponse.of(item);
    }
}
