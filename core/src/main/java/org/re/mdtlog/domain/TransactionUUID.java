package org.re.mdtlog.domain;

import jakarta.persistence.Column;

import java.nio.ByteBuffer;
import java.util.UUID;

public record TransactionUUID(
    @Column(name = "tx_uid", columnDefinition = "BINARY(16)", nullable = false)
    UUID value
) {
    public static TransactionUUID from(byte[] dbData) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(dbData);
        long mostSigBits = byteBuffer.getLong();
        long leastSigBits = byteBuffer.getLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        return new TransactionUUID(uuid);
    }

    public static TransactionUUID from(String uuidString) {
        if (uuidString == null || uuidString.isEmpty()) {
            return null;
        }
        UUID uuid = UUID.fromString(uuidString);
        return new TransactionUUID(uuid);
    }

    private static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    public byte[] toBytes() {
        return uuidToBytes(value);
    }
}
