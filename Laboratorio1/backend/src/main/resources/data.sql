-- Insertar 10 clientes (users)
INSERT INTO users (username, email, password) VALUES
('cliente1', 'cliente1@example.com', 'password1'),
('cliente2', 'cliente2@example.com', 'password2'),
('cliente3', 'cliente3@example.com', 'password3'),
('cliente4', 'cliente4@example.com', 'password4'),
('cliente5', 'cliente5@example.com', 'password5'),
('cliente6', 'cliente6@example.com', 'password6'),
('cliente7', 'cliente7@example.com', 'password7'),
('cliente8', 'cliente8@example.com', 'password8'),
('cliente9', 'cliente9@example.com', 'password9'),
('cliente10', 'cliente10@example.com', 'password10');

-- Insertar 5 farmacias
INSERT INTO farmacia (nombre_farmacia, direccion) VALUES
('Farmacia Salud', 'Av. Principal 123'),
('Farmacia Vida', 'Calle Secundaria 456'),
('Farmacia Bienestar', 'Av. Libertad 789'),
('Farmacia Esperanza', 'Calle Paz 101'),
('Farmacia Sol', 'Av. Luz 202');

-- Insertar 20 productos (algunos requieren receta)
INSERT INTO producto (nombre_producto, precio, categoria, requiere_receta) VALUES
('Paracetamol', 5.99, 'Analgésico', false),
('Ibuprofeno', 7.50, 'Antiinflamatorio', false),
('Amoxicilina', 15.00, 'Antibiótico', true),
('Omeprazol', 12.75, 'Gastrointestinal', false),
('Loratadina', 8.30, 'Antialérgico', false),
('Insulina', 45.90, 'Diabetes', true),
('Aspirina', 4.99, 'Analgésico', false),
('Diazepam', 20.00, 'Psiquiátrico', true),
('Vitamina C', 9.99, 'Suplemento', false),
('Simvastatina', 18.40, 'Cardiovascular', true),
('Antialérgico X', 11.20, 'Antialérgico', false),
('Mascarillas', 2.50, 'Protección', false),
('Termómetro', 14.95, 'Monitoreo', false),
('Curitas', 3.00, 'Primeros Auxilios', false),
('Jarabe para la tos', 6.80, 'Respiratorio', false),
('Crema Hidratante', 10.50, 'Dermatológico', false),
('Protector Solar', 13.75, 'Dermatológico', false),
('Tiras Reactivas', 22.00, 'Diabetes', true),
('Morfina', 30.00, 'Analgésico', true),
('Antidepresivo Y', 25.60, 'Psiquiátrico', true);

-- Insertar 5 repartidores
INSERT INTO repartidor (nombre_repartidor, fecha_contratacion) VALUES
('Juan Pérez', '2022-01-15'),
('María García', '2022-03-10'),
('Carlos López', '2022-05-20'),
('Ana Martínez', '2022-07-05'),
('Luis Rodríguez', '2022-09-12');

-- Asignar repartidores a farmacias (farmacia_repartidor)
INSERT INTO farmacia_repartidor (id_farmacia, id_repartidor) VALUES
(1, 1), (1, 2),
(2, 3), (2, 4),
(3, 5), (3, 1),
(4, 2), (4, 3),
(5, 4), (5, 5);

-- Insertar stock de productos en farmacias (producto_farmacia)
-- Asignar 5 productos por farmacia con stock aleatorio
INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto) VALUES
(1, 1, 100), (2, 1, 50), (3, 1, 30), (4, 1, 80), (5, 1, 60),
(6, 2, 40), (7, 2, 90), (8, 2, 20), (9, 2, 70), (10, 2, 25),
(11, 3, 55), (12, 3, 200), (13, 3, 35), (14, 3, 150), (15, 3, 45),
(16, 4, 75), (17, 4, 40), (18, 4, 30), (19, 4, 15), (20, 4, 10),
(1, 5, 80), (3, 5, 25), (5, 5, 50), (7, 5, 60), (9, 5, 30);

