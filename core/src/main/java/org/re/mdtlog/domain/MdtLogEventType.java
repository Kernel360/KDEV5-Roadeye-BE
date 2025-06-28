package org.re.mdtlog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.re.common.domain.PersistenceEnum;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MdtLogEventType implements PersistenceEnum<String> {
    // @formatter:off
    CYCLE_LOG  ("cycle-log", "주기 정보"),
    IGNITION   ("ignition", "시동 이벤트")
    ;
    // @formatter:on

    private final String code;
    private final String description;
}
