import apiClient from '@/api/axios';
import type { NavegacionSinCompraResponse } from "~/api/models";

class NavegacionService {
    async getNavegacionSinCompras(): Promise<NavegacionSinCompraResponse[]> {
        const { data } = await apiClient.get<NavegacionSinCompraResponse[]>('/navegacion/sin-compra');
        return data;
    }

    async getAllLogs() {
        const { data } = await apiClient.get('/navegacion/all');
        return data;
    }
}

export const navegacionService = new NavegacionService();
export default navegacionService;