-- Insertar 20 pedidos (pedido)
INSERT INTO pedido (monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia) VALUES
(45, '2023-04-10 08:30:00', false, 'ENTREGADO', 1, 1),
(32, '2023-04-15 14:15:00', true, 'CONFIRMADO', 2, 2),
(67, '2023-04-20 10:00:00', false, 'CANCELADO', 3, 3),
(28, '2023-05-01 09:45:00', true, 'ENTREGADO', 4, 4),
(90, '2023-05-05 16:20:00', false, 'POR_CONFIRMAR', 5, 5),
(15, '2023-05-10 11:10:00', true, 'ENTREGADO', 6, 1),
(50, '2023-05-15 12:30:00', false, 'CONFIRMADO', 7, 2),
(22, '2023-05-20 17:00:00', true, 'CANCELADO', 8, 1),
(38, '2023-05-25 08:00:00', false, 'ENTREGADO', 9, 4),
(80, '2023-06-01 13:45:00', true, 'POR_CONFIRMAR', 10, 5),
(42, '2023-06-05 10:20:00', false, 'ENTREGADO', 1, 1),
(29, '2023-06-10 15:30:00', true, 'CONFIRMADO', 2, 2),
(55, '2023-06-15 09:00:00', false, 'CANCELADO', 3, 3),
(33, '2023-06-20 14:00:00', true, 'ENTREGADO', 4, 4),
(70, '2023-06-25 11:50:00', false, 'POR_CONFIRMAR', 5, 5),
(18, '2023-07-01 16:10:00', true, 'ENTREGADO', 6, 1),
(47, '2023-07-05 08:45:00', false, 'CONFIRMADO', 7, 2),
(25, '2023-07-10 12:15:00', true, 'CANCELADO', 8, 3),
(60, '2023-07-15 09:30:00', false, 'ENTREGADO', 9, 4),
(85, '2023-07-20 14:50:00', true, 'POR_CONFIRMAR', 10, 5),
(50, '2025-04-15 10:00:00', false, 'ENTREGADO', 1, 1),
(60, '2025-04-20 11:00:00', true, 'ENTREGADO', 2, 2),
(70, '2025-05-10 09:00:00', false, 'ENTREGADO', 3, 3),
(80, '2025-05-15 14:00:00', true, 'ENTREGADO', 4, 4),
(90, '2025-05-20 16:00:00', false, 'ENTREGADO', 5, 5),
(45, '2025-05-25 12:00:00', true, 'ENTREGADO', 6, 1),
(55, '2025-05-28 13:00:00', false, 'ENTREGADO', 7, 2);

-- Insertar detalles de pedido (detalle_pedido)
INSERT INTO detalle_pedido (id_pedido, id_repartidor, metodo_pago, fecha_entrega) VALUES
(1, 1, 'Tarjeta', '2023-04-11 10:30:00'),
(2, 3, 'Efectivo', '2023-04-16 14:45:00'),
(4, 4, 'Tarjeta', '2023-05-02 09:00:00'),
(6, 2, 'Efectivo', '2023-05-11 16:20:00'),
(7, 5, 'Transferencia', '2023-05-16 11:15:00'),
(9, 1, 'Tarjeta', '2023-05-26 18:10:00'),
(11, 2, 'Efectivo', '2023-06-06 08:30:00'),
(13, 3, 'Transferencia', '2023-06-16 12:00:00'),
(14, 4, 'Tarjeta', '2023-06-21 17:05:00'),
(16, 5, 'Efectivo', '2023-07-02 15:40:00'),
(17, 1, 'Transferencia', '2023-07-06 13:25:00'),
(19, 3, 'Tarjeta', '2023-07-16 20:00:00');


-- Insertar productos en pedidos (producto_pedido)
-- Cada pedido tiene 1-3 productos
INSERT INTO producto_pedido (id_pedido, id_producto) VALUES
(1, 1), (1, 2), (1, 3),
(2, 4), (2, 5),
(3, 6), (3, 7),
(4, 8), (4, 9), (4, 10),
(5, 11), (5, 12),
(6, 13), (6, 14),
(7, 15), (7, 16),
(8, 17), (8, 18),
(9, 19), (9, 20),
(10, 1), (10, 2),
(11, 3), (11, 4), (11, 5),
(12, 6), (12, 7),
(13, 8), (13, 9),
(14, 10), (14, 11), (14, 12),
(15, 13), (15, 14),
(16, 15), (16, 16),
(17, 17), (17, 18),
(18, 19), (18, 20),
(19, 1), (19, 3), (19, 5),
(20, 7), (20, 9),
-- April 2025 pedidos (id 21-22)
(21, 1),   -- Paracetamol (Analgésico)
(22, 2),   -- Ibuprofeno (Antiinflamatorio)
-- May 2025 pedidos (id 23-27)
(23, 1), (23, 7),                  -- Paracetamol + Aspirina (Analgésico)
(24, 2), (24, 2),                  -- Ibuprofeno x2 (Antiinflamatorio)
(25, 3),                            -- Amoxicilina (Antibiótico)
(26, 1), (26, 1), (26, 1),         -- Paracetamol x3 (Analgésico)
(27, 2);                            -- Ibuprofeno (Antiinflamatorio)

-- Insertar calificaciones (solo para pedidos entregados)
-- Insertar calificaciones (solo para pedidos entregados y con detalles existentes)
INSERT INTO calificacion (id_detalle_pedido, cliente_id, puntuacion) VALUES
(1, 1, 4.5),    -- DetallePedido 1 (Pedido 1)
(2, 2, 3.8),    -- DetallePedido 2 (Pedido 2)
(4, 4, 4.2),    -- DetallePedido 4 (Pedido 4)
(6, 6, 4.7),    -- DetallePedido 6 (Pedido 6)
(7, 7, 4.0),    -- DetallePedido 7 (Pedido 7)
(9, 9, 4.9),    -- DetallePedido 9 (Pedido 9)
(11, 1, 4.1),   -- DetallePedido 11 (Pedido 11)
(12, 4, 3.5),   -- DetallePedido 12 (Pedido 14) **Corregido de 14→12**
(10, 6, 4.8),   -- DetallePedido 10 (Pedido 16) **Corregido de 16→10**
(5, 7, 4.3),    -- DetallePedido 5 (Pedido 5) **Nueva fila válida**
(8, 9, 5.0);    -- DetallePedido 8 (Pedido 13) **Nueva fila válida**