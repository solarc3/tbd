import apiClient from '@/api/axios'

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
}


export default new RepartidorService()