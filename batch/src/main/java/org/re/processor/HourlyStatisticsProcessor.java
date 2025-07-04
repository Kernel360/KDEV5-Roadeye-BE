package org.re.processor;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.re.driving.domain.DrivingHistory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Getter
public class HourlyStatisticsProcessor implements ItemProcessor<DrivingHistory, Map<Integer,Integer>> {
    private final Map<Integer, Integer> hourlyCount = new HashMap<>();

    @Override
    public Map<Integer, Integer> process(DrivingHistory item) throws Exception {
        int startHour = item.getPreviousDrivingSnapShot().datetime().getHour();
        assert item.getEndDrivingSnapShot() != null;
        int endHour = item.getEndDrivingSnapShot().datetime().getHour();
        for(int hour = startHour; hour <= endHour; hour++) {
            hourlyCount.put(hour, hourlyCount.getOrDefault(hour, 0) + 1);
        }

        return hourlyCount;
    }
}
