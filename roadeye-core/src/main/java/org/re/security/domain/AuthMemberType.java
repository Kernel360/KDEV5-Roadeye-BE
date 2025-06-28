package org.re.security.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthMemberType {
    // @formatter:off
    ADMIN ("Auth:Admin"),
    USER  ("Auth:User"),
    ;
    // @formatter:on

    private final String value;
}
