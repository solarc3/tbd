import apiClient from "@/api/axios";

export interface SectorStats {
  nombreSector: string;
  cantidadTareas: number;
}

// i
export interface ClosestTask {
  tituloTarea: string;
  descripcionTarea: string;
  nombreSector: string;
  distanciaAlSectorMetros: number;
}

export interface TareaCountBySectorDTO {
  idSector: number;
  nombreSector: string;
  cantidadTareas: number;
}

export interface DistanciaTareaPromedioResponse {
  idUsuario: number;
  distanciaPromedio: number; // también en metros
}

class EstadisticasService {
  async getTareaCountByUserAndSector(userId: number): Promise<TareaCountBySectorDTO[]> {
    try {
      const { data } = await apiClient.get<TareaCountBySectorDTO[]>(`/tarea/usuario/${userId}/count-by-sector`);
      return data;
    } catch (error) {
      console.error("Error al obtener conteo de tareas por sector:", error);
      throw error;
    }
  }

  async getClosestPendingTask(userId: number): Promise<ClosestTask[]> {
    try {
      const { data } = await apiClient.get<ClosestTask[]>(`/tarea/usuario/${userId}/mas-cercana`);
      return data;
    } catch (error) {
      console.error("Error al obtener la tarea pendiente más cercana:", error);
      throw error;
    }
  }

  async getMostCompletedNearMe(userId: number, radiusKm: number): Promise<SectorStats[]> {
    try {
      const { data } = await apiClient.get<SectorStats[]>(`/sector/most-completed-near-me/${radiusKm}`);
      return data;
    } catch (error) {
      console.error("Error al obtener sectores con más tareas completadas:", error);
      throw error;
    }
  }

  async getAverageDistanceOfCompletedTasks(userId: number): Promise<DistanciaTareaPromedioResponse[]> {
    try {
      const { data } = await apiClient.get<DistanciaTareaPromedioResponse[]>(`/users/promedio-distancia-completadas/${userId}`);
      return data;
    } catch (error) {
      console.error("Error al obtener promedio de distancia de tareas completadas:", error);
      throw error;
    }
  }

  async getTasksBySector(): Promise<SectorStats[]> {
    try {
      const { data } = await apiClient.get<SectorStats[]>("/sector/tareas-pendientes");
      return data;
    } catch (error) {
      console.error("Error al obtener sectores con tareas pendientes:", error);
      throw error;
    }
  }

  async getMostCompletedNearUser(userId: number, radiusKm: number): Promise<SectorStats[]> {
    try {
      const { data } = await apiClient.get<SectorStats[]>(`/sector/most-completed-near/${userId}/${radiusKm}`);
      return data;
    } catch (error) {
      console.error("Error al obtener sectores con más tareas completadas cerca del usuario:", error);
      throw error;
    }
  }
}

export default new EstadisticasService();