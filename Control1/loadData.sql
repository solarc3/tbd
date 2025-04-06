TRUNCATE TABLE
    detalle_venta,
    venta,
    empleado,
    tienda,
    producto,
    vendedor,
    comuna
    RESTART IDENTITY CASCADE;

INSERT INTO comuna (nombre_comuna) VALUES
('Providencia'), ('Las Condes'), ('Santiago Centro'), ('Nunoa'), ('La Florida');

INSERT INTO tienda (direccion, id_comuna) VALUES
('Av. Providencia 1234', 1), ('Av. Apoquindo 5680', 2), ('Alameda 123', 3),
('Av. Irarrazaval 4050', 4), ('Av. Vicuna Mackenna 6100', 5), ('Merced 738', 3);

INSERT INTO producto (nombre_producto, precio, categoria, stock) VALUES
('Laptop HP 15.6 Core i5', 899990, 'Electronica', 25), ('Smartphone Samsung Galaxy S23', 699990, 'Electronica', 35),
('Zapatillas Running Nike Air', 89990, 'Calzado', 50), ('Cafetera Dolce Gusto', 49990, 'Hogar', 60),
('Set de Toallas 6 piezas', 29990, 'Hogar', 80), ('Vino Casillero del Diablo', 7990, 'Alimentos', 150),
('Mochila Impermeable 25L', 34990, 'Accesorios', 40), ('Pulsera Smart Xiaomi Mi Band 7', 29990, 'Accesorios', 55),
('Teclado Mecanico Gamer RGB', 65000, 'Electronica', 30), ('Monitor Curvo 27 Samsung', 249990, 'Electronica', 15),
('Jeans Levis 501', 45990, 'Vestuario', 60), ('Polera Algodon Basica', 9990, 'Vestuario', 100),
('Pack Cervezas Artesanales', 12990, 'Alimentos', 70), ('Libro Cien Anos de Soledad', 14990, 'Libros', 45),
('Audifonos Bluetooth Sony', 59990, 'Electronica', 50), ('Smart TV 55 LG', 449990, 'Electronica', 20),
('Refrigerador NoFrost Samsung', 549990, 'Hogar', 10), ('Bicicleta Montana Aro 29', 199990, 'Deportes', 18),
('Perfume Carolina Herrera 100ml', 75990, 'Belleza', 40), ('Saco de Dormir Termico', 39990, 'Deportes', 22),
('Juego de Ollas Tramontina 7pz', 69990, 'Hogar', 28), ('Taladro Inalambrico Bosch', 89990, 'Herramientas', 15),
('Mouse Inalambrico Logitech', 19990, 'Electronica', 60), ('Silla de Oficina Ergonomica', 129990, 'Hogar', 25),
('Consola PlayStation 5', 649990, 'Electronica', 8), ('Balon de Futbol N5', 15990, 'Deportes', 40),
('Crema Facial Anti-Edad', 24990, 'Belleza', 50), ('Destornillador Electrico', 29990, 'Herramientas', 33);

INSERT INTO vendedor (nombre_vendedor) VALUES
('Juan Perez Gonzalez'), ('Maria Jose Fernandez'), ('Carlos Andrade Silva'), ('Ana Maria Riquelme'),
('Pedro Pablo Vargas'), ('Laura Gomez Diaz'), ('Matias Contreras Soto'), ('Valeria Torres Munoz'),
('Francisco Rojas Silva');

