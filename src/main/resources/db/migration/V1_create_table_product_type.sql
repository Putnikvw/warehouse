create table if not exists product_type
(
    id     serial,
    name varchar(100),
    constraint pk_product_type primary key (id),
    constraint ak_product_type unique (name)
);