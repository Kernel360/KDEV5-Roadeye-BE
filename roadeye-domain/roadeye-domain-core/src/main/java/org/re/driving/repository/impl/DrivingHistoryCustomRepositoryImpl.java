package org.re.driving.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.re.driving.domain.QDrivingHistory;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.repository.DrivingHistoryCustomRepository;
import com.querydsl.core.types.dsl.StringTemplate;

@RequiredArgsConstructor
public class DrivingHistoryCustomRepositoryImpl implements
    DrivingHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<DrivingHistoryMonthlyCountResult> countByMonth(LocalDateTime startDate) {
        QDrivingHistory dh = QDrivingHistory.drivingHistory;

        StringTemplate yearMonthTemplate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m')", dh.previousDrivingSnapShot.datetime
        );

        List<Tuple> result = queryFactory
            .select(
                yearMonthTemplate,
                dh.count()
            )
            .from(dh)
            .where(dh.previousDrivingSnapShot.datetime.goe(startDate))
            .groupBy(yearMonthTemplate)
            .orderBy(yearMonthTemplate.asc())
            .fetch();

        return result.stream()
            .map(tuple -> new DrivingHistoryMonthlyCountResult(
                tuple.get(yearMonthTemplate),
                tuple.get(dh.count())
            ))
            .toList();
    }
}