INSERT INTO empleado (nombre_empleado, cargo, sueldo, id_comuna, id_tienda) VALUES
('Luis Martinez', 'Gerente Tienda', 2500000, 1, 1), ('Javiera Contreras', 'Marketing', 950000, 1, 1),
('Ricardo Soto', 'Cajero', 680000, 1, 1), ('Carolina Vega', 'Vendedor Sala', 720000, 3, 1),
('Miguel Angel Parra', 'Reponedor', 610000, 1, 1), ('Marcela Diaz', 'Gerente Tienda', 2600000, 2, 2),
('Camila Rodriguez', 'Cajero', 650000, 2, 2), ('Andres Lopez', 'Reponedor', 600000, 2, 2),
('Sofia Morales', 'Vendedor Sala', 750000, 2, 2), ('Beatriz Herrera', 'Vendedor Sala', 740000, 2, 2),
('Roberto Nunez', 'Gerente Tienda', 2400000, 3, 3), ('Fernando Gutierrez', 'Reponedor', 580000, 3, 3),
('Daniela Silva', 'Cajero', 660000, 3, 3), ('Jorge Arevalo', 'Vendedor Sala', 710000, 3, 3),
('Valentina Mendez', 'Supervisor', 1200000, 4, 4), ('Esteban Rojas', 'Cajero', 670000, 4, 4),
('Paula Herrera', 'Vendedor Sala', 700000, 4, 4), ('Ignacio Castillo', 'Reponedor', 590000, 1, 4),
('Constanza Bravo', 'Vendedor Sala', 715000, 4, 4), ('Veronica Lagos', 'Gerente Tienda', 2450000, 5, 5),
('Diego Suarez', 'Seguridad', 620000, 5, 5), ('Gabriela Munoz', 'Cajero', 640000, 5, 5),
('Felipe Torres', 'Vendedor Sala', 710000, 5, 5), ('Natalia Guzman', 'Reponedor', 605000, 5, 5),
('Alberto Plaza', 'Gerente Tienda', 2480000, 3, 6), ('Carmen Gloria Arroyo', 'Cajero', 675000, 3, 6),
('Patricio Cornejo', 'Vendedor Sala', 730000, 3, 6), ('Silvia Paredes', 'Vendedor Sala', 725000, 3, 6),
('Benjamin Vicuna', 'Reponedor', 615000, 3, 6);

