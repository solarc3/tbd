# Cambios Propuestos - Control 1

## Tabla `sueldo`
- Se elimina la tabla sueldo en función de ser un atributo para los empleados
- Se asumió que el sueldo es constante.

## Tabla `producto_tienda`
- Se agrega esta tabla en función de responder la consulta de productos más económicos por tienda
- Cómo estaba planteado inicialmente, no se podía responder puesto que:
  - Si no se vendía el producto, no habría manera de saber si este pertenece a una tienda.
  - **Solución**: Una tienda tiene muchos productos, y un producto puede estar en varias tiendas, por lo que **se genera tabla intermedia.**

## Tabla `detalle_venta`
- En un principio, seguía el modelo inicial de `prod_venta`, pero de igual forma actúa cómo tabla intermedia para satisfacer relación muchos a muchos.
- Este además posee atributos distintivos, separándolo de la tabla `venta`.

## Tabla `tipo_doc`
- Cómo grupo, decidimos que este podría ser fácilmente un atributo de esta tabla, además solo se utiliza para una sentencia puntual.

## Tabla `tienda_emp`
- asd