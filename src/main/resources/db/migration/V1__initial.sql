CREATE TABLE public.products (
    id serial NOT NULL,
    name varchar(100) NOT NULL,
    description varchar(255) NULL,
    price float NOT NULL,
    PRIMARY KEY (id)
);