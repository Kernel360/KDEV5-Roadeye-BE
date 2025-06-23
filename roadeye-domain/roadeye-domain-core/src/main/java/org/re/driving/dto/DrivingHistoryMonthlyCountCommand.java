package org.re.driving.dto;

public record DrivingHistoryMonthlyCountCommand(
    String month,
    Long count
) {
}
