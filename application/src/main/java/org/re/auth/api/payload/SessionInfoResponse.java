package org.re.auth.api.payload;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.re.common.api.payload.SuccessResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
public class SessionInfoResponse extends SuccessResponse<SessionInfoResponse.SessionInfo> {
    public SessionInfoResponse(SessionInfo session) {
        super(session);
    }

    public record SessionInfo(
        String id,
        ZonedDateTime createdAt,
        ZonedDateTime lastAccessedAt,
        ZonedDateTime expireAt
    ) {
        private static final SessionInfo INVALIDATED_SESSION = new SessionInfo("INVALIDATED", null, null, null);

        private static SessionInfo from(HttpSession session) {
            var createdAt = LocalDateTime.ofEpochSecond(session.getCreationTime() / 1000, 0, ZoneOffset.UTC);
            var lastAccessedAt = LocalDateTime.ofEpochSecond(session.getLastAccessedTime() / 1000, 0, ZoneOffset.UTC);
            var expireAt = lastAccessedAt.plusSeconds(session.getMaxInactiveInterval());
            return new SessionInfo(
                session.getId(),
                ZonedDateTime.ofLocal(createdAt, ZoneOffset.UTC, null),
                ZonedDateTime.ofLocal(lastAccessedAt, ZoneOffset.UTC, null),
                ZonedDateTime.ofLocal(expireAt, ZoneOffset.UTC, null)
            );
        }
    }

    public static SessionInfoResponse from(HttpSession session) {
        try {
            return new SessionInfoResponse(SessionInfo.from(session));
        } catch (IllegalStateException | NullPointerException e) {
            return new SessionInfoResponse(SessionInfo.INVALIDATED_SESSION);
        }
    }
}
