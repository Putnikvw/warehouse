create table if not exists product
(
    id           bigint,
    title        varchar(300),
    handle       varchar(200),
    product_type varchar(200),
    created_at   timestamp with time zone default now(),
    updated_at   timestamp with time zone,
    constraint pk_product primary key (id)
);