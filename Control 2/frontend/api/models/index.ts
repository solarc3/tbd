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
    latitude?: number | null; 
    longitude?: number | null;
}

// User data returned by el servidor
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

export interface Tarea {
  id: number;
  titulo: string;
  descripcion: string;
  fechaVencimiento: string;
  idUsuario: number;
  estado: string;
  idSector: number;
}

export interface ClosestTask {
	title: string;
	description: string;
	distance: number;
}