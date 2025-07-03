package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.lang.annotation.*;

@Min(ValidBatteryVoltage.MIN_VERSION)
@Max(ValidBatteryVoltage.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidBatteryVoltage {
    int MIN_VERSION = 0;
    int MAX_VERSION = 9999;

    String message() default "{org.re.mdtlog.constraints.ValidBatteryVoltage.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
