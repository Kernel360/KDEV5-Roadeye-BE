package org.re.common.converter;

import jakarta.persistence.AttributeConverter;
import org.re.common.domain.PersistenceEnum;

import java.util.Objects;

@SuppressWarnings("ConverterNotAnnotatedInspection")
public abstract class AbstractPersistenceEnumConverter<T extends Enum<T> & PersistenceEnum<E>, E> implements AttributeConverter<T, E> {
    private final Class<T> enumClass;
    private final boolean allowsNull;

    public AbstractPersistenceEnumConverter(Class<T> enumClass) {
        this(enumClass, false);
    }

    public AbstractPersistenceEnumConverter(Class<T> enumClass, boolean allowsNull) {
        this.enumClass = enumClass;
        this.allowsNull = allowsNull;
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        if (Objects.isNull(attribute)) {
            if (allowsNull) {
                return null;
            } else {
                throw new IllegalArgumentException("Null value not allowed for " + enumClass.getSimpleName());
            }
        }
        return attribute.getCode();
    }

    @Override
    public T convertToEntityAttribute(E dbData) {
        if (Objects.isNull(dbData)) {
            if (allowsNull) {
                return null;
            } else {
                throw new IllegalArgumentException("Null value not allowed for " + enumClass.getSimpleName());
            }
        }
        return getEnumInstance(dbData);
    }

    private T getEnumInstance(E dbData) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (Objects.equals(enumConstant.getCode(), dbData)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("No enum constant found for value: " + dbData);
    }
}
