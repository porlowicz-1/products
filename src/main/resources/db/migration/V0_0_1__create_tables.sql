CREATE TABLE category (
    id INT auto_increment PRIMARY KEY,
    title CHAR,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product (
    id INT auto_increment PRIMARY KEY,
    title CHAR,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    best_price DOUBLE,
    pdp_url CHAR,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE SET NULL
);

CREATE TABLE offer (
    id INT auto_increment PRIMARY KEY,
    title CHAR,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    price DOUBLE,
    delivery_constraint_days INT,
    shop_id CHAR
);

CREATE TABLE offer_product (
    id INT auto_increment PRIMARY KEY,
    offer_id INT NOT NULL,
    product_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product (id),
    FOREIGN KEY (offer_id) REFERENCES offer (id)
);