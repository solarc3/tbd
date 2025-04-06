# Control 1

## Modificaciones al MER

Las modificaciones al mer se encuentran justificadas en [CambiosPropuestos.md](https://github.com/solarc3/tbd/blob/main/Control1/CambiosPropuestos.md) y el [Diagrama](https://github.com/solarc3/tbd/blob/main/Control1/Diagrama_MR_Grupo_1.png). 
## Ejecutar mediante consola `psql`
### Pasos a seguir
- Ingresar a la consola de postgres mediante `psql`, cuidadando que estén bien puestas las variables de entoerno.
- Dentro de la consola de psql crear una base de datos usando `CREATE DATABASE db_name;`, el nombre no importa para el uso.
- Conectarse a la base de datos creada utilizando `\c db_name`.

#### Ejemplo de ejecucion:
```
> postgres=# create database dbtest;
CREATE DATABASE
> postgres=# \c dbtest
You are now connected to database "dbtest" as user "postgres".
```
- Al estar dentro de la base de datos, para crear las tablas se puede copiar y pegar todo el contenido de `dbCreate.sql` en la terminal de `psql`.
    - Se puede verificar el ingreso correcto de las tablas por medio del comando `\dt`, este listará todas las tablas añadidas.
- Para agregar el contenido de prueba se puede tambien copiar y pegar todo el contenido de `loadData.sql` en la terminal de `psql`.
  - Para verificar el correcto registro de los datos, se puede tomar una tabla de ejemplo y observar su contenido con la sentencia `TABLE atribute;`, por ejemplo: `TABLE comuna;`.

Con esto ya se tendran las tablas con sus contenidos, para empezar a probar las sentencias se puede ir copiando desde `runStatements.sql` **cada una *por separado*** ya que algunas sentencias generan tablas que interrumpen la ejecucion. En el caso que se genere una tabla muy grande se puede salir del menu utilizando la tecla `q`.

> [!IMPORTANT] 
> Ya que las sentencias son multi-lineas, se agregaron espacios para separar, es importante que dentro de la ejecucion la consola cambie su icono de `=` a `-`, esto implica que la query se volvio multi-linea, en el caso que no suceda esto (que el icono se mantenga como `=` en distintas lineas de una misma query), se debe revisar la configuracion de como tratar los espacios al copiar y pegar en una terminal. Fue probado exitosamente en `bash`, `zsh` y `alacritty`con `powershell` en windows. 

**Ejemplo de ejecucion de query:**

```
entrega1=# SELECT DISTINCT ON (id_tienda) id_tienda, producto.id_producto, nombre_producto, precio
entrega1-#
entrega1-# FROM producto_tienda
entrega1-#
entrega1-#          INNER JOIN producto ON producto_tienda.id_producto = producto.id_producto
entrega1-#
entrega1-# ORDER BY id_tienda, precio;
 id_tienda | id_producto |      nombre_producto       | precio
-----------+-------------+----------------------------+--------
         1 |          14 | Libro Cien Anos de Soledad |  14990
         2 |          12 | Polera Algodon Basica      |   9990
         3 |           6 | Vino Casillero del Diablo  |   7990
         4 |           6 | Vino Casillero del Diablo  |   7990
         5 |           6 | Vino Casillero del Diablo  |   7990
         6 |          12 | Polera Algodon Basica      |   9990
(6 rows)

entrega1=#
```

