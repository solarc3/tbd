-- Updated INSERT statement for users, now including location, refresh_token, and refresh_token_expiration
INSERT INTO users (username, email, password, first_name, last_name, rut, location, refresh_token, refresh_token_expiration) VALUES
('cliente1', 'cliente1@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Juan', 'González', '12.345.678-9', ST_SetSRID(ST_MakePoint(-70.6500, -33.4500), 4326), 'tokenCliente1Refresh', EXTRACT(EPOCH FROM TIMESTAMP '2025-06-01 00:00:00') * 1000),
('cliente2', 'cliente2@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'María', 'Rodríguez', '11.987.654-3', NULL, NULL, NULL),
('cliente3', 'cliente3@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Carlos', 'Pérez', '15.678.901-2', ST_SetSRID(ST_MakePoint(-70.6400, -33.4400), 4326), 'tokenCliente3Refresh', EXTRACT(EPOCH FROM TIMESTAMP '2025-06-05 00:00:00') * 1000),
('cliente4', 'cliente4@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Ana', 'López', '17.432.765-K', NULL, 'tokenCliente4Refresh', EXTRACT(EPOCH FROM TIMESTAMP '2025-06-10 00:00:00') * 1000),
('cliente5', 'cliente5@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Diego', 'Martínez', '14.765.238-5', ST_SetSRID(ST_MakePoint(-70.6600, -33.4600), 4326), NULL, NULL),
('cliente6', 'cliente6@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Valentina', 'Hernández', '16.543.219-8', ST_SetSRID(ST_MakePoint(-70.6250, -33.4250), 4326), 'tokenCliente6Refresh', EXTRACT(EPOCH FROM TIMESTAMP '2025-06-15 00:00:00') * 1000),
('cliente7', 'cliente7@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Sebastián', 'Torres', '13.987.654-1', NULL, NULL, NULL),
('cliente8', 'cliente8@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Camila', 'Sánchez', '18.234.567-7', ST_SetSRID(ST_MakePoint(-70.6050, -33.4050), 4326), 'tokenCliente8Refresh', EXTRACT(EPOCH FROM TIMESTAMP '2025-06-20 00:00:00') * 1000),
('cliente9', 'cliente9@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Matías', 'Fernández', '19.876.543-4', NULL, NULL, NULL),
('cliente10', 'cliente10@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Javiera', 'Muñoz', '10.543.876-6', ST_SetSRID(ST_MakePoint(-70.6555, -33.4555), 4326), 'tokenCliente10Refresh', EXTRACT(EPOCH FROM TIMESTAMP '2025-06-25 00:00:00') * 1000);

-- Existing Sector Data (ensure this is present before running new tarea inserts that might reference these)
INSERT INTO sector (nombre, area) VALUES
('Zona 1', ST_GeogFromText('SRID=4326;POLYGON((-70.65 -33.45, -70.66 -33.45, -70.66 -33.46, -70.65 -33.46, -70.65 -33.45))')),
('Zona 2',  ST_GeogFromText('SRID=4326;POLYGON((-70.64 -33.44, -70.65 -33.44, -70.65 -33.45, -70.64 -33.45, -70.64 -33.44))')),
('Zona 3',  ST_GeogFromText('SRID=4326;POLYGON((-70.66 -33.46, -70.67 -33.46, -70.67 -33.47, -70.66 -33.47, -70.66 -33.46))')),
('Zona 4', ST_GeogFromText('SRID=4326;POLYGON((-70.62 -33.42, -70.63 -33.42, -70.63 -33.43, -70.62 -33.43, -70.62 -33.42))')),
('Zona 5',  ST_GeogFromText('SRID=4326;POLYGON((-70.60 -33.40, -70.61 -33.40, -70.61 -33.41, -70.60 -33.41, -70.60 -33.40))'));

-- Updated INSERT statement for tareas, now including id_sector
-- Assuming sector IDs 1-5 exist from the initial data.
INSERT INTO tareas (titulo, descripcion, fecha_vencimiento, id_usuario, id_sector, estado) VALUES
('Completar informe mensual', 'Elaborar el informe de ventas del mes de abril', '2025-05-25 12:00:00', 1, 1, 'PENDIENTE'),
('Reunión con cliente', 'Reunión con el cliente para discutir nuevos requerimientos', '2025-05-22 15:30:00', 2, 2, 'PENDIENTE'),
('Preparar presentación', 'Preparar presentación para la junta directiva', '2025-05-23 09:00:00', 3, 3, 'EN_PROGRESO'),
('Actualizar documentación', 'Actualizar la documentación del proyecto XYZ', '2025-05-27 17:00:00', 4, 4, 'PENDIENTE'),
('Revisar código', 'Revisar el código de la última funcionalidad implementada', '2025-05-21 14:00:00', 5, 5, 'COMPLETADA'),
('Enviar propuesta', 'Enviar propuesta comercial al cliente potencial', '2025-05-24 11:00:00', 1, 1, 'PENDIENTE'),
('Configurar entorno de desarrollo', 'Configurar el nuevo entorno de desarrollo para el equipo', '2025-05-26 10:00:00', 2, 2, 'EN_PROGRESO'),
('Reunión de equipo', 'Reunión semanal con el equipo de desarrollo', '2025-05-20 16:00:00', 3, 3, 'COMPLETADA'),
('Resolver tickets pendientes', 'Resolver los tickets pendientes en el sistema de seguimiento', '2025-05-28 13:00:00', 4, 4, 'PENDIENTE'),
('Investigar nueva tecnología', 'Investigar sobre la nueva tecnología para posible implementación', '2025-05-29 09:30:00', 5, 5, 'PENDIENTE');