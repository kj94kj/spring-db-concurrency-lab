package com.example.dbpractice.product;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/concurrency")
public class ConcurrentTestController {

    private final ConcurrentTestService testService;

    public ConcurrentTestController(
            ConcurrentTestService testService) {
        this.testService = testService;
    }

    @PostMapping("/{mode}")
    public ConcurrentTestService.TestResult test(
            @PathVariable("mode") String mode,
            @RequestParam(name = "requests", defaultValue = "100") int requests
    ) throws InterruptedException {

        if (!mode.equals("unsafe") && !mode.equals("atomic")) {
            throw new IllegalArgumentException(
                    "mode는 unsafe 또는 atomic이어야 합니다."
            );
        }

        if (requests < 1 || requests > 1000) {
            throw new IllegalArgumentException(
                    "requests는 1~1000 사이여야 합니다."
            );
        }

        return testService.runTest(1L, mode, requests);
    }
}