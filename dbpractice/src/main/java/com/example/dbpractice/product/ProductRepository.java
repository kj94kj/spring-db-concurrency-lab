package com.example.dbpractice.product;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 상품 조회
    public Product findById(Long id) {

        String sql = """
            SELECT id, name, stock, price
            FROM product
            WHERE id = ?
            """;

        return jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("stock"),
                rs.getInt("price")
            ),
            id
        );
    }

    // 재고 변경
    public int updateStock(Long id, int stock) {

        String sql = """
            UPDATE product
            SET stock = ?
            WHERE id = ?
            """;

        return jdbcTemplate.update(sql, stock, id);
    }

    // 재고 1개 감소
    public int decreaseStock(Long id) {

        String sql = """
            UPDATE product
            SET stock = stock - 1
            WHERE id = ?
            """;

        return jdbcTemplate.update(sql, id);
    }

    // 재고가 있을 때만 감소
    public int decreaseStockIfAvailable(Long id) {

        String sql = """
            UPDATE product
            SET stock = stock - 1
            WHERE id = ?
              AND stock > 0
            """;

        return jdbcTemplate.update(sql, id);
    }
    
    // 현재 재고 조회
    public int findStockById(Long id) {

        String sql = """
            SELECT stock
            FROM product
            WHERE id = ?
            """;

        return jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            id
        );
    }
}
