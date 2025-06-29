package org.re.company.supports;

import org.jspecify.annotations.Nullable;

public class CompanyIdContextProvider implements CompanyIdProvider {
    @Override
    @Nullable
    public Long getCurrentCompanyId() {
        var t = CompanyIdContext.getCompanyId();
        if (t == null) {
            return null;
        }
        return t.value();
    }
}
