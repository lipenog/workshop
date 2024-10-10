CREATE TABLE public.orders(
    id serial NOT NULL,
    name VARCHAR(255),
    mail VARCHAR(255),
    phone VARCHAR(30),
    city VARCHAR(50),
    country VARCHAR(10),
    line1 VARCHAR(255),
    line2 VARCHAR(255),
    postal_code VARCHAR(30),
    state VARCHAR(10),
    PRIMARY KEY (id)
);

CREATE TABLE public.orders_items(
    id serial NOT NULL,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES public.orders (id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES public.products (id)
);