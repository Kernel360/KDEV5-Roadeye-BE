package org.re.mdtlog.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record MdtEventMessage<D>(
    @Nullable
    UUID transactionId,
    D payload,
    @NotNull
    LocalDateTime sentAt,
    @NotNull
    LocalDateTime receivedAt
) {
}
