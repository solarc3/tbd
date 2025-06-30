import axiosInstance from "../axios";
import type { ClienteGasto, ClienteZonaCobertura, ClienteLejanoDeFarmacia } from "@/api/models";

export const userService = {
	getAllClientsWithSpending: async (): Promise<ClienteGasto[]> => {
		try {
			const response = await axiosInstance.get("/users/clientes-gasto");
			return response.data;
		} catch (error) {
			console.error("Error fetching clients with spending:", error);
			throw error;
		}
	},
	getZonaCoberturaByClienteId: async (id: number): Promise<ClienteZonaCobertura> => {
        try {
            const response = await axiosInstance.get(`/users/zona-cobertura/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching zona de cobertura for client ID ${id}:`, error);
            throw error;
        }
    },
    getClientesLejanosDeFarmacia: async (radiusKm: number): Promise<ClienteLejanoDeFarmacia[]> => {
        try {
            const response = await axiosInstance.get(`/users/mas-lejanos-farmacia/${radiusKm}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching clientes lejanos for radius ${radiusKm}:`, error);
            throw error;
        }
    },
    getAllUsers: async (): Promise<UserInfoResponse[]> => {
        try {
            const response = await axiosInstance.get("/users/");
            return response.data;
        } catch (error) {
            console.error("Error fetching all users:", error);
            throw error;
        }
    }
};
