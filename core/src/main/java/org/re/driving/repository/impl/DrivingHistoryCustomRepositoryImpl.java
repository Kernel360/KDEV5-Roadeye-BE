package org.re.driving.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.re.car.domain.QCar;
import org.re.driving.domain.DrivingHistory;
import org.re.driving.domain.DrivingHistoryStatus;
import org.re.driving.domain.QDrivingHistory;
import org.re.driving.dto.DrivingHistoryMonthlyCountResult;
import org.re.driving.repository.DrivingHistoryCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Override
    public Page<DrivingHistory> findDrivingHistoryByCompanyId(Long companyId, Pageable pageable) {
        QDrivingHistory history = QDrivingHistory.drivingHistory;
        QCar car = QCar.car;

        BooleanExpression condition = car.company.id.eq(companyId)
            .and(history.status.eq(DrivingHistoryStatus.ENDED));

        Long total = queryFactory
            .select(history.count())
            .from(history)
            .join(history.car, car)
            .where(condition)
            .fetchOne();

        long count = total != null ? total : 0L;

        List<DrivingHistory> result = queryFactory
            .selectFrom(history)
            .join(history.car, car)
            .where(condition)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(result, pageable, count);
    }
}
