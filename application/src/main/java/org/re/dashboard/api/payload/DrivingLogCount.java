package org.re.dashboard.api.payload;

import org.re.driving.dto.DrivingHistoryMonthlyCountResult;

public record DrivingLogCount(
        String month,
        Long count
) {
    public static DrivingLogCount from(DrivingHistoryMonthlyCountResult command) {
        return new DrivingLogCount(command.month(), command.count());
    }
}
