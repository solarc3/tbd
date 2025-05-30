import apiClient from "@/api/axios";

export interface SectorEntity {
  id: number;
  nombreSector: string;
  area: string;
}

class SectorService {
  async getAllSectores(): Promise<SectorEntity[]> {
    try {
      const { data } = await apiClient.get("/sector/");
      return data;
    } catch (error) {
      console.error("Error al obtener los sectores:", error);
      throw error;
    }
  }
}

export default new SectorService();
