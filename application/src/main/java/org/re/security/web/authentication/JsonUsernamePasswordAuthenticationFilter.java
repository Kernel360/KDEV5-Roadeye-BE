package org.re.security.web.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String JSON_FIELD_USERNAME = "username";
    private static final String JSON_FIELD_PASSWORD = "password";

    private final ObjectMapper objectMapper;

    public JsonUsernamePasswordAuthenticationFilter(
        String loginPath,
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper
    ) {
        super(new AntPathRequestMatcher(loginPath, "POST"), authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var mimeType = request.getContentType();
        if (!MediaType.APPLICATION_JSON_VALUE.equals(mimeType)) {
            throw new AuthenticationServiceException("Content-Type is not application/json");
        }

        try {
            var jsonNode = objectMapper.readTree(request.getInputStream());
            var username = obtainUsername(jsonNode);
            var password = obtainPassword(jsonNode);

            var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    private String obtainUsername(JsonNode jsonMap) {
        var username = getJsonFieldAsString(jsonMap, JSON_FIELD_USERNAME);
        return (username == null) ? "" : username;
    }

    private String obtainPassword(JsonNode jsonMap) {
        var password = getJsonFieldAsString(jsonMap, JSON_FIELD_PASSWORD);
        return (password == null) ? "" : password;
    }

    private String getJsonFieldAsString(JsonNode jsonNode, String fieldName) {
        var field = jsonNode.get(fieldName);
        if (field == null || field instanceof NullNode) {
            return null;
        }
        return field.asText();
    }
}
