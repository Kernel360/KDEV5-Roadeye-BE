package org.re.mdtlog.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.re.mdtlog.domain.TransactionUUID;

@Converter
public class TransactionIdConverter implements AttributeConverter<TransactionUUID, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(TransactionUUID attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toBytes();
    }

    @Override
    public TransactionUUID convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }
        return TransactionUUID.from(dbData);
    }
}
