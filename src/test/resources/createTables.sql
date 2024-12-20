DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS receipts;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS menu_types;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS workers;
DROP TABLE IF EXISTS work_positions;
DROP TABLE IF EXISTS clients;

CREATE TABLE IF NOT EXISTS public.menu_types
(
    id SERIAL PRIMARY KEY,
    name character varying(30) UNIQUE NOT NULL
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.menu
(
    id SERIAL PRIMARY KEY,
    name character varying(40) NOT NULL,
    type_id integer NOT NULL,
    price numeric(10, 2) NOT NULL,
    FOREIGN KEY (type_id) REFERENCES menu_types (id)  ON DELETE CASCADE
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.work_positions
(
    id SERIAL PRIMARY KEY,
    name character varying(30) UNIQUE NOT NULL
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.workers
(
    id SERIAL PRIMARY KEY,
    name character varying(40) NOT NULL,
    surname character varying(40) NOT NULL,
    phone character varying(20) NOT NULL,
    email character varying(40) NOT NULL,
    position_id integer NOT NULL,
    FOREIGN KEY (position_id) REFERENCES work_positions (id)  ON DELETE CASCADE
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.clients
(
    id SERIAL PRIMARY KEY,
    name character varying(40) NOT NULL,
    surname character varying(40) NOT NULL,
    phone character varying(20),
    email character varying(40),
    birthday DATE,
    discount DECIMAL(5,2) DEFAULT 0 CHECK (discount >= 0 AND discount <= 100)
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.schedule
(
    id SERIAL PRIMARY KEY,
    worker_id integer NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES workers (id)  ON DELETE CASCADE
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.receipts
(
    id SERIAL PRIMARY KEY,
    client_id integer NOT NULL,
    total_price numeric(10, 2) NOT NULL,
    date date default '1900-01-01'::date not null,
    FOREIGN KEY (client_id) REFERENCES clients (id)  ON DELETE CASCADE
)

    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.orders
(
    id SERIAL PRIMARY KEY,
    worker_id integer NOT NULL,
    menu_position_id integer NOT NULL,
    receipt_Id integer NOT NULL,
    price numeric(10, 2) NOT NULL,
    waiter_id integer NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES workers (id)  ON DELETE CASCADE,
    FOREIGN KEY (menu_position_id) REFERENCES menu (id)  ON DELETE CASCADE,
    FOREIGN KEY (receipt_Id) REFERENCES receipts (id)  ON DELETE CASCADE,
    FOREIGN KEY (waiter_id) REFERENCES workers (id)  ON DELETE CASCADE
)

    TABLESPACE pg_default;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM menu_types) THEN
            INSERT INTO menu_types (name) VALUES
             ('Drink'), ('Dessert');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM work_positions) THEN
            INSERT INTO work_positions (name) VALUES
            ('Waiter'), ('Barista'), ('Pastry Chef');
        END IF;
    END $$;