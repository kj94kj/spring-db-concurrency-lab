package com.example.dbpractice.product;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    // 상품 조회
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return repository.findById(id);
    }

    // 재고 초기화
    @PostMapping("/{id}/reset")
    public String resetStock(
            @PathVariable("id") Long id,
            @RequestParam("stock") int stock) {

        repository.updateStock(id, stock);

        return "재고 초기화 완료";
    }

    // 조건부 재고 감소
    @PostMapping("/{id}/decrease")
    public String decreaseStock(@PathVariable("id") Long id) {

        int updated =
            repository.decreaseStockIfAvailable(id);

        if (updated == 0) {
            return "재고 부족";
        }

        return "재고 감소 성공";
    }
}
