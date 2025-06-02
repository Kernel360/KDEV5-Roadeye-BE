package org.re.hq.test.api.extension;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.re.hq.company.CompanyFixture;
import org.re.hq.company.domain.Company;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Component
public class CompanyParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var paramType = parameterContext.getParameter().getType();
        return Company.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var springContext = SpringExtension.getApplicationContext(extensionContext);
        var em = springContext.getBean(EntityManager.class);

        var company = CompanyFixture.create();
        em.persist(company);
        em.flush();
        return company;
    }
}
