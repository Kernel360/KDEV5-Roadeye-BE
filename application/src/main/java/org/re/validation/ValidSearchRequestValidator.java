package org.re.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class ValidSearchRequestValidator implements ConstraintValidator<ValidSearchRequest, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(value) != null) {
                    return true;
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return false;
    }
}

