package org.re.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.re.auth.api.payload.SessionInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionApi {
    @GetMapping("/my")
    public SessionInfoResponse getSessionInfo(
        HttpServletRequest request
    ) {
        var session = request.getSession(false);
        return SessionInfoResponse.from(session);
    }
}
