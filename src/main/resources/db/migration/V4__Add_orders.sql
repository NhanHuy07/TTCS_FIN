INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (1, 'Ha Dong', 'Ha Noi', '2024-04-07', 'test123@test.com', 'Thanh', 'Nguyen Tien', '1234567890', 1234567890, 840, 2);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (2, 'Ha Dong', 'Ha Noi', '2024-04-07', 'test123@test.com', 'Thanh', 'Nguyen Tien', '1234567890', 1234567890, 240, 2);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (3, 'Cau Giay', 'Ha Noi', '2024-04-07', 'test456@test.com', 'Hong', 'Nguyen Thu', '1234567890', 1234567890, 163, 3);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (4, 'Cau Giay', 'Ha Noi', '2024-04-07', 'test456@test.com', 'Hong', 'Nguyen Thu', '1234567890', 1234567890, 780, 3);
INSERT INTO orders (id, address, city, date, email, first_name, last_name, phone_number, post_index, total_price, user_id)
    VALUES (5, 'Cau Giay', 'Ha Noi', '2024-04-07', 'test456@test.com', 'Hong', 'Nguyen Thu', '1234567890', 1234567890, 196, 3);

INSERT INTO orders_products (order_id, products_id) VALUES (1, 33);
INSERT INTO orders_products (order_id, products_id) VALUES (1, 34);
INSERT INTO orders_products (order_id, products_id) VALUES (2, 39);
INSERT INTO orders_products (order_id, products_id) VALUES (2, 43);
INSERT INTO orders_products (order_id, products_id) VALUES (3, 3);
INSERT INTO orders_products (order_id, products_id) VALUES (3, 48);
INSERT INTO orders_products (order_id, products_id) VALUES (3, 8);
INSERT INTO orders_products (order_id, products_id) VALUES (4, 16);
INSERT INTO orders_products (order_id, products_id) VALUES (4, 17);
INSERT INTO orders_products (order_id, products_id) VALUES (5, 6);
INSERT INTO orders_products (order_id, products_id) VALUES (5, 9);
