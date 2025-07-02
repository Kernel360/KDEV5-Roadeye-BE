package org.re.test.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.re.common.api.payload.BaseMdtResponse;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.mdtlog.constraints.ValidLatitude;
import org.re.mdtlog.constraints.ValidPacketVersion;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.test.api.payload.TodoItem;
import org.re.test.service.TestService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Valid
@RestController
public class TestApi {
    private final TestService testService;

    public TestApi(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/api/test")
    public void testError1() {
        testService.invoke();
    }

    @GetMapping("/method-test/get")
    public String get() {
        return "get";
    }

    @PostMapping(value = "/content-type-test/json", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public String contentTypeTestJson() {
        return "json";
    }

    @PostMapping("/test/json")
    public Object testJson(
        @RequestBody Map<String, Object> jsonRequest
    ) {
        return jsonRequest;
    }

    @PostMapping("/test/json/todo")
    public Object testJson2(
        @Valid @RequestBody TodoItem todoItem
    ) {
        return todoItem;
    }

    @PostMapping("/test/tuid")
    public Object tuidMissing(
        TransactionUUID uuid
    ) {
        return new BaseMdtResponse(MdtLogExceptionCode.Success);
    }

    @PostMapping("/test/timestamp")
    public Object timestampMissing(
        @NotNull MdtLogRequestTimeInfo timeInfo
    ) {
        System.out.println("timeInfo = " + timeInfo);
        return new BaseMdtResponse(MdtLogExceptionCode.Success);
    }

    @PostMapping("/test/validation")
    public Object jakartaValidation(
        @Min(10) @RequestParam Integer value
    ) {
        return value;
    }

    @PostMapping("/test/field/packet-version")
    public BaseMdtResponse packetVersion(
        @RequestParam @ValidPacketVersion Integer packetVersion
    ) {
        System.out.println("packetVersion = " + packetVersion);
        return new BaseMdtResponse(MdtLogExceptionCode.Success);
    }

    @PostMapping("/test/field/latitude")
    public BaseMdtResponse latitude(
        @RequestParam @ValidLatitude BigDecimal latitude
    ) {
        System.out.println("latitude = " + latitude);
        return new BaseMdtResponse(MdtLogExceptionCode.Success);
    }
}
