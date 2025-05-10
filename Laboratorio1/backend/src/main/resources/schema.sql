-- Drop tables in the correct order to avoid foreign key constraint issues
DROP TABLE IF EXISTS producto_pedido CASCADE;

DROP TABLE IF EXISTS farmacia_repartidor CASCADE;

DROP TABLE IF EXISTS calificacion CASCADE;

DROP TABLE IF EXISTS producto_farmacia CASCADE;

DROP TABLE IF EXISTS detalle_pedido CASCADE;

DROP TABLE IF EXISTS pedido CASCADE;

DROP TABLE IF EXISTS repartidor CASCADE;

DROP TABLE IF EXISTS producto CASCADE;

DROP TABLE IF EXISTS farmacia CASCADE;

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS notificacion CASCADE;

-- This will drop the enum type if it exists and isn't being used
DROP TYPE IF EXISTS estado_pedido CASCADE;

-- Create the enum type
CREATE TYPE estado_pedido AS ENUM (
	'POR_CONFIRMAR',
	'CONFIRMADO',
	'ENTREGADO',
	'CANCELADO'
);

-- Create tables
CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(255) NOT NULL UNIQUE,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	rut VARCHAR(20) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	refresh_token VARCHAR(500),
	refresh_token_expiration BIGINT
);

CREATE TABLE farmacia (
	id_farmacia BIGSERIAL PRIMARY KEY,
	nombre_farmacia VARCHAR(255) NOT NULL,
	direccion TEXT NOT NULL
);

CREATE TABLE producto (
	id_producto BIGSERIAL PRIMARY KEY,
	nombre_producto VARCHAR(255) NOT NULL,
	precio FLOAT NOT NULL,
	categoria VARCHAR(255) NOT NULL,
	requiere_receta BOOLEAN NOT NULL,
	image_url VARCHAR(500)
);

CREATE TABLE pedido (
	id_pedido BIGSERIAL PRIMARY KEY,
	monto INTEGER NOT NULL,
	fecha_pedido TIMESTAMP NOT NULL,
	es_urgente BOOLEAN NOT NULL,
	estado_pedido estado_pedido NOT NULL,
	id_cliente BIGINT REFERENCES users (id) ON DELETE CASCADE,
	id_farmacia BIGINT REFERENCES farmacia (id_farmacia) ON DELETE CASCADE
);

CREATE TABLE detalle_pedido (
	id_detalle_pedido BIGSERIAL PRIMARY KEY,
	id_pedido BIGINT REFERENCES pedido (id_pedido) ON DELETE CASCADE,
	id_repartidor BIGINT,
	metodo_pago VARCHAR(50) NOT NULL,
	fecha_entrega TIMESTAMP
);

CREATE TABLE producto_farmacia (
	id_producto_farmacia BIGSERIAL PRIMARY KEY,
	id_producto BIGINT REFERENCES producto (id_producto) ON DELETE CASCADE,
	id_farmacia BIGINT REFERENCES farmacia (id_farmacia) ON DELETE CASCADE,
	stock_producto BIGINT NOT NULL
);

CREATE TABLE repartidor (
	id_repartidor BIGSERIAL PRIMARY KEY,
	nombre_repartidor VARCHAR(255),
	fecha_contratacion DATE NOT NULL
);

CREATE TABLE calificacion (
	id_calificacion BIGSERIAL PRIMARY KEY,
	id_detalle_pedido BIGINT REFERENCES detalle_pedido (id_detalle_pedido) ON DELETE CASCADE,
	cliente_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
	puntuacion FLOAT NOT NULL
);

CREATE TABLE farmacia_repartidor (
	id_farmacia_repartidor BIGSERIAL PRIMARY KEY,
	id_farmacia BIGINT REFERENCES farmacia (id_farmacia) ON DELETE CASCADE,
	id_repartidor BIGINT REFERENCES repartidor (id_repartidor) ON DELETE CASCADE
);

CREATE TABLE producto_pedido (
	id_producto_pedido BIGSERIAL PRIMARY KEY,
	id_pedido BIGINT REFERENCES pedido (id_pedido) ON DELETE CASCADE,
	id_producto BIGINT REFERENCES producto (id_producto) ON DELETE CASCADE,
	cantidad BIGINT,
	receta_validada BOOLEAN
);

-- notificacion para Trigger 11 - medicamentos que pidan recetas, no validadas.
CREATE TABLE notificacion (
	id_notificacion BIGSERIAL PRIMARY KEY,
	id_pedido BIGINT,
	id_producto BIGINT,
	mensaje TEXT,
	fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_pedido_cliente ON pedido (id_cliente);

CREATE INDEX idx_pedido_farmacia ON pedido (id_farmacia);

CREATE INDEX idx_detalle_pedido_repartidor ON detalle_pedido (id_repartidor);

CREATE INDEX idx_producto_farmacia_stock ON producto_farmacia (stock_producto);
