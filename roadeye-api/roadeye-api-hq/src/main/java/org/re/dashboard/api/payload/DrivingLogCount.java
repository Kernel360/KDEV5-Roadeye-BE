package org.re.dashboard.api.payload;

import org.re.driving.dto.DrivingHistoryMonthlyCountCommand;

public record DrivingLogCount(
    String month,
    Long count
) {
    public static DrivingLogCount from(DrivingHistoryMonthlyCountCommand command) {
        return new DrivingLogCount(command.month(), command.count());
    }
}
