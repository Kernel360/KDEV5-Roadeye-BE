package org.re.common.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.re.common.exception.MdtLogExceptionCode;

public class BaseMdtLogResponse extends BaseMdtResponse {
    private final Long carId;

    public BaseMdtLogResponse(Long carId) {
        super(MdtLogExceptionCode.Success);
        this.carId = carId;
    }

    @JsonProperty("mdn")
    public Long getCarId() {
        return carId;
    }
}
