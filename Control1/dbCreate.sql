DROP TABLE IF EXISTS detalle_venta CASCADE;
DROP TABLE IF EXISTS producto_tienda CASCADE;
DROP TABLE IF EXISTS venta CASCADE;
DROP TABLE IF EXISTS empleado CASCADE;
DROP TABLE IF EXISTS tienda CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS vendedor CASCADE;
DROP TABLE IF EXISTS comuna CASCADE;


CREATE TABLE IF NOT EXISTS comuna (
    id_comuna SERIAL,
    nombre_comuna VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_comuna)
);

CREATE TABLE IF NOT EXISTS vendedor(
    id_vendedor SERIAL,
    nombre_vendedor VARCHAR(150) NOT NULL,
    PRIMARY KEY (id_vendedor)
);

CREATE TABLE IF NOT EXISTS producto (
    id_producto SERIAL,
    nombre_producto VARCHAR(150) NOT NULL,
    precio BIGINT NOT NULL CHECK (precio > 0),
    categoria VARCHAR(50) NOT NULL,
    stock INTEGER CHECK (stock > 0),
    PRIMARY KEY (id_producto)
);

CREATE TABLE IF NOT EXISTS tienda(
    id_tienda SERIAL,
    direccion VARCHAR(100),
    id_comuna INT NOT NULL,
    PRIMARY KEY (id_tienda),
    FOREIGN KEY (id_comuna) REFERENCES comuna(id_comuna)
);

CREATE TABLE IF NOT EXISTS empleado(
    id_empleado SERIAL,
    nombre_empleado VARCHAR(200) NOT NULL,
    cargo VARCHAR(50),
    sueldo BIGINT CHECK (sueldo >= 0),
    id_comuna INT NOT NULL,
    id_tienda INT NOT NULL,
    PRIMARY KEY (id_empleado),
    FOREIGN KEY (id_comuna) REFERENCES comuna(id_comuna),
    FOREIGN KEY (id_tienda) REFERENCES tienda(id_tienda)
);

CREATE TABLE IF NOT EXISTS venta(
    id_venta SERIAL,
    id_tienda INT NOT NULL,
    fecha_venta DATE NOT NULL,
    tipo_doc VARCHAR(50) NOT NULL CHECK (tipo_doc IN ('Boleta', 'Factura')),
    PRIMARY KEY (id_venta),
    FOREIGN KEY (id_tienda) REFERENCES tienda(id_tienda)
);

CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle_venta SERIAL,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    id_vendedor INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_neto BIGINT NOT NULL CHECK (precio_neto > 0),
    PRIMARY KEY (id_detalle_venta),
    FOREIGN KEY (id_venta) REFERENCES venta(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_vendedor) REFERENCES vendedor(id_vendedor)
);

CREATE TABLE IF NOT EXISTS producto_tienda (
    id_producto_tienda SERIAL,
    id_producto SERIAL,
    id_tienda SERIAL,
    PRIMARY KEY (id_producto_tienda),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_tienda) REFERENCES tienda(id_tienda)
);