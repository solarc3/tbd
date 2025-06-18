
# Laboratorio 2 - Bases de Datos Avanzadas

## Prerequisitos
- **Docker Desktop/Engine** -> Docker y Docker Compose

## Pasos a seguir para ejecutar
> Se ejecutan 3 componentes en simultaneo

### Base de Datos, Backend y Frontend
- **Ejecutar Docker Compose:** Abriendo una terminal en el directorio principal `Laboratorio2`, ejecutar el comando `docker compose up`
- Luego esperar la inicialización de los contenedores, network y volumen de la BD

#### Posibles errores:
- **Puerto ya utilizado:** Es posible que el puerto esté ya esté utilizado por la actual instalación de PostgreSQL localmente, por lo que dependiendo del sistema operativo se debe matar el proceso que esté escuchando en ese puerto.
- **No se encuentran las variables de entorno:** Es posible que no se encuentre en la carpeta backend el archivo `.env`, que contiene los parámetros para la base de datos.

#### Si se siguieron correctamente los pasos en el órden establecido, la aplicación será accesible desde cualquier navegador con el URL `http://localhost:3000`
