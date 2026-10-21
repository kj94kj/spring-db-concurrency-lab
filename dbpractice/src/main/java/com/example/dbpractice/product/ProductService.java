package com.example.dbpractice.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // 방법 1: 조회 후 재고 변경 (Lost Update 가능)
    @Transactional
    public boolean purchaseUnsafe(Long id) {

        int stock = repository.findStockById(id);

        if (stock <= 0) {
            return false;
        }

        // 동시 요청에서 문제를 쉽게 재현하기 위한 지연
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }

        repository.updateStock(id, stock - 1);

        return true;
    }

    // 방법 2: 조건부 UPDATE
    @Transactional
    public boolean purchaseAtomic(Long id) {

        int updated =
                repository.decreaseStockIfAvailable(id);

        return updated == 1;
    }
}
