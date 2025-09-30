create table if not exists product
(
    id           bigint,
    title       varchar(300),
    product_type_id integer,
    created_at   timestamp with time zone,
    published_at timestamp with time zone,
    updated_at   timestamp with time zone,
    constraint pk_product primary key (id),
    constraint fk_product_type foreign key (product_type_id) references product_type (id)
);

alter table if exists product add column handle varchar(200);