-- Replace the existing users INSERT statement with this updated version
INSERT INTO users (username, email, password, first_name, last_name, rut) VALUES
('cliente1', 'cliente1@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Juan', 'González', '12.345.678-9'),
('cliente2', 'cliente2@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'María', 'Rodríguez', '11.987.654-3'),
('cliente3', 'cliente3@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Carlos', 'Pérez', '15.678.901-2'),
('cliente4', 'cliente4@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Ana', 'López', '17.432.765-K'),
('cliente5', 'cliente5@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Diego', 'Martínez', '14.765.238-5'),
('cliente6', 'cliente6@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Valentina', 'Hernández', '16.543.219-8'),
('cliente7', 'cliente7@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Sebastián', 'Torres', '13.987.654-1'),
('cliente8', 'cliente8@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Camila', 'Sánchez', '18.234.567-7'),
('cliente9', 'cliente9@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Matías', 'Fernández', '19.876.543-4'),
('cliente10', 'cliente10@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Javiera', 'Muñoz', '10.543.876-6');

INSERT INTO tareas (titulo, descripcion, fecha_vencimiento, id_usuario, estado) VALUES
                                                                                    ('Completar informe mensual', 'Elaborar el informe de ventas del mes de abril', '2025-05-25 12:00:00', 1, 'PENDIENTE'),
                                                                                    ('Reunión con cliente', 'Reunión con el cliente para discutir nuevos requerimientos', '2025-05-22 15:30:00', 2, 'PENDIENTE'),
                                                                                    ('Preparar presentación', 'Preparar presentación para la junta directiva', '2025-05-23 09:00:00', 3, 'EN_PROGRESO'),
                                                                                    ('Actualizar documentación', 'Actualizar la documentación del proyecto XYZ', '2025-05-27 17:00:00', 4, 'PENDIENTE'),
                                                                                    ('Revisar código', 'Revisar el código de la última funcionalidad implementada', '2025-05-21 14:00:00', 5, 'COMPLETADA'),
                                                                                    ('Enviar propuesta', 'Enviar propuesta comercial al cliente potencial', '2025-05-24 11:00:00', 1, 'PENDIENTE'),
                                                                                    ('Configurar entorno de desarrollo', 'Configurar el nuevo entorno de desarrollo para el equipo', '2025-05-26 10:00:00', 2, 'EN_PROGRESO'),
                                                                                    ('Reunión de equipo', 'Reunión semanal con el equipo de desarrollo', '2025-05-20 16:00:00', 3, 'COMPLETADA'),
                                                                                    ('Resolver tickets pendientes', 'Resolver los tickets pendientes en el sistema de seguimiento', '2025-05-28 13:00:00', 4, 'PENDIENTE'),
                                                                                    ('Investigar nueva tecnología', 'Investigar sobre la nueva tecnología para posible implementación', '2025-05-29 09:30:00', 5, 'PENDIENTE');