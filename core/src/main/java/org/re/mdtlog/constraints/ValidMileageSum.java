package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Min(ValidMileageSum.MIN_VERSION)
@Max(ValidMileageSum.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidMileageSum {
    int MIN_VERSION = 0;
    int MAX_VERSION = 9999999;

    String message() default "{org.re.mdtlog.constraints.ValidMileageSum.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
