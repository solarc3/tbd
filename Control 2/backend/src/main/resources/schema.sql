DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS tareas CASCADE;
DROP TABLE IF EXISTS sector CASCADE;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    rut VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(500),
    refresh_token_expiration BIGINT,
    location GEOMETRY(Point, 4326)
);

CREATE TABLE sector (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    area GEOGRAPHY(POLYGON, 4326)
);

CREATE TABLE tareas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_vencimiento TIMESTAMP,
    id_usuario BIGINT REFERENCES users(id),
    id_sector BIGINT REFERENCES sector(id),
    estado VARCHAR(50) DEFAULT 'PENDIENTE'
);

CREATE INDEX idx_tarea_usuario ON tareas (id_usuario);