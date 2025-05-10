export interface LoginRequest {
	username: string;
	password: string;
}

export interface SignupRequest {
	username: string;
	email: string;
	password: string;
}

// User data returned by el servidor
export interface User {
	id: number;
	username: string;
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
