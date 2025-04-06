-- Producto más vendido por mes el 2021.
Select count(id_producto) FROM detalle_venta GROUP BY id_producto;

-- Producto más económico por tienda.
SELECT DISTINCT ON (id_tienda) * FROM producto_tienda INNER JOIN producto ON producto_tienda.id_producto = producto.id_producto ORDER BY id_tienda, precio;

-- Ventas por mes, separadas entre Boletas y Facturas.
-- https://stackoverflow.com/questions/17492167/group-query-results-by-month-and-year-in-postgresql
SELECT SUM(detalle_venta.precio_neto), date_trunc('month', venta.fecha_venta) as venta_mes,venta.tipo_doc FROM venta INNER JOIN detalle_venta ON detalle_venta.id_venta = venta.id_venta group by venta_mes, venta.tipo_doc ORDER BY venta_mes DESC;

-- Empleado que ganó más por tienda en 2020, indicando la comuna donde vive y el cargo que tiene en la empresa.
SELECT max(sueldo) FROM empleado INNER JOIN tienda ON empleado.id_tienda = tienda.id_tienda GROUP BY nombre_empleado, empleado.id_comuna;

-- La tienda que tiene menos empleados.
with conteoxtienda as (select id_tienda, count(*) as num_empleados from empleado group by id_tienda) SELECT id_tienda, num_empleados FROM conteoxtienda where num_empleados=(SELECT MIN(num_empleados) FROM conteoxtienda);

select id_tienda, count(*) as num_empleados from empleado group by id_tienda order by num_empleados ASC limit 1;

-- El vendedor con más ventas por mes.
--select MAX(max_vendedor.maximo) FROM (
select detalle_venta.id_vendedor,count(detalle_venta.id_vendedor) AS maximo, date_trunc('month', venta.fecha_venta) FROM detalle_venta GROUP BY id_vendedor;-- max_vendedor;

-- El vendedor que ha recaudado más dinero para la tienda por año.


-- El vendedor con más productos vendidos por tienda.
select id_tienda, id_vendedor from (select count(detalle_venta.id_vendedor) from detalle_venta) as dvc;

-- El empleado con mayor sueldo por mes.
-- CAGAZO: el sueldo depende del tiempo

-- La tienda con menor recaudación por mes.
