-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Données d'exemple pour faciliter les tests locaux
INSERT INTO franchisees (id, email, password_hash, first_name, last_name, phone, company_name, address, created_at, role)
VALUES
    (1, 'demo@drivncook.test', '$2a$10$PrPBDyIp4dAqFft16iQwUuAc4Fc7zjLvw50XtgUvlbs8IpYiMkHWe', 'Demo', 'Owner', '+33123456789', 'Demo Cook', '1 rue Demo, Paris', now(), 'FRANCHISEE'),
    (2, 'admin@drivncook.test', '$2a$10$PrPBDyIp4dAqFft16iQwUuAc4Fc7zjLvw50XtgUvlbs8IpYiMkHWe', 'Admin', 'Manager', '+33198765432', 'Admin Cook', '2 avenue Admin, Lyon', now(), 'ADMIN');
SELECT setval(pg_get_serial_sequence('franchisees', 'id'), 2, true);

INSERT INTO warehouses (id, name, address, phone) VALUES
    (1, 'Paris Depot Nord', '10 rue des Fleurs, Paris', '+3311111111'),
    (2, 'Paris Depot Bastille', '22 boulevard Beaumarchais, Paris', '+3312222222'),
    (3, 'Paris Depot Montparnasse', '48 avenue du Maine, Paris', '+3313333333'),
    (4, 'Paris Depot Nation', '75 cours de Vincennes, Paris', '+3314444444');
SELECT setval(pg_get_serial_sequence('warehouses', 'id'), 4, true);

INSERT INTO trucks (id, plate_number, status, franchisee_id, current_warehouse_id)
VALUES
    (1, 'FR-101-AA', 'ASSIGNED', 1, 1),
    (2, 'FR-102-BB', 'IN_SERVICE', NULL, 1),
    (3, 'FR-201-CC', 'IN_SERVICE', NULL, 2),
    (4, 'FR-202-DD', 'IN_SERVICE', NULL, 2),
    (5, 'FR-301-EE', 'IN_SERVICE', NULL, 3),
    (6, 'FR-302-FF', 'IN_SERVICE', NULL, 3),
    (7, 'FR-401-GG', 'IN_SERVICE', NULL, 4),
    (8, 'FR-402-HH', 'IN_SERVICE', NULL, 4);
SELECT setval(pg_get_serial_sequence('trucks', 'id'), 8, true);

INSERT INTO supply_orders (id, status, franchisee_id, pickup_warehouse_id, paid, payment_method, payment_ref, created_at, updated_at)
VALUES
    (1, 'DRAFT', 1, 1, false, NULL, NULL, now(), now()),
    (2, 'CONFIRMED', 1, 2, false, NULL, NULL, now(), now()),
    (3, 'READY', 1, 3, true, 'CASH', 'PAY-READY-1', now(), now()),
    (4, 'DRAFT', 2, 2, false, NULL, NULL, now(), now()),
    (5, 'CONFIRMED', 2, 3, false, NULL, NULL, now(), now()),
    (6, 'READY', 2, 4, true, 'CASH', 'PAY-READY-2', now(), now());
SELECT setval(pg_get_serial_sequence('supply_orders', 'id'), 6, true);

INSERT INTO menus (id, franchisee_id, status, updated_at) VALUES
    (1, 1, 'PUBLISHED', now());
SELECT setval(pg_get_serial_sequence('menus', 'id'), 1, true);

INSERT INTO menu_items (id, menu_id, name, price_cash, points_price, available) VALUES
    (1, 1, 'Tacos classique', 12.50, 900, true),
    (2, 1, 'Nachos fromage', 10.90, 750, true),
    (3, 1, 'Burger signature', 14.30, NULL, true),
    (4, 1, 'Wrap poulet', 13.40, 950, true),
    (5, 1, 'Salade fraîcheur', 12.20, NULL, true),
    (6, 1, 'Frites maison', 6.50, 450, true),
    (7, 1, 'Boisson artisanale', 5.90, 400, true),
    (8, 1, 'Dessert brownie', 7.20, 520, true),
    (9, 1, 'Tacos veggie', 11.80, 880, true),
    (10, 1, 'Hot-dog gourmet', 9.70, 700, true);
