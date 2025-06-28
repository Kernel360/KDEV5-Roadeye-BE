package org.re.driving.converter;

import jakarta.persistence.Converter;
import org.re.common.converter.AbstractPersistenceEnumConverter;
import org.re.driving.domain.DrivingHistoryStatus;

@Converter(autoApply = true)
public class DrivingHistoryStatusConverter extends AbstractPersistenceEnumConverter<DrivingHistoryStatus, String> {
    public DrivingHistoryStatusConverter() {
        super(DrivingHistoryStatus.class);
    }
}
