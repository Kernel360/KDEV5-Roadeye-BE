package org.re.hq.car.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.re.hq.car.domain.CarIgnitionStatus;

@Converter(autoApply = true)
public class CarIgnitionStatusConverter implements AttributeConverter<CarIgnitionStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CarIgnitionStatus attribute) {
        if (attribute == null) {
            // TODO: 커스텀 예외 처리
            throw new IllegalArgumentException("CarIgnitionStatus cannot be null");
        }
        return attribute.getValue();
    }

    @Override
    public CarIgnitionStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            // TODO: 커스텀 예외 처리
            throw new IllegalArgumentException("Database value for CarIgnitionStatus cannot be null");
        }
        return CarIgnitionStatus.of(dbData);
    }
}