SELECT setval(pg_get_serial_sequence('menu_items', 'id'), 10, true);

INSERT INTO customer_orders (id, type, status, franchisee_id, paid, payment_method, total_cash, total_points, created_at, updated_at)
VALUES
    (1, 'ON_SITE', 'PREPARING', 1, false, 'CASH', 48.50, 0, now(), now()),
    (2, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 32.40, 0, '2025-12-20 10:50:00', '2025-12-20 11:05:00'),
    (3, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 41.10, 0, '2025-12-21 13:10:00', '2025-12-21 13:45:00'),
    (4, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 27.80, 0, '2025-12-22 11:55:00', '2025-12-22 12:20:00'),
    (5, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 54.25, 0, '2025-12-24 18:15:00', '2025-12-24 18:55:00'),
    (6, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 36.90, 0, '2025-12-26 09:45:00', '2025-12-26 10:15:00'),
    (7, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 63.50, 0, '2025-12-27 18:50:00', '2025-12-27 19:25:00'),
    (8, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 29.60, 0, '2025-12-29 08:30:00', '2025-12-29 08:55:00'),
    (9, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 45.75, 0, '2025-12-30 13:55:00', '2025-12-30 14:20:00'),
    (10, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 38.20, 0, '2026-01-01 12:20:00', '2026-01-01 12:50:00'),
    (11, 'ON_SITE', 'COMPLETED', 1, true, 'CASH', 52.10, 0, '2026-01-03 17:30:00', '2026-01-03 18:00:00');
SELECT setval(pg_get_serial_sequence('customer_orders', 'id'), 11, true);

INSERT INTO sales (id, customer_order_id, menu_item_id, quantity, total_amount, channel, date)
VALUES
    (1, 1, 1, 3, 48.50, 'ON_SITE', now()),
    (2, 2, 2, 2, 32.40, 'ON_SITE', '2025-12-20 11:00:00'),
    (3, 3, 3, 3, 41.10, 'ON_SITE', '2025-12-21 13:30:00'),
    (4, 4, 4, 2, 27.80, 'ON_SITE', '2025-12-22 12:15:00'),
    (5, 5, 5, 4, 54.25, 'ON_SITE', '2025-12-24 18:45:00'),
    (6, 6, 6, 3, 36.90, 'ON_SITE', '2025-12-26 10:05:00'),
    (7, 7, 3, 5, 63.50, 'ON_SITE', '2025-12-27 19:20:00'),
    (8, 8, 2, 2, 29.60, 'ON_SITE', '2025-12-29 08:50:00'),
    (9, 9, 4, 3, 45.75, 'ON_SITE', '2025-12-30 14:10:00'),
    (10, 10, 5, 3, 38.20, 'ON_SITE', '2026-01-01 12:40:00'),
    (11, 11, 1, 4, 52.10, 'ON_SITE', '2026-01-03 17:55:00');
SELECT setval(pg_get_serial_sequence('sales', 'id'), 11, true);

INSERT INTO customer_order_items (id, customer_order_id, menu_item_id, quantity, line_cash_total, line_points_total) VALUES
    (1, 1, 1, 2, 25.00, NULL),
    (2, 1, 2, 1, 23.50, NULL),
    (3, 2, 1, 1, 12.00, NULL),
    (4, 2, 2, 2, 20.40, NULL),
    (5, 3, 3, 2, 28.60, NULL),
    (6, 3, 4, 1, 12.50, NULL),
    (7, 4, 2, 1, 10.90, NULL),
    (8, 4, 4, 2, 16.90, NULL),
    (9, 5, 3, 2, 28.80, NULL),
    (10, 5, 5, 2, 25.45, NULL),
    (11, 6, 1, 1, 12.50, NULL),
    (12, 6, 5, 2, 24.40, NULL),
    (13, 7, 3, 3, 43.20, NULL),
    (14, 7, 6, 2, 20.30, NULL),
    (15, 8, 2, 1, 10.90, NULL),
    (16, 8, 6, 1, 18.70, NULL),
    (17, 9, 3, 2, 28.80, NULL),
    (18, 9, 4, 1, 16.95, NULL),
    (19, 10, 5, 1, 12.20, NULL),
    (20, 10, 6, 2, 26.00, NULL),
    (21, 11, 1, 2, 25.00, NULL),
    (22, 11, 3, 2, 27.10, NULL);
