package org.re.hq.company.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.re.hq.company.domain.CompanyQuoteStatus;

@Converter
public class CompanyQuoteStatusConverter implements AttributeConverter<CompanyQuoteStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CompanyQuoteStatus attribute) {
        if (attribute == null) {
            throw new NullPointerException("CompanyQuoteStatus cannot be null");
        }
        return attribute.getCode();
    }

    @Override
    public CompanyQuoteStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            throw new IllegalArgumentException("Database value for CompanyQuoteStatus cannot be null");
        }
        return CompanyQuoteStatus.of(dbData);
    }
}
