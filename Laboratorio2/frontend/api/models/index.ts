export interface LoginRequest {
	email: string;
	password: string;
}

export interface SignupRequest {
    username: string;
    firstName: string;
    lastName: string;
    rut: string;
    email: string;
    password: string;
    latitude?: number | null; // Add latitude
    longitude?: number | null; // Add longitude
}

export interface User {
	id: number;
	username: string;
	firstName: string;
	lastName: string;
	rut: string;
	email: string;
}

export interface Product {
	idProducto: number;
	nombreProducto: string;
	precio: number;
	categoria: string;
	requiereReceta: boolean;
	imageUrl: string;
}

export interface ClienteGasto {
	id: number;
	username: string;
	email: string;
	totalGastado: number;
}

export interface FarmaciaEntity {
        idFarmacia: number;
        nombreFarmacia: string;
        direccion: string;
        telefono?: string;
        ubicacion?: {
                x: number;
                y: number;
        };
}

export interface ProductRequest {
	idProducto: number;
	cantidad: number;
	recetaValidada: boolean;
}

export interface RegistrarPedidoRequest {
	idCliente: number;
	idFarmacia: number;
	esUrgente: boolean;
	productos: Array<ProductRequest>;
	monto: number;
}

export interface RepartidorDistanciaTotal {
  id: number
  nombreRepartidor: string
  distanciaTotalKm: number
}

export interface PedidoCruzaZonas {
	idPedido: number
	idCliente: number
	nombreCliente: string
	fechaPedido: string
	nombresZonas: string[]
}

// lab 2
export interface ClienteZonaCobertura {
    id: number;
    nombre: string;
    apellido: string;
    nombreSector: string;
	latitud: string;
	longitud: string;
}

export interface FarmaciaClosestDelivery {
    nombreUsuario: string;
    distanciaEntrega: number;
}

export interface ClienteLejanoDeFarmacia {
    idCliente: number;
    nombreCliente: string;
    nombreFarmacia: string;
    distanciaKm: number;
}

export interface Ubicacion {
	longitude: number;
	latitude: number;
}

export interface FarmaciaClosestDeliveryResponse {
	nombreUsuario: string;
	nombreFarmacia: string;
	distanciaEntrega: number;
	ubicacionFarmacia: Ubicacion;
	ubicacionUsuario: Ubicacion;
}