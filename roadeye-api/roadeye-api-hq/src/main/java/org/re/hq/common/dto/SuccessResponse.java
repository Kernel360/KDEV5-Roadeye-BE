package org.re.hq.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class SuccessResponse<T> extends BaseResponse {
    private final T data;
}
