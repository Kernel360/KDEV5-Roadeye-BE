package org.re.test.api;

import org.jspecify.annotations.Nullable;
import org.re.company.domain.CompanyId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CompanyIdTestApi {
    @GetMapping("/test/companyId")
    public Object testCompanyId(
        @Nullable CompanyId companyId
    ) {
        return Map.of(
            "companyId", companyId.value()
        );
    }
}
