package com.example.dbpractice.product;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ConcurrentTestService {

    private final ProductService productService;
    private final ProductRepository repository;

    public ConcurrentTestService(
            ProductService productService,
            ProductRepository repository) {

        this.productService = productService;
        this.repository = repository;
    }

    public TestResult runTest(
            Long productId,
            String mode,
            int requests) throws InterruptedException {

        // 재고 초기화
        repository.updateStock(productId, 100);

        ExecutorService executor =
                Executors.newFixedThreadPool(20);

        CountDownLatch ready = new CountDownLatch(requests);
        CountDownLatch start = new CountDownLatch(1);

        List<Future<Boolean>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < requests; i++) {

                futures.add(executor.submit(() -> {

                    ready.countDown();
                    start.await();

                    if ("unsafe".equals(mode)) {
                        return productService.purchaseUnsafe(productId);
                    }

                    if ("atomic".equals(mode)) {
                        return productService.purchaseAtomic(productId);
                    }

                    throw new IllegalArgumentException(
                            "지원하지 않는 mode: " + mode
                    );
                }));
            }

            // 작업 제출 후 동시에 시작
            start.countDown();

            int success = 0;
            int failed = 0;

            for (Future<Boolean> future : futures) {
                try {
                    if (future.get()) {
                        success++;
                    } else {
                        failed++;
                    }
                } catch (ExecutionException e) {
                    failed++;
                    e.getCause().printStackTrace();
                }
            }

            int finalStock =
                    repository.findStockById(productId);

            return new TestResult(
                    mode,
                    requests,
                    success,
                    failed,
                    finalStock
            );

        } finally {
            executor.shutdownNow();
        }
    }

    public record TestResult(
            String mode,
            int requests,
            int success,
            int failed,
            int finalStock
    ) {}
}