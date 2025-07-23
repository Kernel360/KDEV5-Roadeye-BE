package org.re.common.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.re.common.exception.MdtLogExceptionCode;

@RequiredArgsConstructor
public class BaseMdtResponse {
    private final MdtLogExceptionCode code;

    @JsonProperty("rstCd")
    public String getCode() {
        return code.getCode();
    }

    @JsonProperty("rstMsg")
    public String getMessage() {
        return code.getMessage();
    }
}
