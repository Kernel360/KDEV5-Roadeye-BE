package org.re.processor;

import java.util.HashMap;
import java.util.Map;
import org.re.driving.domain.DrivingHistory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class HourlyStatisticsProcessor implements ItemProcessor<DrivingHistory, DrivingHistory> {

    @Override
    public DrivingHistory process(DrivingHistory item) throws Exception {

        return item;
    }
}
