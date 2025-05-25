import apiClient from "@/api/axios";

export interface SectorStats {
  name: string;
  tasks: number;
  percentage: number;
}

export interface ClosestTask {
  title: string;
  description: string;
  distance: number;
}

class EstadisticasService {
  async getSectorStats(): Promise<SectorStats[]> {
    try {
      const { data } = await apiClient.get<SectorStats[]>("/estadisticas/sector");
      return data;
    } catch (error) {
      console.error("Error al obtener estadísticas por sector:", error);
      throw error;
    }
  }

  async getCompletedTasks(): Promise<number> {
    try {
      const { data } = await apiClient.get<number>("/estadisticas/completed");
      return data;
    } catch (error) {
      console.error("Error al obtener tareas completadas:", error);
      throw error;
    }
  }

  async getPendingTasks(): Promise<number> {
    try {
      const { data } = await apiClient.get<number>("/estadisticas/pending");
      return data;
    } catch (error) {
      console.error("Error al obtener tareas pendientes:", error);
      throw error;
    }
  }

  async getTasksBySector(): Promise<SectorStats[]> {
    try {
      const { data } = await apiClient.get<SectorStats[]>("/estadisticas/tareas-por-sector");
      return data;
    } catch (error) {
      console.error("Error al obtener tareas por sector:", error);
      throw error;
    }
  }

  async getClosestPendingTask(userId: number): Promise<ClosestTask> {
    try {
      const { data } = await apiClient.get<ClosestTask>(`/tarea-pendiente-cercana/${userId}`);
      return data;
    } catch (error) {
      console.error("Error al obtener la tarea pendiente más cercana:", error);
      throw error;
    }
  }

  async getSectorWithMostCompletedTasks(userId: number, radius: number): Promise<SectorStats> {
    try {
      const { data } = await apiClient.get<SectorStats>(`/sector-completadas-radio/${userId}/${radius}`);
      return data;
    } catch (error) {
      console.error("Error al obtener el sector con más tareas completadas:", error);
      throw error;
    }
  }

  async getAverageDistanceOfCompletedTasks(userId: number): Promise<number> {
    try {
      const { data } = await apiClient.get<number>(`/promedio-distancia-completadas/${userId}`);
      return data;
    } catch (error) {
      console.error("Error al calcular el promedio de distancia:", error);
      throw error;
    }
  }
}

export default new EstadisticasService();