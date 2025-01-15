CREATE SEQUENCE products_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE products
(
    id          BIGINT         NOT NULL,
    uuid        UUID           NOT NULL,
    created_on  TIMESTAMP      NOT NULL,
    updated_on  TIMESTAMP      NOT NULL,
    version     BIGINT         NOT NULL,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(500),
    price       DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);
