package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Min(ValidSpeed.MIN_VERSION)
@Max(ValidSpeed.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidSpeed {
    int MIN_VERSION = 0;
    int MAX_VERSION = 255;

    String message() default "{org.re.mdtlog.constraints.ValidSpeed.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
