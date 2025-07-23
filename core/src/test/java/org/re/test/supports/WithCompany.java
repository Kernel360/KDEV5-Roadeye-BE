package org.re.test.supports;

import org.junit.jupiter.api.extension.ExtendWith;
import org.re.test.api.extension.CompanyParameterResolver;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ExtendWith(CompanyParameterResolver.class)
public @interface WithCompany {
}
