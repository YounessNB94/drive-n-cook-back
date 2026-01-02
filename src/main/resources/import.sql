-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Données d'exemple pour faciliter les tests locaux
INSERT INTO franchisees (id, email, password_hash, first_name, last_name, phone, company_name, address, created_at)
VALUES
    (1, 'demo@drivncook.test', '$2a$10$demo', 'Demo', 'Owner', '+33123456789', 'Demo Cook', '1 rue Demo, Paris', now());

INSERT INTO warehouses (id, name, address, phone) VALUES
    (1, 'Paris Depot', '10 rue des Fleurs, Paris', '+3311111111');

INSERT INTO trucks (id, plate_number, status, franchisee_id, current_warehouse_id)
VALUES
    (1, 'FR-101-AA', 'ASSIGNED', 1, 1);

INSERT INTO customer_orders (id, type, status, franchisee_id, paid, payment_method, total_cash, total_points, created_at, updated_at)
VALUES
    (1, 'ON_SITE', 'PREPARING', 1, true, 'CASH', 48.50, 0, now(), now());

INSERT INTO sales (id, customer_order_id, quantity, total_amount, channel, date)
VALUES
    (1, 1, 3, 48.50, 'ON_SITE', now());
