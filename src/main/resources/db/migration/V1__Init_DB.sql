create sequence product_id_seq start 11 increment 1;
create sequence users_id_seq start 24 increment 1;

CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    product_title VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    product_color VARCHAR(255) NOT NULL,
    cpu VARCHAR(255),
    ram VARCHAR(255),
    hard_disk VARCHAR(255),
    card VARCHAR(255),
    screen VARCHAR(255),
    connection_port VARCHAR(255),
    webcam VARCHAR(255),
    battery VARCHAR(255) NOT NULL,
    filename VARCHAR(255),
    price NUMERIC(19,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 20,
    weight VARCHAR(255),
    operating_system VARCHAR(255) NOT NULL
);

create table users
(
    id                  int8    not null,
    activation_code     varchar(255),
    active              boolean not null,
    address             varchar(255),
    city                varchar(255),
    email               varchar(255) not null,
    first_name          varchar(255) not null,
    last_name           varchar(255),
    password            varchar(255) not null,
    password_reset_code varchar(255),
    phone_number        varchar(255),
    post_index          varchar(255),
    primary key (id)
);