SELECT setval(pg_get_serial_sequence('customer_order_items', 'id'), 22, true);

INSERT INTO loyalty_cards (id, customer_ref, points_balance, created_at, franchisee_id) VALUES
    (1, 'CUST-0001', 1200, now() - interval '10 days', 1),
    (2, 'CUST-0002', 450, now() - interval '5 days', 1),
    (3, 'CUST-ADMIN-1', 800, now() - interval '7 days', 2);
SELECT setval(pg_get_serial_sequence('loyalty_cards', 'id'), 3, true);

INSERT INTO inventory_items (id, warehouse_id, name, unit, available_quantity)
VALUES
    -- Warehouse 1 stocks every menu item
    (1, 1, 'Tacos classique', 'portion', 150),
    (2, 1, 'Nachos fromage', 'portion', 120),
    (3, 1, 'Burger signature', 'portion', 90),
    (4, 1, 'Wrap poulet', 'portion', 110),
    (5, 1, 'Salade fraîcheur', 'portion', 80),
    (6, 1, 'Frites maison', 'portion', 200),
    (7, 1, 'Boisson artisanale', 'bouteille', 140),
    (8, 1, 'Dessert brownie', 'portion', 95),
    (9, 1, 'Tacos veggie', 'portion', 130),
    (10, 1, 'Hot-dog gourmet', 'portion', 105),
    -- Warehouse 2 mirrors the same catalog with different stock levels
    (11, 2, 'Tacos classique', 'portion', 95),
    (12, 2, 'Nachos fromage', 'portion', 85),
    (13, 2, 'Burger signature', 'portion', 75),
    (14, 2, 'Wrap poulet', 'portion', 88),
    (15, 2, 'Salade fraîcheur', 'portion', 70),
    (16, 2, 'Frites maison', 'portion', 160),
    (17, 2, 'Boisson artisanale', 'bouteille', 120),
    (18, 2, 'Dessert brownie', 'portion', 82),
    (19, 2, 'Tacos veggie', 'portion', 90),
    (20, 2, 'Hot-dog gourmet', 'portion', 78),
    -- Warehouse 3
    (21, 3, 'Tacos classique', 'portion', 60),
    (22, 3, 'Nachos fromage', 'portion', 55),
    (23, 3, 'Burger signature', 'portion', 65),
    (24, 3, 'Wrap poulet', 'portion', 58),
    (25, 3, 'Salade fraîcheur', 'portion', 62),
    (26, 3, 'Frites maison', 'portion', 140),
    (27, 3, 'Boisson artisanale', 'bouteille', 115),
    (28, 3, 'Dessert brownie', 'portion', 74),
    (29, 3, 'Tacos veggie', 'portion', 68),
    (30, 3, 'Hot-dog gourmet', 'portion', 72),
    -- Warehouse 4
    (31, 4, 'Tacos classique', 'portion', 110),
    (32, 4, 'Nachos fromage', 'portion', 95),
    (33, 4, 'Burger signature', 'portion', 85),
    (34, 4, 'Wrap poulet', 'portion', 92),
    (35, 4, 'Salade fraîcheur', 'portion', 88),
    (36, 4, 'Frites maison', 'portion', 175),
    (37, 4, 'Boisson artisanale', 'bouteille', 150),
    (38, 4, 'Dessert brownie', 'portion', 98),
    (39, 4, 'Tacos veggie', 'portion', 112),
    (40, 4, 'Hot-dog gourmet', 'portion', 100);
