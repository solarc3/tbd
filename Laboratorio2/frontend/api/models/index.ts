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