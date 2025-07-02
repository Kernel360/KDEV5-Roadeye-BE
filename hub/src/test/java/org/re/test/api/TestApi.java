package org.re.test.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.re.common.api.payload.BaseMdtResponse;
import org.re.common.api.payload.MdtLogRequestTimeInfo;
import org.re.common.exception.MdtLogExceptionCode;
import org.re.mdtlog.domain.TransactionUUID;
import org.re.test.service.TestService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
