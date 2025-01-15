INSERT INTO products (id, uuid, created_on, updated_on, version, name, description, price)
VALUES
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Laptop', 'Wysokiej jakości laptop', 2999.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Smartphone', 'Nowoczesny smartfon z dużym ekranem', 1999.50),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Headphones', 'Bezprzewodowe słuchawki z redukcją szumów', 499.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Monitor', 'Monitor 27 cali 4K UHD', 1299.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Keyboard', 'Mechaniczna klawiatura gamingowa', 299.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Mouse', 'Bezprzewodowa mysz optyczna', 99.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Chair', 'Ergonomiczne krzesło biurowe', 699.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Desk', 'Biurko z regulacją wysokości', 899.50),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Speaker', 'Głośnik Bluetooth z wysoką jakością dźwięku', 399.99),
    (NEXTVAL('products_seq'), RANDOM_UUID(), NOW(), NOW(), 1, 'Camera', 'Kamera internetowa Full HD', 249.99);
