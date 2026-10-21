package com.example.dbpractice;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbConnectionController {

    private final JdbcTemplate jdbcTemplate;

    public DbConnectionController(
            JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/db/test")
    public String testConnection() {

        String version = jdbcTemplate.queryForObject(
            "SELECT VERSION()",
            String.class
        );

        return "MySQL 연결 성공! 버전: " + version;
    }
}