import apiClient from '@/api/axios'
import type { RepartidorDistanciaTotal, RepartidorRutasFrecuentesDTO} from "@/api/models";
interface RepartidorRendimiento {
  idRepartidor: number
  nombreRepartidor: string
  pedidos: number
  puntuacionPromedio: number
  indiceRendimiento: number
}

interface RepartidorInfo {
  nombre: string
  cantPaquetesEntregados: number
}

interface RepartidorTiempoPromedioResponse {
  idRepartidor: number
  nombreRepartidor: string
  promedioHoras: number
}

export interface Repartidor {
  idRepartidor: number
  nombreRepartidor: string
}



class RepartidorService {
  async getTopRepartidores(): Promise<RepartidorRendimiento[]> {
    const { data } = await apiClient.get<RepartidorRendimiento[]>('/repartidor/rendimiento')
    return data
  }
  
  async getAllRepartidoresInfo(): Promise<RepartidorInfo[]> {
    const { data } = await apiClient.get<RepartidorInfo[]>('/repartidor/info')
    return data
  }

  async getAllRepartidores(): Promise<Repartidor[]> {
    const { data } = await apiClient.get<Repartidor[]>('/repartidor/')
    return data
  }

  async getRepartidorTiempoPromedio(): Promise<RepartidorTiempoPromedioResponse[]>{
    const { data } = await apiClient.get<RepartidorTiempoPromedioResponse[]>('/repartidor/tpromedio')
    return data
  }

  async getRepartidorDistanciaTotal(): Promise<RepartidorDistanciaTotal[]> {
    const { data } = await apiClient.get<RepartidorDistanciaTotal[]>('/repartidor/distancia-total')
    return data
  }
}

export const getRutasFrecuentes = async (): Promise<RepartidorRutasFrecuentesDTO[]> => {
  const response = await apiClient.get<RepartidorRutasFrecuentesDTO[]>('/repartidor/rutas-frecuentes')
  return response.data
}
export default new RepartidorService()