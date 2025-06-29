package org.re.company.supports;

@FunctionalInterface
public interface CompanyIdProvider {
    Long getCurrentCompanyId();
}
