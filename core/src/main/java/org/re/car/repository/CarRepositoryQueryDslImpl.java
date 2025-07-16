package org.re.car.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.re.car.domain.Car;
import org.re.car.domain.QCar;
import org.re.car.dto.CarSearchCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CarRepositoryQueryDslImpl implements CarRepositoryQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Car> search(Long companyId, CarSearchCommand command, Pageable pageable) {
        var car = QCar.car;

        var builder = new BooleanBuilder();
        if (companyId != null) {
            builder.and(car.company.id.eq(companyId));
        }
        if (command.name() != null) {
            builder.and(car.profile.name.like("%" + command.name() + "%"));
        }
        if (command.licenseNumber() != null) {
            builder.and(car.profile.licenseNumber.like("%" + command.licenseNumber() + "%"));
        }

        var result = queryFactory
            .selectFrom(car)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        long total = queryFactory
            .selectFrom(car)
            .where(builder)
            .fetchCount();
        return new PageImpl<>(result, pageable, total);
    }
}
