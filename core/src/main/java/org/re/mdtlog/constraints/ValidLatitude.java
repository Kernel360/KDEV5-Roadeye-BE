package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Min(ValidLatitude.MIN_VERSION)
@Max(ValidLatitude.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidLatitude {
    int MIN_VERSION = -90;
    int MAX_VERSION = 90;

    String message() default "{org.re.mdtlog.constraints.ValidLatitude.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
