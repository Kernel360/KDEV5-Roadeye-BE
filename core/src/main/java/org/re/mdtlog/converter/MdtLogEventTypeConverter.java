package org.re.mdtlog.converter;

import jakarta.persistence.Converter;
import org.re.common.converter.AbstractPersistenceEnumConverter;
import org.re.mdtlog.domain.MdtLogEventType;

@Converter
public class MdtLogEventTypeConverter extends AbstractPersistenceEnumConverter<MdtLogEventType, String> {
    public MdtLogEventTypeConverter() {
        super(MdtLogEventType.class, true);
    }
}
