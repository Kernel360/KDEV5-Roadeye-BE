package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Min(ValidLongitude.MIN_VERSION)
@Max(ValidLongitude.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidLongitude {
    int MIN_VERSION = -180;
    int MAX_VERSION = 180;

    String message() default "{org.re.mdtlog.constraints.ValidLongitude.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
