package org.re.mdtlog.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@NotNull
@Min(ValidPacketVersion.MIN_VERSION)
@Max(ValidPacketVersion.MAX_VERSION)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
public @interface ValidPacketVersion {
    int MIN_VERSION = 0;
    int MAX_VERSION = 65535;

    String message() default "{org.re.mdtlog.constraints.ValidPacketVersion.message}";

    Class<?>[] groups() default {};

    Class<? extends Annotation>[] payload() default {};
}
