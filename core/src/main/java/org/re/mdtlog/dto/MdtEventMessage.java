package org.re.mdtlog.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.re.mdtlog.domain.TransactionUUID;

import java.time.LocalDateTime;

public record MdtEventMessage<D>(
    @Nullable
    TransactionUUID transactionId,
    D payload,
    @NotNull
    LocalDateTime sentAt,
    @NotNull
    LocalDateTime receivedAt
) {
}
