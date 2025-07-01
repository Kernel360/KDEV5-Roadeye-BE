package org.re.test.security;

import org.re.employee.domain.EmployeeRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@WithSecurityContext(factory = MockCompanyUserDetailsSecurityContextFactory.class)
public @interface MockCompanyUserDetails {
    long id() default 1L;

    String username() default "username";

    String password() default "{noop}password";

    EmployeeRole role() default EmployeeRole.NORMAL;
}
