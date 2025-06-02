
# Control 2 - Bases de Datos Avanzadas

## Pasos a seguir para ejecutar

Existen dos formas de hacer funcionar nuestra app.

### 1) Usando Docker Compose para todas las componentes

#### Prerequisitos
- **Docker Desktop/Engine** -> Docker/Docker Desktop y Docker Compose

Dirigiendose a la carpeta raíz `Control2`, se abre la terminal en linux o nos dirigimos al directorio desde 
Docker Desktop, se procede a escribir ```sudo docker compose up``` o ```docker compose up``` respectivamente.

#### Se procede a esperar a que termine la ejecución y nos dirigimos a ```http://localhost:3000/```.

#### Posibles Errores durante la ejecución:
- **Componentes no cargan:** En caso de que se haya ejecutado por Docker Compose y no se estén cargando apropiadamente los componentes dentro del frontend, se debe de recargar la página.

### 2) Usando Docker Compose para la Base de Datos y el resto manualmente

#### Prerequisitos
- **Docker Desktop/Engine** -> Docker/Docker Desktop y Docker Compose
- **IntelliJ**: Java 17
- **Node**: Node Package Manager (npm)

> Se ejecutan 3 componentes en simultaneo

#### Base de Datos
- En una terminal dentro del directorio `Control 2/backend`, levantar el correspondiente `docker-compose.yaml` con el comando
```docker compose up -d```
- El contenedor de PostrgeSQL quedaría levantado y listo para uso.

**Posibles errores**:
- **Puerto ya utilizado:** Es posible que el puerto esté ya esté utilizado por la actual instalación de PostgreSQL localmente, por lo que dependiendo del sistema operativo se debe matar el proceso que esté escuchando en ese puerto. En este caso seguramente este chocando en el puerto 5432 por lo que el siguiente comando (en linux) resuelve el error
``` sudo systemctl stop postgresql ```.
- **No se encuentran las variables de entorno:** Es posible que no se encuentre en la carpeta backend el archivo `.env`, que contiene los parámetros para la base de datos. En ese caso, se debe de crear el archivo e ingresar los siguientes parámetros:
```
POSTGRES_USER:postgres
POSTGRES_PASSWORD:postgres
POSTGRES_DB:tbd_control2
```

#### Backend
- Desde IntelliJ, abrir la carpeta dentro del proyecto `Control 2/backend`.
- **IMPORTANTE**: Cómo se configuró inicialmente para correr con Docker Compose, se debe de modificar el archivo `application.properties`, puesto que el URL datasource de Spring originalmente debería ser `spring.datasource.url=jdbc:postgresql://localhost:5432/tbd_control2`, por lo que application properties debería ser:
```
# ...
spring.datasource.url=jdbc:postgresql://localhost:5432/tbd_control2
spring.datasource.username=postgres
spring.datasource.password=postgres
# ...
```
- En su defecto, se puede obtener de VCS Git con el énlace corresponediente al repositorio (`https://github.com/solarc3/tbd.git`).
- Hacer click en el botón de "play", osease "Run TbdLab1Application", o Shift + F10.

#### Frontend
- Desde una terminal, acceder al directorio `Control 2/frontend`.
- En este, ingresar el comando para instalar los paquetes y dependencias:
```npm install```
- Finalmente, abrir el proyecto en modo developer con el comando:
```npm run dev```

### Si se siguieron correctamente los pasos en el órden establecido, la aplicación será accesible desde cualquier navegador con el URL `http://localhost:3000`

---

## Funcionalidades

### Registro de Usuarios

> *"Los usuarios deben poder registrarse en el sistema utilizando un nombre de usuario
y una contraseña. Además, deben ingresar su dirección geográfica, guardando un
punto geoespacial en la base de datos utilizando PostGIS. Posteriormente, podrán
iniciar sesión en el sistema"*

Con el proyecto ya iniciado, se redirigirá inmediatamente al login, en caso de tener cuenta se puede iniciar sesión normalmente.
En cualquier otro caso, el usuario debe registrarse ingresando nombre, apellido, RUT, correo, ubicación en el mapa y contraseña.