SELECT setval(pg_get_serial_sequence('inventory_items', 'id'), 40, true);

INSERT INTO supply_order_items (id, supply_order_id, inventory_item_id, quantity)
VALUES
    -- Order 1 (warehouse 1 stock)
    (1, 1, 1, 40),
    (2, 1, 2, 25),
    (3, 1, 6, 50),
    -- Order 2 (warehouse 2)
    (4, 2, 11, 30),
    (5, 2, 12, 20),
    (6, 2, 16, 45),
    -- Order 3 (warehouse 3)
    (7, 3, 21, 35),
    (8, 3, 23, 25),
    (9, 3, 27, 40),
    -- Order 4 (warehouse 2, admin franchisee)
    (10, 4, 14, 18),
    (11, 4, 15, 16),
    (12, 4, 20, 22),
    -- Order 5 (warehouse 3)
    (13, 5, 24, 28),
    (14, 5, 25, 24),
    (15, 5, 29, 32),
    -- Order 6 (warehouse 4)
    (16, 6, 31, 42),
    (17, 6, 33, 30),
    (18, 6, 36, 55);
SELECT setval(pg_get_serial_sequence('supply_order_items', 'id'), 18, true);


INSERT INTO franchise_terms (version, entry_fee_text, royalty_text, supply_rule_text, content)
VALUES (
    '2025-01',
    'Frais d''entrée : 50 000 EUR',
    'Redevance mensuelle : 4% du chiffre d''affaires',
    'Règles d''approvisionnement : 80% minimum via les entrepôts Driv''n Cook',
    'Conditions officielles de la franchise Driv''n Cook (version 2025-01) - frais à l''entrée, redevances et règles d''approvisionnement applicables à chaque franchisé.'
);

INSERT INTO appointments (id, type, status, datetime, created_at, franchisee_id, warehouse_id, supply_order_id, truck_id)
VALUES (1, 'SUPPLY_PICKUP', 'SCHEDULED', '2026-01-05 09:00:00', '2026-01-01 10:00:00', 1, 1, 1, null);
SELECT setval(pg_get_serial_sequence('appointments', 'id'), 1, true);

INSERT INTO incidents (id, truck_id, description, status, created_at)
VALUES (1, 1, 'Remplacement de courroie terminé', 'OPEN', now());
SELECT setval(pg_get_serial_sequence('incidents', 'id'), 1, true);

INSERT INTO maintenance_records (id, truck_id, date, description) VALUES
    (1, 1, '2025-12-20 08:00:00', 'Vidange moteur complétée'),
    (2, 1, '2025-12-28 09:30:00', 'Contrôle des freins et remplacement des plaquettes'),
    (3, 2, '2025-12-18 10:00:00', 'Révision générale du circuit électrique'),
    (4, 2, '2025-12-27 14:15:00', 'Changement du filtre à air'),
    (5, 3, '2025-12-17 07:45:00', 'Alignement des roues avant'),
    (6, 3, '2025-12-26 11:20:00', 'Nettoyage du système d’injection'),
    (7, 4, '2025-12-16 15:10:00', 'Remplacement des bougies d’allumage'),
    (8, 4, '2025-12-24 08:50:00', 'Test batterie et câblage'),
    (9, 5, '2025-12-15 13:05:00', 'Graissage des articulations de porte'),
    (10, 5, '2025-12-23 09:40:00', 'Remplacement courroie auxiliaire'),
    (11, 6, '2025-12-14 16:30:00', 'Contrôle du système de climatisation'),
    (12, 6, '2025-12-22 10:25:00', 'Équilibrage des pneus arrière'),
    (13, 7, '2025-12-13 11:55:00', 'Inspection visuelle châssis'),
    (14, 7, '2025-12-21 07:35:00', 'Vidange du différentiel'),
    (15, 8, '2025-12-12 12:45:00', 'Remplacement amortisseurs avant'),
    (16, 8, '2025-12-20 17:15:00', 'Contrôle système d’éclairage');
SELECT setval(pg_get_serial_sequence('maintenance_records', 'id'), 16, true);
