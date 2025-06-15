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

        async getEntregasCercanas(idFarmacia: number): Promise<FarmaciaClosestDelivery[]> {
                try {
                        const { data } = await apiClient.get<FarmaciaClosestDelivery[]>(
                                `/farmacia/entregas-cercanas/${idFarmacia}`,
                        );
                        return data;
                } catch (error) {
                        console.error(`Error fetching entregas cercanas for farmacia ${idFarmacia}:`, error);
                        throw error;
                }
        }
}

export default new FarmaciaService();