![registro](https://github.com/user-attachments/assets/f63f59dc-2b25-4bbd-8d02-5218c1c0c507)

Luego, se le será redirigido al mismo apartado de inicio de sesión, donde se debe ingresar correo y contraseña, paso seguido será enviado al home page.

![inicio_sesion](https://github.com/user-attachments/assets/5bf0d1a3-9668-4676-b79c-f12d8eb909cb)

### Gestión de Tareas

Luego de iniciar sesión, el usuario puede acceder al sistema gestor de tareas.
![home_page](https://github.com/user-attachments/assets/4d54d76f-ba31-4726-8691-63170c17c1ad)

- **Crear una tarea:** Habiendo iniciado sesión, en la página principal se tiene la opción de crear una tarea, haciendo click a este apartado 
se mostrará un formulario donde se ingresará el título, descripción, fecha de vencimiento y sector al cual se desea asociar.

![crear_tarea](https://github.com/user-attachments/assets/8accc53b-1b4e-474b-9abb-51cf35b08c4e)

Luego, para poder visualizar la tarea creada y todas las demás se debe acceder al apartado de 'Gestor'.

- **Ver lista de tareas**: Habiendo accedido al apartado 'Gestor', se listan todas las tareas posibles, separadas por su estado: Pendientes, En Progreso y Completadas.

![gestor](https://github.com/user-attachments/assets/10f253ef-b8a9-42ff-9f4f-6e9f092623eb)

Aquí se muestran todos los detalles para cada tarea, y las opciones disponibles para interactuar con estas. 

- **Marcar tarea como en progreso / completada:** Simplemente presionando el checkbox se puede cambiar una tarea al siguiente estado, siguiendo el orden de Pendiente -> En Progreso -> Completada.

- **Editar una tarea** Presionando el botón 'Editar', se desplegará nuevamente un formulario con los datos de la tarea, los cuales pueden ser modificados a gusto.

![image](https://github.com/user-attachments/assets/280340af-8b3c-408d-b4dd-e9e3e4c776b4)


Además, también se puede cambiar el estado de una tarea manualmente, la utilidad de esto puede ser si cambiar una tarea directamente desde el estado Pendiente hacia Completada. 

![image](https://github.com/user-attachments/assets/c126bf45-5bdb-43ae-a295-d2f73a11159f)

- **Eliminar una tarea:** Parte de gestionar tareas es la capacidad de eliminarlas en caso de que no sean deseadas, notar que **no existe un mensaje de confirmación** así que al presionarse eliminar desaparecerá inmediatamente la tarea.

![image](https://github.com/user-attachments/assets/05a35638-9b7f-4bd6-b61c-09d6fbc36aee)


### Notificaciones
> Los usuarios deben recibir notificaciones cuando se acerque la fecha de vencimiento de una tarea.

Las notificaciones solamente aparecen si es que la tarea no se encuentra completada y tiene la misma fecha de vencimiento (DD/MM/YY) que el día actual.

Tomando en consideración la tarea previamente creada, que a contar del día de la confección del README (01/06/2025), vence a las 21:00, se encontrará en el header de la aplicación

![image](https://github.com/user-attachments/assets/039efcc3-8400-4172-84f0-8de055efae89)

### Consultas

En el apartado de 'Estadísticas', es posible visualizar las distintas consultas que atienden a las diversas preguntas sobre las tareas y sectores.
Para interactuar con estas sólo se deben ir completando tareas utilizando el gestor. Al ingresar en este apartado se actualizarán apropiadamente.
A continuación se describe cada una de estas, junto a cómo se llevó a cabo su implementación a nivel de base de datos.

#### ¿Cuántas tareas ha hecho el usuario por sector?

Tras completar algunas tareas, lo primero que se puede ver en el apartado de 'Estadísticas' es un recuento de la cantidad de tareas que posee el usuario por cada sector al cual están asociadas, independiente de si están completadas o no.

<img src="https://github.com/user-attachments/assets/84e3f6a6-ffbd-4ca1-83a5-a5b4ed553765" width="650">

Este recuento se computa utilizando la siguiente consulta SQL:

```sql
SELECT s.id AS id_sector, s.nombre_sector, COUNT(t.id) AS cantidad_tareas, t.id_usuario
FROM tareas t
JOIN sectores s ON t.id_sector = s.id
GROUP BY t.id_usuario, s.id, s.nombre_sector
ORDER BY s.nombre_sector
```

donde simplemente se aprovecha de la función nativa `COUNT` para contar las ocurrencias de las tareas. Cabe destacar que esta consulta realiza dicho procedimiento para todos los usuarios en la base de datos, por lo que además de recibir el resultado de esta, se selecciona aquel correspondiente al usuario actualmente autenticado en la aplicación.

#### ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario?

Justo al lado de lo anterior, se muestran los detalles de la tarea pendiente más cercana al usuario, además de la distancia a la cual se encuentran.

![image](https://github.com/user-attachments/assets/72d556e4-9b9d-4f3d-8590-be1eeb84ee2b)

De no existir ninguna tarea, se muestra un gráfico vacío.

Para esto se hace uso de la función nativa de PostGIS [ST_Distance](https://postgis.net/docs/ST_Distance.html) que calcula la distancia entre dos objetos geométricos- en este caso, el polígono que representa a un sector, y el punto de ubicación asociado al usuario. Se debe hacer un casteo al tipo de dato `geography` para poder recibir la distancia medida en metros. 

```sql
SELECT t.id AS id_tarea, t.titulo AS titulo_tarea, t.descripcion AS descripcion_tarea,
    t.fecha_vencimiento, t.estado AS estado_tarea,
    s.id AS id_sector, s.nombre_sector,
    ST_Distance(u.location::geography, s.area::geography) AS distancia_al_sector_metros
FROM tareas t
INNER JOIN sectores s ON t.id_sector = s.id
INNER JOIN users u ON t.id_usuario = u.id
WHERE t.id_usuario = ?
    AND t.estado = 'PENDIENTE'
    AND u.location IS NOT NULL
    AND s.area IS NOT NULL
ORDER BY distancia_al_sector_metros ASC, t.fecha_vencimiento ASC LIMIT 1;
```

#### ¿Cuál es el promedio de distancia entre las tareas completadas y el punto registrado del usuario?

La última vista en este apartado es el valor de la distancia promedio en metros entre la ubicación del usuario y las tareas completadas.

![image](https://github.com/user-attachments/assets/178cf7db-b044-49f8-bd9f-f2d842718e27)

Nuevamente, haciendo uso de `ST_Distance`, debemos obtener el promedio de todas las distancias a todas las tareas:

```sql
SELECT users.id,
    CONCAT(users.first_name, ' ', users.last_name) AS nombre,
    AVG(ST_Distance(sectores.area::geography, users.location::geography)) AS promedio_tareas
FROM (SELECT id_usuario, id_sector FROM tareas WHERE tareas.estado = 'COMPLETADA') AS tareas_completadas
    INNER JOIN sectores ON id_sector = sectores.id
    INNER JOIN users ON users.id = id_usuario
GROUP BY users.id
```

#### ¿Cuál es el sector con más tareas completadas dentro de un radio de 2 km / 5 km desde la ubicación del usuario?

Ya pasando al apartado de Sectores, se muestran gráficos de barras que ilustran los sectores y la cantidad de tareas en base a cierta consulta.
En este caso, aquellas completadas asociadas a sectores que estén dentro de un radio específico desde el punto registrado por el usuario como su ubicación.

<img src="https://github.com/user-attachments/assets/ca7cda80-267c-405a-a9e5-c73d563539b9" width="450">

<img src="https://github.com/user-attachments/assets/31361265-9e48-4673-9750-a90e55f40bd1" width="450">


El uso de [ST_DWithin](https://postgis.net/docs/ST_DWithin.html) nos indica si un objeto geométrico se encuentra a una cierta distancia de otro, como se acaba de mencionar, esto se utiliza con los polígonos de los sectores y el punto del usuario.
La consulta se implementó de modo que crea un nuevo punto idéntico a la ubicación del usuario- latitud y longitud. Esto se podría mejorar con una sub-consulta que lo recupere directamente en base a su ID. 

```sql
WITH tareas_cercanas AS (
                    SELECT id
                    FROM sectores
                    WHERE ST_DWithin(area::geography, ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography, ?)
                )
SELECT s.id, s.nombre_sector, COUNT(t.id) AS tareas_completadas
FROM sectores s
JOIN tareas t ON t.id_sector = s.id
WHERE s.id IN (SELECT id FROM tareas_cercanas)
AND t.estado = 'COMPLETADA'
GROUP BY s.id, s.nombre_sector
ORDER BY tareas_completadas DESC;
```

#### ¿En qué sectores geográficos se concentran la mayoría de las tareas pendientes? (utilizando agrupación espacial).

<img src="https://github.com/user-attachments/assets/0b39d0d6-ffab-447d-ac19-695f35db2ba2" width="450">

En este caso no se requiere de ninguna función nativa de PostGIS y simplemente se deben contar las tareas pendientes para cada sector.

```sql
SELECT sectores.nombre_sector, COUNT(tareas.id_sector) AS cantidad_tareas
FROM tareas LEFT JOIN sectores ON sectores.id = tareas.id_sector
WHERE tareas.estado = 'PENDIENTE'
GROUP BY sectores.nombre_sector
ORDER BY cantidad_tareas DESC
```
