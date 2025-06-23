package org.re.mdtlog.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.re.common.api.payload.BaseMdtLogResponse;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.mdtlog.dto.MdtCycleLogMessage;
import org.re.mdtlog.service.MdtCycleLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cycle-log")
@RequiredArgsConstructor
public class MdtCycleLogApi {
    private final MdtCycleLogService cycleLogService;

    @PostMapping
    public BaseMdtLogResponse addCycleLogs(
        @Valid @RequestBody MdtCycleLogMessage dto,
        @NotNull MdtLogRequestTimeInfo timeInfo,
        TransactionUUID tuid
    ) {
        cycleLogService.addCycleLogs(tuid, dto, timeInfo);
        return new BaseMdtLogResponse(dto.carId());
    }
}
