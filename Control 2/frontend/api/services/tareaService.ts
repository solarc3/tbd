import apiClient from "@/api/axios";

export interface Tarea {
  id: number;
  titulo: string;
  descripcion: string;
  fechaVencimiento: string;
  idUsuario: number;
  estado: string;
  idSector: number;
}

class TareaService {
  async getAllTareas(): Promise<Tarea[]> {
    try {
      const { data } = await apiClient.get<Tarea[]>("/tarea/");
      return data;
    } catch (error) {
      console.error("Error al obtener las tareas:", error);
      throw error;
    }
  }

  async createTarea(tarea: Partial<Tarea>): Promise<Tarea> {
    try {
      console.log("Datos enviados en la solicitud POST:", tarea);

      const { data } = await apiClient.post<Tarea>("/tarea/", tarea);
      return data;
    } catch (error) {
      console.error("Error al crear la tarea:", error);
      throw error;
    }
  }

  async updateTarea(id: number, tarea: Partial<Tarea>): Promise<Tarea> {
    try {
      const { data } = await apiClient.put<Tarea>(`/tarea/${id}`, tarea);
      return data;
    } catch (error) {
      console.error("Error al actualizar la tarea:", error);
      throw error;
    }
  }

  async deleteTarea(id: number): Promise<void> {
    try {
      await apiClient.delete(`/tarea/${id}`);
    } catch (error) {
      console.error("Error al eliminar la tarea:", error);
      throw error;
    }
  }
}

export default new TareaService();