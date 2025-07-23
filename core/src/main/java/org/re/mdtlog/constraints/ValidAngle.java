package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Min(ValidAngle.MIN_VERSION)
@Max(ValidAngle.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidAngle {
    int MIN_VERSION = 0;
    int MAX_VERSION = 365;

    String message() default "{org.re.mdtlog.constraints.ValidAngle.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
