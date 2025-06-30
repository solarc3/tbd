
# Laboratorio 3 - Bases de Datos Avanzadas

## Prerequisitos
- **Docker Desktop/Engine** -> Docker y Docker Compose
- **IntelliJ IDEA** -> Java 17
- **Node.js** -> Node Package Manager (npm)

## Descripción del Proyecto
Este laboratorio implementa una aplicación web con arquitectura de microservicios que utiliza:
- **Base de datos híbrida**: PostgreSQL con PostGIS y MongoDB
- **Backend**: Spring Boot con Java 17
- **Frontend**: Nuxt.js con Vue 3 y Tailwind CSS
- **Gestión de usuarios**: Sistema de autenticación con JWT
- **Funcionalidades**: Gestión de farmacias, productos, pedidos, repartidores y análisis geoespacial

## Pasos a seguir para ejecutar
> Se ejecutan 3 componentes en simultaneo

### Base de Datos
- En una terminal dentro del directorio `Laboratorio3/backend`, levantar el correspondiente `docker-compose.yaml` con el comando:
```bash
docker compose up -d
```
- Se levantarán los siguientes contenedores:
  - **PostgreSQL con PostGIS** en el puerto `5432`
  - **MongoDB** en el puerto `27017`
  - **Mongo Express** (interfaz web para MongoDB) en el puerto `8081`

#### Posibles errores:
- **Puerto ya utilizado:** Es posible que los puertos `5432`, `27017` o `8081` ya estén utilizados por instalaciones locales de PostgreSQL, MongoDB o otras aplicaciones. Dependiendo del sistema operativo se debe matar el proceso que esté escuchando en esos puertos.
- **Problemas de memoria:** MongoDB puede requerir más memoria RAM disponible. Asegúrese de tener al menos 4GB de RAM disponible.

### Backend
- Desde IntelliJ IDEA, abrir la carpeta dentro del proyecto `Laboratorio3/backend`.
- En su defecto, se puede obtener de VCS Git con el enlace correspondiente al repositorio (`https://github.com/solarc3/tbd.git`).
- Hacer clic en el botón de "play", o sea "Run TbdLab2Application", o presionar Shift + F10.

### Frontend
- Desde una terminal, acceder al directorio `Laboratorio3/frontend`.
- En este, ingresar el comando para instalar los paquetes y dependencias:
```bash
npm install
```
- Finalmente, abrir el proyecto en modo developer con el comando:
```bash
npm run dev
```

## Acceso a la Aplicación
Si se siguieron correctamente los pasos en el orden establecido, la aplicación será accesible desde:

- **Frontend (Aplicación principal)**: `http://localhost:3000`
- **Backend API**: `http://localhost:8080`
- **Mongo Express (Interfaz MongoDB)**: `http://localhost:8081`
  - Usuario: `mongo`
  - Contraseña: `mongo`

## Configuración de Base de Datos

### PostgreSQL con PostGIS
- **Host**: localhost
- **Puerto**: 5432
- **Usuario**: postgres
- **Contraseña**: postgres
- **Base de datos**: spring_test

### MongoDB
- **Host**: localhost
- **Puerto**: 27017
- **Usuario**: mongo
- **Contraseña**: mongo
- **Base de datos**: TBD

## Características Principales
- Sistema de autenticación con JWT
- Gestión de usuarios y roles
- Gestión de farmacias con ubicación geoespacial
- Catálogo de productos y medicamentos
- Sistema de pedidos y entregas
- Seguimiento de repartidores
- Análisis geoespacial con PostGIS
- Interfaz web responsive con Nuxt.js y Tailwind CSS

## Estructura del Proyecto
```
Laboratorio3/
├── backend/           # API REST con Spring Boot
├── frontend/          # Aplicación web con Nuxt.js
├── docker-compose.yaml # Configuración de contenedores
└── README.md          # Este archivo
```