INSERT INTO venta (id_tienda, fecha_venta, tipo_doc) VALUES
(1, '2018-01-10', 'Boleta'), (2, '2018-01-15', 'Factura'), (3, '2018-02-05', 'Boleta'), (4, '2018-02-20', 'Factura'), (5, '2018-03-12', 'Boleta'),
(6, '2018-03-18', 'Factura'), (1, '2018-04-09', 'Boleta'), (2, '2018-05-14', 'Factura'), (3, '2018-06-04', 'Boleta'), (4, '2018-07-19', 'Factura'),
(5, '2018-08-13', 'Boleta'), (6, '2018-09-17', 'Factura'), (1, '2018-10-08', 'Boleta'), (2, '2018-11-12', 'Factura'), (3, '2018-12-03', 'Boleta'),
(4, '2019-01-21', 'Factura'), (5, '2019-02-11', 'Boleta'), (6, '2019-03-19', 'Factura'), (1, '2019-04-10', 'Boleta'), (2, '2019-05-15', 'Factura'),
(3, '2019-06-05', 'Boleta'), (4, '2019-07-22', 'Factura'), (5, '2019-08-14', 'Boleta'), (6, '2019-09-18', 'Factura'), (1, '2019-10-09', 'Boleta'),
(2, '2019-11-13', 'Factura'), (3, '2019-12-04', 'Boleta'), (4, '2020-01-20', 'Factura'), (5, '2020-02-12', 'Boleta'), (6, '2020-02-14', 'Factura'),
(1, '2020-03-11', 'Boleta'), (2, '2020-04-16', 'Factura'), (3, '2020-05-06', 'Boleta'), (4, '2020-06-23', 'Factura'), (5, '2020-07-15', 'Boleta'),
(6, '2020-08-19', 'Factura'), (1, '2020-09-10', 'Boleta'), (2, '2020-10-14', 'Factura'), (3, '2020-11-05', 'Boleta'), (4, '2020-11-30', 'Boleta'),
(5, '2020-12-16', 'Factura'), (6, '2021-01-21', 'Boleta'), (1, '2021-02-09', 'Factura'), (2, '2021-03-12', 'Boleta'), (3, '2021-04-07', 'Factura'),
(4, '2021-04-18', 'Factura'), (5, '2021-05-17', 'Boleta'), (6, '2021-06-21', 'Factura'), (1, '2021-07-13', 'Boleta'), (2, '2021-08-17', 'Factura'),
(3, '2021-09-08', 'Boleta'), (4, '2021-10-20', 'Factura'), (5, '2021-11-11', 'Boleta'), (6, '2021-12-15', 'Factura'), (1, '2022-01-11', 'Boleta'),
(2, '2022-02-16', 'Factura'), (3, '2022-03-09', 'Boleta'), (4, '2022-04-19', 'Factura'), (5, '2022-05-10', 'Factura'), (6, '2022-06-14', 'Boleta'),
(1, '2022-07-12', 'Factura'), (2, '2022-08-16', 'Boleta'), (3, '2022-09-07', 'Factura'), (4, '2022-10-19', 'Boleta'), (5, '2022-11-09', 'Factura'),
(6, '2022-12-14', 'Boleta'), (1, '2023-01-10', 'Factura'), (2, '2023-02-15', 'Boleta'), (3, '2023-03-08', 'Factura'), (4, '2023-04-18', 'Boleta'),
(5, '2023-05-09', 'Factura'), (6, '2023-06-13', 'Boleta'), (1, '2023-07-11', 'Factura'), (2, '2023-08-15', 'Boleta'), (3, '2023-09-06', 'Factura'),
(4, '2023-10-18', 'Boleta'), (5, '2023-10-31', 'Boleta'), (6, '2023-11-08', 'Factura'), (1, '2023-12-13', 'Boleta'), (2, '2024-01-17', 'Factura'),
(3, '2024-02-07', 'Boleta'), (4, '2024-03-19', 'Factura'), (5, '2024-04-10', 'Boleta'), (6, '2024-05-14', 'Factura'), (1, '2024-06-12', 'Boleta'),
(2, '2024-07-17', 'Factura'), (3, '2024-08-07', 'Boleta'), (4, '2024-09-11', 'Factura'), (5, '2024-09-01', 'Boleta'), (6, '2024-10-16', 'Boleta'),
(1, '2024-11-06', 'Factura'), (2, '2024-12-11', 'Boleta'), (3, '2025-01-15', 'Factura'), (4, '2025-01-15', 'Factura'), (5, '2025-02-05', 'Boleta'); -- Este genera id_venta 95

