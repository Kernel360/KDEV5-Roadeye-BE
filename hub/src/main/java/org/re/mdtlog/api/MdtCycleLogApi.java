package org.re.mdtlog.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.re.common.api.payload.BaseMdtLogResponse;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.service.MdtCycleLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/cycle-log")
@RequiredArgsConstructor
public class MdtCycleLogApi {
    private final MdtCycleLogService cycleLogService;

    @PostMapping
    public BaseMdtLogResponse addCycleLogs(
        @Valid @RequestBody MdtCycleLogMessage dto,
        @NotNull MdtLogRequestTimeInfo timeInfo,
        TransactionUUID tuid,
        @Nullable @RequestHeader(value = "X-routing-key", required = false) String routingKey
    ) {
        cycleLogService.addCycleLogs(tuid.value(), dto, timeInfo, routingKey);
        return new BaseMdtLogResponse(dto.carId());
    }
}
