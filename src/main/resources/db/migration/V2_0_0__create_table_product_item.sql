create table if not exists product_item
(
    id          bigint,
    title       varchar(300),
    price       numeric(10, 6),
    taxable     boolean default true,
    feature_img jsonb,
    product_id  bigint,
    constraint pk_product_item primary key (id),
    constraint fk_product foreign key (product_id) references product (id)
);