INSERT INTO detalle_venta (id_venta, id_producto, id_vendedor, cantidad, precio_neto) VALUES
(1, 3, 1, 1, 89990), (2, 1, 2, 1, 899990), (3, 4, 3, 1, 49990), (4, 2, 4, 1, 699990), (5, 6, 5, 10, 7990),
(6, 7, 6, 1, 34990), (7, 8, 7, 2, 29990), (8, 9, 8, 1, 65000), (9, 10, 9, 1, 249990), (10, 11, 1, 1, 45990),
(11, 12, 2, 5, 9990), (12, 13, 3, 3, 12990), (13, 14, 4, 2, 14990), (14, 15, 5, 1, 59990), (15, 16, 6, 1, 449990),
(16, 17, 7, 1, 549990), (17, 18, 8, 1, 199990), (18, 19, 9, 1, 75990), (19, 20, 1, 2, 39990), (20, 21, 2, 1, 69990),
(21, 22, 3, 1, 89990), (22, 23, 4, 2, 19990), (23, 24, 5, 1, 129990), (24, 25, 6, 1, 649990), (25, 26, 7, 3, 15990),
(26, 27, 8, 1, 24990), (27, 28, 9, 1, 29990), (28, 1, 1, 1, 899990), (28, 23, 1, 1, 19990), (29, 2, 2, 1, 699990),
(30, 3, 3, 2, 89990), (30, 12, 3, 4, 9990), (31, 4, 4, 1, 49990), (32, 5, 5, 3, 29990), (33, 6, 6, 12, 7990),
(34, 7, 7, 1, 34990), (35, 8, 8, 4, 29990), (36, 9, 9, 1, 65000), (36, 23, 9, 2, 19990), (37, 10, 1, 1, 249990),
(38, 11, 2, 2, 45990), (39, 12, 3, 10, 9990), (40, 13, 4, 5, 12990), (40, 6, 4, 5, 7990), (41, 14, 5, 1, 14990),
(42, 15, 6, 2, 59990), (43, 16, 7, 1, 449990), (44, 17, 8, 1, 549990), (45, 18, 9, 1, 199990), (45, 26, 9, 2, 15990),
(46, 19, 1, 1, 75990), (47, 20, 2, 1, 39990), (48, 21, 3, 1, 69990), (49, 22, 4, 1, 89990), (50, 23, 5, 3, 19990),
(51, 24, 6, 1, 129990), (52, 25, 7, 1, 649990), (53, 26, 8, 4, 15990), (54, 27, 9, 2, 24990), (55, 28, 1, 1, 29990),
(56, 1, 2, 1, 899990), (57, 2, 3, 1, 699990), (58, 3, 4, 1, 89990), (59, 4, 5, 2, 49990), (60, 5, 6, 4, 29990),
(61, 6, 7, 20, 7990), (62, 7, 8, 1, 34990), (62, 8, 8, 1, 29990), (63, 9, 9, 1, 65000), (64, 10, 1, 1, 249990),
(65, 11, 2, 3, 45990), (66, 12, 3, 15, 9990), (67, 13, 4, 6, 12990), (68, 14, 5, 4, 14990), (69, 15, 6, 2, 59990),
(70, 16, 7, 1, 449990), (71, 17, 8, 1, 549990), (72, 18, 9, 1, 199990), (73, 19, 1, 2, 75990), (74, 20, 2, 3, 39990),
(75, 21, 3, 1, 69990), (76, 22, 4, 2, 89990), (77, 23, 5, 5, 19990), (78, 24, 6, 1, 129990), (79, 25, 7, 1, 649990),
(80, 26, 8, 6, 15990), (81, 27, 9, 2, 24990), (82, 28, 1, 1, 29990), (83, 4, 2, 1, 49990), (84, 8, 3, 3, 29990),
(85, 1, 4, 1, 899990), (86, 12, 5, 8, 9990), (87, 3, 6, 2, 89990), (88, 15, 7, 1, 59990), (89, 6, 8, 10, 7990),
(90, 9, 9, 1, 65000), (91, 2, 1, 1, 699990), (92, 7, 2, 2, 34990), (93, 11, 3, 1, 45990), (94, 5, 4, 4, 29990),
(95, 10, 5, 1, 249990),
(95, 14, 6, 3, 14990),
(95, 18, 7, 1, 199990),
(95, 21, 8, 1, 69990),
(95, 24, 9, 1, 129990),
(95, 27, 1, 1, 24990);

-- Asignación realista de productos a tiendas (productos disponibles por tienda)
INSERT INTO producto_tienda (id_producto, id_tienda) VALUES
-- Tienda 1 (id_tienda=1)
(3,1), (8,1), (28,1), (1,1), (23,1), (14,1), (20,1), (24,1), (27,1),

-- Tienda 2 (id_tienda=2)
(1,2), (9,2), (15,2), (21,2), (2,2), (12,2), (25,2), (7,2), (10,2),

-- Tienda 3 (id_tienda=3)
(4,3), (12,3), (22,3), (3,3), (5,3), (16,3), (6,3), (13,3), (19,3),

-- Tienda 4 (id_tienda=4)
(2,4), (6,4), (13,4), (17,4), (23,4), (1,4), (5,4), (10,4), (24,4),

-- Tienda 5 (id_tienda=5)
(6,5), (18,5), (26,5), (19,5), (4,5), (5,5), (20,5), (21,5), (27,5),

-- Tienda 6 (id_tienda=6)
(7,6), (25,6), (9,6), (3,6), (15,6), (18,6), (12,6), (5,6), (24,6);