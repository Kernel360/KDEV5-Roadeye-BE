package org.re.company.converter;

import jakarta.persistence.Converter;
import org.re.common.converter.AbstractPersistenceEnumConverter;
import org.re.company.domain.CompanyQuoteStatus;

@Converter
public class CompanyQuoteStatusConverter extends AbstractPersistenceEnumConverter<CompanyQuoteStatus, Integer> {
    public CompanyQuoteStatusConverter() {
        super(CompanyQuoteStatus.class);
    }
}
