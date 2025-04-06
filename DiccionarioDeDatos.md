# Diccionario de Datos
## Venta
> Representa un documento de respaldo generado tras efectuar una venta.
### Atributos
- `id_venta` (**serial**): Identificador de la venta.
- `id_tienda` (**serial**): Identificador de la tienda en la que se efectuó la venta.
- `fecha_venta` (**date**): Año, mes, y día en el que se efectuó la venta.
- `tipo_doc` (**varchar**): Tipo de documento (`'Boleta'` o `'Factura'`)

## Vendedor
> Representa a un tipo de empleado en particular; aquellos encargados de las ventas en la tienda.
### Atributos
- `id_vendedor` (**serial**): Identificador del empleado vendedor.
- `nombre_vendedor` (**varchar**): Nombre y apellido del empleado vendedor.

## Comuna
> Representa una comuna del territorio chileno.
> Como la Tienda y Empleado están asociados a una comuna mediante su ID,
> se mantiene la consistencia de este dato (por ejemplo, si una comuna fuera renombrada)
### Atributos
- `id_comuna` (**serial**): Identificador de la comuna.
- `nombre_comuna` (**varchar**): Nombre de la comuna.

## Tienda
> Representa un cierto local de la tienda en cuestión, ubicado en cierta comuna.
### Atributos
- `id_tienda` (**serial**): Identificador de la tienda.
- `direccion` (**varchar**): Dirección del local (calle y número).
- `id_comuna` (**serial**): Identificador de la comuna en la que está el local.

## Empleado
> El empleado, a diferencia del vendedor, cumple distintas funciones.
> Este tiene una asociación directa con la tienda física.
### Atributos
- `id_empleado` (**serial**): Identificador único de cada usuario.
- `nombre` (**varchar**): El nombre registrado del empleado.
- `cargo` (**varchar**): Corresponde al cargo que se le asigna en la tienda.
- `sueldo` (**float**): El salario total correspondiente a un solo cliente, cómo es una relación 1 a 1, conviene dejarlo cómo atributo.
- `id_tienda` (**serial**): Llave foranea, una tienda tiene varios empleados y un empleado es asociado con una sola tienda.
- `id_comuna` (**serial**): Llave foranea, una comuna tiene varios empleados y un empleado solo una comuna.

## Prod-Venta
> **Tabla intermedia:**  Tiene por finalidad ser un detalle de venta
> "puentea" los productos con las ventas, cómo una venta puede tener 
> Muchos productos y un producto puede aparecer en muchas ventas.
### Atributos
- `id_prod_venta` (**serial**): Identificador único.
- `cantidad` (**int**): Cantidad neto de productos por cada venta asociado a la venta y los productos.
- `precio_neto` (**float**): Precio final de la venta.
- `id_tienda` (**serial**): Llave foranea, cómo actua de tabla intermedia, se requiere guardar la llave foranea.
- `id_prod` (**serial**): Llave foranea, cómo actua de tabla intermedia, se requiere guardar la llave foranea.

## Producto
> Un producto es la unidad más básica en una tienda comercial.
> Corresponde a lo que se pretende comerciar, puede ser cualquier
> objeto que se encuentre actualmente en venta.

### Atributos
- `id_producto` (**serial**): Llave única para cada producto.
- `nombre_producto` (**varchar**): Nombre asociado al producto.
- `precio` (**float > 0**): Precio asociado al producto unitario, debe ser mayor o igual que 0.
- `categoria` (**varchar**): Criterio que caracteriza o agrupa productos con fines o características en común.
- `stock` (**int >= 0**): Cantidad de productos actualmente.