-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

-- Données d'exemple pour faciliter les tests locaux
INSERT INTO franchisees (id, email, password_hash, first_name, last_name, phone, company_name, address, created_at)
VALUES
    (1, 'demo@drivncook.test', '$2a$10$demo', 'Demo', 'Owner', '+33123456789', 'Demo Cook', '1 rue Demo, Paris', now()),
    (2, 'admin@drivncook.test', '$2a$10$admin', 'Admin', 'Manager', '+33198765432', 'Admin Cook', '2 avenue Admin, Lyon', now());
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

INSERT INTO customer_orders (id, type, status, franchisee_id, paid, payment_method, total_cash, total_points, created_at, updated_at)
VALUES
    (1, 'ON_SITE', 'PREPARING', 1, true, 'CASH', 48.50, 0, now(), now()),
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

INSERT INTO sales (id, customer_order_id, quantity, total_amount, channel, date)
VALUES
    (1, 1, 3, 48.50, 'ON_SITE', now()),
    (2, 2, 2, 32.40, 'ON_SITE', '2025-12-20 11:00:00'),
    (3, 3, 3, 41.10, 'ON_SITE', '2025-12-21 13:30:00'),
    (4, 4, 2, 27.80, 'ON_SITE', '2025-12-22 12:15:00'),
    (5, 5, 4, 54.25, 'ON_SITE', '2025-12-24 18:45:00'),
    (6, 6, 3, 36.90, 'ON_SITE', '2025-12-26 10:05:00'),
    (7, 7, 5, 63.50, 'ON_SITE', '2025-12-27 19:20:00'),
    (8, 8, 2, 29.60, 'ON_SITE', '2025-12-29 08:50:00'),
    (9, 9, 3, 45.75, 'ON_SITE', '2025-12-30 14:10:00'),
    (10, 10, 3, 38.20, 'ON_SITE', '2026-01-01 12:40:00'),
    (11, 11, 4, 52.10, 'ON_SITE', '2026-01-03 17:55:00');
SELECT setval(pg_get_serial_sequence('sales', 'id'), 11, true);

INSERT INTO inventory_items (id, warehouse_id, name, unit, available_quantity)
VALUES
    (1, 1, 'Tortillas', 'kg', 50),
    (2, 1, 'Sauce salsa', 'litre', 80),
    (3, 2, 'Pain burger', 'pièce', 120),
    (4, 2, 'Fromage cheddar', 'kg', 35),
    (5, 3, 'Viande hachée', 'kg', 60),
    (6, 3, 'Laitue', 'kg', 40),
    (7, 4, 'Boissons cola', 'caisse', 25),
    (8, 4, 'Frites surgelées', 'kg', 75);
SELECT setval(pg_get_serial_sequence('inventory_items', 'id'), 8, true);

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
VALUES (1, 1, 'Remplacement de courroie terminé', 'RESOLVED', now());
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
