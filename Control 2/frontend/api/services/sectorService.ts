import apiClient from "@/api/axios";

class SectorService {
  async getAllSectores(): Promise<{ id: number; nombreSector: string }[]> {
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