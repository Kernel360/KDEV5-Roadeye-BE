package org.re.company.supports;

import org.re.company.domain.CompanyId;

public class CompanyIdContext {
    private static final ThreadLocal<CompanyId> companyIdHolder = new ThreadLocal<>();

    public static CompanyId getCompanyId() {
        return companyIdHolder.get();
    }

    public static void setCompanyId(CompanyId companyId) {
        companyIdHolder.set(companyId);
    }

    public static void clear() {
        companyIdHolder.remove();
    }

    private CompanyIdContext() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
