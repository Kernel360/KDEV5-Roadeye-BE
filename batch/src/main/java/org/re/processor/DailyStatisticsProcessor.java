package org.re.processor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.re.driving.domain.DrivingHistory;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class DailyStatisticsProcessor implements ItemProcessor<DrivingHistory, DailyDrivingStatistics> {

    @Override
    public DailyDrivingStatistics process(DrivingHistory item) throws Exception {
        assert item.getEndDrivingSnapShot() != null;

        int distance = item.getEndDrivingSnapShot().mileageSum();
        Duration duration = Duration.between(
            item.getPreviousDrivingSnapShot().datetime(),
            item.getEndDrivingSnapShot().datetime()
        );

        return new DailyDrivingStatistics(
            LocalDate.now().minusDays(1).atStartOfDay(), // 기준일
            1,
            distance,
            (int) duration.getSeconds()
        );
    }
}
