package org.re.writer;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.re.processor.DailyStatisticsProcessor;
import org.re.statistics.domain.DailyDrivingStatistics;
import org.re.statistics.domain.HourlyDrivingStatistics;
import org.re.statistics.repository.DailyStatisticsRepository;
import org.re.statistics.repository.HourlyStatisticsRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyStatisticsWriter implements ItemWriter<DailyDrivingStatistics> {

    private final DailyStatisticsProcessor processor;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final HourlyStatisticsRepository hourlyStatisticsRepository;
    private boolean written = false;

    @Override
    public void write(Chunk<? extends DailyDrivingStatistics> chunk) throws Exception {
        if (written) return;

        DailyDrivingStatistics stat = processor.toStatistics();
        dailyStatisticsRepository.save(stat);

        List<HourlyDrivingStatistics> hourStat = processor.toHourlyStatistics();
        hourlyStatisticsRepository.saveAll(hourStat);

        log.info("✅ Saved DailyDrivingStatistics: {}", stat);
        written = true;
    }
}
