package org.re.test.api;

import org.re.test.service.TestService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
}
