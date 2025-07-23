package org.re.car.converter;

import jakarta.persistence.Converter;
import org.re.car.domain.CarIgnitionStatus;
import org.re.common.converter.AbstractPersistenceEnumConverter;

@Converter
public class CarIgnitionStatusConverter extends AbstractPersistenceEnumConverter<CarIgnitionStatus, Integer> {
    public CarIgnitionStatusConverter() {
        super(CarIgnitionStatus.class);
    }
}
