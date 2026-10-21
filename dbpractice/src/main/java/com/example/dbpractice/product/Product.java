package com.example.dbpractice.product;

public class Product {

    private Long id;
    private String name;
    private int stock;
    private int price;

    public Product(
            Long id,
            String name,
            int stock,
            int price) {

        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public int getPrice() {
        return price;
    }
}
