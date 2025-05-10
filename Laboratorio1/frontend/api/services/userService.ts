import axiosInstance from "../axios";
import type { ClienteGasto } from "@/api/models";

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
};
