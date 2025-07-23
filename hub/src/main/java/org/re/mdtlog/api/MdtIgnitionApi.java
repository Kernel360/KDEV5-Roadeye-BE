package org.re.mdtlog.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.common.api.payload.BaseMdtLogResponse;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtIgnitionOffMessage;
import org.re.mdtlog.dto.MdtIgnitionOnMessage;
import org.re.mdtlog.service.MdtIgnitionService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/ignition")
@RequiredArgsConstructor
public class MdtIgnitionApi {
    private final MdtIgnitionService mdtIgnitionService;

    @PostMapping(value = "/on", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseMdtLogResponse ignitionOn(
        @Valid @RequestBody MdtIgnitionOnMessage dto,
        @NotNull MdtLogRequestTimeInfo timeInfo,
        TransactionUUID tuid,
        @Nullable @RequestHeader(value = "X-routing-key", required = false) String routingKey
    ) {
        mdtIgnitionService.ignitionOn(tuid.value(), dto, timeInfo, routingKey);
        return new BaseMdtLogResponse(dto.carId());
    }

    @PostMapping(value = "/off", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseMdtLogResponse ignitionOn(
        @Valid @RequestBody MdtIgnitionOffMessage dto,
        @NotNull MdtLogRequestTimeInfo timeInfo,
        TransactionUUID tuid,
        @Nullable @RequestHeader(value = "X-routing-key", required = false) String routingKey
    ) {
        mdtIgnitionService.ignitionOff(tuid.value(), dto, timeInfo, routingKey);
        return new BaseMdtLogResponse(dto.carId());
    }
}
