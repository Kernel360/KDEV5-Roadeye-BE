package org.re.mdtlog.converter;

import jakarta.persistence.Converter;
import org.re.common.converter.AbstractPersistenceEnumConverter;
import org.re.mdtlog.domain.MdtLogGpsCondition;

@Converter
public class MdtLogGpsConditionConverter extends AbstractPersistenceEnumConverter<MdtLogGpsCondition, String> {
    public MdtLogGpsConditionConverter() {
        super(MdtLogGpsCondition.class);
    }
}
