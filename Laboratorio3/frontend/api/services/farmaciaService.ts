import apiClient from "@/api/axios";
import type {FarmaciaClosestDeliveryResponse} from "~/api/models";

export interface FarmaciaFallada {
	nombreFarmacia: string;
	cantidad: number;
}

export interface FarmaciaRanking {
	nombreFarmacia: string;
	cantPedidosEntregados: number;
}
class FarmaciaService {
	async getAllFarmacias(): Promise<FarmaciaEntity[]> {
		try {
			const { data } =
				await apiClient.get<FarmaciaEntity[]>("/farmacia/all");
			return data;
		} catch (error) {
			console.error("Error fetching farmacias:", error);
			throw error;
		}
	}

	async getProductosByFarmaciaId(idFarmacia: number): Promise<Product[]> {
		try {
			const { data } = await apiClient.get<Product[]>(
				`/productos/farmacia/${idFarmacia}`,
			);
			return data;
		} catch (error) {
			console.error(
				`Error fetching products for farmacia ${idFarmacia}:`,
				error,
			);
			throw error;
		}
	}

	async getFarmaciasFalladas(): Promise<FarmaciaFallada[]> {
		try {
			const { data } = await apiClient.get<FarmaciaFallada[]>(
				"/farmacia/pedidofallado",
			);
			console.log("Farmacia data received:", data);
			return data;
		} catch (error) {
			console.error("Error fetching farmacias falladas:", error);
			throw error;
		}
	}

	async getFarmaciasRanking(): Promise<FarmaciaRanking[]> {
			try {
					const { data } =
							await apiClient.get<FarmaciaRanking[]>("/farmacia/ranking");
					console.log("Farmacia ranking data received:", data);
					return data;
			} catch (error) {
					console.error("Error fetching farmacias ranking:", error);
					throw error;
			}
	}

	async getEntregasCercanas(idFarmacia: number): Promise<FarmaciaClosestDeliveryResponse[]> {
			try {
					const { data } = await apiClient.get<FarmaciaClosestDeliveryResponse[]>(
							`/farmacia/entregas-cercanas/${idFarmacia}`,
					);
					return data;
			} catch (error) {
					console.error(`Error fetching entregas cercanas for farmacia ${idFarmacia}:`, error);
					throw error;
			}
	}

	// querys lab 3
	async getPromedioPuntuacionPorFarmacia(): Promise<{ nombreFarmacia: string; promedio: number }[]> {
		try {
			const { data } = await apiClient.get<{ nombreFarmacia: string; promedio: number }[]>(
				"/opiniones/farmacia/promedio"
			);
			return data;
		} catch (error) {
			console.error("Error fetching promedio puntuación por farmacia:", error);
			throw error;
		}
	}

	async getOpinionesPorHora(): Promise<{ [hora: string]: any[] }> {
	  try {
	    const { data } = await apiClient.get<{ opiniones: { [hora: string]: any[] } }>("/opiniones/opiniones-por-hora");
	    return data.opiniones;
	  } catch (error) {
	    console.error("Error fetching opiniones por hora:", error);
	    throw error;
	  }
	}

	async createOpinion(opinion: { idPedido: number; puntuacion: number; comentario: string; idCliente: number }) {
		return apiClient.post('/opiniones/', {
			idCliente: opinion.idCliente,
			idPedido: opinion.idPedido,
			puntuacion: opinion.puntuacion,
			comentario: opinion.comentario
		})
	}

	async getAllOpiniones(): Promise<any[]> {
	  const { data } = await apiClient.get('/opiniones/')
	  return data
	}
}

export default new FarmaciaService();
