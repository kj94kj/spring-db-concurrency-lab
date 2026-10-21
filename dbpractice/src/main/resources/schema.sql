CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    price INT NOT NULL
) ENGINE=InnoDB;

INSERT INTO product (id, name, stock, price)
VALUES (1, '테스트 상품', 100, 10000)
ON DUPLICATE KEY UPDATE id = id;
