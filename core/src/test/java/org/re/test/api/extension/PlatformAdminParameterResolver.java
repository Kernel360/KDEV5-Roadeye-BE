package org.re.test.api.extension;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.re.admin.PlatformAdminFixture;
import org.re.admin.domain.PlatformAdmin;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Component
public class PlatformAdminParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var paramType = parameterContext.getParameter().getType();
        return PlatformAdmin.class.isAssignableFrom(paramType);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var springContext = SpringExtension.getApplicationContext(extensionContext);
        var em = springContext.getBean(EntityManager.class);

        var admin = PlatformAdminFixture.create();
        em.persist(admin);
        em.flush();
        return admin;
    }
}
