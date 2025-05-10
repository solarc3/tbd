import apiClient from "@/api/axios";

export interface FarmaciaFallada {
	nombreFarmacia: string;
	cantidad: number;
}

export interface FarmaciaRanking {
	nombreFarmacia: string;
	cantPedidosEntregados: number;
}

class FarmaciaService {
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
}

export default new FarmaciaService();
