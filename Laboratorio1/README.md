
# Laboratorio 1 - Bases de Datos Avanzadas

## Prerequisitos
- **Docker Desktop/Engine** -> Docker y Docker Compose
- **IntelliJ**: Java 17
- **Node**: Node Package Manager (npm)

## Pasos a seguir para ejecutar
> [!INFO]
> Se ejecutan 3 componentes en simultaneo


### Base de Datos
- En una terminal dentro del directorio `Laboratorio1/backend`, levantar el correspondiente `docker-compose.yaml` con el comando `docker compose up -d`
- El contenedor de PostrgeSQL quedaría levantado y listo para uso.

#### Posibles errores:
- **Puerto ya utilizado:** Es posible que el puerto esté ya esté utilizado por la actual instalación de PostgreSQL localmente, por lo que dependiendo del sistema operativo se debe matar el proceso que esté escuchando en ese puerto.
- **No se encuentran las variables de entorno:** Es posible que no se encuentre en la carpeta backend el archivo `.env`, que contiene los parámetros para la base de datos.

### Backend
- Desde IntelliJ, abrir la carpeta dentro del proyecto `Laboratorio1/backend`.
- En su defecto, se puede obtener de VCS Git con el énlace corresponediente al repositorio (`https://github.com/solarc3/tbd.git`).
- Hacer click en el botón de "play", osease "Run TbdLab1Application", o Shift + F10.

### Frontend
- Desde una terminal, acceder al directorio `Laboratorio1/frontend`.
- En este, ingresar el comando para instalar los paquetes y dependencias: `npm install`
- Finalmente, abrir el proyecto en modo developer con el comando: `npm run dev`.

#### SI se siguieron correctamente los pasos en el órden establecido, la aplicación será accesible desde cualquier navegador con el URL `http://localhost:3000`
