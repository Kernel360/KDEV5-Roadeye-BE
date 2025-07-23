package org.re.common.api.payload;

import java.time.LocalDateTime;

public record MdtLogRequestTimeInfo(
    LocalDateTime sentAt,
    LocalDateTime receivedAt
) {
}
