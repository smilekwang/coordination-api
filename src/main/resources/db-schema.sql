drop table if exists brand;
drop table if exists category;
drop table if exists product;

create table brand (
    id bigint auto_increment primary key,
    name varchar(100),
    use_yn tinyint(1),
    created_at datetime DEFAULT CURRENT_TIMESTAMP ,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create index idx_brand_01 ON brand(name);

create table product (
    id bigint auto_increment primary key,
    brand_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    name varchar(100),
    price int(9) not null,
    use_yn tinyint(1),
    created_at datetime DEFAULT CURRENT_TIMESTAMP ,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create index idx_product_01 ON product(category_id, price);
create index idx_product_02 ON product(category_id);
create index idx_product_03 ON product(brand_id);
create index idx_product_04 ON product(brand_id, category_id);

create table category (
    id bigint auto_increment primary key,
    name varchar(100),
    use_yn tinyint(1),
    created_at datetime DEFAULT CURRENT_TIMESTAMP ,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
create index idx_category_01 ON category(name);
