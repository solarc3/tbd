-- Producto más vendido por mes el 2021.
WITH ventasmensuales AS (SELECT date_trunc('month', venta.fecha_venta) AS mes,
                                detalle_venta.id_producto,
                                SUM(detalle_venta.cantidad)            AS cantidad_total_mes
                         FROM detalle_venta
                                  JOIN venta ON detalle_venta.id_venta = venta.id_venta
                         WHERE EXTRACT(YEAR FROM venta.fecha_venta) = 2021
                         GROUP BY mes, detalle_venta.id_producto)
SELECT TO_CHAR(meses.mes, 'YYYY-MM') AS mes_anio,
       top_producto.id_producto,
       producto.nombre_producto,
       top_producto.cantidad_total_mes
FROM (SELECT DISTINCT mes FROM ventasmensuales) AS meses
         CROSS JOIN LATERAL ( SELECT ventasmensuales.id_producto, ventasmensuales.cantidad_total_mes
                              FROM ventasmensuales
                              WHERE ventasmensuales.mes = meses.mes
                              ORDER BY ventasmensuales.cantidad_total_mes DESC FETCH FIRST 1 ROW
                              WITH TIES ) AS top_producto
         JOIN producto ON top_producto.id_producto = producto.id_producto
ORDER BY meses.mes;

-- Producto más económico por tienda.
SELECT DISTINCT ON (id_tienda) id_tienda, producto.id_producto, nombre_producto, precio
FROM producto_tienda
         INNER JOIN producto ON producto_tienda.id_producto = producto.id_producto
ORDER BY id_tienda, precio;

-- Ventas por mes, separadas entre Boletas y Facturas.
-- https://stackoverflow.com/questions/17492167/group-query-results-by-month-and-year-in-postgresql
SELECT SUM(detalle_venta.cantidad * detalle_venta.precio_neto) as total_mes,
       DATE_TRUNC('month', venta.fecha_venta)                  AS venta_mes,
       venta.tipo_doc
FROM venta
         INNER JOIN detalle_venta ON detalle_venta.id_venta = venta.id_venta
GROUP BY venta_mes, venta.tipo_doc
ORDER BY venta_mes DESC;

-- Empleado que ganó más por tienda en 2020, indicando la comuna donde vive y el cargo que tiene en la empresa.
WITH ranking AS (SELECT empleado.id_empleado,
                        empleado.nombre_empleado,
                        empleado.cargo,
                        empleado.sueldo,
                        empleado.id_tienda,
                        empleado.id_comuna,
                        RANK() OVER (PARTITION BY empleado.id_tienda ORDER BY empleado.sueldo DESC) as ranking_sueldo
                 FROM empleado)
SELECT ranking.id_tienda,
       tienda.direccion,
       ranking.nombre_empleado,
       ranking.cargo,
       ranking.sueldo,
       comuna.nombre_comuna
FROM ranking
         JOIN comuna ON ranking.id_comuna = comuna.id_comuna
         JOIN tienda ON ranking.id_tienda = tienda.id_tienda
WHERE ranking.ranking_sueldo = 1
ORDER BY ranking.id_tienda;

-- La tienda que tiene menos empleados.
SELECT id_tienda, COUNT(*) AS num_empleados
from empleado
GROUP BY id_tienda
ORDER BY num_empleados ASC
LIMIT 1;

-- El vendedor con más ventas por mes.
SELECT DISTINCT ON (mes) DATE_TRUNC('month', v.fecha_venta) AS mes, dv.id_vendedor, COUNT(*) AS total_ventas
FROM detalle_venta dv
         INNER JOIN venta v ON dv.id_venta = v.id_venta
GROUP BY mes, dv.id_vendedor
ORDER BY mes, total_ventas DESC;

-- El vendedor que ha recaudado más dinero para la tienda por año.
SELECT DISTINCT ON (ano, v.id_tienda) DATE_TRUNC('year', v.fecha_venta) AS ano,
                                      v.id_tienda,
                                      dv.id_vendedor,
                                      SUM(dv.precio_neto * dv.cantidad) AS total
FROM detalle_venta dv
         JOIN venta v ON dv.id_venta = v.id_venta
GROUP BY ano, v.id_tienda, dv.id_vendedor
ORDER BY ano, v.id_tienda, total DESC;

-- El vendedor con más productos vendidos por tienda.
WITH totales_ventas AS (SELECT id_tienda, id_vendedor, SUM(cantidad) AS total_vendidos
                        FROM detalle_venta
                                 JOIN venta ON venta.id_venta = detalle_venta.id_venta
                        GROUP BY id_tienda, id_vendedor
                        ORDER BY id_tienda)
SELECT t.id_tienda, t.id_vendedor, t.total_vendidos
FROM totales_ventas t
         JOIN (SELECT id_tienda, MAX(total_vendidos) AS total_vendidos FROM totales_ventas GROUP BY id_tienda) mt
              ON t.id_tienda = mt.id_tienda AND t.total_vendidos = mt.total_vendidos
GROUP BY t.id_tienda, t.id_vendedor, t.total_vendidos
ORDER BY t.id_tienda;

-- El empleado con mayor sueldo por mes.
-- Se asume que el sueldo es constante cada mes
SELECT e.nombre_empleado, e.sueldo
FROM empleado AS e
ORDER BY sueldo DESC
LIMIT 1;

-- La tienda con menor recaudación por mes.
SELECT DISTINCT ON (mes) DATE_TRUNC('month', v.fecha_venta) AS mes,
                         v.id_tienda,
                         SUM(dv.precio_neto * dv.cantidad)  AS total
FROM detalle_venta dv
         JOIN venta v ON dv.id_venta = v.id_venta
GROUP BY mes, v.id_tienda
ORDER BY mes, total ASC;