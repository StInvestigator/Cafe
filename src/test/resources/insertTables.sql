INSERT INTO work_positions (name) VALUES
('Waiter'), ('Barista'), ('Pastry Chef');

INSERT INTO menu_types (name) VALUES
('Drink'), ('Dessert');

INSERT INTO clients(name, surname, phone, email, birthday, discount)
VALUES ('name1', 'surname1', '+123123', 'email@gmail.com', '2004-01-01', 0),
       ('name2', 'surname2', '+123123', 'email@gmail.com', '2004-01-01', 15);

INSERT INTO menu(name, type_id, price)
VALUES ('name1', 1, 50.50),
       ('name2', 2, 100);

INSERT INTO workers(name, surname, phone, email, position_id)
VALUES ('name1', 'surname1', '+123123', 'email@gmail.com', 1),
       ('name2', 'surname2', '+123123', 'email@gmail.com', 2),
       ('name3', 'surname3', '+123123', 'email@gmail.com', 3);

INSERT INTO schedule(worker_id, date, start_time, end_time)
VALUES (1, '2024-12-20', '08:00:00', '12:00:00'),
       (2, '2024-12-21', '08:00:00', '12:00:00'),
       (3, '2024-12-22', '08:00:00', '12:00:00');

INSERT INTO receipts(client_id, total_price, date)
VALUES (1, 100, '2024-12-20'),
       (2, 150.50, '2024-12-20'),
       (2, 1000, '2024-12-21');

INSERT INTO orders(worker_id, menu_position_id, receipt_id, price, waiter_id)
VALUES (3, 2, 1, 100, 1),
       (3, 2, 2, 100, 1),
       (2, 1, 2, 50.50, 1),
(2, 1, 3, 100, 1);
